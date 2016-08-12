package com.ruthiefloats.popularmoviesstage2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ruthiefloats.popularmoviesstage2.data.FavoritesContract.Favorites;

import java.sql.Blob;

/**
 * A class to hold a ref to Favorites DB and use with
 * convenience
 */
public class FavoritesDataSource {
    private static final String LOGTAG = "FavoritesDataSource ";
    SQLiteOpenHelper DBHelper;
    SQLiteDatabase database;

    public FavoritesDataSource(Context context) {

        DBHelper = new FavoritesDBHelper(context);
    }

    public void open(){
         /* getWriteableDatabase calls the onCreate
        and create the table
         */
        database = DBHelper.getWritableDatabase();
        Log.i(LOGTAG, "Database opened");
    }

    public void close() {
        Log.i(LOGTAG, "Database closed");
        database.close();
    }

    public void addMovie(String title, byte[] poster, String synopsis,
                         String rating, String release_date){
        ContentValues values = new ContentValues();
        values.put(Favorites.COLUMN_TITLE, title);
        values.put(Favorites.COLUMN_POSTER, poster);
        values.put(Favorites.COLUMN_SYNOPSIS, synopsis);
        values.put(Favorites.COLUMN_RATING, rating);
        values.put(Favorites.COLUMN_RELEASE_DATE, release_date);

        long insertid = database.insert(Favorites.TABLE_NAME, null, values);
        Log.i(LOGTAG, "Id is  " + insertid);

    }

}
