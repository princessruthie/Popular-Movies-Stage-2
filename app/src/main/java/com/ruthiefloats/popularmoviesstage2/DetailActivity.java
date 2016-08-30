package com.ruthiefloats.popularmoviesstage2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ruthiefloats.popularmoviesstage2.model.ObjectWithMovieResults;

/**
 * An Activity for displaying the details of a single Movie.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        /*
        if there is no Bundle, make a DetailFragment and put it
        in the detail_container
         */
        if (savedInstanceState == null) {
            DetailFragment fragment = DetailFragment
                    .newInstance((ObjectWithMovieResults.Movie) getIntent().getParcelableExtra(MainActivity.INSTANCE_STATE_TAG));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, fragment)
                    .commit();
        }
    }
}