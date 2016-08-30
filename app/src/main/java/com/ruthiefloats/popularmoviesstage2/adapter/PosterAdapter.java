package com.ruthiefloats.popularmoviesstage2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.ruthiefloats.popularmoviesstage2.MasterFragment;
import com.ruthiefloats.popularmoviesstage2.R;
import com.ruthiefloats.popularmoviesstage2.model.ObjectWithMovieResults;
import com.ruthiefloats.popularmoviesstage2.utility.ApiUtility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * An adapter to populate a RecyclerView, from a List<Movie>, with
 * poster images
 */
public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.ViewHolder> {

    private static final String LOG_TAG = "Poster Adapter";
    private List<ObjectWithMovieResults.Movie> mMovieList;
    private Context mContext;
    private boolean showingFavorites;

    public PosterAdapter(Context context, List<ObjectWithMovieResults.Movie> movieList) {
        mContext = context;
        mMovieList = movieList;
    }

    public PosterAdapter(Context context, List<ObjectWithMovieResults.Movie> movieList, boolean showingFavorites) {
        this(context, movieList);
        this.showingFavorites = showingFavorites;
    }

    @Override
    public PosterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View posterView = inflater.inflate(R.layout.item_poster, parent, false);
        PosterAdapter.ViewHolder viewHolder = new PosterAdapter.ViewHolder(posterView, context);
        return viewHolder;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p/>
     * Override {@links #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageView imageView = holder.mImageView;
        /*The favorites data points to a byte[] file on disk */
        if (showingFavorites) {
            try {
                /*The file is named the same as the api id */
                String filename = String.valueOf(mMovieList.get(position).getId());
                File photofile = new File(mContext.getFilesDir(), filename);
                Log.i(LOG_TAG, "getting saved photo data");
                Bitmap freshBitMap = BitmapFactory.decodeStream(new FileInputStream(photofile));
                imageView.setImageBitmap(freshBitMap);
            } catch (FileNotFoundException e) {
                imageView.setImageResource(R.drawable.poster_placeholder);
                e.printStackTrace();
            }
        } else {
            Picasso.with(mContext).
                    load(ApiUtility.MovieDbUtility.getCompletePhotoUrl(mMovieList.get(position).getPoster_path()))
                    .error(R.drawable.poster_placeholder)
                    .into(imageView);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;
        public MasterFragment.OnPosterSelectedListener onPosterSelectedListener;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.master_poster);
            mImageView.setOnClickListener(this);
            onPosterSelectedListener = (MasterFragment.OnPosterSelectedListener) context;
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setPadding(8, 8, 8, 8);
            mImageView.setAdjustViewBounds(true);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            ObjectWithMovieResults.Movie movie = mMovieList.get(position);
            Log.i(LOG_TAG, "movie clicked was number " + position);
            onPosterSelectedListener.onPosterSelected(movie);
        }
    }
}
