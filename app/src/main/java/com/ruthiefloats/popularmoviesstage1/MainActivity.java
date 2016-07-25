package com.ruthiefloats.popularmoviesstage1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.ruthiefloats.popularmoviesstage1.adapter.MovieImageAdapter;
import com.ruthiefloats.popularmoviesstage1.model.Movie;
import com.ruthiefloats.popularmoviesstage1.parser.MovieParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The MainActivity by default populates with a list of the most popular movies.
 * <p/>
 * I based networking code off of the official example:
 * https://developer.android.com/training/basics/network-ops/connecting.html
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MasterFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}