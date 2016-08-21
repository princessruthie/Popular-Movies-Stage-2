package com.ruthiefloats.popularmoviesstage2;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruthiefloats.popularmoviesstage2.adapter.ReviewsAdapter;
import com.ruthiefloats.popularmoviesstage2.adapter.TrailerAdapter;
import com.ruthiefloats.popularmoviesstage2.data.FavoritesContract;
import com.ruthiefloats.popularmoviesstage2.data.FavoritesDataSource;
import com.ruthiefloats.popularmoviesstage2.model.Movie;
import com.ruthiefloats.popularmoviesstage2.parser.MovieParser;
import com.ruthiefloats.popularmoviesstage2.utility.ApiUtility;
import com.ruthiefloats.popularmoviesstage2.utility.HttpManager;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private static final String LOG_TAG = "DetailFragment AsyncRes";
    List<String> reviewList;
    private Movie currentMovie;
    private int numReviews;
    private View mView;
    /*Whether this movie in the favorite db */
    private boolean isFavorite;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(Movie movie) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.INSTANCE_STATE_TAG, movie);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentMovie = getArguments().getParcelable(MainActivity.INSTANCE_STATE_TAG);
        //check if the movie is in favorite db
        FavoritesDataSource datasource = new FavoritesDataSource(getContext());
        isFavorite = datasource.isThisMovieFavorited(currentMovie.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // When called builds a valid valid URL for The Movie DB API and starts a DownloadWeb
        // task.
        // Before attempting to fetch the URL, makes sure that there is a network connection.

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
//        getData(REVIEW_ROOT_PREFIX + currentMovie.getId() + REVIEW_ROOT_POSTFIX);
//        Movie currentMovie = DummyData.getSingleDummyDatum();

        /**Populate the Views using the information in the Movie object */
        TextView movieTextView = (TextView) rootView.findViewById(R.id.movieTitle);
        TextView dateTextView = (TextView) rootView.findViewById(R.id.dateTextView);
        TextView summaryTextView = (TextView) rootView.findViewById(R.id.synopsisTextView);
        TextView ratingTextView = (TextView) rootView.findViewById(R.id.voteAverageTextView);
        //make ref to imageView final so that it can be used in inner class
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
        // TODO: 8/10/16 populate the lengthTextView
//        TextView lengthTextView = (TextView) findViewById(R.id.lengthTextView);

        movieTextView.setText(currentMovie.getTitle());
        dateTextView.setText(currentMovie.getRelease_date());
        summaryTextView.setText(currentMovie.getOverview());
        ratingTextView.setText((int) currentMovie.getVote_average() + "/10");

        Picasso.with(getActivity()).
                load(ApiUtility.getCompletePhotoUrl(currentMovie.getPoster_path()))
                .error(R.drawable.poster_placeholder)
                .into(imageView);

        final ImageButton favoriteButton = (ImageButton) rootView.findViewById(R.id.favoriteButton);
        Resources resources = getResources();
        // TODO: 8/11/16 find update method for deprecated getDrawable 
        final Drawable favoriteIcon = resources.getDrawable(R.drawable.ic_favorite_red_24dp);
        final Drawable nonFavoriteIcon = resources.getDrawable(R.drawable.ic_favorite_gray_24dp);
        //check if the current movie is a favorite
        favoriteButton.setImageDrawable(isFavorite ? favoriteIcon : nonFavoriteIcon);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toggle favorite boolean
                toggleFavorite();
                //toggle the drawable
                favoriteButton.setImageDrawable(isFavorite ? favoriteIcon : nonFavoriteIcon);
                //toggle db
                if (isFavorite) {
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    addMovie(currentMovie, byteArray);

                    //try and write to disk
                    Log.i(LOG_TAG, "writing to disk...");
                    String filename = "movieImageFile";
                    FileOutputStream outputStream;
//                    File file = new File(getContext().getFilesDir(), filename);
                    try{
                        outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(byteArray);
                        outputStream.close();
                        Log.i(LOG_TAG, "wrote to disk");
                    } catch (Exception e){
                        e.printStackTrace();
                    }


                } else {
                    removeMovie(currentMovie);
                }
            }
        });
        return rootView;
    }

    private void toggleFavorite() {
        isFavorite = !isFavorite;
    }

    /**
     * Removie movie from favorites db
     *
     * @param currentMovie Movie to remove from db
     */
    private void removeMovie(Movie currentMovie) {
        FavoritesDataSource dataSource = new FavoritesDataSource(getContext());
        dataSource.removeMovie(currentMovie.getId());
    }

    /**
     * add Movie to favorite db
     *
     * @param currentMovie Movie to add to db
     * @param byteArray    Movie poster drawable
     */
    // TODO: 8/13/16 ideally refactor to writing the Drawable to disk and have db ref the file 
    private void addMovie(Movie currentMovie, byte[] byteArray) {
        String voteAverage = Double.toString(currentMovie.getVote_average());

        addMovieUsingContentProvider(currentMovie.getTitle(), byteArray, currentMovie.getOverview()
                , voteAverage, currentMovie.getRelease_date(), currentMovie.getId());
    }

    /**
     * a method to add a Movie to the database using Content Provider
     *
     * @param title        Movie title
     * @param poster       Movie poster as a byte[]
     * @param synopsis     Movie synopsis
     * @param rating       Movie rating
     * @param release_date Movie release date
     * @param api_id       Movie id from the API
     */
    private void addMovieUsingContentProvider(String title, byte[] poster, String synopsis, String rating, String release_date, int api_id) {

        ContentValues values = new ContentValues();
        values.put(FavoritesContract.Favorites.COLUMN_TITLE, title);
        values.put(FavoritesContract.Favorites.COLUMN_POSTER, poster);
        values.put(FavoritesContract.Favorites.COLUMN_SYNOPSIS, synopsis);
        values.put(FavoritesContract.Favorites.COLUMN_RATING, rating);
        values.put(FavoritesContract.Favorites.COLUMN_RELEASE_DATE, release_date);
        values.put(FavoritesContract.Favorites.COLUMN_API_ID, api_id);

        Uri insertedMovieUri = getContext().getContentResolver().insert(FavoritesContract.Favorites.CONTENT_URI, values);
        Log.i(LOG_TAG, "newly inserted movie uri: " + insertedMovieUri);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mView = view;
        String REVIEW_ROOT_PREFIX = "/movie/";
        getData(REVIEW_ROOT_PREFIX + currentMovie.getId(), "&append_to_response=reviews,videos");
        super.onViewCreated(view, savedInstanceState);
    }

    public void getData(String resourceRoot, String appendix) {
        String fullUrl = ApiUtility.BuildUrl(resourceRoot, appendix);
        Log.i(LOG_TAG, fullUrl);
        boolean hasConnection = HttpManager.checkConnection(getContext());
        if (hasConnection) {
            new DownloadWebpageTask().execute(fullUrl);
        } else {
            Toast.makeText(getContext(), "No network", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Uses AsyncTask to create a task away from the main UI thread. This task takes a
     * URL string and uses it to create an HttpUrlConnection. Once the connection
     * has been established, the AsyncTask downloads the contents as
     * an InputStream. Finally, the InputStream is converted into a String, which is
     * parsed into a List<Movie>, List<Strings> for reviews and List<String> for trailer
     * Ids and used to the make the review RecyclerView, trailer thumbnails
     * onPostExecute method.
     */
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


        // TODO: 8/10/16 this could use a refactor.  something something what changes
        // something something what stays the same.

        /**
         * Populate the list of reviews and trailer images Because some movies only have
         * one or two trailers, conditionals have been employed
         *
         * @param result JSON String
         */
        @Override
        protected void onPostExecute(String result) {
            Log.i(LOG_TAG, result);
            //check that there are reviews
            numReviews = MovieParser.getNumReviews(result);
            Log.i(LOG_TAG, numReviews + "  reviews available");
            if (numReviews > 0) {
                reviewList = MovieParser.parseReviews(result);
//                reviewList = MovieParser.parseReviews(DummyData.TEST_JSON);
//                Log.i(LOG_TAG, reviewList.toString());
            } else {
                reviewList = new ArrayList<>();
                reviewList.add("There aren't any reviews for this film.");
            }


            RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.reviewRecyclerView);
//            reviewList = DummyData.getDummyReviews();
            ReviewsAdapter adapter = new ReviewsAdapter(getContext(), reviewList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            List<String> trailerIds = MovieParser.getTrailers(result);
            int numTrailers = trailerIds.size();
            Log.i(LOG_TAG, "Number of trailers: " + numTrailers);
            String youtubePrefix = "http://img.youtube.com/vi/";
            String youtubePostfix = "/0.jpg";

            RecyclerView trailerRecyclerView = (RecyclerView) mView.findViewById(R.id.trailerRecyclerView);
            TrailerAdapter trailerAdapter = new TrailerAdapter(getContext(), trailerIds);
            trailerRecyclerView.setAdapter(trailerAdapter);
            trailerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
    }
}
