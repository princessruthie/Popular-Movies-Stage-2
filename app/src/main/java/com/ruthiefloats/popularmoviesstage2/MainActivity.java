package com.ruthiefloats.popularmoviesstage2;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ruthiefloats.popularmoviesstage2.model.Movie;

/**
 * The MainActivity by default populates with a list of the most popular movies.
 * <p/>
 * I based networking code off of the official example:
 * https://developer.android.com/training/basics/network-ops/connecting.html
 */
public class MainActivity extends AppCompatActivity implements MasterFragment.OnPosterSelectedListener {

    public static final String INSTANCE_STATE_TAG = "heres the movie";
    private static final String MASTER_FRAGMENT_TAG = "master frag tag";
    MasterFragment mMasterFragment;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if layout has detail_container then using a tablet and
        using a two-pane layout.
         */
        if (findViewById(R.id.detail_container) != null) {
            mTwoPane = true;
        }

        /*
        restore the reference to the saved fragment otherwise make a new
        fragment
         */
        if (savedInstanceState != null) {
            mMasterFragment = (MasterFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "mFragment");
        } else {
            mMasterFragment = new MasterFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.master_container, mMasterFragment, MASTER_FRAGMENT_TAG)
                    .commit();
        }
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


    /*onPosterSelect method is required to implement the OnPosterSelectedListener
    interface.  Depending on whether in a tablet, either replace the detail
    fragment or launch a detail activity.
     */
    @Override
    public void onPosterSelected(Movie currentMovie) {
        Toast.makeText(this, "Movie selected is " + currentMovie.getTitle(), Toast.LENGTH_SHORT).show();
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(INSTANCE_STATE_TAG, currentMovie);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(INSTANCE_STATE_TAG, currentMovie);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        getSupportFragmentManager().putFragment(outState, "mFragment", mMasterFragment);
    }
}