package com.ruthiefloats.popularmoviesstage2;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ruthiefloats.popularmoviesstage2.model.ObjectWithMovieResults;

/**
 * The MainActivity by default populates with a list of the most popular movies.
 * <p/>
 * I based networking code off of the official example:
 * https://developer.android.com/training/basics/network-ops/connecting.html
 */
public class MainActivity extends AppCompatActivity implements MasterFragment.OnPosterSelectedListener {

    public static final String INSTANCE_STATE_TAG = "heres the movie";
    private static final String MASTER_FRAGMENT_TAG = "master frag tag";
    public static final String MASTER_FRAGMENT_BUNDLE_ID = "mFragment";
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
                    .getFragment(savedInstanceState, MASTER_FRAGMENT_BUNDLE_ID);
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
            fragment.getPopularData();
        } else if (item.getItemId() == R.id.menu_sort_rating) {
            fragment.getTopRatedData();
        } else if (item.getItemId() == R.id.menu_show_favorites) {
            fragment.getData();
        }
        return false;
    }


    /*onPosterSelect method is required to implement the OnPosterSelectedListener
    interface.  Depending on whether in a tablet, either replace the detail
    fragment or launch a detail activity.
     */
    @Override
    public void onPosterSelected(ObjectWithMovieResults.Movie currentMovie) {
        Toast.makeText(this, "Movie selected is " + currentMovie.getTitle(), Toast.LENGTH_SHORT).show();
        if (mTwoPane) {
            /*
            pass the current Movie as a parcelable extra and add a DetailFragment
            to the RHS of the ContentView
             */
            Bundle arguments = new Bundle();
            arguments.putParcelable(INSTANCE_STATE_TAG, currentMovie);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
             /*
            pass the current Movie as an extra and start a new
            DetailActivity
             */
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(INSTANCE_STATE_TAG, currentMovie);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        getSupportFragmentManager().putFragment(outState, MASTER_FRAGMENT_BUNDLE_ID, mMasterFragment);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, MASTER_FRAGMENT_BUNDLE_ID, mMasterFragment);
    }
}