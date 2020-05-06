package com.firstapp.helpapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlertReciever extends BroadcastReceiver {

    String notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        notification = intent.getStringExtra("notification");
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(notification);
        notificationHelper.getManager().notify(1, nb.build());
    }
}
