package com.blogspot.droidcrib.mobilenetworkstracker.internet;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import com.blogspot.droidcrib.mobilenetworkstracker.application.MobileNetworksTrackerApp;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.DatabaseManager;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.NotificationProvider;
import com.blogspot.droidcrib.mobilenetworkstracker.eventbus.UploadFinished;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

// TODO: show notification progress from here

public class UploadDataService extends Service {

    public static final String TAG = "mobilenetworkstracker";
    private static final int ID_NOTIFICATION_UPLOAD = 1;
    NotificationManager mNotificationManager;
    @Inject
    DatabaseManager databaseManager;

    public UploadDataService() {
        Log.d(TAG, "UploadDataService()");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        ((MobileNetworksTrackerApp) getApplication()).getBaseComponent().inject(this);
        EventBus.getDefault().register(this);
        Log.d(TAG, "UploadDataService.onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "UploadDataService.onStartCommand()");

        int max = databaseManager.queryCountOfNotUploadedPinpoints();
        Log.d(TAG, "CountOfNotUploadedPinpoints = " + max);

        startForeground(ID_NOTIFICATION_UPLOAD, NotificationProvider.uploadProgress(this, max, 0));




        String ip = intent.getStringExtra("ipAddress");

        //String url = "http://httpbin.org/post";
        String url = "http://139.59.165.59" + ip + ":8080/GeoMap/webapi/data/add";
        Log.d(TAG, "JSON URL: " + url);
        if(isConnected()) {
            PostGetIntentService.startActionPostLocations(getApplication(), url);
        } else {
            Log.d(TAG, "No internet connection");
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Log.d(TAG, "UploadDataService.onDestroy()");
    }


    /**
     *  Checks Internet connection availability
     */
    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Subscribe
    public void onEvent(UploadFinished uploadFinishedEvent){
        Log.d(TAG, "Message received from IntentService: "  + uploadFinishedEvent.message);
        stopForeground(true);
        mNotificationManager.notify(ID_NOTIFICATION_UPLOAD, NotificationProvider.uploadFinished(this));
    }
}
