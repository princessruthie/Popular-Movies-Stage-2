package com.ruthiefloats.popularmoviesstage2.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Has two methods to take in a String of JSON and return a List<> of
 * Movie or String
 */
public class MovieParser {

    private static final String LOG_TAG = "MovieParser ";

    /**
     * @param content JSON from MovieDB API
     * @return List<String> with user reviews.  If error, a 1-item List
     * stating the problem.
     */
    public static List<String> parseReviews(String content) {
        try {
            JSONObject obj = new JSONObject(content);
            JSONObject reviewsObject = obj.getJSONObject("reviews");
            JSONArray results = reviewsObject.getJSONArray("results");

            List<String> reviewList = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentResult = results.getJSONObject(i);

                String currentReview = currentResult.optString("content", "No review for this result");
                reviewList.add(currentReview);
            }
            Log.i(LOG_TAG, reviewList.toString());
            return reviewList;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(LOG_TAG, "exception caught");
            List<String> errorList = new ArrayList<>();
            errorList.add("Wow.  There aren't any reviews for this film.");
            return errorList;
        }
    }

    public static String parseRuntime(String content) {
        try {
            JSONObject obj = new JSONObject(content);
            String runtime = obj.optString("runtime", "0");
            Log.i(LOG_TAG, "Runtime: " + runtime);
            return runtime;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * checks how many reviews are return by API
     *
     * @param content JSON
     * @return the number of reviews api returns
     */
    public static int getNumReviews(String content) {
        try {
            JSONObject obj = new JSONObject(content);
            JSONObject reviewsObject = obj.getJSONObject("reviews");
            int numReviews = reviewsObject.optInt("total_results", 0);
            return numReviews;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(LOG_TAG, "exception caught");
            return 0;
        }
    }

    public static List<String> getTrailers(String content) {

        try {
            List<String> trailerList = new ArrayList<>();
            JSONObject obj = new JSONObject(content);
            JSONObject reviewsObject = obj.getJSONObject("videos");
            JSONArray videos = reviewsObject.getJSONArray("results");
            //limit the number of trailers
            int maxTrailers = 3;
            int numTrailers = maxTrailers;
            if (videos.length() < maxTrailers) {
                numTrailers = videos.length();
            }

            for (int i = 0; i < numTrailers; i++) {
                JSONObject currentResult = videos.getJSONObject(i);

                String currentKey = currentResult.optString("key", "No review for this result");
                trailerList.add(currentKey);
            }
            Log.i(LOG_TAG, trailerList.toString());
            return trailerList;

        } catch (JSONException e) {
            List<String> trailerList = new ArrayList<>();
            trailerList.add("");
            return trailerList;
        }
    }
}