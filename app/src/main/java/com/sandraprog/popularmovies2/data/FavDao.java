package com.sandraprog.popularmovies2.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.sandraprog.popularmovies2.model.Movie;

import java.util.List;

/**
 * Created by sandrapog on 12.06.2018.
 */
@Dao
public interface FavDao {
    @Query("SELECT * from movie")
    LiveData<List<Movie>> getAllFavorites();

    @Insert
    void insert(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * from movie where id=:id")
    List<Movie> getMovieById(int id);

}
