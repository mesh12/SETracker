package com.example.shobhana.feature3;

import android.*;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

public class MapsMainActivity extends AppCompatActivity {

    private static final String TAG = "MapsMainActivity";
    final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    EditText text;
    String textMsg;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    String imei;
    final int RequestImeiid = 0;
    final String [] PermissionsImei =
            {

                    Manifest.permission.READ_PHONE_STATE
            };


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm);

        findViewById(R.id.btn_submit).setOnClickListener(handleClick);

        text = (EditText) findViewById(R.id.text);



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(TokenStatus.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {

                    Toast.makeText(getBaseContext(), "token obtained", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(getBaseContext(), "Error...", Toast.LENGTH_LONG).show();
                }
            }


        };

        //mInformationTextView = (TextView) findViewById(R.id.informationTextView);

        registerReceiver();


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        if (checkPlayServices()) {
            System.out.println("services are enabled");
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
            //sendMessage();
        } else {
            //go to message interface
            Log.i("Token", "Token already obtained");
            //sendMessage();


        }


    }

    private View.OnClickListener handleClick = new View.OnClickListener() {
        public void onClick(View arg0) {
            Button btn = (Button) arg0;
            textMsg = MapsMainActivity.this.text.getText().toString();
            text.setText("");
            sendMessage();


        }
    };

    /*private View.OnClickListener handleClick1 = new View.OnClickListener() {
        public void onClick(View arg0) {
            Button btn = (Button) arg0;

            String coordinates;

            Gps gpsObject=new Gps(getApplicationContext());
            gpsObject.getLocation();
            coordinates=gpsObject.getCoordinates();
            sendLatLng(coordinates);

        }
    };*/


    /*private void sendLatLng(String coordinates){

        String message="location="+coordinates+"&phonenumber="+mPhoneNumber;

        System.out.println(coordinates);//null value
        StudentSendCoordinates object=new StudentSendCoordinates(message);
        object.init();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(TokenStatus.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }



    private void sendMessage() {

        //String s="Hello from gcm";
        String message=null;

        if ((int) Build.VERSION.SDK_INT < 23) {


            System.out.println("inside <23");
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            imei=telephonyManager.getDeviceId();
        }

        else{
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED ) {

                System.out.println("inside granted");
                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                imei=telephonyManager.getDeviceId();

            }
            else{
                System.out.println("inside request");
                ActivityCompat.requestPermissions(this, PermissionsImei, RequestImeiid);

            }



        }

        message="message="+textMsg+"&phonenumber="+imei;
        SendMessage object=new SendMessage(message);
        object.init();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    //system generated
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



    public boolean checkPlayServices() {
        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
        int result = googleApi.isGooglePlayServicesAvailable(this);
        System.out.println(result);
        if (result != ConnectionResult.SUCCESS) {
            if (googleApi.isUserResolvableError(result)) {
                googleApi.getErrorDialog(this, result, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }

            return false;
        }

        else
        {
            System.out.println("enabled");
            return true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.shobhana.feature3/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    //System generated
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.shobhana.feature3/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}
