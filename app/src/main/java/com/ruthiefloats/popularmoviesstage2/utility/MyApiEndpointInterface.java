package com.ruthiefloats.popularmoviesstage2.utility;

import com.ruthiefloats.popularmoviesstage2.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by  on 8/22/16.
 */
public interface MyApiEndpointInterface {

    ///movie/popular
    @GET("/movie/popular")
    Call<List<Movie>> getMovies(@Query("api_key") String api_key);
}
