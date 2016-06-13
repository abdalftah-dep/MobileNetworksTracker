package com.blogspot.droidcrib.mobilenetworkstracker.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by abulanov on 13.06.2016.
 */
public class PositioningManager {

    public static final String ACTION_LOCATION_CHANGED = "com.blogspot.droidcrib.mobilenetworkstracker.action.LOCATION_CHANGED";

    private LocationManager mLocationManager;
    private Context mContext;

    public PositioningManager(Context applicationContext) {
        mContext = applicationContext;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    // Starts location updates
    public void startLocationUpdates() {
        String providerGps = LocationManager.GPS_PROVIDER;
        String providerNetwork = LocationManager.NETWORK_PROVIDER;
        // Launch updates from LocationManager
        PendingIntent pi = getLocationPendingIntent(true);
        mLocationManager.requestLocationUpdates(providerGps, 0, 15, pi);
        mLocationManager.requestLocationUpdates(providerNetwork, 0, 15, pi);
    }

    // Stops location updates
    public void stopLocationUpdates() {
        PendingIntent pi = getLocationPendingIntent(false);
        if (pi != null) {
            mLocationManager.removeUpdates(pi);
            pi.cancel();
        }
    }

    //
    public boolean isLocationUpdatesOn() {
        return getLocationPendingIntent(false) != null;
    }

    // Returns last known location
    public Location getLastKnownLocation() {
        return mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    // Crates PendingIntent for location updates
    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent broadcast = new Intent(ACTION_LOCATION_CHANGED);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(mContext, 0, broadcast, flags);
    }

}
