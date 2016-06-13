// ++ insertTrack(Track track) - Inserts record if a new track into table "track"
// ++ insertPinPoint(long trackId, PinPoint point) - Inserts new PinPoint data - row that represents PinPoint object into database table
// ++ queryAllTracks() -  Queries all records from table "track"
// ?? queryTrack(long id) - Queries single record with defined id from table "track"
// ++ queryPinPointsForTrack(long trackId) - Queries all PinPoints for given track
// ++ queryPinPointsForTrackMapVisibleArea(long trackId, double latSw, double lonSw, double latNe, double lonNe) - Queries PinPoints for given track which are within map visible area
// ++ queryFirstPinPointForTrack(long trackId) - Queries first PinPoint for given track
// ++ queryFirstNotUploadedPinPoint() - Queries first PinPoint which not uploaded to server yet
// ++ queryAllNotUploadedPinPoints() - Queries all PinPoints which not uploaded to server yet
// ++ updateUploadedPinPoint(long pinpointId) - Updates field "upload" of table "location" with TRUE
//not needed ++ deletePinPointsForTrack(long trackId) - Deletes all records with specified track_id from table "location"
// ++ deleteTrack(long id) - Deletes record with specified id from table "track"


package com.blogspot.droidcrib.mobilenetworkstracker.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.blogspot.droidcrib.mobilenetworkstracker.model.PinPoint;
import com.blogspot.droidcrib.mobilenetworkstracker.model.Track;

import java.util.Date;
import java.util.List;

/**
 * Created by Andrey on 07.01.2016.
 */
public class TrackingManager {

    private static final String TAG = "mobilenetworkstracker";

    private static final String PREFS_FILE = "tracks";
    public static final String PREF_CURRENT_TRACK_ID = "TrackingManager.currentTrackId";
    private static final String PREF_IS_TRACKING_ON = "TrackingManager.isTrackingOn";
    private Context mAppContext;
    private SharedPreferences mPrefs;
    private long mCurrentTrackId;

    // constructor
    public TrackingManager(Context appContext) {
        mAppContext = appContext;
        mPrefs = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mCurrentTrackId = mPrefs.getLong(PREF_CURRENT_TRACK_ID, -1);
    }

    /**
     * Starts network tracking
     */

    public void startTracking() {
        Track track = new Track();
        track.startDate = new Date();
        track.save();
        // Get Id
        mCurrentTrackId = track.getId();
        // Save id in shared prefs
        mPrefs.edit().putLong(PREF_CURRENT_TRACK_ID, mCurrentTrackId).apply();
        mPrefs.edit().putBoolean(PREF_IS_TRACKING_ON, true).apply();
        Log.d(TAG, "startTracking called");
    }


    /**
     * Stops network tracking
     */
    public void stopTracking() {
        mCurrentTrackId = -1;
        mPrefs.edit().remove(PREF_CURRENT_TRACK_ID).apply();
        mPrefs.edit().remove(PREF_IS_TRACKING_ON).apply();
        Log.d(TAG, "stopTracking called");
    }

    /**
     * Provides network tracking status
     * @return
     */
    public boolean isTrackingOn() {
        Log.d(TAG, "isTrackingOn: " + (mPrefs.getBoolean(PREF_IS_TRACKING_ON, false)));
        return mPrefs.getBoolean(PREF_IS_TRACKING_ON, false);
    }





}
