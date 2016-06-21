package com.blogspot.droidcrib.mobilenetworkstracker.internet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UploadDataService extends Service {

    public static final String TAG = "mobilenetworkstracker";

    public UploadDataService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);

        String ip = intent.getStringExtra("ipAddress");

        //String url = "http://httpbin.org/post";
        String url = "http://" + ip + ":8080/GeoMap/webapi/data/add";
        Log.d(TAG, "JSON URL: " + url);
        PostGetIntentService.startActionPostLocations(getApplication(), url);

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
