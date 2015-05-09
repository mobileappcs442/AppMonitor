package com.cs442project.appmonitor;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Snehal on 4/14/2015.
 */

@SuppressWarnings("ALL")
public class SettingPage extends PreferenceActivity {
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
//        Preference selectApps = (Preference) findPreference("select");
//        selectApps.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                Intent i = new Intent(SettingPage.this, MainScreen.class);
//                startActivity(i);
//                finish();
//                return false;
//            }
//        });

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


//        setContentView(R.layout.setting);
//        Button b1 = (Button)findViewById(R.id.selectDeselect);
//        Button b2 = (Button)findViewById(R.id.appLimit);
//        Button b3 = (Button)findViewById(R.id.about);
//
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent i = new Intent(SettingPage.this, MainScreen.class);
//                startActivity(i);
//                finish();
//            }
//        });
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(SettingPage.this, AppLimit.class));
//            }
//        });
//        b3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(SettingPage.this, About.class));
//            }
//        });

    }

    public void back(View v) {
        super.onBackPressed();

    }
}
