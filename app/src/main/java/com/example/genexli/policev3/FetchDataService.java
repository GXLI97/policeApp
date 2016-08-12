package com.example.genexli.policev3;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.example.genexli.policev3.data.DataContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by genexli on 7/25/16.
 */
public class FetchDataService extends IntentService{

    public String LOG_TAG = "FetchDataService";
    public FetchDataService() {
        super("FetchDataService");
    }

    @Override
    public void onHandleIntent (Intent workIntent) {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // try to connect.
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            String dataJsonStr = null;

            try {
                // construct URL
                String baseURL = "http://10.0.2.2:8080/policeserver/updateJSON";

                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String precinct = SP.getString("precinct", "");
                String ID = SP.getString("user_id","");
                // build the GET request with the key value pairs of precinct and ID.
                Uri.Builder builder = Uri.parse(baseURL).buildUpon();
                builder.appendQueryParameter("precinct", precinct);
                builder.appendQueryParameter("ID", ID);

                String urlString = builder.build().toString();
                URL url = new URL(urlString);

                // create request to API call.
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                // starts query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(LOG_TAG, "The response is " + response); // 200 means success

                // read input stream back to stream to build json str
                InputStream inputStream = conn.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return;
                }

                dataJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                // return strings to populate the table with.
                populateDatabase(dataJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            Log.v("Fetch Data Service", "done");
        }
    }

