package com.ruthiefloats.popularmoviesstage2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ruthiefloats.popularmoviesstage2.MasterFragment;
import com.ruthiefloats.popularmoviesstage2.R;
import com.ruthiefloats.popularmoviesstage2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * TODO: add a class header comment.
 */
public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.ViewHolder> {

    private static final String PHOTOS_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String PHOTOS_SIZE_URL = "w185/";
    private static final String LOG_TAG = "Poster Adapter";
    private List<Movie> mMovieList;
    private Context mContext;

    public PosterAdapter(Context context, List<Movie> movieList) {
        mContext = context;
        mMovieList = movieList;
    }

    public static String getCompletePhotoUrl(String photoUrl) {
        String completeUrl = PHOTOS_BASE_URL +
                PHOTOS_SIZE_URL +
                photoUrl;
        return completeUrl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View posterView = inflater.inflate(R.layout.item_poster, parent, false);
        ViewHolder viewHolder = new ViewHolder(posterView, context);
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
        Picasso.with(mContext).
                load(getCompletePhotoUrl(mMovieList.get(position).getPoster_path()))
                .error(R.drawable.poster_placeholder)
                .into(imageView);
        Log.i(LOG_TAG, mMovieList.get(position).getPoster_path());

    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
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
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Movie movie = mMovieList.get(position);
            Log.i(LOG_TAG, "movie clicked was number " + position);
            onPosterSelectedListener.onPosterSelected(movie);
        }
    }
}
