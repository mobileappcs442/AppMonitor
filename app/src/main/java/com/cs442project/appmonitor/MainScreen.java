package com.cs442project.appmonitor;

/**
 * Created by Snehal on 2/27/2015.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cs442project.appmonitor.AppLoading.ProcessInfo;
import com.cs442project.appmonitor.AppLoading.Programe;
import com.cs442project.appmonitor.comparator.AppNameComparator;
import com.cs442project.appmonitor.comparator.LastTimeUsedComparator;
import com.cs442project.appmonitor.comparator.UsageTimeComparator;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class MainScreen extends Activity {
    private static final String LOG_TAG = "AppMonitor-" + MainScreen.class.getSimpleName();

    private ProcessInfo processInfo;
    private ListView lstViProgramme;

    private List<Programe> programes;
    private ListAdapter mAdapter;
    private ArrayAdapter<Programe> mListAdapter;

    ArrayList<String> selectedApps = new ArrayList<String>();
    ArrayList<String> getSavedApps = new ArrayList<String>();
    HashSet<String> set;
    HashMap<Integer, String> posMap = new HashMap<>();
    ArrayList<String> result;
    SharedPreferences sp;
    SharedPreferences.Editor edit;


    public static final String PREFS_NAME = "MainScreen";
    int len, pos;
    private Programe prr;
    Button button_calc;


    View previouslySelectedItem = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if permission enabled
        if (UStats.getUsageStatsList(this).isEmpty()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

        sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        edit = sp.edit();

        setContentView(R.layout.main_screen);

        Window window = MainScreen.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(MainScreen.this.getResources().getColor(R.color.dark_blue));
        window.setTransitionBackgroundFadeDuration(10000);


        lstViProgramme = (ListView) findViewById(R.id.processList);
        processInfo = new ProcessInfo();
        mAdapter = new ListAdapter();
        lstViProgramme.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lstViProgramme.setAdapter(mAdapter);


        button_calc = (Button) findViewById(R.id.button);
        button_calc.setEnabled(false);



        len = lstViProgramme.getCount();
        System.out.println("--------------------->LENGTH OF LISTVIEW: " + len);

        lstViProgramme.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("ResourceAsColor")
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                pos = position;

                Viewholder holder = (Viewholder) view.getTag();

                //Second Tap.. REMOVE
                if (posMap.containsKey(pos)) {
                    posMap.remove(pos);
                    //posMap.put(pos, "false");
                    System.out.println("***MainScreen***pos key - " + pos + "***Value***" + posMap.get(pos));
                    prr = (Programe) programes.get(pos);
                    selectedApps.remove(prr.getPackageName());
                    System.out.println("***MainScreen***Removed***" + prr.getPackageName());
                    //view.setBackground(null);
                    holder.chckBX.setEnabled(false);
                    holder.chckBX.setChecked(false);

                }
                //First Tap.. ADD
                else {
                    posMap.put(pos, "true");
                    System.out.println("***MainScreen***pos key" + pos + "***Value***" + posMap.get(pos));
                    prr = (Programe) programes.get(pos);
                    selectedApps.add(prr.getPackageName());
                    System.out.println("***MainScreen***Added***" + prr.getPackageName());
                    //view.setBackgroundColor(R.color.light_blue);
                    holder.chckBX.setEnabled(true);
                    holder.chckBX.setChecked(true);
                }

                set = new HashSet<>(selectedApps);
                result = new ArrayList<>(set);

                for (int i = 0; i < result.size(); i++) {
                    //System.out.println("****Individual Selected Package: " + result.get(i));
                }
                System.out.println("****Current Array: " + result);
                if (result.isEmpty()) {
                    button_calc.setEnabled(false);
                } else {
                    button_calc.setEnabled(true);
                }
            }
        });

        button_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit.putInt("Status_size", result.size()); /* sKey is an array */
                for (int i = 0; i < result.size(); i++) {
                    edit.remove("Status_" + i);
                    edit.putString("Status_" + i, result.get(i));
                }
                edit.commit();

                Intent intent = new Intent(MainScreen.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private static class Viewholder {
        public TextView txtAppName;
        public ImageView imgViAppIcon;
        public Switch switchT;
        public CheckBox chckBX;
    }

    private class ListAdapter extends BaseAdapter {


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

            Programe pr = (Programe) programes.get(position);                   //***
            //System.out.println("--------------------->"+pr.getPackageName());   //***
            //System.out.println("--------------------->"+pr.getProcessName());   //***


            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.list_item_icon_text, parent, false);

            Viewholder holder = (Viewholder) convertView.getTag();

            if (holder == null) {
                holder = new Viewholder();
                convertView.setTag(holder);

                holder.imgViAppIcon = (ImageView) convertView.findViewById(R.id.icon);
                holder.txtAppName = (TextView) convertView.findViewById(R.id.text);

                //Checkbox block
                holder.chckBX = (CheckBox) convertView.findViewById(R.id.cbx);

                holder.chckBX.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        int getPosition = (Integer) compoundButton.getTag();
                        programes.get(getPosition).setSelected(compoundButton.isChecked());
                    }
                });


                convertView.setTag(holder);
                convertView.setTag(R.id.icon, holder.imgViAppIcon);
                convertView.setTag(R.id.text, holder.txtAppName);
                convertView.setTag(R.id.cbx, holder.chckBX);

            } else {
                holder = (Viewholder) convertView.getTag();
            }

            holder.chckBX.setTag(position);
            holder.imgViAppIcon.setImageDrawable(pr.getIcon());
            holder.txtAppName.setText(pr.getProcessName());

            holder.chckBX.setChecked(programes.get(position).isSelected());
            holder.chckBX.setEnabled(false);
            //holder.chckBX.setChecked(true);


            return convertView;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.setting:
                Intent i = new Intent(getBaseContext(), SettingPage.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void back(View v) {
        super.onBackPressed();

    }

}

