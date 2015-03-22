package com.cs442project.appmonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Snehal on 2/27/2015.
 */
public class LastMonth extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View last_month = inflater.inflate(R.layout.last_month, container, false);
        ((TextView) last_month.findViewById(R.id.textView)).setText("Same UI as 'TODAY' tab");
        return last_month;

    }
}