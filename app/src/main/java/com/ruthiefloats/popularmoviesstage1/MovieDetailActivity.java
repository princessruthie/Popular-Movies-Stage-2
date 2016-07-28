package com.ruthiefloats.popularmoviesstage1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ruthiefloats.popularmoviesstage1.model.Movie;

/**
 * An Activity for displaying the details of a single Movie.
 */

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

       if (savedInstanceState == null){
           MovieDetailFragment fragment = MovieDetailFragment
                   .newInstance((Movie) getIntent().getParcelableExtra(MainActivity.INSTANCE_STATE_TAG));
//           Bundle arguments = new Bundle();
//           arguments.putParcelable(MovieDetailFragment.ARG_ITEM_ID,
//                   getIntent().getParcelableExtra(MainActivity.INSTANCE_STATE_TAG));
//           MovieDetailFragment fragment = new MovieDetailFragment();
//           fragment.setArguments(arguments);
           getSupportFragmentManager().beginTransaction()
                   .add(R.id.detail_container, fragment)
                   .commit();
       }

//        getSupportFragmentManager().beginTransaction().
//                replace(R.id.detail_container, new MovieDetailFragment())
//                .commit();

    }
}