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
public class PatrolFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private PatrolAdapter patrolAdapter;
    private static final int PATROLFRAGMENTLOADER = 401;
    private static final String[] PATROL_COLS = {
            DataContract.PatrolsEntry.TABLE_NAME + "." + DataContract.PatrolsEntry._ID,
            DataContract.PatrolsEntry.COLUMN_IDENTIFIER,
            DataContract.PatrolsEntry.COLUMN_LOCATION,
            DataContract.PatrolsEntry.COLUMN_LATITUDE,
            DataContract.PatrolsEntry.COLUMN_LONGITUDE,
            DataContract.PatrolsEntry.COLUMN_PRECINCT
    };
    public PatrolFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patrol, container, false);
        patrolAdapter = new PatrolAdapter(getActivity(), null, 0);
        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_patrol);
        listView.setAdapter(patrolAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(PATROLFRAGMENTLOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                DataContract.PatrolsEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        patrolAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        patrolAdapter.swapCursor(null);
    }
}
