package com.ruthiefloats.popularmoviesstage2.utility;

import com.ruthiefloats.popularmoviesstage2.model.ObjectWithMovieResults;
import com.ruthiefloats.popularmoviesstage2.model.ObjectWithMovieDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * An interface for retrofit methods
 */
public interface MovieDbEndpointInterface {

    @GET("3/movie/top_rated")
    Call<ObjectWithMovieResults> getTopRated(@Query(ApiUtility.MovieDbUtility.RETROFIT_API_KEY_PREFIX) String apikey);

    @GET("3/movie/popular")
    Call<ObjectWithMovieResults> getPopular(@Query(ApiUtility.MovieDbUtility.RETROFIT_API_KEY_PREFIX) String apikey);

    @GET("3/movie/{id}")
    Call<ObjectWithMovieDetails> getMovieDetails(@Path ("id") int id,
                                                 @Query(ApiUtility.MovieDbUtility.RETROFIT_API_KEY_PREFIX) String apikey,
                                                 @Query("append_to_response") String appendix);

}
