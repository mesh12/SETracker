package com.example.shobhana.feature3;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    public static final String PREFS_NAME = "LoginPrefs";

    public MyGcmListenerService() {

            Log.i(TAG,"GCM listener");

    }


    @Override
    public void onMessageReceived(String from, Bundle data) {

        super.onMessageReceived(from, data);
        String message ;
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if((message=data.getString("message"))!=null){

            Log.i(TAG ,from);
            Log.i(TAG, message);
            sendNotification(message);
        }

        else if ((message=data.getString("location"))!=null){

            Log.i(TAG ,from);
            System.out.println("IN GCM LOCATION");
            Log.i(TAG, message);
            /*SharedPreferences settings = getSharedPreferences(TokenStatus.GCM_MESSAGE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("message", "gcm message");
            editor.putString("LatLng", message);
            editor.commit();

            Bundle extras = new Bundle();
            extras.putString("LatLng", message);
            Intent intent = new Intent(getApplicationContext(), StudentMapsActivity.class);
            intent.putExtras(extras);
            startActivity(intent);*/
            if (settings.getString("who", "").toString().equals("student")) {
                Intent intent = new Intent(getApplicationContext(), StudentMapsActivity.class);
                intent.putExtra("LatLng", message);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }



        }


    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, MapsMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());
    }


}
