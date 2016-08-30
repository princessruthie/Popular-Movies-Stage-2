package com.ruthiefloats.popularmoviesstage2.utility;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A class for Api Strings and uri construction
 */
public class ApiUtility {
    public static final String LOG_TAG = "ApiUtility";

    public static MovieDbEndpointInterface getMovieDbEndpointInterface() {
        Gson gson = new Gson();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieDbUtility.RETROFIT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(MovieDbEndpointInterface.class);
    }

    public static class MovieDbUtility {

        /**
         * Strings for MovieDB roots used
         */

        public static final String RETROFIT_REVIEWS_APPENDIX = "reviews,videos";
        public static final String RETROFIT_BASE_URL = "http://api.themoviedb.org/";
        public static final String RETROFIT_API_KEY_PREFIX = "api_key";
        private static final String PHOTOS_BASE_URL = "http://image.tmdb.org/t/p/";
        private static final String PHOTOS_SIZE_URL = "w185/";

        /**
         * takes the poster URL provided by the API response and builds the entire valid URL
         */
        public static String getCompletePhotoUrl(String photoUrl) {
            String completeUrl = PHOTOS_BASE_URL +
                    PHOTOS_SIZE_URL +
                    photoUrl;
            return completeUrl;
        }
    }

    public static class YoutubeUtility {

        private static final String YOUTUBE_IMAGE_PREFIX = "http://img.youtube.com/vi/";
        private static final String YOUTUBE_IMAGE_POSTFIX = "/0.jpg";
        private static final String YOUTUBE_TRAILER_PREFIX = "https://www.youtube.com/watch?v=";

        /*given a youtube trailer id, generate the thumbnail url */
        public static String getPosterUrlFromTrailerId(String trailerIdString) {
            return YOUTUBE_IMAGE_PREFIX + trailerIdString + YOUTUBE_IMAGE_POSTFIX;
        }

        /*given a youtube trailer id, generate the trailer url */
        public static String getTrailerUrlFromTrailerId(String trailerId) {
            return YOUTUBE_TRAILER_PREFIX + trailerId;
        }
    }
}
