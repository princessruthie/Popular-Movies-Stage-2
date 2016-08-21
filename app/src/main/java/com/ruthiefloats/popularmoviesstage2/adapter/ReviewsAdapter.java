package com.ruthiefloats.popularmoviesstage2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruthiefloats.popularmoviesstage2.R;

import java.util.List;

/**
 * An adapter to populate a RecyclerView from a List<String>
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private List<String> mReviews;
    private Context mContext;

    public ReviewsAdapter(Context context, List<String> reviews){
        mContext = context;
        mReviews = reviews;
    }

    private Context getContext(){
        return mContext;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View reviewView = inflater.inflate(R.layout.item_review, parent, false);
        ReviewsAdapter.ViewHolder viewHolder = new ViewHolder(reviewView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String reviewString = mReviews.get(position);

        TextView textView = holder.reviewTextView;
        textView.setText(reviewString);
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView reviewTextView;
        public ViewHolder(View itemView) {
            super(itemView);

            reviewTextView = (TextView) itemView.findViewById(R.id.review_content);
        }
    }
}
