package com.ruthiefloats.popularmoviesstage1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.ruthiefloats.popularmoviesstage1.adapter.MovieImageAdapter;
import com.ruthiefloats.popularmoviesstage1.model.Movie;
import com.ruthiefloats.popularmoviesstage1.parser.MovieParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The MainActivity by default populates with a list of the most popular movies.
 * <p/>
 * I based networking code off of the official example:
 * https://developer.android.com/training/basics/network-ops/connecting.html
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Strings for keys in savedInstanceState
     */
    private static final String GRID_VIEW_POSITION = "grid view position";
    private static final String MOVIE_LIST = "list";
    /**
     * Roots for the two APIS used
     */
    private static final String POPULAR_RESOURCE_ROOT = "/movie/popular";
    private static final String TOP_RATED_RESOURCE_ROOT = "/movie/top_rated";

    private static final String DEBUG_TAG = "MainActivity";

    private GridView mGridView;
    private List<Movie> mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView = (GridView) findViewById(R.id.gridview);
        /**If restoring, use the stored data.  Otherwise get data. */
        if (savedInstanceState != null) {
            mMovieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            if (mMovieList != null) {
                MovieImageAdapter adapter = new MovieImageAdapter(MainActivity.this, mMovieList);
                mGridView.setAdapter(adapter);
            }
        } else {
            getData(POPULAR_RESOURCE_ROOT);
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
        if (item.getItemId() == R.id.menu_sort_popularity) {
            getData(POPULAR_RESOURCE_ROOT);
        } else if (item.getItemId() == R.id.menu_sort_rating) {
            getData(TOP_RATED_RESOURCE_ROOT);
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /**Record both GridView position and the List<Movie> */
        int currentPosition = mGridView.getFirstVisiblePosition();
        outState.putInt(GRID_VIEW_POSITION, currentPosition);
        if (mMovieList != null) {
            outState.putParcelableArrayList(MOVIE_LIST, (ArrayList<Movie>) mMovieList);
        }
    }

    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        /**Restore the scroll position */
        mGridView.setSelection(savedInstanceState.getInt(GRID_VIEW_POSITION));
    }

    // When called builds a valid valid URL for The Movie DB API and starts a DownloadWeb
    // task.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void getData(String resourceRoot) {

        String baseUrl = "http://api.themoviedb.org/3";
        String apiKeyUrl = "/?api_key=" +
                BuildConfig.DEVELOPER_API_KEY;
        String sortByVoteFullUrl = (new StringBuilder(baseUrl +
                resourceRoot +
                apiKeyUrl)).
                toString();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(sortByVoteFullUrl);
        } else {
            Toast.makeText(this, "No network", Toast.LENGTH_SHORT);
        }
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // parsed into a List<Movie> and used to construct/set the GridView's adapter in
    // the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return HttpManager.downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            mMovieList = MovieParser.parseFeed(result);
            MovieImageAdapter adapter = new MovieImageAdapter(MainActivity.this, mMovieList);
            mGridView.setAdapter(adapter);
        }
    }
}