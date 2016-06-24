package com.blogspot.droidcrib.mobilenetworkstracker.ui;

/**
 * Created by andrey on 16.02.2016.
 */


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.blogspot.droidcrib.mobilenetworkstracker.application.MobileNetworksTrackerApp;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.LocationReceiver;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.PositioningManager;
import com.blogspot.droidcrib.mobilenetworkstracker.telephony.CustomPhoneStateListener;
import com.blogspot.droidcrib.mobilenetworkstracker.telephony.TelephonyInfo;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.TrackingManager;
import com.blogspot.droidcrib.mobilenetworkstracker.R;
import com.blogspot.droidcrib.mobilenetworkstracker.internet.UploadDataService;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity implements TrackListFragment.Callbacks{


    private static final String TAG = "mobilenetworkstracker";
    private ViewPager mViewPager;
    private Menu mMenu;
    @Inject TelephonyInfo mTelephonyInfo;
    @Inject TrackingManager mTrackingManager;
    @Inject PositioningManager mPositioningManager;
    @Inject CustomPhoneStateListener mCustomPhoneStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        ((MobileNetworksTrackerApp)getApplication()).getBaseComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.bs4);
        toolbar.setLogoDescription("logo description here");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabTextColors(getResources().getColorStateList(R.color.theme_window_background));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.tab_data)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.tab_series)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.tab_map)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //
        //  Setup ViewPager
        //
        mViewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //Refresh network information when DataFragment is shown
                if (position == 0) {
                    DataFragment.getInstance().updateUI();
                }
                //Refresh list of tracks when ListFragment is shown
                if (position == 1) {
                    TrackListFragment.getInstance().refreshTrackList();
                }
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start location updates on program first start if not started yet
        if (!mPositioningManager.isLocationUpdatesEnabled()) {
            mPositioningManager.startLocationUpdates();
        }

        
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop tracking if isTrackingOn == false
        if (!mTrackingManager.isTrackingOn()) {
            mPositioningManager.stopLocationUpdates();
            handleNotification(LocationReceiver.ACTION_STOP_NOTIFICATION);
        }
    }

    /**
     * Create menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.track_list_options, menu);
        if (mTrackingManager.isTrackingOn()) {
            mMenu.getItem(0).setIcon(R.drawable.menu_item_location_off_selector);
        }
        return true;
    }

    /**
     * Menu item selected
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_new_track) {
            if (!mTrackingManager.isTrackingOn()) {

                // Insert record of new track into database
                mTrackingManager.startTracking();
                // Start notification of tracking
                handleNotification(LocationReceiver.ACTION_START_NOTIFICATION);
                // Change icon
                mMenu.getItem(0).setIcon(R.drawable.menu_item_location_off_selector);

                if(TrackListFragment.getInstance().isVisible()){
                    TrackListFragment.getInstance().refreshTrackList();
                }

            } else if (mTrackingManager.isTrackingOn()) {

                mTrackingManager.stopTracking();
                // Stop notification
                handleNotification(LocationReceiver.ACTION_STOP_NOTIFICATION);
                // Change icon
                mMenu.getItem(0).setIcon(R.drawable.menu_item_location_on_selector);
            }
            return true;

        } else if (id == R.id.menu_item_upload) {

            // Start service to upload data
            Intent uploadDataService = new Intent(getApplicationContext(), UploadDataService.class);
            getApplicationContext().startService(uploadDataService);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Interface method TrackListFragment.Callbacks
     * Passes track ID to MapFragment in order to show selected track on map.
     * Switching to map fragment
     * @param trackId
     */
    @Override
    public void onTrackSelected(long trackId) {
        // Switch to tab with Map
        mViewPager.setCurrentItem(2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        // find currently shown fragment by tag
        Fragment fragment = fragmentManager
                .findFragmentByTag("android:switcher:" + R.id.pager + ":" + mViewPager.getCurrentItem());
        if (fragment != null) {
            // pass current trackId to fragment if fragment exist
            ((TrackMapFragment) fragment).onTrackSelected(trackId);
        }
    }

    /**
     * Sends broadcast message to stop notifications
     */
    private void handleNotification(String action) {
        Intent i = new Intent();
        i.setAction(action);
        sendBroadcast(i);
    }

}
