package com.example.shobhana.feature3;

import android.os.AsyncTask;
import android.util.Log;
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

/**
 * Created by shobhana on 19/04/16.
 */
public class SendDetails extends AsyncTask<String,String,String> {

    private String TAG="SendDetails";
    @Override
    protected String doInBackground(String... params) {
        String JsonResponse = null;
        //String JsonData = params[0];
        String PayloadData=params[0];

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

        //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        //Rreturn route here

    }

}
