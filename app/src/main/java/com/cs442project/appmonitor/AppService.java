package com.cs442project.appmonitor;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;

import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442project.appmonitor.AppLoading.ProcessInfo;
import com.cs442project.appmonitor.comparator.AppNameComparator;
import com.cs442project.appmonitor.comparator.LastTimeUsedComparator;
import com.cs442project.appmonitor.comparator.UsageTimeComparator;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.sql.SQLOutput;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Snehal on 4/16/2015.
 */

public class AppService extends Service {

    ScheduledExecutorService notificationExecutor;
    private static final String TAG = "UsageStatsActivity";
    private static final boolean localLOGV = false;
    private UsageStatsManager mUsageStatsManager;
    private LayoutInflater mInflater;
    private UsageStatsAdapter mAdapter;
    private PackageManager mPm;
    private ProcessInfo processInfo;
    SharedPreferences sp;
    ArrayList<String> getSavedApps = new ArrayList<String>();
    public static final String PREFS_NAME = "MainScreen";
    LinkedHashSet<String> set;
    long duration;
    ArrayList<String> exceededApps = new ArrayList<String>();
    int NOTIFICATION_ID = 1;
    int limit;
    String hms;
    long localDuration;

    public AppService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //startCounterNotification();

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    class UsageStatsAdapter extends BaseAdapter {
        // Constants defining order for display order
        private static final int _DISPLAY_ORDER_USAGE_TIME = 0;
        private static final int _DISPLAY_ORDER_LAST_TIME_USED = 1;
        private static final int _DISPLAY_ORDER_APP_NAME = 2;

        private int mDisplayOrder = _DISPLAY_ORDER_USAGE_TIME;

        private LastTimeUsedComparator mLastTimeUsedComparator = new LastTimeUsedComparator();
        private UsageTimeComparator mUsageTimeComparator = new UsageTimeComparator();
        private AppNameComparator mAppLabelComparator;

        public final ArrayMap<String, String> mAppLabelMap = new ArrayMap<>();
        public final ArrayList<UsageStats> mPackageStats = new ArrayList<>();
        public List<UsageStats> stats;

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        UsageStatsAdapter() {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -1);

            //System.out.println("**Today****cal.getTime()*****" + cal.getTime() + "***getTimeMillis()***" + cal.getTimeInMillis());
            //System.out.println("**Today**System.currentTimeMillis()**** " + System.currentTimeMillis());

            //final List<UsageStats>
            stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                    cal.getTimeInMillis(), System.currentTimeMillis());

            System.out.println("---------------------" + stats);

            for (UsageStats usg : stats) {
              /*  System.out.println("***stats count "+stats.size());
                System.out.println("********************");
                System.out.println("" + usg.getPackageName());
//              System.out.println("First time: " + usg.getFirstTimeStamp());*/


                //System.out.println("Last time :" + usg.getLastTimeStamp());
                // System.out.println("Total Time: " + usg.getTotalTimeInForeground());
                //System.out.println(usg.getLastTimeUsed());


            }
            if (stats == null) {
                return;
            }

            ArrayMap<String, UsageStats> map = new ArrayMap<>();

            final int statCount = stats.size();
            for (int i = 0; i < statCount; i++) {
                final UsageStats pkgStats = stats.get(i);
                // load application labels for each application
                try {
                    ApplicationInfo appInfo = mPm.getApplicationInfo(pkgStats.getPackageName(), 0);
                    String label = appInfo.loadLabel(mPm).toString();
                    mAppLabelMap.put(pkgStats.getPackageName(), label);

                    UsageStats existingStats = map.get(pkgStats.getPackageName());

                    /*for(int j=0;j<getSavedApps.size();j++){
                        System.out.println("**********4");
                        System.out.println("*****getSavedApps.get(i)*****"+getSavedApps.get(i).toString());
                        System.out.println("*****pkgStats.getPackageName()*****"+pkgStats.getPackageName().toString());*/

                    if (getSavedApps.contains(pkgStats.getPackageName().toString())) {
                        //System.out.println("*** adding to map "+getSavedApps.get(j)+" & "+pkgStats.getPackageName());
                        if (existingStats == null) {
                            map.put(pkgStats.getPackageName(), pkgStats);
                        } else {
                            existingStats.add(pkgStats);
                        }
                    } else System.out.println("****no match found");

                } catch (PackageManager.NameNotFoundException e) {
                    // This package may be gone.
                }
            }
            mPackageStats.addAll(map.values());

