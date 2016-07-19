package com.ruthiefloats.popularmoviesstage1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ruthiefloats.popularmoviesstage1.R;
import com.ruthiefloats.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created on 7/18/16.
 */
public class MovieImageAdapter extends BaseAdapter {

    private static final String PHOTOS_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private List<Movie> mMovieList;
    private Context mContext;

    public MovieImageAdapter(Context c, List<Movie> movieList) {
        mContext = c;
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
    public View getView(int position, View convertView, ViewGroup parent) {
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
                load(PHOTOS_BASE_URL + mMovieList.get(position).getPoster_path())
                .error(R.drawable.placeholder)
                .into(imageView);
        return imageView;
    }
}
