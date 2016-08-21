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
import android.widget.Toast;

import com.ruthiefloats.popularmoviesstage2.adapter.PosterAdapter;
import com.ruthiefloats.popularmoviesstage2.data.FavoritesContract;
import com.ruthiefloats.popularmoviesstage2.model.Movie;
import com.ruthiefloats.popularmoviesstage2.parser.MovieParser;
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
    public View mView;
    private OnPosterSelectedListener mCallback;
    private PosterAdapter posterAdapter;
    private List<Movie> mMovieList;

    public MasterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");
        setRetainInstance(true);
        getData(ApiUtility.POPULAR_RESOURCE_ROOT);
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

    // When called builds a valid valid URL for The Movie DB API and starts a DownloadWeb
    // task.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void getData(String resourceRoot) {
        String fullUrl = ApiUtility.BuildUrl(resourceRoot);
        boolean hasConnection = HttpManager.checkConnection(getContext());
        if (hasConnection) {
            new DownloadWebpageTask().execute(fullUrl);
        } else {
            Toast.makeText(getContext(), "No network", Toast.LENGTH_SHORT);
        }
    }

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
    }

    public void getData() {
        // TODO: 8/18/16 : 
        /*
        get a List of Movies and make an adapter get the recyclerview and set everyone up!
         */
        Log.i(LOG_TAG, "Favorites tab selected ");

        Cursor cursor = getContext().getContentResolver().query(FavoritesContract.Favorites.CONTENT_URI,
                new String[]{FavoritesContract.Favorites.COLUMN_API_ID,
                        FavoritesContract.Favorites.COLUMN_TITLE,
                FavoritesContract.Favorites.COLUMN_RATING,
                FavoritesContract.Favorites.COLUMN_POSTER,
                FavoritesContract.Favorites.COLUMN_SYNOPSIS,
                FavoritesContract.Favorites.COLUMN_RELEASE_DATE},
                null,
                null,
                null);

        /*use the results from the cursor to make a movie list and update ui */
        List<Movie> moviesFromCursor = new ArrayList<>();
        if (cursor !=null && cursor.getCount() != 0){
            while (cursor.moveToNext()){
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String vote_average_string = cursor.getString(2);
                // TODO: 8/19/16 de-hardcode this
                String poster_path = "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg";
                String overview = cursor.getString(4);
                String release_date = cursor.getString(5);

                double vote_average = Double.valueOf(vote_average_string);

                moviesFromCursor.add(new Movie(title, release_date, poster_path, vote_average, overview, id));
            }
        }
        posterAdapter = new PosterAdapter(getContext(), moviesFromCursor);
        Log.i(LOG_TAG, "posterAdapter set");
        RecyclerView posterRecyclerView = (RecyclerView) mView.findViewById(R.id.posterRecyclerView);
        posterRecyclerView.setAdapter(posterAdapter);
        posterRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

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
