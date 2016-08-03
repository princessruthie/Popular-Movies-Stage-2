package com.ruthiefloats.popularmoviesstage2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruthiefloats.popularmoviesstage2.adapter.MovieImageAdapter;
import com.ruthiefloats.popularmoviesstage2.model.Movie;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MovieDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private Movie currentMovie;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentMovie = getArguments().getParcelable(MainActivity.INSTANCE_STATE_TAG);
    }

    public static MovieDetailFragment newInstance(Movie movie){
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.INSTANCE_STATE_TAG, movie);
        movieDetailFragment.setArguments(args);
        return movieDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

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
}
