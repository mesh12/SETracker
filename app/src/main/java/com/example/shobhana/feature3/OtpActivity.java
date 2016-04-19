package com.example.shobhana.feature3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Puneeth Shivaraju on 09-03-2016.
 */
public class OtpActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "LoginPrefs";

    Bundle b = new Bundle();
    /*Intent in = getIntent();
    Bundle extras = in.getExtras();
    String tmp = extras.getString("who");*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        findViewById(R.id.btn_next).setOnClickListener(handleClick);

        /*SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            if(tmp.compareTo("student")==0) {
                Intent intent = new Intent(OtpActivity.this, StudentMapsActivity.class);
                startActivity(intent);
            }
            else if(tmp.compareTo("driver")==0)
            {
                Intent intent = new Intent(OtpActivity.this, DriverMapsActivity.class);
                startActivity(intent);
            }
        }*/
    }

    private View.OnClickListener handleClick = new View.OnClickListener(){
        public void onClick(View arg0)
        {
            /*SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("logged", "logged");
            editor.commit();*/

            //Button btn = (Button)arg0;

            Intent in = getIntent();
            Bundle extras = in.getExtras();
            String tmp = extras.getString("who");

            if(tmp.compareTo("student")==0) {
                Intent intent = new Intent(OtpActivity.this, StudentMapsActivity.class);
                b.putString("who", "student");
                intent.putExtras(b);
                startActivity(intent);
            }

            else if(tmp.compareTo("driver")==0) {
                Intent intent = new Intent(OtpActivity.this, DriverMapsActivity.class);
                b.putString("who", "driver");
                intent.putExtras(b);
                startActivity(intent);
            }

        }
    };

    /*@Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/

}
