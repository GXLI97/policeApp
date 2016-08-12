/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.genexli.policev3;

import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.genexli.policev3.data.DataContract;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DetailRouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_route);
        // this creates the up navigation button on the top.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // go up page if the home button is clicked.
        if(id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fragment that displays information about the route.
     */
    public static class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        private String mRoute;
        private double lat;
        private double longg;
        private static final int DETAIL_LOADER = 201;

        // a projection.
        private static final String[] ROUTE_COLUMNS = {
                DataContract.RoutesEntry.TABLE_NAME + "." + DataContract.RoutesEntry._ID,
                DataContract.RoutesEntry.COLUMN_TIME,
                DataContract.RoutesEntry.COLUMN_LOCATION,
                DataContract.RoutesEntry.COLUMN_DIST_TO,
                DataContract.RoutesEntry.COLUMN_TIME_TO,
                DataContract.RoutesEntry.COLUMN_LATITUDE,
                DataContract.RoutesEntry.COLUMN_LONGITUDE,
                DataContract.RoutesEntry.COLUMN_ROUTE_DESCRIPTION
        };

        public static String routename;

        public DetailFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail_route, container, false);
            final Button button = (Button) rootView.findViewById(R.id.button_accept);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("Clicked", "The accept button");
                    // navigate up.
                    NavUtils.navigateUpFromSameTask(getActivity());

                    // go to google maps.
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(lat+","+longg));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Log.v("Log", "In onCreateLoader");
            Intent intent = getActivity().getIntent();
            if(intent == null) {
                return null;
            }

            return new CursorLoader(
                    getActivity(),
                    intent.getData(),
                    ROUTE_COLUMNS,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (!data.moveToFirst()) { return; }
            int idx_location = data.getColumnIndexOrThrow(DataContract.RoutesEntry.COLUMN_LOCATION);
            int idx_timeto = data.getColumnIndexOrThrow(DataContract.RoutesEntry.COLUMN_TIME_TO);
            int idx_distto = data.getColumnIndexOrThrow(DataContract.RoutesEntry.COLUMN_DIST_TO);
            int idx_desc = data.getColumnIndexOrThrow(DataContract.RoutesEntry.COLUMN_ROUTE_DESCRIPTION);
            lat = data.getDouble(data.getColumnIndexOrThrow(DataContract.RoutesEntry.COLUMN_LATITUDE));
            longg = data.getDouble(data.getColumnIndexOrThrow(DataContract.RoutesEntry.COLUMN_LONGITUDE));

            // Gets address from GPS coordinates.
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            String _Location = "";
            try {
                List<Address> listAddresses = geocoder.getFromLocation(lat, longg, 1);
                if(null!=listAddresses&&listAddresses.size()>0) {
                    _Location = listAddresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            mRoute = _Location + "\n\n" + data.getInt(idx_timeto)
                    + " minutes away \n" + data.getDouble(idx_distto)
                    + " miles away \n \n" + data.getString(idx_desc);

            TextView detailTextView = (TextView)getView().findViewById(R.id.detail_route_text);
            detailTextView.setText(mRoute);
            routename = data.getString(idx_location);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {}
    }
}