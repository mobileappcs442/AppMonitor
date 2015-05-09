package com.cs442project.appmonitor;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.cs442project.appmonitor.comparator.AppNameComparator;
import com.cs442project.appmonitor.comparator.LastTimeUsedComparator;
import com.cs442project.appmonitor.comparator.UsageTimeComparator;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;

import org.eazegraph.lib.charts.BarChart;

import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Snehal on 2/27/2015.
 */
public class LastMonth extends Fragment {
    private static final String TAG = "UsageStatsActivity";
    private static final boolean localLOGV = false;
    private UsageStatsManager mUsageStatsManager;
    private LayoutInflater mInflater;
    private UsageStatsAdapter mAdapter;
    private PackageManager mPm;
    BarChart mBarChart;
    SharedPreferences sp;
    ArrayList<String> getSavedApps = new ArrayList<String>();
    public static final String PREFS_NAME = "MainScreen";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View last_month = inflater.inflate(R.layout.last_month, container, false);
        //((TextView) last_month.findViewById(R.id.textView)).setText("Same UI as 'TODAY' tab");
        mUsageStatsManager = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPm = getActivity().getPackageManager();

        sp = this.getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        getSavedApps.clear();
        int size = sp.getInt("Status_size", 0);
        for (int i = 0; i < size; i++) {
            getSavedApps.add(sp.getString("Status_" + i, null));
        }
        for (int i = 0; i < getSavedApps.size(); i++) {
            System.out.println("******Saved Package LiSt: " + getSavedApps.get(i));
        }

        ListView listView = (ListView) last_month.findViewById(R.id.LastMonthList);
        mAdapter = new UsageStatsAdapter();
        listView.setAdapter(mAdapter);

        mBarChart = (BarChart) last_month.findViewById(R.id.barchart);


        for (int i = 0; i < mAdapter.mPackageStats.size(); i++) {
            Random rnd = new Random();
            //int color = Color.argb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            System.out.println("***pack name**" + mAdapter.mPackageStats.get(i).getPackageName());
            System.out.println("***label name**" + mAdapter.mAppLabelMap.get(mAdapter.mPackageStats.get(i).getPackageName()));

            float duration = (int) mAdapter.mPackageStats.get(i).getTotalTimeInForeground() / 1000;

            //mPieChart.addPieSlice(new PieModel(mAdapter.mAppLabelMap.get(mAdapter.mPackageStats.get(i).getPackageName()), duration, Color.rgb(255, rnd.nextInt(), rnd.nextInt())));
            mBarChart.addBar(new BarModel(mAdapter.mAppLabelMap.get(mAdapter.mPackageStats.get(i).getPackageName()), duration, Color.rgb(255, rnd.nextInt(), rnd.nextInt())));


            System.out.println("***color = " + Color.rgb(255, rnd.nextInt(), rnd.nextInt()));
            System.out.println("***duration***" + duration);
        }
        mBarChart.startAnimation();
        return last_month;

    }

    static class AppViewHolder {
        TextView pkgName;
        TextView usageTime;
        ImageView imgViAppIcon;
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
            cal.add(Calendar.DAY_OF_YEAR, -5);

            System.out.println("**Today****cal.getTime()*****" + cal.getTime() + "***getTimeMillis()***" + cal.getTimeInMillis());
            System.out.println("**Today**System.currentTimeMillis()**** " + System.currentTimeMillis());

            //final List<UsageStats>
            stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_MONTHLY,
                    cal.getTimeInMillis(), System.currentTimeMillis());

            System.out.println("---------------------" + stats);
            for (UsageStats usg : stats) {
//                System.out.println("***stats count "+stats.size());
//                System.out.println("********************");
//                System.out.println("" + usg.getPackageName());
//                System.out.println("First time: " + usg.getFirstTimeStamp());
//                System.out.println("Last time :" + usg.getLastTimeStamp());
//                System.out.println("Total Time: " + usg.getTotalTimeInForeground());
//                System.out.println(usg.getLastTimeUsed());
//                System.out.println("********************");
                //mPieChart.addPieSlice(new PieModel("", usg.getTotalTimeInForeground(), Color.parseColor(color)));

            }

            //mPieChart.startAnimation();

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
                    //}


                    /*if (existingStats == null) {
                        map.put(pkgStats.getPackageName(), pkgStats);
                    } else {
                        existingStats.add(pkgStats);
                    }*/

                } catch (PackageManager.NameNotFoundException e) {
                    // This package may be gone.
                }
            }
            mPackageStats.addAll(map.values());

            // Sort list
            //mAppLabelComparator = new AppNameComparator(mAppLabelMap);
            //sortList();
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

            AppViewHolder holder;
            convertView = mInflater.inflate(R.layout.list_item_icon_duration, null);
            holder = (AppViewHolder) convertView.getTag();

            if (holder == null) {
                holder = new AppViewHolder();
                //holder.imgViAppIcon = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            }

            holder.pkgName = (TextView) convertView.findViewById(R.id.text);
            holder.imgViAppIcon = (ImageView)convertView.findViewById(R.id.icon);
            holder.usageTime = (TextView) convertView.findViewById(R.id.duration);

            UsageStats pkgStats = mPackageStats.get(position);

            if (pkgStats != null) {
                for (int i = 0; i < getSavedApps.size(); i++) {
                    if (getSavedApps.get(i).toString().equals(pkgStats.getPackageName().toString())) {
                        System.out.println("*** equal package found " + getSavedApps.get(i) + " & " + pkgStats.getPackageName());
                        String label = mAppLabelMap.get(pkgStats.getPackageName());
                        holder.pkgName.setText(label);
                        try {
                            Drawable icon = mPm.getApplicationIcon(pkgStats.getPackageName());
                            holder.imgViAppIcon.setImageDrawable(icon);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        holder.usageTime.setText(DateUtils.formatElapsedTime(pkgStats.getTotalTimeInForeground() / 1000));
                        convertView.setTag(holder);
                    }
                }
            } else {
                Log.w(TAG, "No usage stats info for package:" + position);
            }
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

}