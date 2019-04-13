package com.share_will.mobile.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by ChenGD on 2018/3/4.
 *
 * @author chenguandu
 */

public class AlarmReceiver extends BroadcastReceiver {
    public final static String ALARM_ACTION_BATTERY = "action_share_will_battery";
    public final static String ALARM_ACTION_LOCATION = "action_share_will_location";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case ALARM_ACTION_BATTERY:
                Log.d("cgd", "ALARM_ACTION_BATTERY");
                context.startService(new Intent(context, BatteryService.class));
                break;
            case ALARM_ACTION_LOCATION:
                Log.d("cgd", "ALARM_ACTION_LOCATION");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    Intent i = new Intent(ALARM_ACTION_LOCATION);
                    PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
                    long intervalMillis  = 1 * 30 * 1000;//30秒钟后
                    mAlarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + intervalMillis, pi);
                }
                context.startService(new Intent(context, LocationService.class));
                break;
            default:
                break;
        }

    }
}
