package com.example.genexli.policev3.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


/**
 * Created by genexli on 6/6/16.
 * Content provider that is a layer of abstraction between the database and the rest of the code.
 */
public class DataProvider extends ContentProvider {

    private static final UriMatcher URI_MATCHER;
    private DbHelper mHelper = null;

    static final int ROUTES_LIST = 100;
    static final int ROUTES_ID = 101;
    static final int PATROLS_LIST = 200;
    static final int PATROLS_ID = 201;
    static final int ONCALLCRIMES_LIST = 300;
    static final int ONCALLCRIMES_ID = 301;
    static final int HISTORICCRIMES_LIST = 400;
    static final int HISTORICCRIMES_ID = 401;
    static final int HEATMAP_LIST = 500;
    static final int HEATMAP_ID = 501;

    // instantiates the URI matcher.
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(DataContract.CONTENT_AUTHORITY,
                "routes",
                ROUTES_LIST);
        URI_MATCHER.addURI(DataContract.CONTENT_AUTHORITY,
                "routes/#",
                ROUTES_ID);
        URI_MATCHER.addURI(DataContract.CONTENT_AUTHORITY,
                "patrols",
                PATROLS_LIST);
        URI_MATCHER.addURI(DataContract.CONTENT_AUTHORITY,
                "patrols/#",
                PATROLS_ID);
        URI_MATCHER.addURI(DataContract.CONTENT_AUTHORITY,
                "oncallcrimes",
                ONCALLCRIMES_LIST);
        URI_MATCHER.addURI(DataContract.CONTENT_AUTHORITY,
                "oncallcrimes/#",
                ONCALLCRIMES_ID);
        URI_MATCHER.addURI(DataContract.CONTENT_AUTHORITY,
                "historiccrimes",
                HISTORICCRIMES_LIST);
        URI_MATCHER.addURI(DataContract.CONTENT_AUTHORITY,
                "historiccrimes/#",
                HISTORICCRIMES_ID);
        URI_MATCHER.addURI(DataContract.CONTENT_AUTHORITY,
                "heatmap",
                HEATMAP_LIST);
        URI_MATCHER.addURI(DataContract.CONTENT_AUTHORITY,
                "heatmap/#",
                HEATMAP_ID);
    }

    @Override
    public boolean onCreate() {
        mHelper = new DbHelper(getContext());
        return true;
    }

    // gets the type based off of the URI.
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case ROUTES_LIST:
                return DataContract.RoutesEntry.CONTENT_TYPE;
            case ROUTES_ID:
                return DataContract.RoutesEntry.CONTENT_ITEM_TYPE;
            case PATROLS_LIST:
                return DataContract.PatrolsEntry.CONTENT_TYPE;
            case PATROLS_ID:
                return DataContract.PatrolsEntry.CONTENT_ITEM_TYPE;
            case ONCALLCRIMES_LIST:
                return DataContract.OnCallCrimesEntry.CONTENT_TYPE;
            case ONCALLCRIMES_ID:
                return DataContract.OnCallCrimesEntry.CONTENT_ITEM_TYPE;
            case HISTORICCRIMES_LIST:
                return DataContract.HistoricCrimesEntry.CONTENT_TYPE;
            case HISTORICCRIMES_ID:
                return DataContract.HistoricCrimesEntry.CONTENT_ITEM_TYPE;
            case HEATMAP_LIST:
                return DataContract.HeatmapEntry.CONTENT_TYPE;
            case HEATMAP_ID:
                return DataContract.HeatmapEntry.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    // query method
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;

        switch (URI_MATCHER.match(uri)) {
            case ROUTES_LIST: {
                retCursor = mHelper.getReadableDatabase().query(
                        DataContract.RoutesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case ROUTES_ID: {
                selection = DataContract.RoutesEntry._ID + " = " +
                        uri.getLastPathSegment();
                retCursor = mHelper.getReadableDatabase().query(
                        DataContract.RoutesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case PATROLS_LIST: {
                retCursor = mHelper.getReadableDatabase().query(
                        DataContract.PatrolsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case PATROLS_ID: {
                selection = DataContract.PatrolsEntry._ID + " = " +
                        uri.getLastPathSegment();
                retCursor = mHelper.getReadableDatabase().query(
                        DataContract.PatrolsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case ONCALLCRIMES_LIST: {
                retCursor = mHelper.getReadableDatabase().query(
                        DataContract.OnCallCrimesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case ONCALLCRIMES_ID: {
                selection = DataContract.OnCallCrimesEntry._ID + " = " +
                        uri.getLastPathSegment();
                retCursor = mHelper.getReadableDatabase().query(
                        DataContract.OnCallCrimesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case HISTORICCRIMES_LIST: {
                retCursor = mHelper.getReadableDatabase().query(
                        DataContract.HistoricCrimesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case HISTORICCRIMES_ID: {
                selection = DataContract.HistoricCrimesEntry._ID + " = " +
                        uri.getLastPathSegment();
                retCursor = mHelper.getReadableDatabase().query(
                        DataContract.HistoricCrimesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case HEATMAP_LIST: {
                retCursor = mHelper.getReadableDatabase().query(
                        DataContract.HeatmapEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case HEATMAP_ID: {
                selection = DataContract.HeatmapEntry._ID + " = " +
                        uri.getLastPathSegment();
                retCursor = mHelper.getReadableDatabase().query(
                        DataContract.HeatmapEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    //insert method
    public Uri insert(Uri uri, ContentValues values) {
        Uri returnUri;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case ROUTES_LIST: {
                long _id = db.insert(DataContract.RoutesEntry.TABLE_NAME, null, values);
                if(_id > 0 ) {
                    returnUri = DataContract.RoutesEntry.buildRoutesUri(_id);
                }
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PATROLS_LIST: {
                long _id = db.insert(DataContract.PatrolsEntry.TABLE_NAME, null, values);
                if(_id > 0 ) {
                    returnUri = DataContract.PatrolsEntry.buildPatrolsUri(_id);
                }
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case ONCALLCRIMES_LIST: {
                long _id = db.insert(DataContract.OnCallCrimesEntry.TABLE_NAME, null, values);
                if(_id > 0 ) {
                    returnUri = DataContract.OnCallCrimesEntry.buildOnCallCrimesUri(_id);
                }
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case HISTORICCRIMES_LIST: {
                long _id = db.insert(DataContract.HistoricCrimesEntry.TABLE_NAME, null, values);
                if(_id > 0 ) {
                    returnUri = DataContract.HistoricCrimesEntry.buildHistoricCrimesUri(_id);
                }
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case HEATMAP_LIST: {
                long _id = db.insert(DataContract.HeatmapEntry.TABLE_NAME, null, values);
                if(_id > 0 ) {
                    returnUri = DataContract.HeatmapEntry.buildHeatmapUri(_id);
                }
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported URI for insertion: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    //update entries method.
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);
        int rowsUpdated;

        switch(match) {
            case ROUTES_LIST:
                rowsUpdated = db.update(DataContract.RoutesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case PATROLS_LIST:
                rowsUpdated = db.update(DataContract.PatrolsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case ONCALLCRIMES_LIST:
                rowsUpdated = db.update(DataContract.OnCallCrimesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case HISTORICCRIMES_LIST:
                rowsUpdated = db.update(DataContract.HistoricCrimesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case HEATMAP_LIST:
                rowsUpdated = db.update(DataContract.HeatmapEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("unknown uri: " + uri);
        }

        if(rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    //delete method.
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);
        int rowsDeleted;

        if(null == selection) selection = "1";

        switch (match) {
            case ROUTES_LIST:
                rowsDeleted = db.delete(DataContract.RoutesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PATROLS_LIST:
                rowsDeleted = db.delete(DataContract.PatrolsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ONCALLCRIMES_LIST:
                rowsDeleted = db.delete(DataContract.OnCallCrimesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HISTORICCRIMES_LIST:
                rowsDeleted = db.delete(DataContract.HistoricCrimesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HEATMAP_LIST:
                rowsDeleted = db.delete(DataContract.HeatmapEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("unknown uri: " + uri);
        }

        if(rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        switch (match) {
            case ROUTES_LIST: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.RoutesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case PATROLS_LIST: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.PatrolsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case ONCALLCRIMES_LIST: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.OnCallCrimesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case HISTORICCRIMES_LIST: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.HistoricCrimesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case HEATMAP_LIST: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.HeatmapEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            default:
                return super.bulkInsert(uri,values);
        }
    }

}
