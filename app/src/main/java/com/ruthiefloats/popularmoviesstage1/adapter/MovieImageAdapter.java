package com.ruthiefloats.popularmoviesstage1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ruthiefloats.popularmoviesstage1.MovieDetailActivity;
import com.ruthiefloats.popularmoviesstage1.R;
import com.ruthiefloats.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * This is an Adapter class for populating the GridView in the MainActivity
 */
public class MovieImageAdapter extends BaseAdapter {

    private static final String PHOTOS_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String PHOTOS_SIZE_URL = "w185/";
    /**
     * A key String for the Intent extras
     */
    public static final String CURRENT_MOVIE = "currentMovie";
    private List<Movie> mMovieList;
    private Context mContext;

    public MovieImageAdapter(Context context, List<Movie> movieList) {
        mContext = context;
        mMovieList = movieList;
    }

    public int getCount() {
        return mMovieList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setAdjustViewBounds(true);
        Picasso.with(mContext).
                load(getCompletePhotoUrl(mMovieList.get(position).getPoster_path()))
                .error(R.drawable.placeholder)
                .into(imageView);
        /**Clicking on the ImageView starts a MovieDetailActivity based on the current
         * Movie.
         */
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MovieDetailActivity.class);
//                intent.putExtra(CURRENT_MOVIE, mMovieList.get(position));
//                mContext.startActivity(intent);
//            }
//        });
        return imageView;
    }

    /**
     * takes the poster URL provided by the API response and builds the entire valid URL
     */
    public static String getCompletePhotoUrl(String photoUrl) {
        String completeUrl = PHOTOS_BASE_URL +
                PHOTOS_SIZE_URL +
                photoUrl;
        return completeUrl;
    }
}
