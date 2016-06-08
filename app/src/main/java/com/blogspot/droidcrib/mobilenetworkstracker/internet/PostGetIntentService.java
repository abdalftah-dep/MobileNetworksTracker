package com.blogspot.droidcrib.mobilenetworkstracker.internet;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.blogspot.droidcrib.mobilenetworkstracker.application.MobileNetworksTrackerApp;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.TrackManager;
import com.blogspot.droidcrib.mobilenetworkstracker.database.DatabaseHelper;
import com.blogspot.droidcrib.mobilenetworkstracker.model.PinPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * helper methods.
 */
public class PostGetIntentService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PostGetIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

//    public static final String TAG = "mobilenetworkstracker";
//    NotificationManager mNotificationManager;
//    HttpConnectionManager mHttpConnectionManager;
//    @Inject
//    TrackManager mTrackManager;
//
//    private static final int ID_NOTIFICATION_UPLOAD = 1;
//    private static final String ACTION_POST_LOCATIONS = "com.blogspot.droidcrib.mobilenetworkstracker.action.POST_LOCATIONS";
//    private static final String EXTRA_POST_URL = "com.blogspot.droidcrib.mobilenetworkstracker.extra.POST_URL";
//
//    /**
//     * Starts this service to perform action Foo with the given parameters. If
//     * the service is already performing a task this action will be queued.
//     *
//     * @see IntentService
//     */
//    // TODO: Customize helper method
//    public static void startActionPostLocations(Context context, String urlPost) {
//        Intent intent = new Intent(context, PostGetIntentService.class);
//        intent.setAction(ACTION_POST_LOCATIONS);
//        intent.putExtra(EXTRA_POST_URL, urlPost);
//        context.startService(intent);
//    }
//
//    public PostGetIntentService() {
//        super("PostGetIntentService");
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        ((MobileNetworksTrackerApp)getApplication()).getBaseComponent().inject(this);
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        if (intent != null) {
//            final String action = intent.getAction();
//            if (ACTION_POST_LOCATIONS.equals(action)) {
//                final String urlPost = intent.getStringExtra(EXTRA_POST_URL);
//                handleActionPostLocations(urlPost);
//            }
//        }
//    }
//
//    /**
//     * Handle action PostLocations in the provided background thread with the provided
//     * parameters.
//     */
//    private void handleActionPostLocations(String urlPost) {
//        mHttpConnectionManager = new HttpConnectionManager();
//
//        // TODO: get some READY responce from server before sending data
//
//        // Query records of all not uploaded points
//        DatabaseHelper.PinPointCursor cur = mTrackManager.queryAllNotUploadedPinPoints();
//
//        if (cur != null) {
//            int maxRecords = cur.getCount();
//            int progress = 0;
//            if (cur.moveToFirst()) {
//                do {
//                    // get PinPoint object from Cursor
//                    PinPoint point = cur.getPinPoint();
//                    // Convert PinPoint object to JSON
//                    JSONObject json = new JSONObject();
//                    try {
//                        json = point.toJson();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    // post json to server
//                    try {
//                        mHttpConnectionManager.postJsonObject(json, urlPost);
//                        // change record status in database -- cur.getInt(0) is equal to "_id"
//                        //TODO: activate this command after test pass
//                        // mTrackManager.updateUploadedPinPoint(cur.getInt(0));
//                        notificationProgress(maxRecords, progress++);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        // TODO: throw Toast from here
//                        Log.d(TAG, "No Internet connection.");
//                        break;
//                    }
//                } while (cur.moveToNext());
//            }
//        }
//    }
//
//    /**
//     * Sends notification of download progress
//     */
//    private void notificationProgress(int maximum, int progress) {
//        Notification notification = new NotificationCompat.Builder(this)
//                .setSmallIcon(android.R.drawable.ic_menu_zoom)
//                .setContentTitle("Data upload is in progress")
//                .setProgress(maximum, progress, false)
//                .build();
//        mNotificationManager.notify(ID_NOTIFICATION_UPLOAD, notification);
//
//    }


}
