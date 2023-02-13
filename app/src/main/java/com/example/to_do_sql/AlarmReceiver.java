package com.example.to_do_sql;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int ALARM_DURATION = 60000; // 1 minute in milliseconds
    @Override
    public void onReceive(Context context, Intent intent) {
      String todo=intent.getStringExtra("TODO");
      NotificationHelper.createNotification(context,todo);
      RingtoneHelper.startRingtone(context);
    }
}

