package com.ruthiefloats.popularmoviesstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.ruthiefloats.popularmoviesstage1.adapter.MovieImageAdapter;
import com.ruthiefloats.popularmoviesstage1.model.DummyData;
import com.ruthiefloats.popularmoviesstage1.model.Movie;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Movie> movies = DummyData.getDummyData();
        MovieImageAdapter adapter = new MovieImageAdapter(this, movies);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
    }
}