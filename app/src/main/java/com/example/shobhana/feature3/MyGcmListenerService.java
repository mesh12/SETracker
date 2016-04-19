package com.example.shobhana.feature3;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    public MyGcmListenerService() {

            Log.i(TAG,"GCM listener");

    }


    @Override
    public void onMessageReceived(String from, Bundle data) {

        super.onMessageReceived(from, data);
        String message ;

        if((message=data.getString("message"))!=null){

            Log.i(TAG ,from);
            Log.i(TAG, message);
            sendNotification(message);
        }

        else if ((message=data.getString("location"))!=null){

            Log.i(TAG ,from);
            Log.i(TAG, message);
            Intent intent = new Intent(this, StudentMapsActivity.class);
            intent.putExtra("LatLng", message);
            startActivity(intent);

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
