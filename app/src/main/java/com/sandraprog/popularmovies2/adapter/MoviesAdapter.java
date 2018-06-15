package com.sandraprog.popularmovies2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sandraprog.popularmovies2.DetailActivity;
import com.sandraprog.popularmovies2.R;
import com.sandraprog.popularmovies2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sandrapog on 05.06.2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";
    private List<Movie> mMovieList;
    private Context mContext;
    boolean favoriteMovie;

    public MoviesAdapter(Context context, List<Movie> movieList) {
        this.mMovieList = movieList;
        mContext = context;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new MoviesViewHolder(view);
    }

    public void setMovies(List<Movie> movieList){
        this.mMovieList = movieList;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder viewHolder, int position) {
        String posterPath = POSTER_BASE_URL + mMovieList.get(position).getPosterPath();
        Picasso.with(mContext).load(posterPath).into(viewHolder.mPosterImage);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {
        private ImageView mPosterImage;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            mPosterImage = (ImageView) itemView.findViewById(R.id.iv_poster);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        final Movie itemMovie = mMovieList.get(position);
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("movie", itemMovie);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);

                    }
                }
            });
        }
    }
}
