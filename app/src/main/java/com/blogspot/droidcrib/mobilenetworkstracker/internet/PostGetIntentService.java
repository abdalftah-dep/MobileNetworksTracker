package com.blogspot.droidcrib.mobilenetworkstracker.internet;

import android.app.Application;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blogspot.droidcrib.mobilenetworkstracker.application.MobileNetworksTrackerApp;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.DatabaseManager;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.NotificationProvider;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.TrackingManager;
import com.blogspot.droidcrib.mobilenetworkstracker.model.PinPoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final int ID_NOTIFICATION_UPLOAD = 1;
    private int max = 0;

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
    @Inject
    OkHttpClient mOkHttpClient;
    @Inject
    Application mApplication;


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
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        max = mDatabaseManager.queryCountOfNotUploadedPinpoints();
        Log.d(TAG, "CountOfNotUploadedPinpoints = " + max);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

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

        // Generate JSON
        mGson = mGsonBuilder
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting().create();

        int i = 0;
        while ((mPinPoint = mDatabaseManager.queryFirstNotUploadedPinPoint()) != null) {

            String jsonOutput = mGson.toJson(mPinPoint);
            Log.d(TAG, "Pinpoint json: " + jsonOutput);
            mNotificationManager.notify(ID_NOTIFICATION_UPLOAD, NotificationProvider.uploadProgress(this, max, ++i));

            try {
                String postResp = postJson(urlPost, jsonOutput);
                Log.d(TAG, "POST responce: " + postResp);
            } catch (IOException e) {
                Log.d(TAG, "POST IOException: " + e.toString());
                break;
            } catch (IllegalArgumentException e) {
                Log.d(TAG, "POST IllegalArgumentException: " + e.toString());
                break;
            } catch (IllegalStateException e) {
                Log.d(TAG, "POST IllegalStateException: " + e.toString());
                break;
            }

        }

        mNotificationManager.notify(ID_NOTIFICATION_UPLOAD, NotificationProvider.uploadFinished(this));
    }


    private String postJson(String url, String json) throws IOException,
            IllegalArgumentException, IllegalStateException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = mOkHttpClient.newCall(request).execute();
        // Set pinpoint status as Uploaded
        mDatabaseManager.updateUploadedPinPoint(mPinPoint.getId());
        return response.body().string();
    }


    private void stopUploadingService() {
        Intent uploadDataService = new Intent(mApplication, UploadDataService.class);
        stopService(uploadDataService);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "PostGetIntentService.onDestroy()");
    }

    // TODO: Call uploadProgress from here with the samr ID as in UploadDataService
}
