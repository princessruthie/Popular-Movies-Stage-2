package com.ruthiefloats.popularmoviesstage2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ruthiefloats.popularmoviesstage2.data.FavoritesContract.Favorites;
import com.ruthiefloats.popularmoviesstage2.model.Movie;

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

    /**
     * a method to add a Movie to the database
     * @param title Movie title
     * @param poster Movie poster as a byte[]
     * @param synopsis Movie synopsis
     * @param rating Movie rating
     * @param release_date Movie release date
     * @param api_id Movie id from the API
     */
    public void addMovie(String title, byte[] poster, String synopsis,
                         String rating, String release_date, int api_id){
        open();
        ContentValues values = new ContentValues();
        values.put(Favorites.COLUMN_TITLE, title);
        values.put(Favorites.COLUMN_POSTER, poster);
        values.put(Favorites.COLUMN_SYNOPSIS, synopsis);
        values.put(Favorites.COLUMN_RATING, rating);
        values.put(Favorites.COLUMN_RELEASE_DATE, release_date);
        values.put(Favorites.COLUMN_API_ID, api_id);

        long insertid = database.insert(Favorites.TABLE_NAME, null, values);
        Log.i(LOGTAG, "Id is  " + insertid);
        close();
    }

    /**
     * A method to remove a Movie from favorite db
     * @param apiId Movie id from the API
     */
    public void removeMovie(int apiId){
        open();
        String whereClause = Favorites.COLUMN_API_ID + " = " + apiId;
        int rowsDeleted = database.delete(Favorites.TABLE_NAME, whereClause, null);
        Log.i(LOGTAG, "Row(s) with app_id " + apiId + "were deleted: " + rowsDeleted);
        close();
    }

    /**
     * A method to determine whether a Movie is in the favorite db
     * @param apiId Movie id from the API
     * @return Whether any Movie exists in db with given id
     */
    public boolean isThisMovieFavorited(int apiId){
        open();
        boolean isFavorited = false;
        String whereClause = Favorites.COLUMN_API_ID + " = " + apiId;
        Cursor cursor = database.query(Favorites.TABLE_NAME,
                null, whereClause, null, null, null, null);
        //check whether the cursor is non-empty
        if (cursor.moveToNext()){
            isFavorited = true;
        }
        cursor.close();
        close();
        return isFavorited;
    }

    public void printAllMovies(){
        open();
        Cursor cursor = database.query(Favorites.TABLE_NAME,
                null, null, null, null, null, null);
        int i = 0;
        while(cursor.moveToNext()){
            Log.i(LOGTAG, "Here's a movie!");
            i++;
        }
        Log.i(LOGTAG, "There were this many movies" + i);
        close();
    }
}
