package com.cs442project.appmonitor.comparator;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.os.Build;

import java.util.Comparator;
import java.util.Map;

/**
 * @author Pritom Gogoi
 *         on 3/28/2015.
 */
public  class AppNameComparator implements Comparator<UsageStats> {
    private Map<String, String> mAppLabelList;

    public AppNameComparator(Map<String, String> appList) {
        mAppLabelList = appList;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public final int compare(UsageStats a, UsageStats b) {
        String alabel = mAppLabelList.get(a.getPackageName());
        String blabel = mAppLabelList.get(b.getPackageName());
        return alabel.compareTo(blabel);
    }
}