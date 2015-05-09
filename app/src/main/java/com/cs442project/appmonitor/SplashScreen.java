package com.cs442project.appmonitor;

import android.app.Activity;

/**
 * Created by Snehal on 3/26/2015.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;
    public static final String PREFS_NAME = "MainScreen";
    SharedPreferences sp;
    ArrayList<String> getSavedApps = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //ProgressDialog.show(getApplicationContext(), "WAIT", "LOADING");

        sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        getSavedApps.clear();
        final int size = sp.getInt("Status_size", 0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                System.out.println("Shared prefe: "+sp);
                System.out.println("SP size: "+size);
                for (int i = 0; i < size; i++) {
                    getSavedApps.add(sp.getString("Status_" + i, null));
                }
                for (int i = 0; i < getSavedApps.size(); i++) {
                    System.out.println("******Saved Package LiSt: " + getSavedApps.get(i));
                }
                if(getSavedApps.isEmpty()){
                    startActivity(new Intent(SplashScreen.this, MainScreen.class));
                    finish();
                }
                else {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);




    }

}
