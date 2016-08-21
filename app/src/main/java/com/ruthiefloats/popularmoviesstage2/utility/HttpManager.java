package com.ruthiefloats.popularmoviesstage2.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ruthiefloats.popularmoviesstage2.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A class that takes in a URL and returns the server response
 * as a String.  Adjusted from the official google example.
 */
public class HttpManager {

    private static final String DEBUG_TAG = "HttpManager";

    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    public static String downloadUrl(String myurl) throws IOException {
        InputStream inputStream = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response code is: " + response);
            inputStream = conn.getInputStream();

            // Return the InputStream converted into a string
            return readIt(inputStream);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public static String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        StringBuffer stringBuffer = new StringBuffer();

        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
        }

        Log.d(DEBUG_TAG, "The response is: " + stringBuffer.toString());
        return stringBuffer.toString();
    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean hasConnection = (networkInfo != null && networkInfo.isConnected());
        return hasConnection;
    }
}