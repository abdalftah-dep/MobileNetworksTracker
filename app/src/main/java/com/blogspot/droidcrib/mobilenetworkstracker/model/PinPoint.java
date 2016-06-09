package com.blogspot.droidcrib.mobilenetworkstracker.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by Andrey on 11.01.2016.
 * POJO contains fields of data measurements
 */

@Table(name = "Pinpoints")
public class PinPoint extends Model{

    public static final String TAG = "mobilenetworkstracker";

    @Column(name = "signal_strengths")
    public double signalStrengths;
    @Column(name = "network_type")
    public String networkType;
    @Column(name = "lac")
    public String lac;
    @Column(name = "ci")
    public String ci;
    @Column(name = "terminal")
    public String terminal;
    @Column(name = "lat")
    public double lat;
    @Column(name = "lon")
    public double lon;
    @Column(name = "event_time")
    public long eventTime;
    @Column(name = "operator")
    public String operator;
    @Column(name = "country")
    public String country;
    @Column(name = "upload")
    public boolean upload;
    @Column(name = "track_id")
    public long trackId;


    public PinPoint() {
        super();
    }

    //    private static final String JSON_ID = "id";
//    private static final String JSON_SIGNAL_STRENGHTS = "signStr";
//    private static final String JSON_LAT = "latitude";
//    private static final String JSON_LON = "longitude";
//    private static final String JSON_OPERATOR_NAME = "operator";
//    private static final String JSON_NETWORK_TYPE = "networkType";
//    private static final String JSON_LAC = "lac";
//    private static final String JSON_CI = "ci";
//    private static final String JSON_TERMINAL = "terminal";
//    private static final String JSON_EVENT_TIME = "eventTime";
//    private static final String JSON_NETWORK_COUNTRY = "location";
//    private static final String JSON_UPLOAD_TIME = "uploadTime";

//    public double getSignalStrengths() {
//        return mSignalStrengths;
//    }
//
//    public String getNetworkType() {
//        return mNetworkType;
//    }
//
//    public String getLac() {
//        return mLac;
//    }
//
//    public String getCi() {
//        return mCi;
//    }
//
//    public String getTerminal() {
//        return mTerminal;
//    }
//
//    public double getLat() {
//        return mLat;
//    }
//
//    public double getLon() {
//        return mLon;
//    }
//
//    public long getEventTime() {
//        return mEventTime;
//    }
//
//    public String getOperator() {
//        return mOperator;
//    }
//
//    public String getCountry() {
//        return mCountry;
//    }
//
//
//    public PinPoint(int signalStrenghts, double lat, double lon, String operator,
//                    String networkType, String lac, String ci, String terminal) {
//        mSignalStrengths = signalStrenghts;
//        mLat = lat;
//        mLon = lon;
//        mOperator = operator;
//        mNetworkType = networkType;
//        mLac = lac;
//        mCi = ci;
//        mTerminal = terminal;
//        mEventTime = System.currentTimeMillis();
//    }
//
//    public PinPoint(int signalStrenghts, double lat, double lon, String operator,
//                    String networkType, String lac, String ci, String terminal, long eventTime) {
//        mSignalStrengths = signalStrenghts;
//        mLat = lat;
//        mLon = lon;
//        mOperator = operator;
//        mNetworkType = networkType;
//        mLac = lac;
//        mCi = ci;
//        mTerminal = terminal;
//        mEventTime = System.currentTimeMillis();
//    }
//
//    public PinPoint(JSONObject json) throws JSONException {
//
//        mSignalStrengths = json.optDouble(JSON_SIGNAL_STRENGHTS);
//        mNetworkType = json.getString(JSON_NETWORK_TYPE);
//        mLac = json.getString(JSON_LAC);
//        mCi = json.getString(JSON_CI);
//        mTerminal = json.getString(JSON_TERMINAL);
//        mLat = json.optDouble(JSON_LAT);
//        mLon = json.optDouble(JSON_LON);
//        mEventTime = json.getLong(JSON_EVENT_TIME);
//        mOperator = json.getString(JSON_OPERATOR_NAME);
//    }
//
//    public JSONObject toJson() throws JSONException {
//        long uploadTime = System.currentTimeMillis();
//        JSONObject json = new JSONObject();
//        json.put(JSON_ID, 0);
//        json.put(JSON_SIGNAL_STRENGHTS, mSignalStrengths);
//        json.put(JSON_LAT, mLat);
//        json.put(JSON_LON, mLon);
//        json.put(JSON_OPERATOR_NAME, mOperator);
//        json.put(JSON_NETWORK_TYPE, mNetworkType);
//        json.put(JSON_LAC, mLac);
//        json.put(JSON_CI, mCi);
//        json.put(JSON_TERMINAL, mTerminal);
//        json.put(JSON_EVENT_TIME, mEventTime);
//        json.put(JSON_UPLOAD_TIME, uploadTime);
//        json.put(JSON_NETWORK_COUNTRY, "UA");
//        return json;
//    }

}
