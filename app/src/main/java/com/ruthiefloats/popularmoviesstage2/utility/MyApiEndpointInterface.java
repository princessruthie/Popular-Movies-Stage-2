package com.ruthiefloats.popularmoviesstage2.utility;

import com.ruthiefloats.popularmoviesstage2.model.BOMR;
import com.ruthiefloats.popularmoviesstage2.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created on 8/22/16.
 */
public interface MyApiEndpointInterface {

    ///movie/popular
    @GET("/3/movie/popular")
    Call<List<Movie>> getMovies(@Query("api_key") String api_key);

    @GET("/3/movie/popular")
    Call<Movie> getOneMovie(@Query("api_key") String api_key);

    @GET("/3/movie/popular")
    public Call<BOMR> listRepos(@Query("api_key") String api_key);
}
