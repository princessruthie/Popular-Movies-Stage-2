package com.ruthiefloats.popularmoviesstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ruthiefloats.popularmoviesstage2.data.FavoritesContract.Favorites;

/**
 * Manages a local database for favorite movie data.
 */
public class FavoritesDBHelper extends SQLiteOpenHelper{

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "favorites.db";
    private static final String LOGTAG = "FavoritesDBHelper ";

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     */

    public FavoritesDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(LOGTAG, "Helper constructed");
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + Favorites.TABLE_NAME + " (" +
                Favorites._ID + " INTEGER PRIMARY KEY," +
                Favorites.COLUMN_TITLE + " TEXT NOT NULL, " +
                Favorites.COLUMN_POSTER + " BLOB NOT NULL, " +
                Favorites.COLUMN_RATING + " TEXT NOT NULL, " +
                Favorites.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                Favorites.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                Favorites.COLUMN_API_ID + " INTEGER NOT NULL" +
                " );";
        Log.i(LOGTAG, SQL_CREATE_LOCATION_TABLE);
        db.execSQL(SQL_CREATE_LOCATION_TABLE);
        Log.i(LOGTAG, "Table has been created");
    }


    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param sqLiteDatabase  The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Favorites.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     * During debugging used this to toggle between v1 and v2
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    // TODO: 8/12/16 remove from "production" code 
    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Favorites.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
