package com.blogspot.droidcrib.mobilenetworkstracker.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.location.Location;

import com.blogspot.droidcrib.mobilenetworkstracker.R;
import com.blogspot.droidcrib.mobilenetworkstracker.application.MobileNetworksTrackerApp;
import com.blogspot.droidcrib.mobilenetworkstracker.filesystem.PointsToJSONSerializer;
import com.blogspot.droidcrib.mobilenetworkstracker.model.PinPoint;
import com.blogspot.droidcrib.mobilenetworkstracker.telephony.TelephonyInfo;
import com.blogspot.droidcrib.mobilenetworkstracker.ui.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Andrey on 23.01.2016.
 */
public class LocationReceiver extends BroadcastReceiver {

    private static final String TAG = "mobilenetworkstracker";

    public static final String ACTION_STOP_NOTIFICATION = "com.blogspot.droidcrib.mobilenetworkstracker.action.STOP_NOTIFICATION";
    public static final String ACTION_START_NOTIFICATION = "com.blogspot.droidcrib.mobilenetworkstracker.action.START_NOTIFICATION";
    private static final int ID_NOTIFICATION_1 = 555;

    private int mSignalStrenghts;
    private String mLac;
    private String mCi;
    private String mTerminal;
    private double mLat;
    private double mLon;
    private String mEventTime;
    private String mOperatorName;
    private Context mContext;
    private NotificationManager mNotificationManager;
    private PointsToJSONSerializer mSerializer;
    private SimpleDateFormat mSdf;
    private TelephonyInfo mTelephonyInfo;
    private TrackManager mTrackManager;


    @Override
    public void onReceive(Context context, Intent intent) {

        mTelephonyInfo = MobileNetworksTrackerApp.getBaseComponent().getTelephonyInfo();
        mTrackManager = MobileNetworksTrackerApp.getBaseComponent().getTrackManager();

        mContext = context;

        // Initialize notification manager
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String action = intent.getAction();

        // Stop notifications by command
        if (action.equals(ACTION_STOP_NOTIFICATION)) {
            mNotificationManager.cancel(ID_NOTIFICATION_1);
            return;
        } else if (action.equals(ACTION_START_NOTIFICATION)) {
            startNotification();
            return;
        }

        // use PinPoint extra if available
        android.location.Location loc = (Location) intent
                .getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if (loc != null) {

            // Get coordinates
            mLat = loc.getLatitude();
            mLon = loc.getLongitude();

            // Get event time
            mSdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.US);
            mEventTime = mSdf.format(new Date());

            // Get terminal info
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            mTerminal = manufacturer + ";" + model;

            // Enabling WakeLock mode
            PowerManager.WakeLock screenLock = ((PowerManager) context
                    .getSystemService(Context.POWER_SERVICE))
                    .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            screenLock.acquire();

            // Get telephony data
            mOperatorName = mTelephonyInfo.getNetworkOperator();
            mLac = mTelephonyInfo.getLac();
            mCi = mTelephonyInfo.getCi();
            String networkTypeForJSON = mTelephonyInfo.getNetworkTypeForJSON();
            mSignalStrenghts = mTelephonyInfo.getSignalStrengths();

            // Release WakeLock mode
            screenLock.release();

            // Used for UI update
            onLocationReceived(context, loc, mSignalStrenghts);

            //TODO: change code below with mTrackManager.insertPinPoint();
            // Create new PinPoint object and save it to database
            PinPoint pinPoint = new PinPoint();
            pinPoint.signalStrengths = mSignalStrenghts;
            pinPoint.networkType = networkTypeForJSON;
            pinPoint.lac = mLac;
            pinPoint.ci = mCi;
            pinPoint.terminal = mTerminal;
            pinPoint.lat = mLat;
            pinPoint.lon = mLon;
            pinPoint.eventTime = System.currentTimeMillis();
            pinPoint.operator = mOperatorName;
            pinPoint.country = "UA";
            pinPoint.upload = false;
            pinPoint.trackId = 2;
            pinPoint.save();









//            PinPoint pinPoint = new PinPoint(mSignalStrenghts, mLat, mLon, mOperatorName, networkTypeForJSON,
//                    mLac, mCi, mTerminal);

            // Add PinPoint record to database
            //mTrackManager.insertPinPoint(pinPoint);

            if (mTrackManager.isTrackingEnabled()) {
                startNotification();
            }

            return;
        }

        // Something other happened if we are here
        if (intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)) {
            boolean enabled = intent
                    .getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);
            onProviderEnabledChanged(enabled);
        }


    }

    protected void onLocationReceived(Context context, Location loc, int signalStrengths) {
    }

    protected void onProviderEnabledChanged(boolean enabled) {
        Log.d(TAG, "Provider " + (enabled ? "enabled" : "disabled"));
    }

    /**
     * Sends notification of current tracking status
     */
    public void startNotification() {
        mSdf = new SimpleDateFormat("dd.MM HH:mm:ss", Locale.US);
        mEventTime = mSdf.format(new Date());

        String ticker = mContext.getResources().getString(R.string.notif_ticker);
        String title = mContext.getResources().getString(R.string.notif_title);
        String text = mContext.getResources().getString(R.string.notif_text);

        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(mContext)
                .setTicker(ticker)
                .setSmallIcon(android.R.drawable.ic_dialog_map)
                .setContentTitle(title)
                .setContentText(text + " " + mEventTime)
                .setContentIntent(pi)
                .setAutoCancel(false)
                .setOngoing(true)
                .build();
        mNotificationManager.notify(ID_NOTIFICATION_1, notification);
    }
}
