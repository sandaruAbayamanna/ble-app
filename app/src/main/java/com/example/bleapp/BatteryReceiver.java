package com.example.bleapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.BatteryManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class BatteryReceiver extends BroadcastReceiver {

    private static final int BATTERY_THRESHOLD = 86;

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int batteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPercentage = (batteryLevel / (float) batteryScale) * 100;

        if (batteryPercentage <= BATTERY_THRESHOLD) {
            // Create and show the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                    .setSmallIcon(R.drawable.icon_launch_foreground)
                    .setContentTitle("Low battery")
                    .setContentText("Your battery level is " + batteryPercentage + "%.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            notificationManager.notify(1, builder.build());

        }
    }
}
