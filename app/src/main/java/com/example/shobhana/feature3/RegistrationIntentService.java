package com.example.shobhana.feature3;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RegistrationIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.shobhana.feature3.action.FOO";
    private static final String ACTION_BAZ = "com.example.shobhana.feature3.action.BAZ";

    //Defined by user
   // public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
   // public static final String REGISTRATION_COMPLETE = "registrationComplete";
    private static final String[] TOPICS = {"global"};
    String mPhoneNumber="9844116260";

    private static final String TAG = "RegIntentService";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.shobhana.feature3.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.shobhana.feature3.extra.PARAM2";

    public RegistrationIntentService() {
        super(TAG);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    //System generated
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RegistrationIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    //System generated
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RegistrationIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {

            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.i(TAG, "GCM Registration Token: " + token);


            sendRegistrationToServer(token);


            sharedPreferences.edit().putBoolean(TokenStatus.SENT_TOKEN_TO_SERVER, true).apply();

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);

            sharedPreferences.edit().putBoolean(TokenStatus.SENT_TOKEN_TO_SERVER, false).apply();
        }

        Intent registrationComplete = new Intent(TokenStatus.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    //System generated
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    //System generated
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void sendRegistrationToServer(String token)
    {
        String register_token=null;

        register_token="token="+token+"&phonenumber="+mPhoneNumber;
        Log.i("phone number",mPhoneNumber);
        new SendRegistration().execute(register_token);

    }

    class SendRegistration extends AsyncTask<String,String,String> {

        //This is the thread which handles data transfer:Required so that UI will not freeze


        // private static final String TAG = " ";

        /** Code performing Http connection and POST request to server
         * is implemented here */
        @Override
        protected String doInBackground(String... params)
        {
            String JsonResponse = null;
            String PayloadData=params[0];
            System.out.println(PayloadData);
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try
            {
                URL url = new URL("http://seteambackend-saimadhav.rhcloud.com/register_token/");

                connection = (HttpURLConnection) url.openConnection();

                connection.setDoOutput(true);
                // is output buffer writter
                connection.setRequestMethod("POST");

                Writer writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.write(PayloadData);
                writer.close();
                PayloadData="";
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
                Log.i("Server response", JsonResponse);

               // System.out.println("response: "+JsonResponse);
                return JsonResponse;






            } catch (IOException e) {

                e.printStackTrace();
            }
            finally
            {
                if (connection != null) {

                    connection.disconnect();
                }
                if (reader != null) {
                    try {

                        reader.close();
                    } catch (final IOException e) {

                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
            /*TODO send JSON response to another activity where the data is extracted and displayed*/


        }

    }



}
