package com.sandraprog.popularmovies2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.sandraprog.popularmovies2.adapter.MoviesAdapter;
import com.sandraprog.popularmovies2.api.RetrofitClient;
import com.sandraprog.popularmovies2.api.RetrofitInterface;
import com.sandraprog.popularmovies2.data.FavDatabase;
import com.sandraprog.popularmovies2.model.Movie;
import com.sandraprog.popularmovies2.model.MoviesList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    private List<Movie> mList;
    private SharedPreferences preferences;
    private Menu mOptionsMenu;

    private FavDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mDb = FavDatabase.getInstance(getApplicationContext());
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        sortMovies();
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mAdapter.setMovies(movies);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        mOptionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_most_popular:
                preferences.edit().putString(this.getString(R.string.pref_sort_order_key),
                        this.getString(R.string.pref_most_popular)).apply();
                sortMovies();
                return true;
            case R.id.menu_highest_rated:
                preferences.edit().putString(this.getString(R.string.pref_sort_order_key),
                        this.getString(R.string.pref_highest_rated)).apply();
                sortMovies();
                return true;
            case R.id.menu_favorites:
                preferences.edit().putString(this.getString(R.string.pref_sort_order_key),
                        this.getString(R.string.pref_favorites)).apply();
                sortMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mList = new ArrayList<>();
        mAdapter = new MoviesAdapter(this, mList);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        else
            mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void loadJSON(String sortOrder) {

        RetrofitClient client = new RetrofitClient();
        RetrofitInterface apiService = RetrofitClient.getClient().create(RetrofitInterface.class);

        Call<MoviesList> call;
        String apiKey = getString(R.string.api_key);
        if (sortOrder.equals(this.getString(R.string.pref_most_popular)))
            call = apiService.getPopularMovies(apiKey);
        else
            call = apiService.getTopRatedMovies(apiKey);

        call.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                List<Movie> movies = response.body().getResults();
                mRecyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                mRecyclerView.smoothScrollToPosition(0);
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.error_no_data_from_retrofit, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sortMovies() {
        String sortOrder = preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular));

        if (sortOrder.equals(this.getString(R.string.pref_most_popular))) {
            loadJSON(sortOrder);
        } else if (sortOrder.equals(this.getString(R.string.pref_highest_rated))) {
            loadJSON(sortOrder);
        } else if (sortOrder.equals(this.getString(R.string.pref_favorites))) {
            setupViewModel();
        }
    }
}
