package com.example.genexli.policev3;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.genexli.policev3.data.DataContract;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by genexli on 6/15/16.
 */
public class CrimeAdapter extends CursorAdapter{
    public CrimeAdapter(Context context, Cursor c, int flags) {super(context,c,flags);}

    private String convertCursorRowtoUXFormat(Cursor cursor) {
        // showing time, location, and description.
        final int idx_time = cursor.getColumnIndexOrThrow(DataContract.OnCallCrimesEntry.COLUMN_TIME);
        final int idx_loc = cursor.getColumnIndexOrThrow(DataContract.OnCallCrimesEntry.COLUMN_LOCATION);
        final int idx_desc = cursor.getColumnIndexOrThrow(DataContract.OnCallCrimesEntry.COLUMN_DESC);

        // convert date formats.
        String datetime = cursor.getString(idx_time);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat tdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        String formatted ="";
        try {
            Date d = df.parse(datetime);
            formatted = tdf.format(d);
        } catch(Exception e) {

        }

        String ret = formatted + ": " +cursor.getString(idx_loc) + "\n" + cursor.getString(idx_desc);
        return ret;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_crime, parent, false);

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
