package com.example.genexli.policev3;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.genexli.policev3.data.DataContract;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * A fragment for the map
 */


public class MapFragment extends Fragment implements OnMapReadyCallback,
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int ONCALLLOADER = 301;
    private static final int HISTORICLOADER = 302;
    private static final int PATROLLOADER = 303;
    private static final int HEATMAPLOADER = 304;

    MapView m;
    GoogleMap map;

    Cursor oncallcrimes;
    Cursor historiccrimes;
    Cursor patrols;
    Cursor heatmap;

    List<Marker> patrolMarkers = new ArrayList<Marker>();
    List<Marker> oncallcrimesMarkers = new ArrayList<Marker>();
    List<Marker> historiccrimesMarkers = new ArrayList<Marker>();

    ToggleButton togglePatrols;
    ToggleButton toggleOnCallCrimes;
    ToggleButton toggleHistoricCrimes;
    ToggleButton toggleHeatmap;

    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // init three loaders with different IDs.
        getLoaderManager().initLoader(ONCALLLOADER, null, this);
        getLoaderManager().initLoader(HISTORICLOADER, null, this);
        getLoaderManager().initLoader(PATROLLOADER, null, this);
        getLoaderManager().initLoader(HEATMAPLOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate and return the layout
        View v = inflater.inflate(R.layout.fragment_map, container,
                false);
        // this creates the mapview.
        m = (MapView) v.findViewById(R.id.mapView);
        m.onCreate(savedInstanceState);
        m.getMapAsync(this);

        togglePatrols = (ToggleButton) v.findViewById(R.id.togglebuttonP);
        togglePatrols.setChecked(true);
        togglePatrols.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showhidePatrols(isChecked);
            }
        });

        toggleOnCallCrimes = (ToggleButton) v.findViewById(R.id.togglebuttonO);
        toggleOnCallCrimes.setChecked(true);
        toggleOnCallCrimes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showhideOnCallCrimes(isChecked);
            }
        });

        toggleHistoricCrimes = (ToggleButton) v.findViewById(R.id.togglebuttonH);
        toggleHistoricCrimes.setChecked(true);
        toggleHistoricCrimes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showhideHistoricCrimes(isChecked);
            }
        });
        toggleHeatmap = (ToggleButton) v.findViewById(R.id.togglebuttonHt);
        toggleHeatmap.setChecked(true);
        toggleHeatmap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    // show heatmap
                    addHeatMap();
                } else {
                    // hide heatmap
                    mOverlay.remove();

                }
            }
        });

        return v;
    }

    private void showhidePatrols(boolean b) {
        for(Marker m : patrolMarkers) {
            m.setVisible(b);
        }
    }
    private void showhideOnCallCrimes(boolean b) {
        for(Marker m : oncallcrimesMarkers) {
            m.setVisible(b);
        }
    }
    private void showhideHistoricCrimes(boolean b) {
        for(Marker m : historiccrimesMarkers) {
            m.setVisible(b);
        }
    }

    // Some required methods for MapView to function correctly.
    @Override
    public void onResume() {
        m.onResume();
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        m.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        m.onLowMemory();
    }

    @Override
    public void onMapReady (GoogleMap googleMap){
        map = googleMap;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v("Map", "location enabled");
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        // currently map is positioning onto a specific coordinate at FGH.
        Location location = null;
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
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
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if(isNetworkEnabled) {
                    // use network
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                } else {
                    // use GPS
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
        }
        CameraPosition target;
        if(location != null) {
            target = CameraPosition.builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(13)
                    .build();
        }
        else {
            target = CameraPosition.builder()
                    .target(new LatLng(36.1447034, -86.8032))
                    .zoom(13)
                    .build();
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(target);
        map.moveCamera(cameraUpdate);
        map.getUiSettings().setZoomControlsEnabled(true);
        redrawMap();
    }

    // buggy method that adds in precinct boundaries.
    private void geojson() {
        try {
            GeoJsonLayer layer = new GeoJsonLayer(map, R.raw.precinctsg, getContext());
            for(GeoJsonFeature feature : layer.getFeatures()) {

            }
            layer.addLayerToMap();

        } catch (IOException e) {
            Log.e("geojson", "GeoJSON file could not be read");
        } catch (JSONException e) {
            Log.e("geojson", "GeoJSON file could not be converted to a JSONObject");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle b) {
        // switch here to assign loader to different cursor based off of the call.
        switch(i) {
            case ONCALLLOADER: {
                return new CursorLoader(getActivity(), DataContract.OnCallCrimesEntry.CONTENT_URI,
                        null, null, null, null);
            }
            case HISTORICLOADER:  {
                return new CursorLoader(getActivity(), DataContract.HistoricCrimesEntry.CONTENT_URI,
                        null, null, null, null);
            }
            case PATROLLOADER: {
                return new CursorLoader(getActivity(), DataContract.PatrolsEntry.CONTENT_URI,
                        null, null, null, null);
            }
            case HEATMAPLOADER: {
                return new CursorLoader(getActivity(), DataContract.HeatmapEntry.CONTENT_URI,
                        null, null, null, null);
            }
        }
        return null;
    }

    // on loadfinished swaps loaders: use getId() to find the right cursor
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        int id = cursorLoader.getId();
        switch(id) {
            case ONCALLLOADER: {
                oncallcrimes = cursor;
                break;
            }
            case HISTORICLOADER: {
                historiccrimes = cursor;
                break;
            }
            case PATROLLOADER: {
                patrols = cursor;
                break;
            }
            case HEATMAPLOADER: {
                heatmap = cursor;
                break;
            }
        }
        redrawMap();
        addHeatMap();
    }

    private void redrawMap() {
        if (map != null) {
            map.clear();

            if (oncallcrimes != null) {
                if(oncallcrimes.moveToFirst())
                    // put in new marker.
                    do {
                        final int idx_lat = oncallcrimes.getColumnIndexOrThrow("latitude");
                        final int idx_long = oncallcrimes.getColumnIndexOrThrow("longitude");
                        final int desc = oncallcrimes.getColumnIndexOrThrow("description");
                        LatLng ltlng = new LatLng(oncallcrimes.getDouble(idx_lat), oncallcrimes.getDouble(idx_long));
                        Marker marker = map.addMarker(new MarkerOptions().position(ltlng).title("On Call Crime")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .snippet(oncallcrimes.getString(desc)));

                        oncallcrimesMarkers.add(marker);
                        if(!toggleOnCallCrimes.isChecked()) marker.setVisible(false);
                    } while (oncallcrimes.moveToNext());
            }


            if (historiccrimes != null) {
                if(historiccrimes.moveToFirst())
                    // put in new marker.
                    do {
                        final int idx_lat = historiccrimes.getColumnIndexOrThrow("latitude");
                        final int idx_long = historiccrimes.getColumnIndexOrThrow("longitude");
                        final int desc = historiccrimes.getColumnIndexOrThrow("description");
                        LatLng ltlng = new LatLng(historiccrimes.getDouble(idx_lat), historiccrimes.getDouble(idx_long));
                        Marker marker = map.addMarker(new MarkerOptions().position(ltlng).title("Historic Crime")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                .snippet(historiccrimes.getString(desc)));
                        historiccrimesMarkers.add(marker);
                        if(!toggleHistoricCrimes.isChecked()) marker.setVisible(false);
                    } while (historiccrimes.moveToNext());
            }


            if (patrols != null) {
                if(patrols.moveToFirst())
                    // put in new marker.
                    do {
                        int idx_lat = patrols.getColumnIndexOrThrow("latitude");
                        int idx_long = patrols.getColumnIndexOrThrow("longitude");
                        int id = patrols.getColumnIndexOrThrow("identifier");
                        LatLng ltlng = new LatLng(patrols.getDouble(idx_lat), patrols.getDouble(idx_long));
                        Marker marker = map.addMarker(new MarkerOptions().position(ltlng).title("Patrol")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                .snippet(patrols.getString(id)));
                        patrolMarkers.add(marker);
                        if(!togglePatrols.isChecked()) marker.setVisible(false);
                    } while (patrols.moveToNext());
            }

            // calling that method that puts in precinct boundaries
            // it doesn't work if I put it in other places :(
            geojson();
        }
    }

    // onloaderreset
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        int id = cursorLoader.getId();
        switch(id) {
            case ONCALLLOADER: {
                oncallcrimes = null;
                break;
            }
            case HISTORICLOADER: {
                historiccrimes = null;
                break;
            }
            case PATROLLOADER: {
                patrols = null;
                break;
            }
            case HEATMAPLOADER: {
                heatmap = null;
                break;
            }
        }
    }

    // heatmap stuff
    private void addHeatMap() {
        if(map!= null) {
            // Create the gradient.
            int[] colors = {
                    Color.rgb(102, 225, 0), // green
                    Color.rgb(255, 0, 0)    // red
            };

            float[] startPoints = {
                    0.2f, 1f
            };

            double opacity = .6;

            Gradient gradient = new Gradient(colors, startPoints);

            ArrayList<WeightedLatLng> list = new ArrayList<WeightedLatLng>();


            if (heatmap != null) {
                if (heatmap.moveToFirst()) {
                    do {
                        int idx_lat = heatmap.getColumnIndexOrThrow("latitude");
                        int idx_long = heatmap.getColumnIndexOrThrow("longitude");
                        int idx_wt = heatmap.getColumnIndexOrThrow("weight");
                        LatLng ltlng = new LatLng(heatmap.getDouble(idx_lat), heatmap.getDouble(idx_long));
                        double wt = heatmap.getDouble(idx_wt);

                        WeightedLatLng wll = new WeightedLatLng(ltlng, wt);
                        list.add(wll);

                    } while (heatmap.moveToNext());

                    if (toggleHeatmap.isChecked()) {
                        mProvider = new HeatmapTileProvider.Builder()
                                .weightedData(list)
                                .gradient(gradient)
                                .opacity(opacity)
                                .build();
                        mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                    }
                }
            }
        }

        /*
        try {
            list = readItems(R.raw.police_stations);
        } catch( JSONException e) {
            Toast.makeText(getContext(), "Problem reading list of locations", Toast.LENGTH_LONG).show();
        }

        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(list)
                .gradient(gradient)
                .opacity(opacity)
                .build();
        mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));*/
    }

    private ArrayList<WeightedLatLng> readItems(int resource) throws JSONException {
        ArrayList<WeightedLatLng> list = new ArrayList<WeightedLatLng>();
        InputStream inputStream = getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            double wt = object.getDouble("wt");
            list.add(new WeightedLatLng(new LatLng(lat, lng), wt));
        }
        return list;
    }



}