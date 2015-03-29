package com.cs442project.appmonitor.comparator;

import android.app.usage.UsageStats;

import java.util.Comparator;

/**
 * @author Pritom Gogoi
 *         on 3/28/2015.
 */

public class UsageTimeComparator implements Comparator<UsageStats> {
    @Override
    public final int compare(UsageStats a, UsageStats b) {
        return (int)(b.getTotalTimeInForeground() - a.getTotalTimeInForeground());
    }
}
