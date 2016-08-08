package com.ruthiefloats.popularmoviesstage2.parser;

import android.util.Log;

import com.ruthiefloats.popularmoviesstage2.model.Movie;

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

    public static List<Movie> parseFeed(String content) {

        try {
            /*
            Construct a Json object out of the passed-in String,
            get a Json array and initialize an empty List of Articles.
             */

            JSONObject obj = new JSONObject(content);
            JSONArray results = obj.getJSONArray("results");

            /*
            Debugging log.
             */

            List<Movie> movieList = new ArrayList<>();
            /*Initialize the tags object now so that it doesn't */

            /*
            Iterate over the elements of the JSON array.
             */
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentResult = results.getJSONObject(i);

                /*
                Get the String info and if it's missing, say so.
                 */

                String poster_path = currentResult.optString("poster_path", "");
                String overview = currentResult.optString("overview", "No summary available");
                String release_date = currentResult.optString("release_date", "No date available");
                String title = currentResult.optString("original_title", "No title info");
                double vote_average = currentResult.optDouble("vote_average");
                int id = currentResult.optInt("id");

                /*
                Create a new movie List<>.
                 */
                movieList.add(new Movie(title, release_date, poster_path, vote_average, overview, id));
            }
            return movieList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> parseReviews(String content) {
        // TODO: 8/5/16 parse out whatever reviews there may be.
        try {
            JSONObject obj = new JSONObject(content);
            JSONArray results = obj.getJSONArray("results");

            List<String> reviewList = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentResult = results.getJSONObject(i);

                String currentReview = currentResult.optString("content", "No review for this result");
                reviewList.add(currentReview);
                Log.i(LOG_TAG, reviewList.toString());
            }
            return reviewList;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(LOG_TAG, "exception caught");
            return null;
        }
    }

    public static int getNumReviews(String content){
        try {
            JSONObject obj = new JSONObject(content);
            int numReviews = obj.optInt("total_results", 0);
            return numReviews;
        } catch (JSONException e){
            e.printStackTrace();
            Log.i(LOG_TAG, "exception caught");
            return 0;
        }
    }
}