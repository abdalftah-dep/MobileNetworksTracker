package com.blogspot.droidcrib.mobilenetworkstracker.internet;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.blogspot.droidcrib.mobilenetworkstracker.application.MobileNetworksTrackerApp;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.DatabaseManager;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.TrackingManager;
import com.blogspot.droidcrib.mobilenetworkstracker.model.PinPoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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


    public static final String TAG = "mobilenetworkstracker";
    NotificationManager mNotificationManager;
    PinPoint mPinPoint;
    @Inject
    TrackingManager mTrackManager;
    @Inject
    DatabaseManager mDatabaseManager;
    @Inject
    GsonBuilder mGsonBuilder;
    @Inject
    Gson mGson;

    private static final int ID_NOTIFICATION_UPLOAD = 1;
    private static final String ACTION_POST_LOCATIONS = "com.blogspot.droidcrib.mobilenetworkstracker.action.POST_LOCATIONS";
    private static final String EXTRA_POST_URL = "com.blogspot.droidcrib.mobilenetworkstracker.extra.POST_URL";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionPostLocations(Context context, String urlPost) {
        Intent intent = new Intent(context, PostGetIntentService.class);
        intent.setAction(ACTION_POST_LOCATIONS);
        intent.putExtra(EXTRA_POST_URL, urlPost);
        context.startService(intent);
    }

    public PostGetIntentService() {
        super("PostGetIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((MobileNetworksTrackerApp) getApplication()).getBaseComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POST_LOCATIONS.equals(action)) {
                final String urlPost = intent.getStringExtra(EXTRA_POST_URL);
                handleActionPostLocations(urlPost);
            }
        }
    }

    /**
     * Handle action PostLocations in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPostLocations(String urlPost) {

        while((mPinPoint = mDatabaseManager.queryFirstNotUploadedPinPoint()) != null) {

            mGsonBuilder = new GsonBuilder();
            mGson = mGsonBuilder
                    .excludeFieldsWithoutExposeAnnotation()
                    .setPrettyPrinting().create();
            String jsonOutput = mGson.toJson(mPinPoint);

            Log.d(TAG, "Pinpoint json: " + jsonOutput);

            mDatabaseManager.updateUploadedPinPoint(mPinPoint.getId());

        }
    }

    /**
     * Sends notification of download progress
     */
    private void notificationProgress(int maximum, int progress) {
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_zoom)
                .setContentTitle("Data upload is in progress")
                .setProgress(maximum, progress, false)
                .build();
        mNotificationManager.notify(ID_NOTIFICATION_UPLOAD, notification);

    }


}
