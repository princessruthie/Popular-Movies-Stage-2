package com.ruthiefloats.popularmoviesstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruthiefloats.popularmoviesstage1.adapter.MovieImageAdapter;
import com.ruthiefloats.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * An Activity for displaying the details of a single Movie.
 */

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        /**Set the current movie by getting an Extra from the calling intent */
        Movie currentMovie = getIntent().getExtras().getParcelable(MovieImageAdapter.CURRENT_MOVIE);

        /**Populate the Views using the information in the Movie object */
        TextView movieTextView = (TextView) findViewById(R.id.movieTitle);
        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);
        TextView summaryTextView = (TextView) findViewById(R.id.synopsisTextView);
        TextView ratingTextView = (TextView) findViewById(R.id.voteAverageTextView);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        //for part 2 we'll populate the lengthTextView TextView
        //from the looks of it, I'll need a separate call to the server for the
        //duration portion because it's not exposed in  /popular or /top_rated
        //I guess the Movie model will need to get the id, too.  That's a tomorrow thing.
//        TextView lengthTextView = (TextView) findViewById(R.id.lengthTextView);

        movieTextView.setText(currentMovie.getTitle());
        dateTextView.setText(currentMovie.getRelease_date());
        summaryTextView.setText(currentMovie.getOverview());
        ratingTextView.setText((int) currentMovie.getVote_average() + "/10");

        Picasso.with(this).
                load(MovieImageAdapter.getCompletePhotoUrl(currentMovie.getPoster_path()))
                .error(R.drawable.placeholder)
                .into(imageView);
    }
}