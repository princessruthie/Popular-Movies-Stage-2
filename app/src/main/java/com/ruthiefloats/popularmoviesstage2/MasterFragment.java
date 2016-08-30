package com.ruthiefloats.popularmoviesstage2;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruthiefloats.popularmoviesstage2.adapter.PosterAdapter;
import com.ruthiefloats.popularmoviesstage2.data.FavoritesContract;
import com.ruthiefloats.popularmoviesstage2.model.ObjectWithMoviesWithin;
import com.ruthiefloats.popularmoviesstage2.utility.ApiUtility;
import com.ruthiefloats.popularmoviesstage2.utility.HttpManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * The master fragment in a master-detail flow.
 */
public class MasterFragment extends Fragment {

    private static final String LOG_TAG = "MasterFragment";
    public static final String BUNDLE_KEY_FAVORITES = "faves";
    public static final String BUNDLE_KEY_MOVIE_LIST = "movie list key";
    public View mView;
    private OnPosterSelectedListener mCallback;
    private List<ObjectWithMoviesWithin.ResultsBean> mMovieList;
    /*whether using data from the Favorites Provider */
    private boolean mFavorites;


    public MasterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");
        setRetainInstance(true);

        getData(ApiUtility.MovieDbUtility.POPULAR_RESOURCE_ROOT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(LOG_TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_master, container, false);
        mView = rootView;
        return rootView;
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
    }

    /* When called builds a valid valid URL for The Movie DB API and starts a DownloadWeb
     task.
     Before attempting to fetch the URL, makes sure that there is a network connection.
     */
    public void getData(String resourceRoot) {
        mFavorites = false;
        String fullUrl = ApiUtility.MovieDbUtility.buildUrl(resourceRoot);
        boolean hasConnection = HttpManager.checkConnection();
        if (hasConnection) {
            new DownloadWebpageTask().execute(fullUrl);
        } else {
            Toast.makeText(getContext(), "No network", Toast.LENGTH_SHORT);
        }
    }

    /*Calling getData without a resource root gets local data */
    public void getData() {
        mFavorites = true;
        Cursor cursor = getContext().getContentResolver().query(FavoritesContract.Favorites.CONTENT_URI,
                new String[]{FavoritesContract.Favorites.COLUMN_API_ID,
                        FavoritesContract.Favorites.COLUMN_TITLE,
                        FavoritesContract.Favorites.COLUMN_RATING,
                        FavoritesContract.Favorites.COLUMN_SYNOPSIS,
                        FavoritesContract.Favorites.COLUMN_RELEASE_DATE},
                null,
                null,
                null);

        /*use the results from the cursor to make a movie list and update ui */
        mMovieList = new ArrayList<>();
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String vote_average_string = cursor.getString(2);
                String overview = cursor.getString(3);
                String release_date = cursor.getString(4);

                double vote_average = Double.valueOf(vote_average_string);
                /*the poster will be set by the adapter, so pass null*/
                // TODO: 8/29/16 reinstate this wasteful workaround
                mMovieList.add(new ObjectWithMoviesWithin.ResultsBean(null, overview, release_date, id, title, vote_average));
            }
            cursor.close();
            populateRecyclerView();
        } else {
            // TODO: 8/26/16 in event that user has no favorites, prompt them
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movie list key", (ArrayList<? extends Parcelable>) mMovieList);
        outState.putBoolean(BUNDLE_KEY_FAVORITES, mFavorites);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mMovieList = savedInstanceState.getParcelableArrayList(BUNDLE_KEY_MOVIE_LIST);
            mFavorites = savedInstanceState.getBoolean(BUNDLE_KEY_FAVORITES);
            populateRecyclerView();
        }
    }

    private void populateRecyclerView() {
        RecyclerView rv = (RecyclerView) mView.findViewById(R.id.posterRecyclerView);
        PosterAdapter posterAdapter = new PosterAdapter(getContext(), mMovieList, mFavorites);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv.setAdapter(posterAdapter);
    }

    public interface OnPosterSelectedListener {
        void onPosterSelected(ObjectWithMoviesWithin.ResultsBean currentMovie);
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
//            mMovieList = MovieParser.parseFeed(result);
            Gson gson = new Gson();
            ObjectWithMoviesWithin objectWithMoviesWithin = gson.fromJson(result, ObjectWithMoviesWithin.class);
            mMovieList = objectWithMoviesWithin.getResults();
            populateRecyclerView();
        }
    }
}
