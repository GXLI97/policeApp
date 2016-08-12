package com.example.genexli.policev3;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.genexli.policev3.data.DataContract;

/**
 * Created by genexli on 5/25/16.
 */
public class CrimeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private CrimeAdapter crimeAdapter;
    private static final int CRIMELOADER = 100;

    // projection.
    private static final String[] CRIME_COLS ={
            DataContract.OnCallCrimesEntry.TABLE_NAME + "." + DataContract.OnCallCrimesEntry._ID,
            DataContract.OnCallCrimesEntry.COLUMN_TIME,
            DataContract.OnCallCrimesEntry.COLUMN_LOCATION,
            DataContract.OnCallCrimesEntry.COLUMN_DESC,
            DataContract.OnCallCrimesEntry.COLUMN_LATITUDE,
            DataContract.OnCallCrimesEntry.COLUMN_LONGITUDE,
            DataContract.OnCallCrimesEntry.COLUMN_PRECINCT
    };

    public CrimeFragment(){
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        crimeAdapter = new CrimeAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_crime, container, false);
        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_crime);
        listView.setAdapter(crimeAdapter);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CRIMELOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                DataContract.OnCallCrimesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        crimeAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        crimeAdapter.swapCursor(null);
    }
}
