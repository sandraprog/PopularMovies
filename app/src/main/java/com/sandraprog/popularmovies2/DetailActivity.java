package com.sandraprog.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sandraprog.popularmovies2.adapter.ReviewsAdapter;
import com.sandraprog.popularmovies2.adapter.TrailersAdapter;
import com.sandraprog.popularmovies2.api.RetrofitClient;
import com.sandraprog.popularmovies2.api.RetrofitInterface;
import com.sandraprog.popularmovies2.data.AppExecutors;
import com.sandraprog.popularmovies2.data.FavDatabase;
import com.sandraprog.popularmovies2.model.Movie;
import com.sandraprog.popularmovies2.model.Review;
import com.sandraprog.popularmovies2.model.ReviewsList;
import com.sandraprog.popularmovies2.model.Trailer;
import com.sandraprog.popularmovies2.model.TrailersList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private static final String POSTER_BIG_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private Movie movie;
    private int mMovieId;
    private RecyclerView mTrailersRecycleView;
    private TrailersAdapter mTrailersAdapter;
    private List<Trailer> mTrailerList;

    private RecyclerView mReviewsRecycleView;
    private ReviewsAdapter mReviewsAdapter;
    private List<Review> mReviewList;
    private String title;
    private String releaseDate;
    private double vote;
    private String overview;
    private String poster, backdropPath;

    private FavDatabase mDb;

    private Button favButton;
    private boolean favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageView mPosterImage = (ImageView) findViewById(R.id.iv_poster);
        TextView mTitle = (TextView) findViewById(R.id.tv_title);
        TextView mReleaseDate = (TextView) findViewById(R.id.tv_date);
        TextView mOverview = (TextView) findViewById(R.id.tv_overview);
        TextView mVote = (TextView) findViewById(R.id.tv_vote);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("movie")) {

            movie = getIntent().getParcelableExtra("movie");

            String posterPath = POSTER_BIG_BASE_URL + movie.getBackdropPath();
            Picasso.with(this).load(posterPath).into(mPosterImage);

            title = movie.getTitle();
            mTitle.setText(title);
            releaseDate = movie.getReleaseDate();
            mReleaseDate.setText(releaseDate);
            vote = movie.getVoteAverage();
            mVote.setText(String.valueOf(vote));
            overview = movie.getOverview();
            mOverview.setText(overview);
            mMovieId = movie.getId();
            poster = movie.getPosterPath();
            backdropPath = movie.getBackdropPath();

        } else {
            Toast.makeText(this, R.string.no_data_text, Toast.LENGTH_SHORT).show();
        }
        favButton = this.findViewById(R.id.favorite_button);
        isFavoriteMovie();

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (!favorite) {
                            FavDatabase
                                    .getInstance(getApplicationContext())
                                    .getFavDao()
                                    .insert(new Movie(backdropPath, mMovieId, overview, poster, releaseDate, title, vote));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setupFavButton(true);
                                }
                            });

                        } else {
                            FavDatabase
                                    .getInstance(getApplicationContext())
                                    .getFavDao()
                                    .delete(movie);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setupFavButton(false);
                                }
                            });
                        }
                    }
                });
            }
        });
        initViews();
    }

    void setupFavButton(boolean isFavorite) {
        favButton.setText(isFavorite ? getResources().getString(R.string.favorite_text) : getResources().getString(R.string.not_favorite_text));
        if (isFavorite)
            favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_gold, 0, 0, 0);
        else
            favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_gray, 0, 0, 0);
    }

    private void isFavoriteMovie() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final int size = FavDatabase
                        .getInstance(getApplicationContext())
                        .getFavDao()
                        .getMovieById(mMovieId).size();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        favorite = (size > 0 ? true : false);
                        setupFavButton(favorite);
                    }
                });
            }
        });
    }

    private void initViews() {
        mTrailerList = new ArrayList<>();
        mTrailersAdapter = new TrailersAdapter(this, mTrailerList);

        mTrailersRecycleView = this.findViewById(R.id.recycler_trailers);
        mTrailersRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mTrailersRecycleView.setAdapter(mTrailersAdapter);
        mTrailersAdapter.notifyDataSetChanged();

        mReviewList = new ArrayList<>();
        mReviewsAdapter = new ReviewsAdapter(this, mReviewList);

        mReviewsRecycleView = this.findViewById(R.id.recycler_reviews);
        mReviewsRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mReviewsRecycleView.setAdapter(mReviewsAdapter);
        mReviewsAdapter.notifyDataSetChanged();

        loadJSON();
    }

    private void loadJSON() {
        RetrofitClient client = new RetrofitClient();
        RetrofitInterface apiService = RetrofitClient.getClient().create(RetrofitInterface.class);
        String apiKey = getString(R.string.api_key);
        Call<TrailersList> call = apiService.getMovieTrailer(mMovieId, apiKey);

        call.enqueue(new Callback<TrailersList>() {
            @Override
            public void onResponse(Call<TrailersList> call, Response<TrailersList> response) {
                List<Trailer> movies = response.body().getResults();
                mTrailersRecycleView.setAdapter(new TrailersAdapter(getApplicationContext(), movies));
                mTrailersRecycleView.smoothScrollToPosition(0);
            }

            @Override
            public void onFailure(Call<TrailersList> call, Throwable t) {
                Toast.makeText(DetailActivity.this, R.string.error_no_data_from_retrofit, Toast.LENGTH_SHORT).show();
            }
        });

        Call<ReviewsList> call2 = apiService.getMovieReview(mMovieId, apiKey);

        call2.enqueue(new Callback<ReviewsList>() {
            @Override
            public void onResponse(Call<ReviewsList> call2, Response<ReviewsList> response) {
                List<Review> reviews = response.body().getResults();
                mReviewsRecycleView.setAdapter(new ReviewsAdapter(getApplicationContext(), reviews));
                mReviewsRecycleView.smoothScrollToPosition(0);
            }

            @Override
            public void onFailure(Call<ReviewsList> call2, Throwable t) {
                Toast.makeText(DetailActivity.this, R.string.error_no_data_from_retrofit, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
