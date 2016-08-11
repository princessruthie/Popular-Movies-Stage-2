package com.ruthiefloats.popularmoviesstage2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruthiefloats.popularmoviesstage2.adapter.MovieImageAdapter;
import com.ruthiefloats.popularmoviesstage2.adapter.ReviewsAdapter;
import com.ruthiefloats.popularmoviesstage2.model.Movie;
import com.ruthiefloats.popularmoviesstage2.parser.MovieParser;
import com.squareup.picasso.Picasso;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // When called builds a valid valid URL for The Movie DB API and starts a DownloadWeb
        // task.
        // Before attempting to fetch the URL, makes sure that there is a network connection.

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail_denested, container, false);
//        getData(REVIEW_ROOT_PREFIX + currentMovie.getId() + REVIEW_ROOT_POSTFIX);
//        Movie currentMovie = DummyData.getSingleDummyDatum();

        /**Populate the Views using the information in the Movie object */
        TextView movieTextView = (TextView) rootView.findViewById(R.id.movieTitle);
        TextView dateTextView = (TextView) rootView.findViewById(R.id.dateTextView);
        TextView summaryTextView = (TextView) rootView.findViewById(R.id.synopsisTextView);
        TextView ratingTextView = (TextView) rootView.findViewById(R.id.voteAverageTextView);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
        // TODO: 8/2/16 add Views for trailer and listener to launch intent
        // TODO: 8/10/16 populate the lengthTextView
//        TextView lengthTextView = (TextView) findViewById(R.id.lengthTextView);

        movieTextView.setText(currentMovie.getTitle());
        dateTextView.setText(currentMovie.getRelease_date());
        summaryTextView.setText(currentMovie.getOverview());
        ratingTextView.setText((int) currentMovie.getVote_average() + "/10");

        Picasso.with(getActivity()).
                load(MovieImageAdapter.getCompletePhotoUrl(currentMovie.getPoster_path()))
                .error(R.drawable.placeholder)
                .into(imageView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mView = view;
        String REVIEW_ROOT_PREFIX = "/movie/";
        getData(REVIEW_ROOT_PREFIX + currentMovie.getId(), "&append_to_response=reviews,videos");
        super.onViewCreated(view, savedInstanceState);
    }

    public void getData(String resourceRoot, String appendix) {
        String fullUrl = HttpManager.BuildUrl(resourceRoot, appendix);
        Log.i(LOG_TAG, fullUrl);
        boolean hasConnection = HttpManager.CheckConnection(getContext());
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
                Log.i(LOG_TAG, reviewList.toString());
            } else {
                reviewList = new ArrayList<>();
                reviewList.add("There aren't any reviews for this film.");
            }

            List<String> trailerIds = MovieParser.getTrailers(result);
            int numTrailers = trailerIds.size();
            Log.i(LOG_TAG, "Number of trailers" + numTrailers);

            RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.reviewRecyclerView);
//            reviewList = DummyData.getDummyReviews();
            ReviewsAdapter adapter = new ReviewsAdapter(getContext(), reviewList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            String youtubePrefix = "http://img.youtube.com/vi/";
            String youtubePostfix = "/0.jpg";

            final String trailerId_0 = trailerIds.get(0);

            final String youtubeTrailerPrefix = "https://www.youtube.com/watch?v=";
            ImageView trailerPoster_0 = (ImageView) mView.findViewById(R.id.trailerThumbnail_0);
            ImageView trailerPoster_1 = (ImageView) mView.findViewById(R.id.trailerThumbnail_1);
            ImageView trailerPoster_2 = (ImageView) mView.findViewById(R.id.trailerThumbnail_2);

            // TODO: 8/11/16 should this just be a recyclerview 
            // TODO: 8/11/16 either way could refactor away duplicate code
            Picasso.with(getContext())
                    .load(youtubePrefix + trailerId_0 + youtubePostfix)
                    .error(R.drawable.placeholder)
                    .into(trailerPoster_0);
            
            trailerPoster_0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeTrailerPrefix + trailerId_0)));
                }
            });

            if(numTrailers > 1){
                final String trailerId_1 = trailerIds.get(1);
                Picasso.with(getContext())
                        .load(youtubePrefix + trailerId_1 + youtubePostfix)
                        .error(R.drawable.placeholder)
                        .into(trailerPoster_1);
                trailerPoster_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeTrailerPrefix + trailerId_1)));
                    }
                });
            }

            if(numTrailers > 2){
                final String trailerId_2 = trailerIds.get(2);
                Picasso.with(getContext())
                        .load(youtubePrefix + trailerId_2 + youtubePostfix)
                        .error(R.drawable.placeholder)
                        .into(trailerPoster_2);
                trailerPoster_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeTrailerPrefix + trailerId_2)));
                    }
                });
            }

        }
    }
}
