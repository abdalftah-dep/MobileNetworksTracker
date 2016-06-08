package com.blogspot.droidcrib.mobilenetworkstracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.blogspot.droidcrib.mobilenetworkstracker.model.PinPoint;
import com.blogspot.droidcrib.mobilenetworkstracker.model.Track;

import java.util.Date;

/**
 * Created by Andrey on 01.02.2016.
 */
public class DatabaseHelper
//        extends SQLiteOpenHelper

{


//    public static final String TAG = "mobilenetworkstracker";
//
//    private static final String DB_NAME = "trakcs.sqlite";
//    private static final int VERSION = 1;
//
//    private static final String TABLE_TRACK = "track";
//    private static final String COLUMN_TRACK_START_DATE = "start_date";
//    private static final String COLUMN_TRACK_ID = "_id";
//
//    private static final String TABLE_LOCATION = "location";
//    private static final String COLUMN_LOCATION_ID = "_id";
//    private static final String COLUMN_LOCATION_TIMESTAMP = "timestamp";
//    private static final String COLUMN_LOCATION_LATITUDE = "latitude";
//    private static final String COLUMN_LOCATION_LONGITUDE = "longitude";
//    private static final String COLUMN_LOCATION_SIGNAL_STRENGTHS = "signal_strengths";
//    private static final String COLUMN_LOCATION_LAC = "lac";
//    private static final String COLUMN_LOCATION_CI = "ci";
//    private static final String COLUMN_LOCATION_NETWORK_TYPE = "network_type";
//    private static final String COLUMN_LOCATION_OPERATOR_NAME = "operator_name";
//    private static final String COLUMN_LOCATION_TERMINAL = "terminal";
//    private static final String COLUMN_LOCATION_PROVIDER = "provider";
//    private static final String COLUMN_LOCATION_TRACK_ID = "track_id";
//    private static final String COLUMN_LOCATION_UPLOAD = "upload";
//
//    public DatabaseHelper(Context context) {
//        super(context, DB_NAME, null, VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//        //java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
//
//        // Create table "Track"
//        db.execSQL("create table track " + "(_id integer primary key autoincrement, start_date integer)");
//
//        // Create table "PinPoint"
//        db.execSQL("create table location " +
//                "(_id integer primary key autoincrement, " +
//                "timestamp text, " +
//                "latitude real, " +
//                "longitude real, " +
//                "signal_strengths integer, " +
//                "lac text, " +
//                "ci text, " +
//                "network_type text, " +
//                "operator_name text, " +
//                "terminal text, " +
//                "provider varchar(100), " +
//                "track_id integer references track(_id), " +
//                "upload  text)");
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//    }
//
//    /**
//     * Inserts record if a new track into table "track"
//     *
//     * @param track
//     * @return
//     */
//    public long insertTrack(Track track) {
//        ContentValues cv = new ContentValues();
//        cv.put(COLUMN_TRACK_START_DATE, track.getStartDate().getTime());
//        return getWritableDatabase().insert(TABLE_TRACK, null, cv);
//    }
//
//    /**
//     * Inserts new PinPoint data - row that represents PinPoint object into database table.
//     *
//     * @param trackId id of current track
//     * @param point   instance of PinPoint
//     * @return new row id in database
//     */
//    public long insertPinPoint(long trackId, PinPoint point) {
//        ContentValues cv = new ContentValues();
//        cv.put(COLUMN_LOCATION_TIMESTAMP, point.getEventTime());
//        cv.put(COLUMN_LOCATION_LATITUDE, point.getLat());
//        cv.put(COLUMN_LOCATION_LONGITUDE, point.getLon());
//        cv.put(COLUMN_LOCATION_SIGNAL_STRENGTHS, point.getSignalStrengths());
//        cv.put(COLUMN_LOCATION_LAC, point.getLac());
//        cv.put(COLUMN_LOCATION_CI, point.getCi());
//        cv.put(COLUMN_LOCATION_NETWORK_TYPE, point.getNetworkType());
//        cv.put(COLUMN_LOCATION_OPERATOR_NAME, point.getOperator());
//        cv.put(COLUMN_LOCATION_TERMINAL, point.getTerminal());
//        cv.put(COLUMN_LOCATION_PROVIDER, "GPS");
//        cv.put(COLUMN_LOCATION_TRACK_ID, trackId);
//        cv.put(COLUMN_LOCATION_UPLOAD, "false");
//        return getWritableDatabase().insert(TABLE_LOCATION, null, cv);
//    }
//
//    /**
//     * Queries all records from table "track"
//     *
//     * @return
//     */
//    public TrackCursor queryAllTracks() {
//        Cursor wrapped = getReadableDatabase().query(TABLE_TRACK,
//                null, // all columns
//                null, // all rows
//                null, // no selection criteria
//                null, // group by
//                null, // having
//                COLUMN_TRACK_START_DATE + " asc"); // order by
//        return new TrackCursor(wrapped);
//    }
//
//    /**
//     * Queries single record with defined id from table "track"
//     *
//     * @param id
//     * @return
//     */
//    public TrackCursor queryTrack(long id) {
//        Cursor wrapped = getReadableDatabase().query(TABLE_TRACK,
//                null, // all columns
//                COLUMN_TRACK_ID + " = ?", // search by series ID
//                new String[]{String.valueOf(id)}, // with this value
//                null, // group by
//                null, // having
//                null, // order by
//                "1"); // first line
//        return new TrackCursor(wrapped);
//    }
//
//    /**
//     * Queries all PinPoints for given track
//     *
//     * @param trackId
//     * @return Cursor
//     */
//    public PinPointCursor queryPinPointsForTrack(long trackId) {
//        // TODO: check and correct query
//        Cursor wrapped = getReadableDatabase().query(TABLE_LOCATION,
//                null,
//                COLUMN_LOCATION_TRACK_ID + " = ?", // Отбор по заданной серии
//                new String[]{String.valueOf(trackId)},
//                null, // group by
//                null, // having
//                COLUMN_LOCATION_TIMESTAMP + " asc"); // Упорядочить
//        return new PinPointCursor(wrapped);
//    }
//
//    /**
//     * Queries PinPoints for given track which are within map visible area
//     *
//     * @param trackId
//     * @return Cursor
//     */
//    public PinPointCursor queryPinPointsForTrackMapVisibleArea(long trackId,
//                                                               double latSw, double lonSw,
//                                                               double latNe, double lonNe) {
//        // TODO: check and correct query
//        Cursor wrapped = getReadableDatabase().query(TABLE_LOCATION,
//                null,
//                COLUMN_LOCATION_TRACK_ID + " = ?"
//                        + " AND " + COLUMN_LOCATION_LATITUDE + " > ?"
//                        + " AND " + COLUMN_LOCATION_LONGITUDE + " > ?"
//                        + " AND " + COLUMN_LOCATION_LATITUDE + " < ?"
//                        + " AND " + COLUMN_LOCATION_LONGITUDE + " < ?",
//                new String[]{String.valueOf(trackId)
//                        , String.valueOf(latSw)
//                        , String.valueOf(lonSw)
//                        , String.valueOf(latNe)
//                        , String.valueOf(lonNe)},
//                null, // group by
//                null, // having
//                COLUMN_LOCATION_TIMESTAMP + " asc"); // Упорядочить
//        return new PinPointCursor(wrapped);
//    }
//
//    /**
//     * Queries first PinPoint for given track
//     *
//     * @return PinPointCursor
//     */
//    public PinPointCursor queryFirstPinPointForTrack(long trackId) {
//        Cursor wrapped = getReadableDatabase().query(TABLE_LOCATION,
//                new String[]{COLUMN_LOCATION_LATITUDE, COLUMN_LOCATION_LONGITUDE}, // All columns
//                COLUMN_LOCATION_TRACK_ID + " = ?", // Limit by defined series
//                new String[]{String.valueOf(trackId)},
//                null, // group by
//                null, // having
//                COLUMN_LOCATION_TIMESTAMP + " asc", // newest first
//                "1"); // limit 1
//        return new PinPointCursor(wrapped);
//    }
//
//    /**
//     * Queries first PinPoint which not uploaded to server yet
//     *
//     * @return PinPointCursor
//     */
//    public PinPointCursor queryFirstNotUploadedPinPoint() {
//        Cursor wrapped = getReadableDatabase().query(TABLE_LOCATION,
//                null, // All columns
//                COLUMN_LOCATION_UPLOAD + " = ?", // Limit by defined series
//                new String[]{"false"},
//                null, // group by
//                null, // having
//                COLUMN_LOCATION_TIMESTAMP + " asc", // newest first
//                "1"); // limit 1
//        return new PinPointCursor(wrapped);
//    }
//
//    /**
//     * Queries all PinPoints which not uploaded to server yet
//     *
//     * @return PinPointCursor
//     */
//    public PinPointCursor queryAllNotUploadedPinPoints() {
//        Cursor wrapped = getReadableDatabase().query(TABLE_LOCATION,
//                null, // All columns
//                COLUMN_LOCATION_UPLOAD + " = ?", // Limit by defined series
//                new String[]{"false"},
//                null, // group by
//                null, // having
//                COLUMN_LOCATION_TIMESTAMP + " asc"); // newest first
//        return new PinPointCursor(wrapped);
//    }
//
//    /**
//     * Updates field "upload" of table "location" with TRUE
//     */
//    public int updateUploadedPinPoint(long pinpointId) {
//        ContentValues cv = new ContentValues();
//        cv.put(COLUMN_LOCATION_UPLOAD, "true");
//        return getWritableDatabase().update(TABLE_LOCATION,
//                cv,
//                "_id = ?",
//                new String[]{String.valueOf(pinpointId)}
//        );
//    }
//
//    /**
//     * Deletes all records with specified track_id from table "location"
//     *
//     * @param trackId
//     * @return number of lines deleted
//     */
//    public int deletePinPointsForTrack(long trackId) {
//        return getWritableDatabase().delete(TABLE_LOCATION,
//                "track_id = ?",
//                new String[]{String.valueOf(trackId)});
//    }
//
//    /**
//     * Deletes record with specified id from table "track"
//     *
//     * @param id
//     * @return
//     */
//    public int deleteTrack(long id) {
//        return getWritableDatabase().delete(TABLE_TRACK,
//                "_id = ?",
//                new String[]{String.valueOf(id)});
//    }
//
//
//    /*
//    Сортировка выборки:
//    asc - по возрастанию (для даты - сначала новые )
//    desc - по убыванию (для даты - сначала старые )
//     */
//
//      public static class TrackCursor extends CursorWrapper {
//
//        /**
//         * Creates a cursor wrapper.
//         *
//         * @param cursor The underlying cursor to wrap.
//         */
//        public TrackCursor(Cursor cursor) {
//            super(cursor);
//        }
//
//        /**
//         * @return object Track which represents current line in DB table
//         */
//        public Track getTrack() {
//            if (isBeforeFirst() || isAfterLast())
//                return null;
//            Track track = new Track();
//            long runId = getLong(getColumnIndex(COLUMN_TRACK_ID));
//            track.setId(runId);
//            long startDate = getLong(getColumnIndex(COLUMN_TRACK_START_DATE));
//            track.setStartDate(new Date(startDate));
//            return track;
//        }
//    }
//
//
//
//
//    public static class PinPointCursor extends CursorWrapper {
//
//        public PinPointCursor(Cursor c) {
//            super(c);
//        }
//
//        /**
//         * @return object PinPoint which represents current line in DB table
//         */
//        public PinPoint getPinPoint() {
//
//            if (isBeforeFirst() || isAfterLast())
//                return new PinPoint(0, 0, 0, null, null, null, null, null);
//
//            int signalStrenghts = getInt(getColumnIndex(COLUMN_LOCATION_SIGNAL_STRENGTHS));
//            double lat = getDouble(getColumnIndex(COLUMN_LOCATION_LATITUDE));
//            double lon = getDouble(getColumnIndex(COLUMN_LOCATION_LONGITUDE));
//            String operator = getString(getColumnIndex(COLUMN_LOCATION_OPERATOR_NAME));
//            String networkType = simplifyNetworkType(getString(getColumnIndex(COLUMN_LOCATION_NETWORK_TYPE)));
//            String lac = getString(getColumnIndex(COLUMN_LOCATION_LAC));
//            String ci = getString(getColumnIndex(COLUMN_LOCATION_CI));
//            String terminal = getString(getColumnIndex(COLUMN_LOCATION_TERMINAL));
//
//            return new PinPoint(signalStrenghts, lat, lon, operator,
//                    networkType, lac, ci, terminal);
//        }
//
//
//        /**
//         * Converts network type integer constant to text value
//         */
//        private String simplifyNetworkType(String networkType) {
//            if (networkType.equals("2G EDGE") || networkType.equals("GSM")) {
//                return "GSM";
//            } else if (networkType.equals("2G GPRS") || networkType.equals("GSM")) {
//                return "GSM";
//            } else if (networkType.equals("3G HSDPA") || networkType.equals("UMTS")) {
//                return "UMTS";
//            } else if (networkType.equals("3G HSPA") || networkType.equals("UMTS")) {
//                return "UMTS";
//            } else if (networkType.equals("3G HSPA+") || networkType.equals("UMTS")) {
//                return "UMTS";
//            } else if (networkType.equals("3G HUSPA") || networkType.equals("UMTS")) {
//                return "UMTS";
//            } else if (networkType.equals("3G UMTS") || networkType.equals("UMTS")) {
//                return "UMTS";
//            } else if (networkType.equals("LTE")) {
//                return "LTE";
//            } else {
//                return "OTHER";
//            }
//        }
//    }


}