    private void populateDatabase(String dataJsonStr)
            throws JSONException {

        // list of String that need to be extracted
        final String ROUTE_OPTIONS = "route options";
        final String DATETIME = "datetime";
        final String LOCATION = "Location";
        final String TIME_TO = "time to";
        final String DIST_TO = "distance to";
        final String ROUTE_DESCRIP = "route description";
        final String ROUTELAT = "lat";
        final String ROUTELONG = "long";

        final String PATROLS = "Patrols";
        final String ID = "ID";
        final String LAT = "GPS lat";
        final String LONG = "GPS long";
        final String PRECINCT = "Precinct";

        final String ONCALLCRIMES = "On Call Crimes";
        final String CRIME_DESC = "Description";

        final String HISTORICCRIMES = "Historic Crimes";

        final String HEATMAP = "Heatmap";
        final String WEIGHT = "weight";

        // json objects instantiation
        JSONObject dataJson = new JSONObject(dataJsonStr);

        JSONArray routeArray = dataJson.getJSONArray(ROUTE_OPTIONS);
        JSONArray patrolsArray = dataJson.getJSONArray(PATROLS);
        JSONArray oncallcrimesArray = dataJson.getJSONArray(ONCALLCRIMES);
        JSONArray historiccrimesArray = dataJson.getJSONArray(HISTORICCRIMES);
        JSONArray heatmapArray = dataJson.getJSONArray(HEATMAP);

        // delete all the previous entries.
        getContentResolver().delete(DataContract.RoutesEntry.CONTENT_URI, null, null);
        getContentResolver().delete(DataContract.PatrolsEntry.CONTENT_URI, null, null);
        getContentResolver().delete(DataContract.OnCallCrimesEntry.CONTENT_URI, null, null);
        getContentResolver().delete(DataContract.HistoricCrimesEntry.CONTENT_URI, null, null);
        getContentResolver().delete(DataContract.HeatmapEntry.CONTENT_URI, null, null);

        ContentValues[] routeEntries = new ContentValues[routeArray.length()];
        for (int i = 0; i < routeArray.length(); i++) {
            JSONObject route = routeArray.getJSONObject(i);
            ContentValues values = new ContentValues();

            // values in an entry
            String dt = route.getString(DATETIME);
            String loc = route.getString(LOCATION);
            int timeto = route.getInt(TIME_TO);
            double distto = route.getDouble(DIST_TO);
            double lat = route.getDouble(ROUTELAT);
            double longg = route.getDouble(ROUTELONG);
            String routedescrip = route.getString(ROUTE_DESCRIP);

            values.put(DataContract.RoutesEntry.COLUMN_TIME, dt);
            values.put(DataContract.RoutesEntry.COLUMN_LOCATION, loc);
            values.put(DataContract.RoutesEntry.COLUMN_TIME_TO, timeto);
            values.put(DataContract.RoutesEntry.COLUMN_DIST_TO, distto);
            values.put(DataContract.RoutesEntry.COLUMN_LATITUDE, lat);
            values.put(DataContract.RoutesEntry.COLUMN_LONGITUDE, longg);
            values.put(DataContract.RoutesEntry.COLUMN_ROUTE_DESCRIPTION, routedescrip);


            routeEntries[i] = values;
        }
        getContentResolver().bulkInsert(DataContract.RoutesEntry.CONTENT_URI, routeEntries);

        ContentValues[] patrolEntries = new ContentValues[patrolsArray.length()];
        for (int i = 0; i < patrolsArray.length(); i++) {
            JSONObject patrols = patrolsArray.getJSONObject(i);
            ContentValues values = new ContentValues();

            // values
            String id = patrols.getString(ID);
            String loc = patrols.getString(LOCATION);
            double lat = patrols.getDouble(LAT);
            double longg = patrols.getDouble(LONG);
            String prec = patrols.getString(PRECINCT);

            values.put(DataContract.PatrolsEntry.COLUMN_IDENTIFIER, id);
            values.put(DataContract.PatrolsEntry.COLUMN_LOCATION, loc);
            values.put(DataContract.PatrolsEntry.COLUMN_LATITUDE, lat);
            values.put(DataContract.PatrolsEntry.COLUMN_LONGITUDE, longg);
            values.put(DataContract.PatrolsEntry.COLUMN_PRECINCT, prec);

            patrolEntries[i] = values;
        }
        getContentResolver().bulkInsert(DataContract.PatrolsEntry.CONTENT_URI, patrolEntries);

        ContentValues[] oncallcrimesEntries = new ContentValues[oncallcrimesArray.length()];
        for (int i = 0; i < oncallcrimesArray.length(); i++) {
            JSONObject oncallcrimes = oncallcrimesArray.getJSONObject(i);
            ContentValues values = new ContentValues();

            // values
            String dt  = oncallcrimes.getString(DATETIME);
            String loc = oncallcrimes.getString(LOCATION);
            String desc = oncallcrimes.getString(CRIME_DESC);
            double lat = oncallcrimes.getDouble(LAT);
            double longg = oncallcrimes.getDouble(LONG);
            String prec = oncallcrimes.getString(PRECINCT);

            values.put(DataContract.OnCallCrimesEntry.COLUMN_TIME, dt);
            values.put(DataContract.OnCallCrimesEntry.COLUMN_LOCATION, loc);
            values.put(DataContract.OnCallCrimesEntry.COLUMN_DESC, desc);
            values.put(DataContract.OnCallCrimesEntry.COLUMN_LATITUDE, lat);
            values.put(DataContract.OnCallCrimesEntry.COLUMN_LONGITUDE, longg);
            values.put(DataContract.OnCallCrimesEntry.COLUMN_PRECINCT, prec);

            oncallcrimesEntries[i] = values;
        }
        getContentResolver().bulkInsert(DataContract.OnCallCrimesEntry.CONTENT_URI, oncallcrimesEntries);

        ContentValues[] historiccrimesEntries = new ContentValues[historiccrimesArray.length()];
        for (int i = 0; i < historiccrimesArray.length(); i++) {
            JSONObject historiccrimes = historiccrimesArray.getJSONObject(i);
            ContentValues values = new ContentValues();

            String dt = historiccrimes.getString(DATETIME);
            String loc = historiccrimes.getString(LOCATION);
            String desc = historiccrimes.getString(CRIME_DESC);
            double lat = historiccrimes.getDouble(LAT);
            double longg = historiccrimes.getDouble(LONG);
            String prec = historiccrimes.getString(PRECINCT);

            values.put(DataContract.HistoricCrimesEntry.COLUMN_TIME, dt);
            values.put(DataContract.HistoricCrimesEntry.COLUMN_LOCATION, loc);
            values.put(DataContract.HistoricCrimesEntry.COLUMN_DESC, desc);
            values.put(DataContract.HistoricCrimesEntry.COLUMN_LATITUDE, lat);
            values.put(DataContract.HistoricCrimesEntry.COLUMN_LONGITUDE, longg);
            values.put(DataContract.HistoricCrimesEntry.COLUMN_PRECINCT, prec);

            historiccrimesEntries[i] = values;

        }
        getContentResolver().bulkInsert(DataContract.HistoricCrimesEntry.CONTENT_URI, historiccrimesEntries);

        ContentValues[] heatmapEntries = new ContentValues[heatmapArray.length()];
        for (int i = 0; i < heatmapArray.length(); i++) {
            JSONObject heatmap = heatmapArray.getJSONObject(i);
            ContentValues values = new ContentValues();

            double lat = heatmap.getDouble(LAT);
            double longg = heatmap.getDouble(LONG);
            double wt = heatmap.getDouble(WEIGHT);
            String prec = heatmap.getString(PRECINCT);

            values.put(DataContract.HeatmapEntry.COLUMN_LATITUDE, lat);
            values.put(DataContract.HeatmapEntry.COLUMN_LONGITUDE, longg);
            values.put(DataContract.HeatmapEntry.COLUMN_WEIGHT, wt);
            values.put(DataContract.HeatmapEntry.COLUMN_PRECINCT, prec);

            Log.v("heatmap entry", lat + " " + longg + " " + wt + " " + prec);

            heatmapEntries[i] = values;

        }
        getContentResolver().bulkInsert(DataContract.HeatmapEntry.CONTENT_URI, heatmapEntries);

    }
}
