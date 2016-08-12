package com.example.genexli.policev3;

import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.genexli.policev3.data.DataContract;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by genexli on 6/7/16.
 */
public class RouteAdapter extends CursorAdapter {

    public Context context;

    public RouteAdapter(Context context, Cursor c, int flags) {
        super(context,c,flags);
        this.context = context;
    }

    private String convertCursorRowtoUXFormat(Cursor cursor) {
        // displays location, distance to, time to.
        final int idx_location = cursor.getColumnIndexOrThrow(DataContract.RoutesEntry.COLUMN_LOCATION);
        final int idx_distance_to = cursor.getColumnIndexOrThrow(DataContract.RoutesEntry.COLUMN_DIST_TO);
        final int idx_time_to = cursor.getColumnIndexOrThrow(DataContract.RoutesEntry.COLUMN_TIME_TO);
        double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(DataContract.RoutesEntry.COLUMN_LATITUDE));
        double longg = cursor.getDouble(cursor.getColumnIndexOrThrow(DataContract.RoutesEntry.COLUMN_LONGITUDE));
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String _Location = "";
        try {
            List<Address> listAddresses = geocoder.getFromLocation(lat, longg, 1);
            if(null!=listAddresses&&listAddresses.size()>0) {
                _Location = listAddresses.get(0).getAddressLine(0) + " " + listAddresses.get(0).getAddressLine(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ret = _Location;
        return ret;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_route, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView) view;
        if(cursor != null) {
            tv.setText(convertCursorRowtoUXFormat(cursor));
        }
    }
}
