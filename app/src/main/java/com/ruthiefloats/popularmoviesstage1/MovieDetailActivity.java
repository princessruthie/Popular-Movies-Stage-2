package com.ruthiefloats.popularmoviesstage1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * An Activity for displaying the details of a single Movie.
 */

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.detail_container, new MovieDetailFragment())
                .commit();

    }
}