package com.ruthiefloats.popularmoviesstage2;

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
import com.ruthiefloats.popularmoviesstage2.model.DummyData;
import com.ruthiefloats.popularmoviesstage2.model.Movie;
import com.ruthiefloats.popularmoviesstage2.parser.MovieParser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private static final String LOG_TAG = "DetailFragment AsyncRes";
    private Movie currentMovie;
    private String REVIEW_ROOT_PREFIX = "/movie/";
    private String REVIEW_ROOT_POSTFIX = "/reviews";
    private int numReviews;
    List<String> reviewList;

    // TODO: 8/3/16 find a maintainable to have distinct tablet/phone detail layouts.
    // TODO: 8/3/16 remove hardcoded review
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
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        getData(REVIEW_ROOT_PREFIX + currentMovie.getId() + REVIEW_ROOT_POSTFIX);

        /**Set the current movie by getting an Extra from the calling intent */
//        Movie currentMovie = getActivity().getIntent().getExtras().getParcelable(MovieImageAdapter.CURRENT_MOVIE);

//            Movie currentMovie = DummyData.getSingleDummyDatum();

        /**Populate the Views using the information in the Movie object */
        TextView movieTextView = (TextView) rootView.findViewById(R.id.movieTitle);
        TextView dateTextView = (TextView) rootView.findViewById(R.id.dateTextView);
        TextView summaryTextView = (TextView) rootView.findViewById(R.id.synopsisTextView);
        TextView ratingTextView = (TextView) rootView.findViewById(R.id.voteAverageTextView);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
        // TODO: 8/2/16 add Views for trailer and listener to launch intent
        // TODO: 8/2/16 add Views for reviews
        //for part 2 we'll populate the lengthTextView TextView
        //from the looks of it, I'll need a separate call to the server for the
        //duration portion because it's not exposed in  /popular or /top_rated
        //I guess the Movie model will need to get the id, too.  That's a tomorrow thing.
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

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // parsed into a List<Movie> and used to construct/set the GridView's adapter in
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

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.i(LOG_TAG, result);
            //check that there are reviews
            numReviews = MovieParser.getNumReviews(result);
            Log.i(LOG_TAG, numReviews + "  reviews available");
            if (numReviews > 0){
                reviewList = MovieParser.parseReviews(result);
                Log.i(LOG_TAG, reviewList.toString());
            }

            // TODO: 8/4/16 update parser to return readable reviews
            // TODO: 8/4/16 update ui here
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.reviewRecyclerView);
        reviewList = DummyData.getDummyReviews();
        ReviewsAdapter adapter = new ReviewsAdapter(getContext(), reviewList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        super.onViewCreated(view, savedInstanceState);
    }

    public void getData(String resourceRoot) {
        String fullUrl = HttpManager.BuildUrl(resourceRoot);
        Log.i(LOG_TAG, fullUrl);
        boolean hasConnection = HttpManager.CheckConnection(getContext());
        if (hasConnection) {
            new DownloadWebpageTask().execute(fullUrl);
        } else {
            Toast.makeText(getContext(), "No network", Toast.LENGTH_SHORT);
        }
    }
}
