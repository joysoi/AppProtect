package com.nikola.appprotect.receivers;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.nikola.appprotect.services.AppLockService;

public class AppWakefulBroadcastReceiver extends WakefulBroadcastReceiver {
    private static boolean isServiceStarted = false;
    private static final int REPEATING_ALARM_TIME = 10000;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isServiceStarted) {
            Intent serviceIntent = new Intent(context, AppLockService.class);
            context.startService(serviceIntent);
            isServiceStarted = true;
        }
        callServiceWithAlarm(context);
    }

    @SuppressLint("ShortAlarm")
    private void callServiceWithAlarm(Context context) {
        Intent intent = new Intent(context, AppLockService.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (REPEATING_ALARM_TIME), (REPEATING_ALARM_TIME), pendingIntent);
    }
}
