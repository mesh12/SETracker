package com.example.shobhana.feature3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class StudentMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double latitude, longitude, latitude1, longitude1;
    String route = "pescollege,corporationbangalore,MGRoadbangalore,indiranagar";
    int count = 0;
    String s1 = null;
    String s2 = null;
    String emsg;
    EditText et;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Send a message ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Intent intent = new Intent(getApplicationContext(), MapsMainActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Give Feedback", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(getApplicationContext(), Feedback.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Emergency", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StudentMapsActivity.this);

                LinearLayout layout = new LinearLayout(StudentMapsActivity.this);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(parms);

                layout.setGravity(Gravity.CLIP_VERTICAL);
                layout.setPadding(2, 2, 2, 2);

                et = new EditText(StudentMapsActivity.this);
                et.setHint("Enter message");


                LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tv1Params.bottomMargin = 5;
                layout.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                alertDialogBuilder.setView(layout);
                alertDialogBuilder.setTitle("Emergency");
                // alertDialogBuilder.setMessage("Input Student ID");
                alertDialogBuilder.setCancelable(false);



                // Setting Negative "Cancel" Button
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

                alertDialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        emsg = et.getText().toString();
                        System.out.println(emsg);

                        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                        String imei=telephonyManager.getDeviceId();
                        String message="message=hello"+"&imei="+imei+"&usertype=student";

                        System.out.println("emergency message="+emsg);
                        SendEmergency eobj=new SendEmergency();
                        eobj.execute(message);

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                try {
                    alertDialog.show();
                } catch (Exception e) {
                    // WindowManager$BadTokenException will be caught and the app would
                    // not display the 'Force Close' message
                    e.printStackTrace();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double currentLat, currentLng;

        LatLng loc = new LatLng(12.9355, 77.5341);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

       /* Bundle bundle;

        if ((bundle = getIntent().getExtras()) != null) {

            String currentLocation = bundle.getString("LatLng");

            String[] arrayLoc = currentLocation.split(",");

            currentLat = Double.parseDouble(arrayLoc[0]);
            currentLng = Double.parseDouble(arrayLoc[1]);

            LatLng currentMarkerPosition = new LatLng(currentLat, currentLng);

            mMap.addMarker(new MarkerOptions().position(currentMarkerPosition).title("current location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentMarkerPosition));
        } else {
            System.out.println("null");
        }*/


        init();
    }

    public void init() {

        String[] routearr = route.split(",");

        for (int i = 1; i < routearr.length; i++)
            new GetRoute().execute(routearr[i - 1], routearr[i]);

        for (String place : routearr) {
            new GetLocation().execute(place);

        }
    }

    public void setup(String place) {


        LatLng loc = new LatLng(latitude1, longitude1);

        mMap.addMarker(new MarkerOptions().position(loc).title(place));

        if (count == 0)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

        count++;

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
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
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
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

    class GetRoute extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            String JsonResponse = null;
            String response = " ";
            s1 = params[0];
            s2 = params[1];
            System.out.println(params[0] + " " + params[1]);
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + s1 + "&destination=" + s2 + "&sensor=false");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.setDoOutput(true);
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    System.out.println(responseCode);
                    InputStream in = connection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (in == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(in));

                    String inputLine;
                    while ((inputLine = reader.readLine()) != null)
                        buffer.append(inputLine + "\n");
                    if (buffer.length() == 0) {

                        return null;
                    }
                    Log.i("Json", String.valueOf(buffer));
                    JSONObject data = new JSONObject(String.valueOf(buffer));
                    System.out.println("json" + data);
                    //response = buffer.toString();
                    return data;

                }


            } catch (IOException e) {

                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        /**
         * This method is called after doInBackground method completes processing.
         * Result from doInBackground is passed to this method.
         */
        @Override
        protected void onPostExecute(JSONObject response) {


            try {
                ArrayList<LatLng> array = getDirection(response);
                draw(array);
                array.clear();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    public ArrayList<LatLng> getDirection(JSONObject data) throws JSONException {

        ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
        System.out.println("json" + data);
        JSONArray legs = null;
        JSONArray steps = null;
        JSONArray routes = data.getJSONArray("routes");
        for (int i = 0; i < routes.length(); i++) {

            legs = ((JSONObject) routes.get(i)).getJSONArray("legs");

            for (int j = 0; j < legs.length(); j++) {
                steps = ((JSONObject) legs.get(j)).getJSONArray("steps");

                //JSONArray start=((JSONObject)steps(j).getJSONArray("start_location");

                for (int k = 0; k < steps.length(); k++) {
                    String polyline = "";
                    polyline = (String) ((JSONObject) ((JSONObject) steps
                            .get(k)).get("polyline")).get("points");
                    ArrayList<LatLng> arr = decodePoly(polyline);

                    for (int j1 = 0; j1 < arr.size(); j1++) {
                        listGeopoints.add(new LatLng(arr.get(j1).latitude, arr.get(j1).longitude));
                    }


                }
            }
        }
        return listGeopoints;
    }


    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }

    public void draw(ArrayList<LatLng> array) {
        PolylineOptions route = new PolylineOptions().width(15).color(Color.BLUE);

        for (int i = 0; i < array.size(); i++) {
            route.add(array.get(i));
        }
        mMap.addPolyline(route);
        System.out.println(array.get(0));
        // mMap.addMarker(new MarkerOptions().position(array.get(0)).title(s1));
        // mMap.addMarker(new MarkerOptions().position(array.get(array.size()-1)).title(s2));
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(array.get(0), 15));


    }

    class GetLocation extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String JsonResponse = null;
            String response = " ";
            String JsonData = params[0];
            System.out.println(params[0]);
            HttpURLConnection connection = null;
            BufferedReader reader = null;


            try {
                URL url = new URL("http://maps.google.com/maps/api/geocode/json?address=" + params[0] + "&sensor=false");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.setDoOutput(true);
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    System.out.println(responseCode);
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";
                }


                response = response.replaceAll("[{}]", " ");
                response = response.replaceAll("\\s+", " ");

                System.out.println(response);
                String[] part1 = response.split("location");
                part1[1] = part1[1].replaceAll(":", " ");
                part1[1] = part1[1].replaceAll("\"", " ");
                part1[1] = part1[1].replaceAll(",", " ");
                response = response.replaceAll("\\s+", " ");
                String[] l1 = part1[1].split("lng");
                String[] lat1 = l1[0].split("lat");
                String lat = lat1[1];
                // String[] l2=part1[1].split("lng");
                String lng = l1[1];
                lat = lat.replaceAll("\\s", "");
                lng = lng.replaceAll("\\s", "");

                latitude1 = Double.parseDouble(lat);
                longitude1 = Double.parseDouble(lng);


                System.out.println("latitude= " + latitude1 + "longitude=  " + longitude1);

                //return value;

                /*for(String e:part1)
                {
                    response = response.replaceAll("\\s+", " ");
                    String[] part2=e.split(":");
                    System.out.println(e);
                }*/

            } catch (IOException e) {
                e.printStackTrace();
            }

            // System.out.println(e);


            String value = params[0];
            return value;
        }


        @Override
        protected void onPostExecute(String place) {

            setup(place);

        }

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /*@Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("No", null).show();
    }*/
}
