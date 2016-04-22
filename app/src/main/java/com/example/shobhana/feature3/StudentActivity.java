package com.example.shobhana.feature3;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StudentActivity extends AppCompatActivity {
    private static final String TAG = "StudentActivity";
    public static final String PREFS_NAME = "LoginPrefs";

    /*TODO Retrieve phone number programmatically (see StudentMapsActivity.java) and request permission for the same */
    /*If permission is granted proceed with registration and use it to direct user to maps screen on
    * subsequent logins*/

    @Bind(R.id.input_sname) EditText _sname;
    @Bind(R.id.input_smobile) EditText _smobile;
    @Bind(R.id.input_sid) EditText _sid;
    @Bind(R.id.input_age) EditText _age;
    @Bind(R.id.input_area) EditText _area;
   // @Bind(R.id.input_pincode) EditText _pincode;
    @Bind(R.id.btn_sreg) Button _sreg;
    String PayloadData="";
    String imei;
    boolean register=true;
    final int RequestImeiid = 0;
    final String [] PermissionsImei =
            {

                    Manifest.permission.READ_PHONE_STATE
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);


        ButterKnife.bind(this);

        _sreg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Enable below for OTP


                login();


                if(register==true)
                {
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("logged", "logged");
                    editor.putString("who", "student");
                    editor.commit();

                    Bundle extras = new Bundle();
                    extras.putString("who", "student");
                    Intent intent = new Intent(getApplicationContext(), DriverMapsActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }

            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            register=false;
            onLoginFailed();
            return;
        }

        register=true;
        _sreg.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(StudentActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String name = _sname.getText().toString();
        String mobile = _smobile.getText().toString();
        String usn = _sid.getText().toString();
        String age = _age.getText().toString();
        String area = _area.getText().toString();

        if ((int) Build.VERSION.SDK_INT < 23) {


            System.out.println("inside <23");
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            imei=telephonyManager.getDeviceId();
        }

        else{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED ) {

                System.out.println("inside granted");
                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                imei=telephonyManager.getDeviceId();

            }
            else{
                System.out.println("inside request");
                ActivityCompat.requestPermissions(this, PermissionsImei, RequestImeiid);

            }



        }



        PayloadData="phone="+mobile+"&phonenumber="+imei+"&name="+name+"&age="+age+"&area="+area+"&usertype="+"student";

        //Code to send data to server: calling thread here
        new TransferData().execute(PayloadData);



        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onLoginSuccess() {
        _sreg.setEnabled(true);
       // MainActivity.hasReg=true;
        setResult(RESULT_OK, null);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _sreg.setEnabled(true);
        register=false;
    }


    public boolean validate() {
        boolean valid = true;

        String name = _sname.getText().toString();
        String mobile = _smobile.getText().toString();
        String usn = _sid.getText().toString();
        String age = _age.getText().toString();
        String area = _area.getText().toString();

        if (name.isEmpty()) {
            _sname.setError("Enter name");
            valid = false;
        } else {
            _sname.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() < 10 || mobile.length() > 10) {
            _smobile.setError("Enter a valid number");
            valid = false;
        } else {
            _smobile.setError(null);
        }


        if (usn.isEmpty()) {
            _sid.setError("Enter ID");
            valid = false;
        } else {
            _sid.setError(null);
        }

        if (age.isEmpty()) {
            _age.setError("Enter age");
            valid = false;
        } else {
            _age.setError(null);
        }

        return valid;
    }

    class TransferData extends AsyncTask<String,String,String>
    {

        //This is the thread which handles data transfer:Required so that UI will not freeze

        //TODO:Remove unnecessary code lines

        private static final String TAG = " ";

        @Override
        protected String doInBackground(String... params)
        {
            String JsonResponse = null;
            String JsonData = params[0];

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try
            {
                URL url = new URL("http://seteambackend-saimadhav.rhcloud.com/userinfo/");

                //http://seteambackend-saimadhav.rhcloud.com/userinfo/9008442961
                connection = (HttpURLConnection) url.openConnection();

                connection.setDoOutput(true);
                // is output buffer writter
                connection.setRequestMethod("POST");
                //connection.setRequestProperty("Content-Type", "application/json");
                //connection.setRequestProperty("Accept", "application/json");

                Writer writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.write(PayloadData);
                    /* json data */
                writer.close();
                PayloadData="";
                //System.out.println("JSONDATA:  "+JsonDATA);
                //line added
                int errorCode = connection.getResponseCode(); System.out.println("GetErrorStream " + errorCode);
                InputStream inputStream = connection.getInputStream();
                //input stream
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0)
                {

                    return null;
                }
                JsonResponse = buffer.toString();
//response data
                Log.i(TAG, JsonResponse);

                //System.out.println("Done!!");
                System.out.println("response: "+JsonResponse);
                return JsonResponse;






            } catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (connection != null)
                {
                    connection.disconnect();
                }
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    } catch (final IOException e)
                    {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s)
        {

            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
            //Add the intent to the screen with route display here as well as the progress dialog

        }

    }

}
