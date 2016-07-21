package com.ruthiefloats.popularmoviesstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruthiefloats.popularmoviesstage1.adapter.MovieImageAdapter;
import com.ruthiefloats.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Movie currentMovie = getIntent().getExtras().getParcelable(MovieImageAdapter.CURRENT_MOVIE);

        TextView movieTextView = (TextView) findViewById(R.id.movieTitle);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);
        TextView ratingTextView = (TextView) findViewById(R.id.voteAverageTextView);
        TextView summaryTextView = (TextView) findViewById(R.id.synopsisTextView);
        //for part 2 we'll populate this textview
        //from the looks of it, I'll need a separate call to the server for the
        //duration portion becuase it's not exposed in  /popular or /top_rated
        //I guess my Movie model will need to get the id, too.  That's a tomorrow thing.
//        TextView durationTextView = (TextView) findViewById(R.id.lengthTextView);

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
