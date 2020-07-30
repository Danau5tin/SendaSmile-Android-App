package com.firstapp.helpapp;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    public static final String CHANNEL_ID = "CHANNEL_ID";

    public static final String DELIVERY_NOT = "delivery";
    public static final int DELIVERY_NOT_TIME = 86400; // 1 day
    public static final String REMINDER_NOT = "reminder";
    public static final int REMINDER_NOT_TIME = 345600; // 4 days

    private NotificationManager mManager;
    Context context;

    public NotificationHelper(Context base) {
        super(base);
        this.context = base;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
    }
}
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(description);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String notification) {

        Intent intent = new Intent(this, IntroHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent,0);


        if (notification.equals(DELIVERY_NOT)) {
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(getString(R.string.del_not_title))
                .setContentText(getString(R.string.del_not_text))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.del_not_text)))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);}
        else {
            return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle(getString(R.string.rem_not_title))
                    .setContentText(getString(R.string.rem_not_text))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(getString(R.string.rem_not_text)))
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        }
    }
}
