package com.cs442project.appmonitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * Created by Snehal on 4/14/2015.
 */
public class AppLimit extends Activity{

    SharedPreferences sp;
    SharedPreferences.Editor edit;

    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.set_limit);


        sp = getSharedPreferences("Limit", Context.MODE_PRIVATE);
        edit = sp.edit();

        Button limit = (Button)findViewById(R.id.button2);
        et = (EditText)findViewById(R.id.editText);

//        String neww = et.getText().toString();
//        if (neww==null) {
//            limit.setEnabled(false);
//        }else{
//            limit.setEnabled(true);
//        }



        limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (  ( !et.getText().toString().equals(""))) {
                    int foo = Integer.parseInt(et.getText().toString());
                    edit.putInt("Status_size", foo); /* sKey is an array */
                    edit.commit();
                    Intent intent = new Intent(getBaseContext(), AppService.class);
                    startService(intent);
                    System.out.println("~ " + foo);
                    finish();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

//                Intent i2 = getBaseContext().getPackageManager()
//                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
//                i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i2);
                }
            }
        });

    }
    public void back(View v) {
        super.onBackPressed();

    }
}
