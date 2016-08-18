package com.ruthiefloats.popularmoviesstage2;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ruthiefloats.popularmoviesstage2.adapter.MovieImageAdapter;
import com.ruthiefloats.popularmoviesstage2.adapter.PosterAdapter;
import com.ruthiefloats.popularmoviesstage2.data.FavoritesContract;
import com.ruthiefloats.popularmoviesstage2.data.FavoritesDataSource;
import com.ruthiefloats.popularmoviesstage2.model.DummyData;
import com.ruthiefloats.popularmoviesstage2.model.Movie;
import com.ruthiefloats.popularmoviesstage2.parser.MovieParser;

import java.io.IOException;
import java.util.List;


/**
 *
 */
public class MasterFragment extends Fragment {

    /**
     * Roots for the two APIs used
     */
    public static final String POPULAR_RESOURCE_ROOT = "/movie/popular";
    public static final String TOP_RATED_RESOURCE_ROOT = "/movie/top_rated";
    /**
     * Strings for keys in savedInstanceState
     */
    private static final String MOVIE_LIST = "list";
    private static final String INT_PLACEHOLDER = "int placeholder";
    private static final String LOG_TAG = "MasterFragment";
    OnPosterSelectedListener mCallback;
//    private GridView mGridView;
    private List<Movie> mMovieList;
    public View mView;
    PosterAdapter posterAdapter;

    public MasterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");
        setRetainInstance(true);
        getData(POPULAR_RESOURCE_ROOT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(LOG_TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_master, container, false);
        mView = rootView;
//        Log.i(DEBUG_TAG, "oncreateview");
//        mGridView = (GridView) rootView.findViewById(R.id.gridview);
//        /**If restoring, use the stored data.  Otherwise get data. */
//        if (savedInstanceState != null) {
//            Log.i(DEBUG_TAG, "using existing data");
//            if (mMovieList != null) {
//                MovieImageAdapter adapter = new MovieImageAdapter(getActivity(), mMovieList);
//                mGridView.setAdapter(adapter);
//            }
//        } else {
//            Log.i(DEBUG_TAG, "getting fresh data");
//            getData(POPULAR_RESOURCE_ROOT);
////            useOfflinePlaceholderData();
//        }
        return rootView;
    }

    // When called builds a valid valid URL for The Movie DB API and starts a DownloadWeb
    // task.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void getData(String resourceRoot) {
        String fullUrl = HttpManager.BuildUrl(resourceRoot);
        boolean hasConnection = HttpManager.CheckConnection(getContext());
        if (hasConnection) {
            new DownloadWebpageTask().execute(fullUrl);
        } else {
            Toast.makeText(getContext(), "No network", Toast.LENGTH_SHORT);
        }
    }

    /*
    a method for offline debugging (plane/train)
     */
//    private void useOfflinePlaceholderData() {
//        mMovieList = DummyData.getDummyData();
//        MovieImageAdapter adapter = new MovieImageAdapter(getContext(), mMovieList);
//        mGridView.setAdapter(adapter);
//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mCallback.onPosterSelected(mMovieList.get(position));
//            }
//        });
//    }

    @Override
    public void onAttach(Context context) {
        Log.i(LOG_TAG, "onAttach");
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
        Log.i(LOG_TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);

//        Log.i(DEBUG_TAG, "onviewcreated");
//        if (savedInstanceState != null) {
//            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    mCallback.onPosterSelected(mMovieList.get(position));
//                }
//            });
//        }
    }

    public void getData() {
        //doing nothing
//        mMovieList = MovieParser.parseFeed(result);
//        Log.i(LOG_TAG, mMovieList.toString());
//        posterAdapter = new PosterAdapter(getContext(), mMovieList);
//        Log.i(LOG_TAG, "posterAdapter set");
//        RecyclerView posterRecyclerView = (RecyclerView) mView.findViewById(R.id.posterRecyclerView);
//        posterRecyclerView.setAdapter(posterAdapter);
//        posterRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        /*
        get a List of Movies and make an adapter get the recyclerview and set everyone up!
         */
        Log.i(LOG_TAG, "Favorites tab selected ");
        FavoritesDataSource dataSource = new FavoritesDataSource(getContext());
        dataSource.printAllMovies();

        Cursor cursor = getContext().getContentResolver().query(FavoritesContract.Favorites.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (cursor != null){
            Log.i(LOG_TAG, "Number of results in cursor: " + cursor.getCount());
        }
    }

    public interface OnPosterSelectedListener {
        void onPosterSelected(Movie currentMovie);
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // parsed into a List<Movie> and used to construct/set the recyclerview's adapter in
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

        // onPostExecute sets the adapter
        @Override
        protected void onPostExecute(String result) {
            mMovieList = MovieParser.parseFeed(result);
            Log.i(LOG_TAG, mMovieList.toString());
            posterAdapter = new PosterAdapter(getContext(), mMovieList);
            Log.i(LOG_TAG, "posterAdapter set");
            RecyclerView posterRecyclerView = (RecyclerView) mView.findViewById(R.id.posterRecyclerView);
            posterRecyclerView.setAdapter(posterAdapter);
            posterRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
    }
}
