package com.ruthiefloats.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ruthiefloats.popularmoviesstage1.model.Movie;

/**
 * The MainActivity by default populates with a list of the most popular movies.
 * <p/>
 * I based networking code off of the official example:
 * https://developer.android.com/training/basics/network-ops/connecting.html
 */
public class MainActivity extends AppCompatActivity implements MasterFragment.OnPosterSelectedListener {

    private static final String MASTER_FRAGMENT_TAG = "master frag tag";
    private boolean mTwoPane;
    public static final String INSTANCE_STATE_TAG = "heres the movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detail_container) != null) {
            mTwoPane = true;
        }
        //either way, populate the master_container with a MasterFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.master_container, new MasterFragment(), MASTER_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Depending on the option selected, get which set of data
     *
     * @param item The menu item selected
     * @return always false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MasterFragment fragment = (MasterFragment) getSupportFragmentManager().
                findFragmentByTag(MASTER_FRAGMENT_TAG);
        if (item.getItemId() == R.id.menu_sort_popularity) {
            fragment.getData(MasterFragment.POPULAR_RESOURCE_ROOT);
        } else if (item.getItemId() == R.id.menu_sort_rating) {
            fragment.getData(MasterFragment.TOP_RATED_RESOURCE_ROOT);
        }
        return false;
    }

    @Override
    public void onPosterSelected(Movie currentMovie) {
        Toast.makeText(this, "Movie selected is " + currentMovie.getTitle() , Toast.LENGTH_SHORT).show();
        if (mTwoPane){
            Bundle arguments = new Bundle();
            arguments.putParcelable(INSTANCE_STATE_TAG, currentMovie);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else{
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(INSTANCE_STATE_TAG, currentMovie);
            startActivity(intent);
        }
    }
}