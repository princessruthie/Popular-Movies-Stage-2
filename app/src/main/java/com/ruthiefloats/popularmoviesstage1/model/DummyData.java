package com.ruthiefloats.popularmoviesstage1.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fieldsru on 7/18/16.
 */
public class DummyData {
    public static List<Movie> getDummyData(){
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg",4.7, "Stuff happens"));
        movies.add(new Movie("Blah", "2016", "/hzjcILBTtVYjVCscmBonnE5wdUX.jpg",4.7, "Stuff happens"));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg",4.7, "Stuff happens"));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg",4.7, "Stuff happens"));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg",4.7, "Stuff happens"));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg",4.7, "Stuff happens"));

        return movies;
    }
}
