package com.ruthiefloats.popularmoviesstage2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ruthiefloats.popularmoviesstage2.model.Movie;

/**
 * An Activity for displaying the details of a single Movie.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

       if (savedInstanceState == null){
           DetailFragment fragment = DetailFragment
                   .newInstance((Movie) getIntent().getParcelableExtra(MainActivity.INSTANCE_STATE_TAG));
//           Bundle arguments = new Bundle();
//           arguments.putParcelable(DetailFragment.ARG_ITEM_ID,
//                   getIntent().getParcelableExtra(MainActivity.INSTANCE_STATE_TAG));
//           DetailFragment fragment = new DetailFragment();
//           fragment.setArguments(arguments);
           getSupportFragmentManager().beginTransaction()
                   .add(R.id.detail_container, fragment)
                   .commit();
       }

//        getSupportFragmentManager().beginTransaction().
//                replace(R.id.detail_container, new DetailFragment())
//                .commit();

    }
}