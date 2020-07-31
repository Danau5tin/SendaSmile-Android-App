package com.firstapp.helpapp.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.firstapp.helpapp.helper.NotificationHelper;

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
