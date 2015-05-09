package com.cs442project.appmonitor;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Snehal on 4/19/2015.
 */
public class StopNotification extends BroadcastReceiver{


    public void onReceive(Context context, Intent intent) {

        System.out.println("[]");
        int notificationId = intent.getIntExtra("notificationId", 0);
        // if you want cancel notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);

    }


}
