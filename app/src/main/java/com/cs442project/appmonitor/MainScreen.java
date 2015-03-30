package com.cs442project.appmonitor;

/**
 * Created by Snehal on 2/27/2015.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.cs442project.appmonitor.AppLoading.ProcessInfo;
import com.cs442project.appmonitor.AppLoading.Programe;
import com.cs442project.appmonitor.comparator.AppNameComparator;
import com.cs442project.appmonitor.comparator.LastTimeUsedComparator;
import com.cs442project.appmonitor.comparator.UsageTimeComparator;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class MainScreen extends Activity {
    private static final String LOG_TAG = "AppMonitor-" + MainScreen.class.getSimpleName();

    private ProcessInfo processInfo;
    private ListView lstViProgramme, listView;
    private List<Programe> processList;

    //private static final int TIMEOUT = 20000;
    //private int pid, uid;
    //private static final String TAG = "UsageStatsActivity";
    //private static final boolean localLOGV = false;
    //private UsageStatsManager mUsageStatsManager;
    //private LayoutInflater mInflater;
    //private UsageStatsAdapter mAdapter;
    //private PackageManager mPm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if permission enabled
        if (UStats.getUsageStatsList(this).isEmpty()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

        setContentView(R.layout.main_screen);
        /*
        mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPm = getPackageManager();
        */

        Window window = MainScreen.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(MainScreen.this.getResources().getColor(R.color.dark_blue));
        window.setTransitionBackgroundFadeDuration(10000);


        lstViProgramme = (ListView) findViewById(R.id.processList);
        processInfo = new ProcessInfo();
        lstViProgramme.setAdapter(new ListAdapter());


        //BUTTON ON CLICK LISTENER WILL START THE MAIN ACTICITY
        Button button_calc = (Button) findViewById(R.id.button);
        button_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //android.widget.ListAdapter adapter = (android.widget.ListAdapter) lstViProgramme.getAdapter();
                startActivity(new Intent(MainScreen.this, MainActivity.class));
            }
        });

    }
    static class Viewholder {
        TextView txtAppName;
        ImageView imgViAppIcon;
        Switch switchT;
    }


    private class ListAdapter extends BaseAdapter {
        List<Programe> programes;
        Programe checkedProg;
        int lastCheckedPosition = -1;

        public ListAdapter() {
            programes = processInfo.getRunningProcess(getBaseContext());
        }

        @Override
        public int getCount() {
            return programes.size();
        }

        @Override
        public Object getItem(int position) {
            return programes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Programe pr = (Programe) programes.get(position);
            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.list_item_icon_text, parent, false);
            Viewholder holder = (Viewholder) convertView.getTag();
            if (holder == null) {
                holder = new Viewholder();
                convertView.setTag(holder);
                holder.imgViAppIcon = (ImageView) convertView.findViewById(R.id.icon);
                holder.txtAppName = (TextView) convertView.findViewById(R.id.text);
                holder.switchT = (Switch) convertView.findViewById(R.id.switch1);
                holder.switchT.setFocusable(false);
                //holder.chkbx.setChecked(false);
                holder.switchT.setOnCheckedChangeListener(checkedChangeListener);
            }
            holder.imgViAppIcon.setImageDrawable(pr.getIcon());
            holder.txtAppName.setText(pr.getProcessName());
            holder.switchT.setId(position);
            holder.switchT.setChecked(checkedProg != null && getItem(position) == checkedProg);
            return convertView;
        }

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    final int checkedPosition = buttonView.getId();

                    //DO SOMETHING HERE...

                    if (lastCheckedPosition != -1) {
                        Switch tempButton = (Switch) findViewById(lastCheckedPosition);
                        if ((tempButton != null) && (lastCheckedPosition != checkedPosition)) {
                            tempButton.setChecked(true);
                        }
                    }


                    checkedProg = programes.get(checkedPosition);
                    lastCheckedPosition = checkedPosition;
                }
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_configure_locale) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
