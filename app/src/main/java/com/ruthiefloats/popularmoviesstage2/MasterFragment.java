package com.ruthiefloats.popularmoviesstage2;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ruthiefloats.popularmoviesstage2.adapter.EndlessScrollListener;
import com.ruthiefloats.popularmoviesstage2.adapter.PosterAdapter;
import com.ruthiefloats.popularmoviesstage2.data.FavoritesContract;
import com.ruthiefloats.popularmoviesstage2.model.ObjectWithMovieResults;
import com.ruthiefloats.popularmoviesstage2.utility.ApiUtility;
import com.ruthiefloats.popularmoviesstage2.utility.HttpManager;
import com.ruthiefloats.popularmoviesstage2.utility.MovieDbEndpointInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ruthiefloats.popularmoviesstage2.model.ObjectWithMovieResults.Movie;

/**
 * The master fragment in a master-detail flow.
 */
public class MasterFragment extends Fragment {

    public static final String BUNDLE_KEY_FAVORITES = "faves";
    public static final String BUNDLE_KEY_MOVIE_LIST = "movie list key";

    private static final String LOG_TAG = "MasterFragment";
    private View mView;
    private OnPosterSelectedListener mCallback;
    private List<Movie> mMovieList;
    /*whether using offline data from the Favorites Provider */
    private boolean mUsingOfflineData;
    private int pageNumber;
    /*An int to hold status: which set of movies are currently being viewed */
    private int currentMovieSet;
    /*Constant ints to indicate status */
    private static final int POPULAR_SET = 100;
    private static final int TOP_RATED_SET = 101;
    private static final int OFFLINE_SET = 102;

