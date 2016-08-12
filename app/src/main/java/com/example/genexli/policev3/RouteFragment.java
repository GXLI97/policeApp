package com.example.genexli.policev3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.genexli.policev3.data.DataContract;

/**
 * Created by genexli on 5/25/16.
 */
public class RouteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RouteAdapter routeAdapter;
    private static final int ROUTELOADER = 200;
    public RouteFragment(){

    }

    private static final String[] ROUTE_COLS = {
            DataContract.RoutesEntry.TABLE_NAME + "." + DataContract.RoutesEntry._ID,
            DataContract.RoutesEntry.COLUMN_TIME,
            DataContract.RoutesEntry.COLUMN_LOCATION,
            DataContract.RoutesEntry.COLUMN_DIST_TO,
            DataContract.RoutesEntry.COLUMN_TIME_TO,
            DataContract.RoutesEntry.COLUMN_LATITUDE,
            DataContract.RoutesEntry.COLUMN_LONGITUDE,
            DataContract.RoutesEntry.COLUMN_ROUTE_DESCRIPTION
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_route, container, false);
        // button for accepting an independent route.
        final Button button = (Button) rootView.findViewById(R.id.independent);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Clicked", "independent routes button");
            }
        });

        routeAdapter = new RouteAdapter(getActivity(), null, 0);
        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_route);
        listView.setAdapter(routeAdapter);

        // When an item is clicked, it opens up an intent to go to the detail Activity
        // This detail activity lists more information about the route.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if(cursor != null) {
                    Intent intent = new Intent(getActivity(),DetailRouteActivity.class)
                            .setData(DataContract.RoutesEntry.buildRoutesUri(l));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(ROUTELOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                DataContract.RoutesEntry.CONTENT_URI,
                ROUTE_COLS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        routeAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        routeAdapter.swapCursor(null);
    }
}
