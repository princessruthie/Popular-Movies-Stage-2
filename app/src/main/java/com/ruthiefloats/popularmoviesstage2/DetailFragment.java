package com.ruthiefloats.popularmoviesstage2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruthiefloats.popularmoviesstage2.adapter.ReviewsAdapter;
import com.ruthiefloats.popularmoviesstage2.adapter.TrailerAdapter;
import com.ruthiefloats.popularmoviesstage2.data.FavoritesContract;
import com.ruthiefloats.popularmoviesstage2.model.ObjectWithMovieDetails;
import com.ruthiefloats.popularmoviesstage2.model.ObjectWithMovieDetails.Response.Reviews;
import com.ruthiefloats.popularmoviesstage2.model.ObjectWithMovieResults;
import com.ruthiefloats.popularmoviesstage2.utility.ApiUtility;
import com.ruthiefloats.popularmoviesstage2.utility.MovieDbEndpointInterface;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ruthiefloats.popularmoviesstage2.utility.ApiUtility.MovieDbUtility.getCompletePhotoUrl;
import static com.ruthiefloats.popularmoviesstage2.utility.ApiUtility.YoutubeUtility.getTrailerUrlFromTrailerId;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private static final String LOG_TAG = "DetailFragment AsyncRes";
    List<Reviews> reviewList;
    private ObjectWithMovieResults.Movie currentMovie;
    private int numReviews;
    private View mView;
    /*Whether this movie in the favorite db */
    private boolean isFavorite;
    private String firstTrailerUrl;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(ObjectWithMovieResults.Movie movie) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.INSTANCE_STATE_TAG, movie);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (firstTrailerUrl != null) {
            inflater.inflate(R.menu.detail, menu);
            MenuItem item = menu.findItem(R.id.action_share);
            ShareActionProvider mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, firstTrailerUrl);
            intent.setType("text/plain");
            mShareActionProvider.setShareIntent(intent);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        currentMovie = getArguments().getParcelable(MainActivity.INSTANCE_STATE_TAG);
        setFavoriteStatus();
    }

    /*checks if the Movie is already in the db.  If so, sets isFavorite to true */
    private void setFavoriteStatus() {
        Cursor cursor = getContext().getContentResolver().query(
                FavoritesContract.Favorites.CONTENT_URI,
                new String[]{FavoritesContract.Favorites.COLUMN_API_ID},
                FavoritesContract.Favorites.COLUMN_API_ID + " = ? ",
                new String[]{String.valueOf(currentMovie.getId())},
                null);
        if (cursor != null) {
            int cursorCount = cursor.getCount();
            isFavorite = cursorCount > 0 ? true : false;
            cursor.close();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        /**Populate the Views using the information in the Movie object */
        TextView movieTextView = (TextView) rootView.findViewById(R.id.movieTitle);
        TextView dateTextView = (TextView) rootView.findViewById(R.id.dateTextView);
        TextView summaryTextView = (TextView) rootView.findViewById(R.id.synopsisTextView);
        TextView ratingTextView = (TextView) rootView.findViewById(R.id.voteAverageTextView);
        //make ref to imageView final so that it can be used in inner class
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);

        movieTextView.setText(currentMovie.getTitle());
        dateTextView.setText(currentMovie.getRelease_date());
        summaryTextView.setText(currentMovie.getOverview());
        ratingTextView.setText((int) currentMovie.getVote_average() + "/10");

        if (isFavorite) {
            try {
                String filename = String.valueOf(currentMovie.getId());
                File photofile = new File(getContext().getFilesDir(), filename);
                Bitmap freshBitMap = BitmapFactory.decodeStream(new FileInputStream(photofile));
                imageView.setImageBitmap(freshBitMap);
            } catch (FileNotFoundException e) {
                imageView.setImageResource(R.drawable.poster_placeholder);
                e.printStackTrace();
            }
        } else {
            Picasso.with(getActivity()).
                    load(getCompletePhotoUrl(currentMovie.getPoster_path()))
                    .error(R.drawable.poster_placeholder)
                    .into(imageView);
        }

        final ImageButton favoriteButton = (ImageButton) rootView.findViewById(R.id.favoriteButton);
        Context context = getContext();
        final Drawable favoriteIcon = ContextCompat.getDrawable(context, R.drawable.ic_favorite_red_24dp);
        final Drawable nonFavoriteIcon = ContextCompat.getDrawable(context, R.drawable.ic_favorite_gray_24dp);
        //check if the current movie is a favorite
        favoriteButton.setImageDrawable(isFavorite ? favoriteIcon : nonFavoriteIcon);

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toggle favorite boolean
                isFavorite = !isFavorite;
                //toggle the drawable
                favoriteButton.setImageDrawable(isFavorite ? favoriteIcon : nonFavoriteIcon);
                //toggle db
                if (isFavorite) {
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    addMovie(byteArray);

                } else {
                    removeMovie();
                }
            }
        });
        return rootView;
    }


    /**
     * Remove Movie from favorites db and delete image from file directory
     */
    private void removeMovie() {
        String currentMovieId = String.valueOf(currentMovie.getId());
        String whereClause = FavoritesContract.Favorites.COLUMN_API_ID + " = ?";
        String[] whereArgs = new String[]{currentMovieId};
        int rowsDeleted = getContext().getContentResolver().delete(FavoritesContract.Favorites.CONTENT_URI, whereClause, whereArgs);

        File photofile = new File(getContext().getFilesDir(), currentMovieId);
        if (photofile.exists()) {
            photofile.delete();
        }
    }

    /**
     * Add Movie to favorite db and write image to file
     */
    private void addMovie(byte[] byteArray) {
        /* Add Movie to ContentProvider */
        ContentValues values = new ContentValues();
        values.put(FavoritesContract.Favorites.COLUMN_TITLE, currentMovie.getTitle());
        values.put(FavoritesContract.Favorites.COLUMN_SYNOPSIS, currentMovie.getOverview());
        values.put(FavoritesContract.Favorites.COLUMN_RATING, currentMovie.getVote_average());
        values.put(FavoritesContract.Favorites.COLUMN_RELEASE_DATE, currentMovie.getRelease_date());
        values.put(FavoritesContract.Favorites.COLUMN_API_ID, currentMovie.getId());
        Uri insertedMovieUri = getContext().getContentResolver().
                insert(FavoritesContract.Favorites.CONTENT_URI, values);

        /* Write the file to disk */
        String filename = String.valueOf(currentMovie.getId());
        FileOutputStream outputStream;
        try {
            outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(byteArray);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mView = view;

        MovieDbEndpointInterface apiService = ApiUtility.getMovieDbEndpointInterface();
        Call<ObjectWithMovieDetails> call = apiService.getMovieDetails(currentMovie.getId());

        call.enqueue(new Callback<ObjectWithMovieDetails>() {
            @Override
            public void onResponse(Call<ObjectWithMovieDetails> call, Response<ObjectWithMovieDetails> response) {
                ObjectWithMovieDetails obj = response.body();
                /*set the runtime textview */
                TextView lengthTextView = (TextView) mView.findViewById(R.id.lengthTextView);
                lengthTextView.setText(obj.getRuntime() + " mins");

                 /*set the reviews rv */
                numReviews = obj.getReviews().getTotal_results();
                if (numReviews > 0) {
                    reviewList = obj.getReviews().getResults();
                } else {
                    reviewList = null;
                }
                if (reviewList != null && reviewList.size() > 0) {
                    RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.reviewRecyclerView);
                    ReviewsAdapter adapter = new ReviewsAdapter(getContext(), reviewList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
                /*set the trailers rv */
                List<ObjectWithMovieDetails.VideosBean.Trailer> videos = obj.getVideos().getResults();
                if (videos != null && videos.size() > 0) {
                    addShareMovieToOptionsMenu(videos.get(0).getKey());
                    RecyclerView trailerRecyclerView = (RecyclerView) mView.findViewById(R.id.trailerRecyclerView);
                    TrailerAdapter trailerAdapter = new TrailerAdapter(getContext(), videos);
                    trailerRecyclerView.setAdapter(trailerAdapter);
                    trailerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                }
            }

            @Override
            public void onFailure(Call<ObjectWithMovieDetails> call, Throwable t) {
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void addShareMovieToOptionsMenu(String trailerId) {
        firstTrailerUrl = getTrailerUrlFromTrailerId(trailerId);
        getActivity().invalidateOptionsMenu();
    }
}