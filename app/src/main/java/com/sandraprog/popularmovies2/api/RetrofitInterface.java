package com.sandraprog.popularmovies2.api;

import com.sandraprog.popularmovies2.model.MoviesList;
import com.sandraprog.popularmovies2.model.ReviewsList;
import com.sandraprog.popularmovies2.model.TrailersList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sandrapog on 05.06.2018.
 */

public interface RetrofitInterface {
    @GET("movie/popular")
    Call<MoviesList> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MoviesList> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<TrailersList> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsList> getMovieReview(@Path("movie_id") int id, @Query("api_key") String apiKey);
}
