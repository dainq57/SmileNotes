package com.example.dainq.smilenotes.ui.notifications;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.dainq.smilenotes.common.Constant;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class NotificationHelper {
    private static final int ALARM_HOUR = 7;
    private static final int ALARM_MINUTE = 30;

    private static AlarmManager alarmManagerRTC;
    private static PendingIntent alarmIntentRTC;

    public static void setRemindRTC(Context context, Date date, Intent data) {
        //get calendar instance to be able to select what time notification should be scheduled
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, ALARM_HOUR);
        calendar.set(Calendar.MINUTE, ALARM_MINUTE);

        Log.d("dainq57 time", calendar.toString());
        int id = data.getIntExtra(Constant.NOTIFICATION_ID, 0);
        alarmIntentRTC = PendingIntent.getBroadcast(context, id, data, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManagerRTC = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManagerRTC.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntentRTC);
    }

    public static void cancelRemindRTC(Context context, int id) {
        alarmManagerRTC = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarmManagerRTC != null) {
            alarmIntentRTC = PendingIntent.getBroadcast(context, id, null, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManagerRTC.cancel(alarmIntentRTC);
        }
    }

    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * Enable boot receiver to persist alarms set for notifications across device reboots
     */
    public static void enableBootReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, NotificationReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * Disable boot receiver when user cancels/opt-out from notifications
     */
    public static void disableBootReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, NotificationReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
