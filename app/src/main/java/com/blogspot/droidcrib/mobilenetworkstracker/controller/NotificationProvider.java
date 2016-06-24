package com.blogspot.droidcrib.mobilenetworkstracker.controller;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Created by abulanov on 24.06.2016.
 */
public class NotificationProvider {

    /**
     * Sends notification of download progress
     */
     public static Notification uploadProgress(Context context, int maximum, int progress) {
         return new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_menu_zoom)
                .setContentTitle("Points uploaded: " + progress + " of " + maximum)
                .setProgress(maximum, progress, false)
                .build();
//        mNotificationManager.notify(ID_NOTIFICATION_UPLOAD, notification);
//        startForeground(ID_NOTIFICATION_UPLOAD, notification);

    }

    /**
     * Sends notification of download progress
     */
    public static Notification uploadFinished(Context context) {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_menu_zoom)
                .setContentTitle("Points upload finished")
                .setContentText("No more points for upload")
                .build();
    }

}
