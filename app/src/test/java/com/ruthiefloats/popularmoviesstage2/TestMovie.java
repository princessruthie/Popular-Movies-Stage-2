package com.ruthiefloats.popularmoviesstage2;

import com.ruthiefloats.popularmoviesstage2.model.Movie;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static org.junit.Assert.assertEquals;

/**
 * A class to test the Movie model
 */

public class TestMovie {

    @Rule
    public TestName name = new TestName();
    @Rule
    public ReportTestExecution mReportTestExecution = new ReportTestExecution();

    @Before
    public void setup() {
        System.out.println("lets test");
    }

    @After
    public void tearDown() {
        System.out.println("done testing");
    }

    @Test
    public void testMovieConstructor() {
        Movie movie = new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens", 209112);
        assertEquals(movie.getTitle(), "Blah");
        assertEquals("2016", movie.getRelease_date());
        assertEquals("/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", movie.getPoster_path());
        assertEquals(4.7, movie.getVote_average(), .1);
        assertEquals("Stuff happens", movie.getOverview());
        assertEquals(209112, movie.getId());
    }
}
