// ++ insertTrack(Track track) - Inserts record if a new track into table "track"
// ++ insertPinPoint(long trackId, PinPoint point) - Inserts new PinPoint data - row that represents PinPoint object into database table
// ++ queryAllTracks() -  Queries all records from table "track"
// ?? queryTrack(long id) - Queries single record with defined id from table "track"
// ++ queryPinPointsForTrack(long trackId) - Queries all PinPoints for given track
// queryPinPointsForTrackMapVisibleArea(long trackId, double latSw, double lonSw, double latNe, double lonNe) - Queries PinPoints for given track which are within map visible area
// queryFirstPinPointForTrack(long trackId) - Queries first PinPoint for given track
// ++ queryFirstNotUploadedPinPoint() - Queries first PinPoint which not uploaded to server yet
// ++ queryAllNotUploadedPinPoints() - Queries all PinPoints which not uploaded to server yet
// ++ updateUploadedPinPoint(long pinpointId) - Updates field "upload" of table "location" with TRUE
// ++ deletePinPointsForTrack(long trackId) - Deletes all records with specified track_id from table "location"
// ++ deleteTrack(long id) - Deletes record with specified id from table "track"


package com.blogspot.droidcrib.mobilenetworkstracker.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.blogspot.droidcrib.mobilenetworkstracker.database.DatabaseHelper;
import com.blogspot.droidcrib.mobilenetworkstracker.model.PinPoint;
import com.blogspot.droidcrib.mobilenetworkstracker.model.Track;

import java.util.Date;
import java.util.List;

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
    public TrackManager(Context appContext) {
        mAppContext = appContext;
        mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
//        mHelper = new DatabaseHelper(mAppContext);
        mPrefs = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mCurrentTrackId = mPrefs.getLong(PREF_CURRENT_TRACK_ID, -1);
    }

    // Creates instance of TrackManager
//    public static TrackManager get(Context context) {
//        if (sTrackManager == null) {
//            sTrackManager = new TrackManager(context);
//        }
//        return sTrackManager;
//    }

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




    public Track insertTrack() {
        Track track = new Track();
        track.startDate = new Date();
        track.save();
        return track;
    }


    public void insertPinPoint(PinPoint point) {
        if (mCurrentTrackId != -1) {
//            mHelper.insertPinPoint(mCurrentTrackId, point);

        } else {
            Log.e(TAG, "PinPoint received with no tracking run; ignoring.");
        }
    }

    public List<Track> queryAllTracks() {
        return new Select().from(Track.class).execute();
    }

    public List<PinPoint> queryAllPinpoints(){
        return new Select().from(PinPoint.class).execute();
    }



    /**
     * Queries all PinPoints for given track.
     */
    public List<PinPoint> queryPinPointsForTrack(long trackId) {
        return new Select().from(PinPoint.class).where("track_id = ?", trackId).execute();
    }

    /**
     * Queries first PinPoint which not uploaded to server yet
     */
    public PinPoint queryFirstNotUploadedPinPoint() {
        return new Select().from(PinPoint.class).where("upload = ?", false).executeSingle();
    }

    /**
     * Queries all PinPoints which were not uploaded to server yet
     */
    public List<PinPoint> queryAllNotUploadedPinPoints() {
        return new Select().from(PinPoint.class).where("upload = ?", false).execute();
    }

    public void updateUploadedPinPoint(long pinpointId) {
//        new Update(PinPoint.class).set("upload = 'true'").where("id = ?", pinpointId).execute();
        PinPoint pinPoint = PinPoint.load(PinPoint.class, pinpointId);
        pinPoint.upload = true;
        pinPoint.save();

        Log.d(TAG, "PinPoint record number " + pinpointId + " is set to TRUE");
    }

    public void deletePinPointsForTrack(long trackId) {
        new Delete().from(PinPoint.class).where("track_id = ?", trackId).execute();
    }

    public void deleteTrack(long id) {
        new Delete().from(Track.class).where("_id = ?", id).execute();
    }

//    public PinPointCursor queryPinPointsForTrackMapVisibleArea(
//            long trackId, double latSw, double lonSw, double latNe, double lonNe) {
//        return mHelper.queryPinPointsForTrackMapVisibleArea(trackId, latSw, lonSw, latNe, lonNe);
//    }
//
//    public PinPointCursor queryFirstPinPointForTrack(long trackId) {
//        return mHelper.queryFirstPinPointForTrack(trackId);
//    }

}
