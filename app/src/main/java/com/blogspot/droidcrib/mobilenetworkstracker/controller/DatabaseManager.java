package com.blogspot.droidcrib.mobilenetworkstracker.controller;

import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.blogspot.droidcrib.mobilenetworkstracker.model.PinPoint;
import com.blogspot.droidcrib.mobilenetworkstracker.model.Track;

import java.util.List;

/**
 * Created by abulanov on 13.06.2016.
 */
public class DatabaseManager {

    private static final String TAG = "mobilenetworkstracker";

    public Track queryTrack(long trackId){
        return new Select()
                .from(Track.class)
                .where("_id = ?", trackId)
                .executeSingle();
    }



    public void insertPinPoint(long trackId, int signalStrenghts, String networkTypeForJSON, String lac,
                               String ci, String terminal, double lat, double lon,
                               String operatorName, Track track) {
            PinPoint pinPoint = new PinPoint();
            pinPoint.signalStrengths = signalStrenghts;
            pinPoint.networkType = networkTypeForJSON;
            pinPoint.lac = lac;
            pinPoint.ci = ci;
            pinPoint.terminal = terminal;
            pinPoint.lat = lat;
            pinPoint.lon = lon;
            pinPoint.eventTime = System.currentTimeMillis();
            pinPoint.operator = operatorName;
            pinPoint.country = "UA";
            pinPoint.upload = false;
            pinPoint.track = track;
            pinPoint.trackId = track.getId();
            pinPoint.save();

            Log.d(TAG, "insert PinPoint");
    }

    public List<Track> queryAllTracks() {
        return new Select()
                .from(Track.class)
                .execute();
    }

    public List<PinPoint> queryAllPinpoints(){
        return new Select()
                .from(PinPoint.class)
                .execute();
    }

    /**
     * Queries all PinPoints for given track.
     */
    public List<PinPoint> queryPinPointsForTrack(long trackId) {
        return new Select()
                .from(PinPoint.class)
                .where("track_id = ?", trackId)
                .execute();
    }

    /**
     * Queries first PinPoint which not uploaded to server yet
     */
    public PinPoint queryFirstNotUploadedPinPoint() {
        return new Select()
                .from(PinPoint.class)
                .where("upload = ?", false)
                .executeSingle();
    }

    /**
     * Queries all PinPoints which were not uploaded to server yet
     */
    public List<PinPoint> queryAllNotUploadedPinPoints() {
        return new Select()
                .from(PinPoint.class)
                .where("upload = ?", false)
                .execute();
    }

    /**
     * Marking current pinpoint as uploaded by setting field "upload" with "true" value
     * @param pinpointId
     */

    public void updateUploadedPinPoint(long pinpointId) {
        PinPoint pinPoint = PinPoint.load(PinPoint.class, pinpointId);
        pinPoint.upload = true;
        pinPoint.save();
    }

    /**
     * Removes track and all related pinpoints as they are connected via external key
     * @param id
     */
    public void deleteTrack(long id) {
        new Delete()
                .from(Track.class)
                .where("_id = ?", id)
                .execute();
    }

    /**
     * Queries all pinpoints related to given track and within selected geo area
     * @param trackId
     * @param latSw
     * @param lonSw
     * @param latNe
     * @param lonNe
     * @return
     */
    public List<PinPoint> queryPinPointsForTrackMapVisibleArea(
            long trackId, double latSw, double lonSw, double latNe, double lonNe) {
        return new Select()
                .from(PinPoint.class)
                .where("track_id = ? AND lat > ? AND lon > ? AND lat < ? AND lon < ?",
                        trackId, latSw, lonSw, latNe, lonNe)
                .execute();
    }

    /**
     * Queries first point for given track
     * @param trackId
     * @return
     */
    public PinPoint queryFirstPinPointForTrack(long trackId) {
        return new Select()
                .from(PinPoint.class)
                .where("track_id = ?", trackId)
                .orderBy("_id ASC")
                .executeSingle();
    }

}
