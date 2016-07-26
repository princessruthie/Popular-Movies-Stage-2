package com.ruthiefloats.popularmoviesstage1.model;

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

        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens"));
        movies.add(new Movie("Blah", "2016", "/hzjcILBTtVYjVCscmBonnE5wdUX.jpg", 4.7, "Stuff happens"));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens"));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens"));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens"));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens"));

        return movies;
    }

    public static Movie getSingleDummyDatum(){
        return new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens");
    }
}
