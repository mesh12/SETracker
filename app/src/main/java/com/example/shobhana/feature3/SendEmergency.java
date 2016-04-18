package com.example.shobhana.feature3;

import android.os.AsyncTask;
import android.util.Log;

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
 * Created by shobhana on 16/04/16.
 */
public class SendEmergency extends AsyncTask<String,String,String> {

    private String TAG="SendEmergency";

    @Override
    protected String doInBackground(String... params) {
        String JsonResponse = null;
        String PayloadData=params[0];

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try
        {
            URL url = new URL("http://seteambackend-saimadhav.rhcloud.com/emergency/");

            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            // is output buffer writer
            connection.setRequestMethod("POST");

            Writer writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.write(PayloadData);
            writer.close();
            PayloadData="";
            int errorCode = connection.getResponseCode();
            System.out.println("GetErrorStream " + errorCode);
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
}
