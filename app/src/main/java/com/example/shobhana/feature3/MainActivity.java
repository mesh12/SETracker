package com.example.shobhana.feature3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
   // public static boolean hasReg = false;
    public static final String PREFS_NAME = "LoginPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm);

        //checked if logged in already
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {

            if (settings.getString("who", "").toString().equals("student")) {
                Intent intent = new Intent(MainActivity.this, StudentMapsActivity.class);
                startActivity(intent);
            }
            else if (settings.getString("who", "").toString().equals("driver")) {
                Intent intent = new Intent(MainActivity.this, DriverMapsActivity.class);
                startActivity(intent);
            }
        }

        //otherwise go to the registration page
        else{
            Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
            startActivity(intent);
        }

        //}



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
