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
 * Created by genexli on 6/14/16.
 */
public class PatrolAdapter extends CursorAdapter {

    private Context context;
    public PatrolAdapter(Context context, Cursor c, int flags) {
        super(context,c,flags);
        this.context = context;
    }

    private String convertCursorRowtoUXFormat(Cursor cursor) {
        final int idx_id = cursor.getColumnIndexOrThrow(DataContract.PatrolsEntry.COLUMN_IDENTIFIER);
        final int idx_loc = cursor.getColumnIndexOrThrow(DataContract.PatrolsEntry.COLUMN_LOCATION);
        final int idx_lat = cursor.getColumnIndexOrThrow(DataContract.PatrolsEntry.COLUMN_LATITUDE);
        final int idx_long = cursor.getColumnIndexOrThrow(DataContract.PatrolsEntry.COLUMN_LONGITUDE);
        double lat = cursor.getDouble(idx_lat);
        double longg = cursor.getDouble(idx_long);

        // their location as an address.
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String _Location = "";
        try {
            List<Address> listAddresses = geocoder.getFromLocation(lat, longg, 1);
            if(null!=listAddresses&&listAddresses.size()>0) {
                _Location = listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String ret = cursor.getString(idx_id) + " at " + _Location + ", "
                + "\n" + cursor.getDouble(idx_lat) + " "
                + cursor.getDouble(idx_long);
        return ret;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_patrol, parent, false);

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
