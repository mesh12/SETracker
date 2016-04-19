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

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        findViewById(R.id.btn_next).setOnClickListener(handleClick);

        
    }

    private View.OnClickListener handleClick = new View.OnClickListener(){
        public void onClick(View arg0)
        {
            
            Bundle b = new Bundle();
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

    
}
