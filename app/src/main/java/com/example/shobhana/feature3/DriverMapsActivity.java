package com.example.shobhana.feature3;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

public class DriverMapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    double latitude, longitude, latitude1, longitude1;
    String route = "pescollege,corporationbangalore,MGRoadbangalore,indiranagar";
    int count = 0;
    String s1 = null;
    String s2 = null;
    GoogleApiClient gClient = null;
    Location LastLocation,CurrentLocation;
    LocationRequest mLocationRequest;
    public static final String PREFS_NAME = "LoginPrefs";
    final int RequestLocationId = 0;
    final String [] PermissionsLocation =
            {

                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (gClient == null) {
            gClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if ((int) Build.VERSION.SDK_INT < 23) {


            System.out.println("inside <23");
            createLocationRequest();
        }

        else{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                System.out.println("inside granted");
                createLocationRequest();

            }
            else{
                System.out.println("inside request");
                ActivityCompat.requestPermissions(this, PermissionsLocation, RequestLocationId);

            }



        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Emergency", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DriverMapsActivity.this);

                LinearLayout layout = new LinearLayout(DriverMapsActivity.this);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(parms);

                layout.setGravity(Gravity.CLIP_VERTICAL);
                layout.setPadding(2, 2, 2, 2);

                final EditText et = new EditText(DriverMapsActivity.this);
                et.setHint("Enter message");
                final String emsg = et.getText().toString();

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

                        String message = "message=" + emsg + "&phonenumber=9844116260" + "&usertype=driver";
                       // SendEmergency eobj = new SendEmergency();
                       // eobj.execute(message);

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                try {
                    alertDialog.show();
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }
        LastLocation = LocationServices.FusedLocationApi.getLastLocation(
                gClient);
        if (LastLocation != null) {
            System.out.println(LastLocation.getLatitude()+" "+LastLocation.getLongitude());
        }


        Location location = LocationServices.FusedLocationApi.getLastLocation(gClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(gClient, mLocationRequest, this);

        }
        else {
            handleNewLocation(location);
        };


    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void handleNewLocation(Location location) {

        Log.d("location", location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        System.out.println("new latitude= " + currentLatitude + "  new longitude= " + currentLongitude);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String imei=telephonyManager.getDeviceId();

        String coordinates1=currentLatitude+","+currentLongitude;
        String coordinates="location="+coordinates1+"&imei="+imei;


        LatLng loc = new LatLng(currentLatitude,currentLongitude);

        mMap.addMarker(new MarkerOptions().position(loc).title("current location"));
        DriverSendCoordinates ob=new DriverSendCoordinates();
        ob.execute(coordinates);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        handleNewLocation(location);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void onStart() {
        gClient.connect();
        super.onStart();
    }

    protected void onStop() {
        gClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(gClient, this);
            gClient.disconnect();
        }
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

            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
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


