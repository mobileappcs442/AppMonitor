package com.cs442project.appmonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Snehal on 2/27/2015.
 */
public class Today extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View today = inflater.inflate(R.layout.today, container, false);
        //((TextView) today.findViewById(R.id.textView)).setText("");
        return today;

    }

}



