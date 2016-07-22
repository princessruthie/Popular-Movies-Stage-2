package com.ruthiefloats.popularmoviesstage1.parser;

import com.ruthiefloats.popularmoviesstage1.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Has one method to take in a String of JSON and return a List<Movie>
 */
public class MovieParser {
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

                /*
                Create a new movie List<>.
                 */
                movieList.add(new Movie(title, release_date, poster_path, vote_average, overview));
            }
            return movieList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}