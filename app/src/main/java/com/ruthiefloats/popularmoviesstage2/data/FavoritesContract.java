package com.ruthiefloats.popularmoviesstage2.data;

import android.provider.BaseColumns;

/**
 * Defines table and column names for the favorites database.
 */
public class FavoritesContract {

    public static final class Favorites implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_API_ID = "api_id";
    }
}
