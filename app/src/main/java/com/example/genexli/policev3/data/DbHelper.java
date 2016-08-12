package com.example.genexli.policev3.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.genexli.policev3.data.DataContract.OnCallCrimesEntry;
import com.example.genexli.policev3.data.DataContract.HistoricCrimesEntry;
import com.example.genexli.policev3.data.DataContract.PatrolsEntry;
import com.example.genexli.policev3.data.DataContract.RoutesEntry;
import com.example.genexli.policev3.data.DataContract.HeatmapEntry;

// database helper that creates the tables that the app will access.
public class DbHelper extends SQLiteOpenHelper {

    // ++ this number every time you change the structure of the underlying database.
    private static final int DATABASE_VERSION = 33;

    static final String DATABASE_NAME = "appdata.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_ROUTES_TABLE = "CREATE TABLE IF NOT EXISTS " + RoutesEntry.TABLE_NAME + " (" +
                RoutesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RoutesEntry.COLUMN_TIME + " TEXT NOT NULL, " +
                RoutesEntry.COLUMN_LOCATION + " TEXT NOT NULL, " +
                RoutesEntry.COLUMN_DIST_TO + " REAL NOT NULL, " +
                RoutesEntry.COLUMN_TIME_TO + " INTEGER NOT NULL, " +
                RoutesEntry.COLUMN_LATITUDE + " REAL NOT NULL, " +
                RoutesEntry.COLUMN_LONGITUDE + " REAL NOT NULL, " +
                RoutesEntry.COLUMN_ROUTE_DESCRIPTION + " TEXT NOT NULL);";
        Log.v("table creation", SQL_CREATE_ROUTES_TABLE);

        sqLiteDatabase.execSQL(SQL_CREATE_ROUTES_TABLE);

        final String SQL_CREATE_PATROLS_TABLE = "CREATE TABLE IF NOT EXISTS " + PatrolsEntry.TABLE_NAME + " (" +
                PatrolsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PatrolsEntry.COLUMN_IDENTIFIER + " TEXT NOT NULL, " +
                PatrolsEntry.COLUMN_LOCATION + " TEXT NOT NULL, " +
                PatrolsEntry.COLUMN_LATITUDE + " REAL NOT NULL, " +
                PatrolsEntry.COLUMN_LONGITUDE + " REAL NOT NULL, " +
                PatrolsEntry.COLUMN_PRECINCT + " TEXT NOT NULL);";
        Log.v("table creation", SQL_CREATE_PATROLS_TABLE);

        sqLiteDatabase.execSQL(SQL_CREATE_PATROLS_TABLE);

        final String SQL_CREATE_ONCALLCRIMES_TABLE = "CREATE TABLE IF NOT EXISTS " + OnCallCrimesEntry.TABLE_NAME + " (" +
                OnCallCrimesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OnCallCrimesEntry.COLUMN_TIME + " TEXT NOT NULL, " +
                OnCallCrimesEntry.COLUMN_LOCATION + " TEXT NOT NULL, " +
                OnCallCrimesEntry.COLUMN_DESC + " TEXT NOT NULL," +
                OnCallCrimesEntry.COLUMN_LATITUDE + " REAL NOT NULL, " +
                OnCallCrimesEntry.COLUMN_LONGITUDE + " REAL NOT NULL, " +
                OnCallCrimesEntry.COLUMN_PRECINCT + " TEXT NOT NULL);";
        Log.v("table creation", SQL_CREATE_ONCALLCRIMES_TABLE);

        sqLiteDatabase.execSQL(SQL_CREATE_ONCALLCRIMES_TABLE);

        final String SQL_CREATE_HISTORICCRIMES_TABLE = "CREATE TABLE IF NOT EXISTS " + HistoricCrimesEntry.TABLE_NAME + " (" +
                HistoricCrimesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                HistoricCrimesEntry.COLUMN_TIME + " TEXT NOT NULL, " +
                HistoricCrimesEntry.COLUMN_DESC + " TEXT NOT NULL," +
                HistoricCrimesEntry.COLUMN_LOCATION + " TEXT NOT NULL, " +
                HistoricCrimesEntry.COLUMN_LATITUDE + " REAL NOT NULL, " +
                HistoricCrimesEntry.COLUMN_LONGITUDE + " REAL NOT NULL, " +
                HistoricCrimesEntry.COLUMN_PRECINCT + " TEXT NOT NULL);";
        Log.v("table creation", SQL_CREATE_HISTORICCRIMES_TABLE);

        sqLiteDatabase.execSQL(SQL_CREATE_HISTORICCRIMES_TABLE);

        final String SQL_CREATE_HEATMAP_TABLE = "CREATE TABLE IF NOT EXISTS " + HeatmapEntry.TABLE_NAME + " (" +
                HeatmapEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                HeatmapEntry.COLUMN_LATITUDE + " REAL NOT NULL, " +
                HeatmapEntry.COLUMN_LONGITUDE + " REAL NOT NULL, " +
                HeatmapEntry.COLUMN_WEIGHT + " REAL NOT NULL, " +
                HeatmapEntry.COLUMN_PRECINCT + " TEXT NOT NULL);";
        Log.v("table creation", SQL_CREATE_HEATMAP_TABLE);

        sqLiteDatabase.execSQL(SQL_CREATE_HEATMAP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RoutesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PatrolsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OnCallCrimesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HistoricCrimesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HeatmapEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
