package com.sandraprog.popularmovies2;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.sandraprog.popularmovies2.data.FavDatabase;
import com.sandraprog.popularmovies2.model.Movie;

import java.util.List;

/**
 * Created by sandrapog on 14.06.2018.
 */

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> movies;

    public MainViewModel(Application application) {
        super(application);
        FavDatabase database = FavDatabase.getInstance(this.getApplication());

        movies = database.getFavDao().getAllFavorites();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