    public MasterFragment() {
        // Required empty public constructor
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
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
            getPopularData();
        } else if (item.getItemId() == R.id.menu_sort_rating) {
            getTopRatedData();
        } else if (item.getItemId() == R.id.menu_show_favorites) {
            getOfflineData();
        }
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.i(LOG_TAG, "onCreate");
        setRetainInstance(true);
        getTopRatedData();
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mMovieList = savedInstanceState.getParcelableArrayList(BUNDLE_KEY_MOVIE_LIST);
            mUsingOfflineData = savedInstanceState.getBoolean(BUNDLE_KEY_FAVORITES);
            populateRecyclerView();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movie list key", (ArrayList<? extends Parcelable>) mMovieList);
        outState.putBoolean(BUNDLE_KEY_FAVORITES, mUsingOfflineData);
    }

    public void getTopRatedData() {
        mUsingOfflineData = false;
        pageNumber = 1;
        currentMovieSet = TOP_RATED_SET;

        if (HttpManager.checkConnection()) {
            MovieDbEndpointInterface apiService = ApiUtility.getMovieDbEndpointInterface();
            Call<ObjectWithMovieResults> call = apiService.getTopRated();
            fetchInitialMovies(call);
        }
    }

    public void getPopularData() {
        currentMovieSet = POPULAR_SET;
        pageNumber = 1;
        mUsingOfflineData = false;
        MovieDbEndpointInterface apiService = ApiUtility.getMovieDbEndpointInterface();
        Call<ObjectWithMovieResults> call = apiService.getPopular();
        fetchInitialMovies(call);
    }

    /*Calling getOfflineData gets local data */
    public void getOfflineData() {
        currentMovieSet = OFFLINE_SET;
        mUsingOfflineData = true;
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
        // TODO: ultimately will switch to Realm and this won't be a thing
        mMovieList = new ArrayList<>();
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int idColumnIndex = cursor.getColumnIndex(FavoritesContract.Favorites.COLUMN_API_ID);
                int titleColumnIndex = cursor.getColumnIndex(FavoritesContract.Favorites.COLUMN_TITLE);
                int voteColumnIndex = cursor.getColumnIndex(FavoritesContract.Favorites.COLUMN_RATING);
                int overviewColumnIndex = cursor.getColumnIndex(FavoritesContract.Favorites.COLUMN_SYNOPSIS);
                int releaseDateColumnIndex = cursor.getColumnIndex(FavoritesContract.Favorites.COLUMN_RELEASE_DATE);

                int id = cursor.getInt(idColumnIndex);
                String title = cursor.getString(titleColumnIndex);
                String vote_average_string = cursor.getString(voteColumnIndex);
                String overview = cursor.getString(overviewColumnIndex);
                String release_date = cursor.getString(releaseDateColumnIndex);

                double vote_average = Double.valueOf(vote_average_string);
                /*the poster will be set by the adapter, so pass null*/
                mMovieList.add(new Movie(null, overview, release_date, id, title, vote_average));
            }
            cursor.close();
            populateRecyclerView();
        } else {
            // TODO: in event that user has no favorites, maybe prompt them
        }
    }

    /*Use Call<> to set mMovies then populate the recyclerview */
    private void fetchInitialMovies(Call<ObjectWithMovieResults> call) {
        call.enqueue(new Callback<ObjectWithMovieResults>() {
            @Override
            public void onResponse(Call<ObjectWithMovieResults> call, Response<ObjectWithMovieResults> response) {
                ObjectWithMovieResults obj = response.body();
                mMovieList = obj.getMovieList();
                populateRecyclerView();
                Log.i(LOG_TAG, "so retrofit did something");
            }

            @Override
            public void onFailure(Call<ObjectWithMovieResults> call, Throwable t) {
                Log.i(LOG_TAG, "so retrofit not so much");
            }
        });
    }

    /**
     * Get RecyclerView, set LayoutManager, set Adapter and add scroll listener
     */
    private void populateRecyclerView() {
        RecyclerView rv = (RecyclerView) mView.findViewById(R.id.posterRecyclerView);
        final PosterAdapter posterAdapter = new PosterAdapter(getContext(), mMovieList, mUsingOfflineData);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rv.addOnScrollListener(new EndlessScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchMoreMovies(posterAdapter);
            }
        });
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(posterAdapter);
    }

    /*
    Get additional movies and add them to the List and notify the adapter
     */
    private void fetchMoreMovies(final PosterAdapter posterAdapter) {
        MovieDbEndpointInterface apiService = ApiUtility.getMovieDbEndpointInterface();

        Call<ObjectWithMovieResults> call;
        switch (currentMovieSet) {
            case POPULAR_SET:
                call = apiService.getMorePopular(++pageNumber);
                break;
            case TOP_RATED_SET:
                call = apiService.getMoreTopRated(++pageNumber);
                break;
            default:
                call = null;
                // TODO: 9/7/16 so still don't know how to sensibly get batches of data from db.
                // could just limit queries to like 20 movies and then do a fetchMoreMovies
                break;
        }
        if (call != null) {
            updateMovieList(posterAdapter, call);
        }
    }

    /*
    Enqueue a Call<> and use Response to add Movies to the List and notify Adapter
     */
    private void updateMovieList(final PosterAdapter posterAdapter, Call<ObjectWithMovieResults> call) {
        call.enqueue(new Callback<ObjectWithMovieResults>() {
            @Override
            public void onResponse(Call<ObjectWithMovieResults> call, Response<ObjectWithMovieResults> response) {
                ObjectWithMovieResults obj = response.body();
                List<Movie> moreMovieList;
                moreMovieList = obj.getMovieList();
                int curSize = posterAdapter.getItemCount();
                mMovieList.addAll(moreMovieList);
                posterAdapter.notifyItemRangeChanged(curSize, mMovieList.size() - 1);
                Log.i(LOG_TAG, "retrofit got extra data");
            }

            @Override
            public void onFailure(Call<ObjectWithMovieResults> call, Throwable t) {
                Log.i(LOG_TAG, "so retrofit not so much");
            }
        });
    }

    public interface OnPosterSelectedListener {
        void onPosterSelected(Movie currentMovie);
    }
}
