package com.jbajic.todoo.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by jbajic on 08.07.17..
 */

public class DeadlineReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setAutoCancel(true)
                .setContentTitle("Deadline is expiring soon")
                .setContentText("Hurry up and finish all tasks")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(contentIntent)
                .setLights(Color.RED, 3000, 1000)
                .setVibrate(new long[]{1000, 1500, 2000, 2500})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        Log.e("RECEIVER", "received=");
        Notification notification = builder.build();
        notificationManager.notify(101, notification);
    }

}
