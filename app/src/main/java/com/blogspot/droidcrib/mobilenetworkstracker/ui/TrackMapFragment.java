package com.blogspot.droidcrib.mobilenetworkstracker.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.location.Location;

import com.blogspot.droidcrib.mobilenetworkstracker.application.MobileNetworksTrackerApp;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.DatabaseManager;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.PositioningManager;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.TrackingManager;
import com.blogspot.droidcrib.mobilenetworkstracker.loaders.PinpointLoader;
import com.blogspot.droidcrib.mobilenetworkstracker.model.PinPoint;
import com.blogspot.droidcrib.mobilenetworkstracker.telephony.CustomPhoneStateListener;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.LocationReceiver;
import com.blogspot.droidcrib.mobilenetworkstracker.R;
import com.blogspot.droidcrib.mobilenetworkstracker.telephony.TelephonyInfo;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Andrey on 13.02.2016.
 */
public class TrackMapFragment extends SupportMapFragment
        implements LoaderManager.LoaderCallbacks<List<?>>{

    private static final String PREFS_FILE = "tracks";
    private static final String PREF_CURRENT_TRACK_ID = "TrackingManager.currentTrackId";
    private static final int LOAD_LOCATIONS = 0;
    public static final String TAG = "mobilenetworkstracker";

    private GoogleMap mGoogleMap;
    private SharedPreferences mPrefs;
    private long mCurrentTrackId = -1;
    private long mSelectedTrackId = -1;
    private double mLatSw;
    private double mLonSw;
    private double mLatNe;
    private double mLonNe;
    private float mCurrentZoom = 16.0F;
    private TrackMapFragment mTrackMapFragment;
    @Inject TelephonyInfo mTelephonyInfo;
    @Inject TrackingManager mTrackingManager;
    @Inject PositioningManager mPositioningManager;
    @Inject DatabaseManager mDatabaseManager;
    List<PinPoint> mPinpointList;

    private BroadcastReceiver mLocationReceiver = new LocationReceiver() {
        @Override
        protected void onLocationReceived(Context context, Location loc, int signalStrengths) {
            super.onLocationReceived(context, loc, signalStrengths);
            if (!mPositioningManager.isLocationUpdatesEnabled()) {
                return;
            }
            if (isVisible() && mSelectedTrackId == -1) {
                drawRealTimePoint(loc, signalStrengths);
            }
        }

        @Override
        protected void onProviderEnabledChanged(boolean enabled) {
            super.onProviderEnabledChanged(enabled);
            int toastText = enabled ? R.string.gps_enabled : R.string.gps_disabled;
            Toast.makeText(getActivity().getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MobileNetworksTrackerApp)getActivity().getApplication())
                .getBaseComponent().inject(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTrackMapFragment = this;
        mPrefs = getActivity().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mCurrentTrackId = mPrefs.getLong(PREF_CURRENT_TRACK_ID, -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        // save link to GoogleMap
        mGoogleMap = getMap();

        // show user location
        mGoogleMap.setMyLocationEnabled(true);

        // add zoom buttons on map
        UiSettings uiSettings = mGoogleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mLocationReceiver,
                new IntentFilter(mPositioningManager.ACTION_LOCATION_CHANGED));
        Location lastKnownLocation = mPositioningManager.getLastKnownLocation();
        if (lastKnownLocation != null) {
            //TODO take RSSI from interface here
            drawRealTimePoint(lastKnownLocation, CustomPhoneStateListener.sSignalStrengths);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLngBounds bounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;
                mLatSw = bounds.southwest.latitude;
                mLonSw = bounds.southwest.longitude;
                mLatNe = bounds.northeast.latitude;
                mLonNe = bounds.northeast.longitude;
                mCurrentZoom = mGoogleMap.getCameraPosition().zoom;
                getLoaderManager().restartLoader(LOAD_LOCATIONS, null, mTrackMapFragment);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mSelectedTrackId = -1;
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mLocationReceiver);
    }



    /**
     * Draws current location marker on map
     */

    private void drawRealTimePoint(Location loc, int signalStrengths) {
        double lat = loc.getLatitude();
        double lon = loc.getLongitude();

        // move camera to current location
        LatLng latLng = new LatLng(lat, lon);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, mCurrentZoom);
        mGoogleMap.moveCamera(cameraUpdate);

        // Draw point on map
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(lat, lon)).radius(15)
                .fillColor(pinPointColor(signalStrengths))
                .strokeWidth(0);
        mGoogleMap.addCircle(circleOptions);
    }


    /**
     * Draws markers for data queried in CursorLoader
     */
    private void drawQueriedDataOnMap() {

        if (mSelectedTrackId != -1) {
            mGoogleMap.clear();
        }
        // positions bust
        for (int i=0; i < mPinpointList.size(); i++) {
            // Get data for marker

            double lat = mPinpointList.get(i).lat;
            double lon = mPinpointList.get(i).lon;
            int rxLevel = (int) mPinpointList.get(i).signalStrengths;

            // Draw signal level marker
            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(lat, lon)).radius(15)
                    .fillColor(pinPointColor(rxLevel))
                    .strokeWidth(0);
            mGoogleMap.addCircle(circleOptions);
        }
    }


    /**
     * Shows selected track data on map
     */
    public void onTrackSelected(long trackId) {

        mSelectedTrackId = trackId;

        if (trackId != -1) {
            Log.d(TAG, "Selected track.ID " + trackId);
            //get first point of track
            PinPoint pinPoint = mDatabaseManager.queryFirstPinPointForTrack(trackId);
            if (pinPoint != null) {
                double lat = pinPoint.lat;
                double lon = pinPoint.lon;


                Log.d(TAG, "FirstPinPointForTrack LAT  = " + lat);
                Log.d(TAG, "FirstPinPointForTrack LON  = " + lon);

                // move camera to first point
                LatLng latLng = new LatLng(lat, lon);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, mCurrentZoom);
                mGoogleMap.moveCamera(cameraUpdate);
            }
        }
    }

    /**
     * Provides color value for signal level
     *
     * @param rxLevel
     * @return color resource id
     */
    private int pinPointColor(int rxLevel) {
        int color = Color.WHITE;
        if (rxLevel < -50 && rxLevel >= -70) {
            color = getResources().getColor(R.color.legend_green);
        } // Green
        else if (rxLevel < -70 && rxLevel >= -80) {
            color = getResources().getColor(R.color.legend_light_green);
        } // Light Green
        else if (rxLevel < -80 && rxLevel >= -90) {
            color = getResources().getColor(R.color.legend_yellow);
        } // Yellow
        else if (rxLevel < -90 && rxLevel >= -100) {
            color = getResources().getColor(R.color.legend_orange);
        } // Orange
        else if (rxLevel < -100 && rxLevel >= -115) {
            color = getResources().getColor(R.color.legend_red);
        } // Red

        return color;
    }

    @Override
    public Loader<List<?>> onCreateLoader(int id, Bundle args) {
        return new PinpointLoader(getActivity(), mDatabaseManager, mSelectedTrackId,
                mLatSw, mLonSw, mLatNe, mLonNe);
    }

    @Override
    public void onLoadFinished(Loader<List<?>> loader, List<?> data) {
        mPinpointList = (List<PinPoint>) data;
        drawQueriedDataOnMap();
    }

    @Override
    public void onLoaderReset(Loader<List<?>> loader) {

    }
}
