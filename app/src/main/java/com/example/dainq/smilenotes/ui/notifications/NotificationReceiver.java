package com.example.dainq.smilenotes.ui.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.NotificationCompat;

import com.example.dainq.smilenotes.ui.MainActivity;
import com.example.dainq.smilenotes.common.Constant;

import nq.dai.smilenotes.R;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // user clicks on notification
        //set flag to restart/relaunch the app
        Intent intentToRepeat = new Intent(context, MainActivity.class);
        intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int id = intent.getIntExtra(Constant.NOTIFICATION_ID, 0);

        //Pending intent to handle launch of Activity in intent above
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT);

        //Build notification
        Notification notification = buildLocalNotification(context, pendingIntent).build();
        notification.defaults = Notification.DEFAULT_ALL;
        //Send local notification
        NotificationHelper.getNotificationManager(context).notify(id, notification);
    }

    public NotificationCompat.Builder buildLocalNotification(Context context, PendingIntent pendingIntent) {
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(R.color.colorAccent)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_profile_image))
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);

        builder.setContentTitle("Thông báo")
                .setContentText("Bạn có thông báo mới, kiểm tra ngay?");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setStyle(bigText);
        }
        return builder;
    }
}

