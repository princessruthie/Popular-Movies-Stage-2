package com.ruthiefloats.popularmoviesstage2.utility;

import android.util.Log;

import com.ruthiefloats.popularmoviesstage2.BuildConfig;

/**
 * A class for Api Strings and uri construction
 */
public class ApiUtility {
    public static final String LOG_TAG = "ApiUtility";
    private static final String PHOTOS_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String PHOTOS_SIZE_URL = "w185/";

    private static final String youtubePrefix = "http://img.youtube.com/vi/";
    private static final String youtubePostfix = "/0.jpg";
    private static final String youtubeTrailerPrefix = "https://www.youtube.com/watch?v=";

    /**
     * Roots for the two MovieDB APIs used
     */
    public static final String POPULAR_RESOURCE_ROOT = "/movie/popular";
    public static final String TOP_RATED_RESOURCE_ROOT = "/movie/top_rated";

    /**
     * takes the poster URL provided by the API response and builds the entire valid URL
     */
    public static String getCompletePhotoUrl(String photoUrl) {
        String completeUrl = PHOTOS_BASE_URL +
                PHOTOS_SIZE_URL +
                photoUrl;
        return completeUrl;
    }

    public static String BuildUrl(String resourceRoot) {
        String baseUrl = "http://api.themoviedb.org/3";
        String apiKeyUrl = "?api_key=" +
                BuildConfig.DEVELOPER_API_KEY;
        String fullUrl = (new StringBuilder(baseUrl +
                resourceRoot +
                apiKeyUrl)).
                toString();
        return fullUrl;
    }

    public static String BuildUrl(String resourceRoot, String appendix) {
        String baseUrl = "http://api.themoviedb.org/3";
        String apiKeyUrl = "?api_key=" +
                BuildConfig.DEVELOPER_API_KEY;
        String fullUrl = (new StringBuilder(baseUrl +
                resourceRoot +
                apiKeyUrl +
                appendix)).
                toString();
        Log.i(LOG_TAG, "full Url: " + fullUrl);
        return fullUrl;
    }

    /*given a youtube trailer id, generate the thumbnail url */
    public static String getPosterUrlFromTrailerId(String trailerIdString) {

        return youtubePrefix + trailerIdString + youtubePostfix;
    }

    public static String getTrailerUrlFromTrailerId(String trailerId){
        return youtubeTrailerPrefix + trailerId;
    }
}
