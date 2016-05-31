package com.blogspot.droidcrib.mobilenetworkstracker.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.blogspot.droidcrib.mobilenetworkstracker.database.DatabaseHelper;
import com.blogspot.droidcrib.mobilenetworkstracker.database.DatabaseHelper.TrackCursor;
import com.blogspot.droidcrib.mobilenetworkstracker.database.DatabaseHelper.PinPointCursor;
import com.blogspot.droidcrib.mobilenetworkstracker.model.PinPoint;
import com.blogspot.droidcrib.mobilenetworkstracker.model.Track;

/**
 * Created by Andrey on 07.01.2016.
 */
public class TrackManager {

    private static final String TAG = "mobilenetworkstracker";

    private static final String PREFS_FILE = "tracks";
    private static final String PREF_CURRENT_TRACK_ID = "TrackManager.currentTrackId";
    private static final String PREF_IS_TRACKING_ON = "TrackManager.isTrackingOn";

    public static final String ACTION_LOCATION_CHANGED = "com.blogspot.droidcrib.mobilenetworkstracker.action.LOCATION_CHANGED";

    private static TrackManager sTrackManager;
    private Context mAppContext;
    private LocationManager mLocationManager;
    private DatabaseHelper mHelper;
    private SharedPreferences mPrefs;
    private long mCurrentTrackId;

    // Private constructor
    private TrackManager(Context appContext) {
        mAppContext = appContext;
        mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
        mHelper = new DatabaseHelper(mAppContext);
        mPrefs = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mCurrentTrackId = mPrefs.getLong(PREF_CURRENT_TRACK_ID, -1);
    }

    // Creates instance of TrackManager
    public static TrackManager get(Context context) {
        if (sTrackManager == null) {
            sTrackManager = new TrackManager(context);
        }
        return sTrackManager;
    }

    // Crates PendingIntent
    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent broadcast = new Intent(ACTION_LOCATION_CHANGED);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);
    }

    public void startLocationUpdates() {
        String providerGps = LocationManager.GPS_PROVIDER;
        String providerNetwork = LocationManager.NETWORK_PROVIDER;
        // Launch updates from LocationManager
        PendingIntent pi = getLocationPendingIntent(true);
        mLocationManager.requestLocationUpdates(providerGps, 0, 15, pi);
        mLocationManager.requestLocationUpdates(providerNetwork, 0, 15, pi);
    }

    public void stopLocationUpdates() {
        PendingIntent pi = getLocationPendingIntent(false);
        if (pi != null) {
            mLocationManager.removeUpdates(pi);
            pi.cancel();
        }
    }

    public Location getLastLocation() {
        return mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    public boolean isTrackingRun() {
        return getLocationPendingIntent(false) != null;
    }

    public boolean isTrackingRun(Track track) {
        return track != null && track.getId() == mCurrentTrackId;
    }

    public Track startNewTrack() {
        // Insert new track object into DB
        Track track = insertTrack();
        // Start tracking series
        startTracking(track);
        return track;
    }

    public void startTracking(Track track) {
        // Get Id
        mCurrentTrackId = track.getId();
        // Save id in shared prefs
        mPrefs.edit().putLong(PREF_CURRENT_TRACK_ID, mCurrentTrackId).commit();
        // Start tracking location
        startLocationUpdates();
    }

    public void stopTrack() {
        stopLocationUpdates();
        mCurrentTrackId = -1;
        mPrefs.edit().remove(PREF_CURRENT_TRACK_ID).commit();
    }

    public void setTrackingState(boolean state) {
        boolean current = mPrefs.getBoolean(PREF_IS_TRACKING_ON, false);
        mPrefs.edit().putBoolean(PREF_IS_TRACKING_ON, state).commit();
    }

    public boolean isTrackingEnabled() {
        return mPrefs.getBoolean(PREF_IS_TRACKING_ON, false);
    }

    public void removeTrackingState() {
        mPrefs.edit().remove(PREF_IS_TRACKING_ON).commit();
    }

    private Track insertTrack() {
        Track track = new Track();
        track.setId(mHelper.insertTrack(track));
        return track;
    }

    public void insertPinPoint(PinPoint point) {
        if (mCurrentTrackId != -1) {
            mHelper.insertPinPoint(mCurrentTrackId, point);
        } else {
            Log.e(TAG, "PinPoint received with no tracking run; ignoring.");
        }
    }

    public TrackCursor queryTracks() {
        return mHelper.queryTracks();
    }

    public Track getTrack(long id) {
        Track track = null;
        TrackCursor cursor = mHelper.queryTrack(id);
        cursor.moveToFirst();
        // get series object if line exist
        if (!cursor.isAfterLast()) {
            track = cursor.getTrack();
        }
        cursor.close();
        return track;
    }

    /**
     * Queries all PinPoints for given track.
     * Fasade for DatabaseHelper.queryPinPointsForTrack.
     *
     * @param trackId
     * @return Cursor
     */
    public PinPointCursor queryPinPointsForTrack(long trackId) {
        return mHelper.queryPinPointsForTrack(trackId);
    }

    /**
     * Queries first PinPoint which not uploaded to server yet
     * Fasade for DatabaseHelper.queryPinPointsForTrack.
     *
     * @return PinPointCursor
     */
    public PinPointCursor queryFirstNotUploadedPinPoint() {
        return mHelper.queryFirstNotUploadedPinPoint();
    }

    /**
     * Queries all PinPoints which were not uploaded to server yet
     * Fasade for DatabaseHelper.queryPinPointsForTrack.
     *
     * @return PinPointCursor
     */
    public PinPointCursor queryAllNotUploadedPinPoints() {
        return mHelper.queryAllNotUploadedPinPoints();
    }

    public int updateUploadedPinPoint(long pinpointId) {
        return mHelper.updateUploadedPinPoint(pinpointId);
    }

    public int deletePinPointsForTrack(long trackId) {
        return mHelper.deletePinPointsForTrack(trackId);
    }

    public int deleteTrack(long id) {
        return mHelper.deleteTrack(id);
    }

    public PinPointCursor queryPinPointsForTrackMapVisibleArea(
            long trackId, double latSw, double lonSw, double latNe, double lonNe) {
        return mHelper.queryPinPointsForTrackMapVisibleArea(trackId, latSw, lonSw, latNe, lonNe);
    }

    public PinPointCursor queryFirstPinPointForTrack(long trackId) {
        return mHelper.queryFirstPinPointForTrack(trackId);
    }

}
