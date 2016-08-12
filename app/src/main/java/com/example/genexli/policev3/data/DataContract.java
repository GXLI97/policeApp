package com.example.genexli.policev3.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by genexli on 6/1/16.
 * Data contract that establishes the tables and columns of the local database.
 */
public class DataContract {

    public static final String CONTENT_AUTHORITY = "com.example.genexli.policev3";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://" + CONTENT_AUTHORITY);

    // possible paths
    public static final String PATH_ROUTES = "routes";
    public static final String PATH_PATROLS = "patrols";
    public static final String PATH_ONCALLCRIMES = "oncallcrimes";
    public static final String PATH_HISTORICCRIMES = "historiccrimes";
    public static final String PATH_HEATMAP = "heatmap";

    public static final class PatrolsEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PATROLS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PATROLS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PATROLS;

        public static final String TABLE_NAME = "patrols";

        // Columns
        public static final String COLUMN_IDENTIFIER = "identifier";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_PRECINCT = "precinct";

        public static Uri buildPatrolsUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }
    }

    public static final class OnCallCrimesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ONCALLCRIMES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ONCALLCRIMES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ONCALLCRIMES;

        public static final String TABLE_NAME = "oncallcrimes";

        public static final String COLUMN_TIME = "datetime";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_PRECINCT = "precinct";

        public static Uri buildOnCallCrimesUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }
    }

    public static final class HistoricCrimesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HISTORICCRIMES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORICCRIMES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORICCRIMES;

        public static final String TABLE_NAME = "crimes";

        public static final String COLUMN_TIME = "datetime";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_PRECINCT = "precinct";

        public static Uri buildHistoricCrimesUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }
    }

    public static final class RoutesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROUTES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ROUTES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ROUTES;

        public static final String TABLE_NAME = "routes";

        // columns
        public static final String COLUMN_TIME = "datetime";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_DIST_TO = "distance_to";
        public static final String COLUMN_TIME_TO = "time_to";
        public static final String COLUMN_LATITUDE = "lat";
        public static final String COLUMN_LONGITUDE = "long";
        public static final String COLUMN_ROUTE_DESCRIPTION = "route_description";

        public static Uri buildRoutesUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }
    }

    public static final class HeatmapEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HEATMAP).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HEATMAP;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HEATMAP;

        public static final String TABLE_NAME = "heatmap";

        // columns
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_PRECINCT = "precinct";

        public static Uri buildHeatmapUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }
    }
}