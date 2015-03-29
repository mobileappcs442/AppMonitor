package com.cs442project.appmonitor.frontend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs442project.appmonitor.R;

/**
 * Created by Snehal on 2/27/2015.
 */
public class Yesterday extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View yesterday = inflater.inflate(R.layout.yesterday, container, false);
        ((TextView) yesterday.findViewById(R.id.textView)).setText("Same UI as 'TODAY' tab");
        return yesterday;

    }
}
