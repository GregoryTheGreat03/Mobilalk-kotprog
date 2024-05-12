package com.application.yogaandmeditation;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;


public class NotificationHandler {
    private static final String CHANNEL_ID = "meditation_and_yoga_notification_channel";
    private final int NOTIFICATION_ID = 0;

    private NotificationManager mManager;
    private Context mContext;


    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel c = new NotificationChannel
                (CHANNEL_ID, "Online Jóga és Meditáció", NotificationManager.IMPORTANCE_DEFAULT);

        c.enableLights(true);
        c.setLightColor(Color.GREEN);
        c.enableVibration(true);
        c.setDescription("Értesítés az Online Jóga és Meditáció alkalmazásból");
        mManager.createNotificationChannel(c);
    }

    public void send(String message) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Online Jóga és Meditáció")
                .setContentText(message)
                .setSmallIcon(R.drawable.infinity);

        mManager.notify(NOTIFICATION_ID, builder.build());
    }
}