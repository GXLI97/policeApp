package com.example.genexli.policev3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private BottomBar bottomBar;
    private MapFragment mf;
    private RouteFragment rf;
    private PatrolFragment pf;
    private CrimeFragment cf;
    private OptionsFragment of;
    private SharedPreferences SP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom bar stuff for navigation.
        // attaches bottom bar
        bottomBar = BottomBar.attach(this, savedInstanceState);
        // bottom bar displays all menu items.
        bottomBar.useFixedMode();
        // dark theme :O
        bottomBar.useDarkTheme();
        // initiates the fragments;
        mf = new MapFragment();
        rf = new RouteFragment();
        pf = new PatrolFragment();
        cf = new CrimeFragment();
        of = new OptionsFragment();
        bottomBar.setItemsFromMenu(R.menu.bottom_menu, new OnMenuTabClickListener() {
            // a switch to populate the fragment with a new fragment, depending on which menu item is clicked.

            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                //hide all fragments.
                if(mf.isAdded()) {ft.hide(mf);}
                if(rf.isAdded()) {ft.hide(rf);}
                if(pf.isAdded()) {ft.hide(pf);}
                if(cf.isAdded()) {ft.hide(cf);}
                if(of.isAdded()) {ft.hide(of);}

                switch (menuItemId) {
                    case R.id.map_item:
                        if(mf.isAdded()) ft.show(mf);
                        else ft.add(R.id.fragment_container, mf);
                        break;
                    case R.id.routes_item:
                        if(rf.isAdded()) ft.show(rf);
                        else ft.add(R.id.fragment_container, rf);
                        break;
                    case R.id.patrols_item:
                        if(pf.isAdded()) ft.show(pf);
                        else ft.add(R.id.fragment_container, pf);
                        break;
                    case R.id.crime_item:
                        if(cf.isAdded()) ft.show(cf);
                        else ft.add(R.id.fragment_container, cf);
                        break;
                    case R.id.options_item:
                        if(of.isAdded()) ft.show(of);
                        else ft.add(R.id.fragment_container, of);
                        break;
                }
                ft.commit();
            }
            // method that has to be declared, but not doing anything as of right now.
            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {}
        });
        SP = PreferenceManager.getDefaultSharedPreferences(this);
        onSharedPreferenceChanged(SP, "gps_sync");
        SP.registerOnSharedPreferenceChangeListener(this);

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if(key.equals("gps_sync")) {
            boolean gps = sharedPreferences.getBoolean(key, false);
            Log.e("GPS SYNC status", gps + "");

            Intent i = new Intent(getApplicationContext(), UpdateGPS.class);
            PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, i, 0);
            AlarmManager alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            if(gps) {
                // can change the times to AlarmManager.INTERVAL_FIFTEEN_MINUTES or something.
                alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME, 5000,
                        60000, pintent);
            }
            else {
                alarmMgr.cancel(pintent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // if user hits the refresh button.
        if(id == R.id.action_refresh) {
            Intent i = new Intent(this, FetchDataService.class);
            this.startService(i);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SP.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SP.unregisterOnSharedPreferenceChangeListener(this);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        bottomBar.onSaveInstanceState(outState);
    }
}










