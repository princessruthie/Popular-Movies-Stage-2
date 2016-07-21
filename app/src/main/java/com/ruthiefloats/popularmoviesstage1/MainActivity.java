package com.ruthiefloats.popularmoviesstage1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.ruthiefloats.popularmoviesstage1.adapter.MovieImageAdapter;
import com.ruthiefloats.popularmoviesstage1.model.Movie;
import com.ruthiefloats.popularmoviesstage1.parser.MovieParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


/**
 * I based this code off of the official example:
 * https://developer.android.com/training/basics/network-ops/connecting.html
 */
public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        List<Movie> movies = DummyData.getDummyData();
//        MovieImageAdapter adapter = new MovieImageAdapter(this, movies);
//
//        GridView gridview = (GridView) findViewById(R.id.gridview);
//        gridview.setAdapter(adapter);

        // TODO: 7/21/16 refactor so that calling getData doesn't need to
        //know exact String  maybe a static string in getData when that's
        //refactored into a class
        getData("&sort_by=popularity.desc");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            if (item.getItemId() == R.id.menu_sort_popularity) {
                getData("&sort_by=popularity.desc");
            } else if (item.getItemId() == R.id.menu_sort_rating){
                getData("&sort_by=vote_average.desc");
            }
        return false;
    }


    // When user clicks button, calls AsyncTask.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void getData(String sortString) {
        String baseUrl = "http://api.themoviedb.org/3";
        String discoverUrl = "/discover/movie";
        String apiKeyUrl = "/?api_key=" +
                BuildConfig.DEVELOPER_API_KEY;
        //change getData to take in the sort String.
        //then change menu to pass in the sort String
        //then change full Url.
        String sortByVote = "&sort_by=vote_average.desc";
        String sortByPopularity = "&sort_by=popularity.desc";
        String sortByVoteFullUrl = (new StringBuilder(baseUrl + discoverUrl + apiKeyUrl + sortString)).toString();
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
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            List<Movie> movieList = MovieParser.parseFeed(result);
            Log.i("movielist", movieList != null ? movieList.toString() : null);
//            List<Movie> movies = DummyData.getDummyData();
            MovieImageAdapter adapter = new MovieImageAdapter(MainActivity.this, movieList);

            GridView gridview = (GridView) findViewById(R.id.gridview);
            gridview.setAdapter(adapter);
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response code is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = null;
        StringBuffer stringBuffer = new StringBuffer();

        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
        }

        Log.d(DEBUG_TAG, "The response is: " + stringBuffer.toString());
        return stringBuffer.toString();
    }

}