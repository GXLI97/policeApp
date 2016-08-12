package com.example.genexli.policev3;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by genexli on 7/22/16.
 */
public class UpdateGPS extends Service {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0 ; // 0 second

    public Location location;
    public double latitude;
    public double longitude;

    @Override
    public void onCreate() {
        Log.e("Service", "created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Service", "started");
        // start an async task that sends location data to a server.
        getLocation();
        new SendLocationToServer().execute(latitude, longitude);
        return Service.START_NOT_STICKY;
    }

    class SendLocationToServer extends AsyncTask<Double, String, Void> {

        @Override
        protected Void doInBackground(Double... params) {

            double latitude = params[0];
            double longitude = params [1];

            HttpURLConnection conn = null;

            try {
                // construct URL
                URL url = new URL("http://10.0.2.2:8080/policeserver/updateJSON");

                // create request to API call.
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();

                // get ID
                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String id = SP.getString("user_id", "");
                String precinct = SP.getString("precinct","");

                JSONObject jsonGPS = new JSONObject();
                // this is the datetime format that we are using.
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                String datetime = sdf.format(new Date());
                // sending a JSON with four key-value pairs
                jsonGPS.put("Datetime", datetime);
                jsonGPS.put("ID", id);
                jsonGPS.put("latitude", latitude);
                jsonGPS.put("longitude", longitude);
                jsonGPS.put("precinct", precinct);
                Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(String.valueOf(jsonGPS));
                writer.close();

                // some logging:
                Log.v("Sent", id + " latitude: " + latitude + ", longitude: " + longitude);
                int response = conn.getResponseCode();
                Log.d("HTTP Connection", "The response is " + response); // 200 means success

            } catch (IOException e) {
                Log.e("HTTP Connection", "Error", e);
                return null;
            } catch(JSONException e) {
                Log.e("JSON exception", "Error", e);
                return null;
            }
            finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return null;
        }
    }

    // we aren't using binders so this method is useless
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e("Service", "destroyed");
    }

    public void getLocation() {
        try {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            };

            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                return;
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    if(isGPSEnabled) {
                        // use GPS
                        Log.v("Location", "using gps");
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    } else {
                        // use network
                        Log.v("Location", "using network");
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
            }
            // assign latitude and longitude if location is not null;
            if(location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            } else {
                Log.v("Location", "is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
