package com.cs442project.appmonitor;

/**
 * Created by Snehal on 4/12/2015.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class NonSwipeableViewPager extends ViewPager {

    private boolean enabled;

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Disable Swiping to check the extended graph on each fragment by swiping the graph instead of page!!!
        this.enabled = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}