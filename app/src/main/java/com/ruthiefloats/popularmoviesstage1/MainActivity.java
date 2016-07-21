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
import java.util.List;


/**
 * I based this code off of the official example:
 * https://developer.android.com/training/basics/network-ops/connecting.html
 */
public class MainActivity extends AppCompatActivity {

    private static final String POPULAR_RESOURCE_ROOT = "/movie/popular";
    private static final String TOP_RATED_RESOURCE_ROOT = "/movie/top_rated";
    private static final String DEBUG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData(POPULAR_RESOURCE_ROOT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_sort_popularity) {
            getData(POPULAR_RESOURCE_ROOT);
        } else if (item.getItemId() == R.id.menu_sort_rating) {
            getData(TOP_RATED_RESOURCE_ROOT);
        }
        return false;
    }


    // When user clicks button, calls AsyncTask.
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
    // displayed in the UI by the AsyncTask's onPostExecute method.
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
            List<Movie> movieList = MovieParser.parseFeed(result);
            Log.i("movielist", movieList != null ? movieList.toString() : null);
            MovieImageAdapter adapter = new MovieImageAdapter(MainActivity.this, movieList);

            GridView gridview = (GridView) findViewById(R.id.gridview);
            gridview.setAdapter(adapter);
        }
    }
}