            // Sort list
            mAppLabelComparator = new AppNameComparator(mAppLabelMap);
            sortList();
        }

        @Override
        public int getCount() {
            return mPackageStats.size();
        }

        @Override
        public Object getItem(int position) {
            return mPackageStats.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

//            AppViewHolder holder;
//            convertView = mInflater.inflate(R.layout.list_item_icon_duration, null);
//            holder = (AppViewHolder) convertView.getTag();
//
//            if (holder == null) {
//                holder = new AppViewHolder();
//                //holder.imgViAppIcon = (ImageView) convertView.findViewById(R.id.icon);
//                convertView.setTag(holder);
//            }
//
//            holder.pkgName = (TextView) convertView.findViewById(R.id.text);
//            holder.lastTimeUsed = (TextView) convertView.findViewById(R.id.text1);
//            holder.usageTime = (TextView) convertView.findViewById(R.id.duration);
//            holder.imgViAppIcon = (ImageView) convertView.findViewById(R.id.icon);
//
//            UsageStats pkgStats = mPackageStats.get(position);
//
//            if (pkgStats != null) {
//                for (int i = 0; i < getSavedApps.size(); i++) {
//                    if (getSavedApps.get(i).toString().equals(pkgStats.getPackageName().toString())) {
//
//                        System.out.println("*** equal package found " + getSavedApps.get(i) + " & " + pkgStats.getPackageName());
//                        String label = mAppLabelMap.get(pkgStats.getPackageName());
//                        try {
//                            Drawable icon = mPm.getApplicationIcon(pkgStats.getPackageName());
//                            holder.imgViAppIcon.setImageDrawable(icon);
//                        } catch (PackageManager.NameNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        holder.pkgName.setText(label);
//                        holder.lastTimeUsed.setText(DateUtils.formatSameDayTime(pkgStats.getLastTimeUsed(), System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM));
//                        holder.usageTime.setText(DateUtils.formatElapsedTime(pkgStats.getTotalTimeInForeground() / 1000));
//
//                        convertView.setTag(holder);
//                    }
//                }
//            } else {
//                Log.w(TAG, "No usage stats info for package:" + position);
//            }


            return convertView;
        }


        void sortList(int sortOrder) {
            if (mDisplayOrder == sortOrder) {
                // do nothing
                return;
            }
            mDisplayOrder = sortOrder;
            sortList();
        }

        private void sortList() {
            if (mDisplayOrder == _DISPLAY_ORDER_USAGE_TIME) {
                if (localLOGV) Log.i(TAG, "Sorting by usage time");
                Collections.sort(mPackageStats, mUsageTimeComparator);
            } else if (mDisplayOrder == _DISPLAY_ORDER_LAST_TIME_USED) {
                if (localLOGV) Log.i(TAG, "Sorting by last time used");
                Collections.sort(mPackageStats, mLastTimeUsedComparator);
            } else if (mDisplayOrder == _DISPLAY_ORDER_APP_NAME) {
                if (localLOGV) Log.i(TAG, "Sorting by application name");
                Collections.sort(mPackageStats, mAppLabelComparator);
            }
            notifyDataSetChanged();
        }


    }

    @Override
    public void onDestroy() {
        //super.onDestroy();
        //Toast.makeText(this, "Notification service stopped...", Toast.LENGTH_LONG).show();
        //stopService(new Intent(getBaseContext(), AppService.class));
        //notificationExecutor.shutdownNow();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onCreate();


        notificationExecutor = Executors.newSingleThreadScheduledExecutor();
        notificationExecutor.scheduleAtFixedRate(ntfctn, 0, 2, TimeUnit.HOURS);
        //Toast.makeText(this, "Notification service started...", Toast.LENGTH_LONG).show();



        return START_NOT_STICKY;
    }

    Runnable ntfctn = new Runnable() {
        public void run() {
            System.out.println("(*)");
            startCounterNotification();
        }
    };

    //Notification builder code
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Notification.Builder startCounterNotification() {

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);

        //Create an Intent for the BroadcastReceiver
        Intent buttonIntent = new Intent(getApplicationContext(), MainScreen.class);
        //Create the PendingIntent
        PendingIntent btPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, buttonIntent, 0);

        mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPm = getPackageManager();
        mAdapter = new UsageStatsAdapter();


        sp = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        getSavedApps.clear();
        int size = sp.getInt("Status_size", 0);
        for (int i = 0; i < size; i++) {
            getSavedApps.add(sp.getString("Status_" + i, null));
            System.out.println("#" + getSavedApps);
        }

        sp = this.getSharedPreferences("Limit", Context.MODE_PRIVATE);
        limit = sp.getInt("Status_size", 0);
        System.out.println("~ " + limit);


        for (int i = 0; i < mAdapter.mPackageStats.size(); i++) {

            duration = (int) mAdapter.mPackageStats.get(i).getTotalTimeInForeground();
            //appname = mAdapter.mAppLabelMap.get(mAdapter.mPackageStats.get(i).getPackageName());

            //System.out.println("***S/PACKAGE** " + mAdapter.mPackageStats.get(i).getPackageName());
            //System.out.println("***S/LABEL** " + appname);
            //System.out.println("***S/DUR*** " + duration);

            if (duration > (limit * 60000)) {
                exceededApps.add(mAdapter.mAppLabelMap.get(mAdapter.mPackageStats.get(i).getPackageName()));
            }
        }

        for (int j = 0; j < exceededApps.size(); j++) {
            set = new LinkedHashSet<String>(exceededApps);
            localDuration = (int) mAdapter.mPackageStats.get(j).getTotalTimeInForeground();
        }

        System.out.println("%%Exceeded Apps set " + set);

        //appname = mAdapter.mAppLabelMap.get(mAdapter.mPackageStats.get(j).getPackageName());


        hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(localDuration),
                TimeUnit.MILLISECONDS.toMinutes(localDuration) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(localDuration)),
                TimeUnit.MILLISECONDS.toSeconds(localDuration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(localDuration)));
        //System.out.println(+ " is used " + hms);


        if (set != null) {

            System.out.println("> IF BLOCK");
            Bitmap myIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.alert1);
            builder.setSmallIcon(R.drawable.ntfctn)
                    .setTicker("Notification")
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("Daily Limit Reached:")
                    .setContentText("" + set)
                    .setContentInfo("")
                    .setLargeIcon(myIconBitmap)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLights(25500, 1000, 2000)
                    .setColor(25500)
                    .setContentIntent(btPendingIntent)
                    .setAutoCancel(true);
            nm.notify(NOTIFICATION_ID, builder.build());
        } else {
            System.out.println("> ELSE BLOCK");
            startCounterNotification();
        }


        //nm.notify(NOTIFICATION_ID, builder.build());
        return builder;

    }

}


