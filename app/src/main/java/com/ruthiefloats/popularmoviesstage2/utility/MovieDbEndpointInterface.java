package com.ruthiefloats.popularmoviesstage2.utility;

import com.ruthiefloats.popularmoviesstage2.model.ObjectWithMovieDetails;
import com.ruthiefloats.popularmoviesstage2.model.ObjectWithMovieResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.ruthiefloats.popularmoviesstage2.utility.ApiUtility.MovieDbUtility.API_KEY_QUERY;
import static com.ruthiefloats.popularmoviesstage2.utility.ApiUtility.MovieDbUtility.APPEND_TO_RESPONSE_QUERY;

/**
 * An interface for retrofit
 */
public interface MovieDbEndpointInterface {

    @GET("3/movie/top_rated" + "?" + API_KEY_QUERY)
    Call<ObjectWithMovieResults> getTopRated();

    @GET("3/movie/popular" + "?" + API_KEY_QUERY)
    Call<ObjectWithMovieResults> getPopular();

    @GET("3/movie/popular" + "?" + API_KEY_QUERY )
    Call<ObjectWithMovieResults> getMorePopular(@Query("page") int pagenumber);

    @GET("3/movie/top_rated" + "?" + API_KEY_QUERY )
    Call<ObjectWithMovieResults> getMoreTopRated(@Query("page") int pagenumber);

    /*get additional info for movie {id} including reviews and videos */
    @GET("3/movie/{id}" + "?" + API_KEY_QUERY + "&" + APPEND_TO_RESPONSE_QUERY)
    Call<ObjectWithMovieDetails> getMovieDetails(@Path("id") int id);
}
