package com.cs442project.appmonitor;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by Snehal on 4/19/2015.
 */
@SuppressWarnings("ALL")
public class MyWidgetProvider extends AppWidgetProvider {

    public static String ACTION_WIDGET_LIMIT = "AppLimit";
    public static String ACTION_WIDGET_SELECTAPP = "MainScreen";
    public static String ACTION_WIDGET_DAILY = "Today";
    public static String ACTION_WIDGET_WEEKLY = "LastWeek";
    public static String ACTION_WIDGET_MONTHLY = "LastMonth";
    public static String ACTION_WIDGET_ALLUSAGE = "Sorting";
    Intent active;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {


        for (int i = 0; i < appWidgetIds.length; i++) {
            // Which layout to show on widget
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            active = new Intent(context, AppLimit.class);
            active.setAction(ACTION_WIDGET_LIMIT);
            PendingIntent actionPendingIntent = PendingIntent.getActivity(context, 0, active, 0);
            remoteViews.setOnClickPendingIntent(R.id.button3, actionPendingIntent);

            active = new Intent(context, MainScreen.class);
            active.setAction(ACTION_WIDGET_SELECTAPP);
            actionPendingIntent = PendingIntent.getActivity(context, 0, active, 0);
            remoteViews.setOnClickPendingIntent(R.id.button4, actionPendingIntent);

            active = new Intent(context, MainActivity.class);
            active.setAction(ACTION_WIDGET_DAILY);
            actionPendingIntent = PendingIntent.getActivity(context, 0, active, 0);
            remoteViews.setOnClickPendingIntent(R.id.button5, actionPendingIntent);



            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);

        }

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_WIDGET_LIMIT)) {
            Log.i("onReceive", ACTION_WIDGET_LIMIT);
        } else if (intent.getAction().equals(ACTION_WIDGET_SELECTAPP)) {
            Log.i("onReceive", ACTION_WIDGET_SELECTAPP);
        } else if (intent.getAction().equals(ACTION_WIDGET_DAILY)) {
            Log.i("onReceive", ACTION_WIDGET_DAILY);
        }else if (intent.getAction().equals(ACTION_WIDGET_WEEKLY)) {
            Log.i("onReceive", ACTION_WIDGET_WEEKLY);
        }else if (intent.getAction().equals(ACTION_WIDGET_MONTHLY)) {
            Log.i("onReceive", ACTION_WIDGET_MONTHLY);
        }else if (intent.getAction().equals(ACTION_WIDGET_ALLUSAGE)) {
            Log.i("onReceive", ACTION_WIDGET_ALLUSAGE);
        }else {
            super.onReceive(context, intent);
        }
    }
}
