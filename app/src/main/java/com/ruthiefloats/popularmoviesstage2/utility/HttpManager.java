package com.ruthiefloats.popularmoviesstage2.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ruthiefloats.popularmoviesstage2.BuildConfig;
import com.squareup.picasso.Downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A class that takes in a URL and returns the server response
 * as a String.  Adjusted from the official google example.
 */
public class HttpManager {

    private static final String DEBUG_TAG = "HttpManager";

    // Given a URL, establishes an HttpUrlConnection and returns
    // the web page content as a String
    public static String downloadUrl(String myurl) throws IOException {

            URL url = new URL(myurl);
            Log.d(DEBUG_TAG, "url: " + url);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
        //if you want to print the response for debugging, recall that response.body()
        //can only be called once, so you'll have to assign a String
            return response.body().string();
    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean hasConnection = (networkInfo != null && networkInfo.isConnected());
        return hasConnection;
    }
}