package com.sandraprog.popularmovies2.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.sandraprog.popularmovies2.model.Movie;

/**
 * Created by sandrapog on 12.06.2018.
 */
@Database(entities = { Movie.class }, version = 1, exportSchema = false)
public abstract class FavDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "favorites.db";
    private static volatile FavDatabase sInstance;
    private static final Object LOCK = new Object();

    public static FavDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {

                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavDatabase.class, FavDatabase.DATABASE_NAME)
                        .build();
            }
        }

        return sInstance;
    }
    public abstract FavDao getFavDao();

}
