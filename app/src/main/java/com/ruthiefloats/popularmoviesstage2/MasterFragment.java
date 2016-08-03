package com.ruthiefloats.popularmoviesstage2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ruthiefloats.popularmoviesstage2.adapter.MovieImageAdapter;
import com.ruthiefloats.popularmoviesstage2.model.DummyData;
import com.ruthiefloats.popularmoviesstage2.model.Movie;
import com.ruthiefloats.popularmoviesstage2.parser.MovieParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class MasterFragment extends Fragment {

    /**
     * Strings for keys in savedInstanceState
     */
    private static final String MOVIE_LIST = "list";
    private static final String INT_PLACEHOLDER = "int placeholder";

    /**
     * Roots for the two APIs used
     */
    public static final String POPULAR_RESOURCE_ROOT = "/movie/popular";
    public static final String TOP_RATED_RESOURCE_ROOT = "/movie/top_rated";

    private static final String DEBUG_TAG = "MasterFragment";

    private GridView mGridView;
    private List<Movie> mMovieList;

    OnPosterSelectedListener mCallback;

    public MasterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_master, container, false);
        Log.i(DEBUG_TAG, "oncreateview");
        mGridView = (GridView) rootView.findViewById(R.id.gridview);
        /**If restoring, use the stored data.  Otherwise get data. */
        if (savedInstanceState != null) {
            Log.i(DEBUG_TAG, "using existing data");
            if (mMovieList != null) {
                MovieImageAdapter adapter = new MovieImageAdapter(getActivity(), mMovieList);
                mGridView.setAdapter(adapter);
            }
        } else {
            Log.i(DEBUG_TAG, "getting fresh data");
            getData(POPULAR_RESOURCE_ROOT);
//            useOfflinePlaceholderData();
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
                    mCallback.onPosterSelected(mMovieList.get(position));
                }
            });
        }
    }

    /*
    a method for offline debugging (plane/train)
     */
    private void useOfflinePlaceholderData() {
        mMovieList = DummyData.getDummyData();
        MovieImageAdapter adapter = new MovieImageAdapter(getContext(), mMovieList);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onPosterSelected(mMovieList.get(position));
            }
        });
    }


    public interface OnPosterSelectedListener {
        void onPosterSelected(Movie currentMovie);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnPosterSelectedListener) context;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(DEBUG_TAG, "onviewcreated");
        if (savedInstanceState != null) {
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mCallback.onPosterSelected(mMovieList.get(position));
                }
            });
        }
    }
}
