package com.sandraprog.popularmovies2.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sandrapog on 05.06.2018.
 */

public class RetrofitClient {
    public static Retrofit retrofit = null;
    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
