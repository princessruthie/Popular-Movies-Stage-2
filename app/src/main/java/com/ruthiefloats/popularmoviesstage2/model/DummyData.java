package com.ruthiefloats.popularmoviesstage2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Some placeholder data for when the dev doesn't have internet.
 */
public class DummyData {
    /**
     * Returns a List<Movie> that can be used to make a MovieImageAdapter
     */
    public static List<Movie> getDummyData() {
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens", 209112));
        movies.add(new Movie("Blah", "2016", "/hzjcILBTtVYjVCscmBonnE5wdUX.jpg", 4.7, "Stuff happens", 209112));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens", 209112));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens", 209112));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens", 209112));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens", 209112));

        return movies;
    }

    public static Movie getSingleDummyDatum(){
        return new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens", 209112);
    }

    public static List<String> getDummyReviews(){
        List<String> reviews = new ArrayList<>();
        for (int i = 0; i<35; i++){
            reviews.add("blah blah " + i);
        }
        return reviews;
    }
}
