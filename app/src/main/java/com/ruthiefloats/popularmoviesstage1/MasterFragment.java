package com.ruthiefloats.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ruthiefloats.popularmoviesstage1.adapter.MovieImageAdapter;
import com.ruthiefloats.popularmoviesstage1.model.Movie;
import com.ruthiefloats.popularmoviesstage1.parser.MovieParser;

import java.io.IOException;
import java.util.List;


/**
 *
 */
public class MasterFragment extends Fragment {

    /**
     * Strings for keys in savedInstanceState
     */
    private static final String GRID_VIEW_POSITION = "grid view position";
    private static final String MOVIE_LIST = "list";
    /**
     * Roots for the two APIS used
     */
    public static final String POPULAR_RESOURCE_ROOT = "/movie/popular";
    public static final String TOP_RATED_RESOURCE_ROOT = "/movie/top_rated";

    private static final String DEBUG_TAG = "MainActivity";
    private static final String CURRENT_MOVIE_INTENT_STRING = "movie intent here";

    private GridView mGridView;
    private List<Movie> mMovieList;

    public MasterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_master, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.gridview);
        /**If restoring, use the stored data.  Otherwise get data. */
        if (savedInstanceState != null) {
            mMovieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            if (mMovieList != null) {
                MovieImageAdapter adapter = new MovieImageAdapter(getActivity(), mMovieList);
                mGridView.setAdapter(adapter);
            }
        } else {
            getData(POPULAR_RESOURCE_ROOT);
        }


        return rootView;
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
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(sortByVoteFullUrl);
        } else {
            Toast.makeText(getContext(), "No network", Toast.LENGTH_SHORT);
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
            MovieImageAdapter adapter = new MovieImageAdapter(getContext(), mMovieList);
            mGridView.setAdapter(adapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), "Second catch", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtra(CURRENT_MOVIE_INTENT_STRING, mMovieList.get(position));
                    getActivity().startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
