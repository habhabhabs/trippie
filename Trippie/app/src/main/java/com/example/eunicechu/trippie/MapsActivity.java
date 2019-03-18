package com.example.eunicechu.trippie;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
//    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private double latitude, longitude;
    private int proximityRadius = 10000;
    private ImageButton foodBtn;
    private ImageButton mallBtn;
    private ImageButton parkBtn;
    private ImageButton trainBtn;


////    private static final int REQUEST_LOCATION_PERMISSION = 1;
//    Marker marker;
//    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        Intent mI = getIntent();
//        int intValue = mI.getIntExtra("buttonID", 0);
//        if(intValue == 0){
//            // error handling
//        } else{
//            if(intValue == R.id.button){
////                mMap.clear();
//                String url = getUrl(latitude, longitude, attraction);
//                transferData[0] = mMap;
//                transferData[1] = url;
//
//                getNearbyPlaces.execute(transferData);
//                Toast.makeText(this, "Searching for nearby attraction...", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "Showing nearby attraction...", Toast.LENGTH_SHORT).show();
//            }
//            if(intValue == R.id.button2){
////                mMap.clear();
//                String url = getUrl(latitude, longitude, food);
//                transferData[0] = mMap;
//                transferData[1] = url;
//
//                getNearbyPlaces.execute(transferData);
//                Toast.makeText(this, "Searching for nearby food...", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "Showing nearby food...", Toast.LENGTH_SHORT).show();
//
//            }
//            if(intValue == R.id.button3){
////                mMap.clear();
//                String url = getUrl(latitude, longitude, mall);
//                transferData[0] = mMap;
//                transferData[1] = url;
//
//                getNearbyPlaces.execute(transferData);
//                Toast.makeText(this, "Searching for nearby mall...", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "Showing nearby mall...", Toast.LENGTH_SHORT).show();
//
//            }
//            if(intValue == R.id.button4){
////                mMap.clear();
//                String url = getUrl(latitude, longitude, park);
//                transferData[0] = mMap;
//                transferData[1] = url;
//
//                getNearbyPlaces.execute(transferData);
//                Toast.makeText(this, "Searching for nearby park...", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "Showing nearby park...", Toast.LENGTH_SHORT).show();
//
//            }
//            if(intValue == R.id.button5){
////                mMap.clear();
//                String url = getUrl(latitude, longitude, train);
//                transferData[0] = mMap;
//                transferData[1] = url;
//
//                getNearbyPlaces.execute(transferData);
//                Toast.makeText(this, "Searching for nearby train...", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "Showing nearby train...", Toast.LENGTH_SHORT).show();
//
//            }
//        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        foodBtn = findViewById(R.id.food_nearby);
        mallBtn = findViewById(R.id.mall_nearby);
        parkBtn = findViewById(R.id.park_nearby);
        trainBtn = findViewById(R.id.train_nearby);
    }

    public void onClick(View v){
        String attraction = "attraction", restaurant = "restaurant", mall = "shopping_mall", park = "park", train = "train_station";
        Object transferData[]           = new Object[2];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
        //mMap.clear();
        switch(v.getId()){
            case R.id.food_nearby: //for food
                //mMap.clear();
                String url      = getUrl(latitude, longitude, restaurant);
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.setId("food");
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Showing nearby food.", Toast.LENGTH_SHORT).show();

                foodBtn.setImageResource(R.drawable.foodmap_s);
                mallBtn.setImageResource(R.drawable.mallsmap);
                parkBtn.setImageResource(R.drawable.parksmap);
                trainBtn.setImageResource(R.drawable.trainmap);

                break;

            case R.id.train_nearby: //for train
                //mMap.clear();
                url             = getUrl(latitude, longitude, train);
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.setId("train");
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Showing nearby train stations.", Toast.LENGTH_SHORT).show();

                foodBtn.setImageResource(R.drawable.foodmap);
                mallBtn.setImageResource(R.drawable.mallsmap);
                parkBtn.setImageResource(R.drawable.parksmap);
                trainBtn.setImageResource(R.drawable.trainmap_s);

                break;

            case R.id.mall_nearby: //for mall
                //mMap.clear();
                url             = getUrl(latitude, longitude, mall);
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.setId("mall");
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Showing nearby malls.", Toast.LENGTH_SHORT).show();

                foodBtn.setImageResource(R.drawable.foodmap);
                mallBtn.setImageResource(R.drawable.mallsmap_s);
                parkBtn.setImageResource(R.drawable.parksmap);
                trainBtn.setImageResource(R.drawable.trainmap);

                break;

            case R.id.park_nearby: //for park
                //mMap.clear();
                url             = getUrl(latitude, longitude, park);
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.setId("park");
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Showing nearby parks.", Toast.LENGTH_SHORT).show();

                foodBtn.setImageResource(R.drawable.foodmap);
                mallBtn.setImageResource(R.drawable.mallsmap);
                parkBtn.setImageResource(R.drawable.parksmap_s);
                trainBtn.setImageResource(R.drawable.trainmap);

                break;

            case R.id.qrbutton: //for park
                //mMap.clear();
                //url             = getUrl(latitude, longitude, park);
                //transferData[0] = mMap;
                //transferData[1] = url;
                //getNearbyPlaces.setId("park");
                //getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Showing bicycle parking near you.", Toast.LENGTH_SHORT).show();

                JSONArray parkingPlaces = null;
                try {
                    // can use the datamall api in place of this parkingPlaces JSONArray variable (via URL connection and BufferedReader)
                    parkingPlaces = new JSONArray("[\n" +
                            "    {\n" +
                            "        \"Description\": \"RP North Canteen\",\n" +
                            "        \"Latitude\": 1.446865,\n" +
                            "        \"Longitude\": 103.784696,\n" +
                            "        \"RackType\": \"Racks\",\n" +
                            "        \"RackCount\": 65,\n" +
                            "        \"ShelterIndicator\": \"N\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"Description\": \"Admiralty Park\",\n" +
                            "        \"Latitude\": 1.448700,\n" +
                            "        \"Longitude\": 103.778879,\n" +
                            "        \"RackType\": \"Yellow Box\",\n" +
                            "        \"RackCount\": 30,\n" +
                            "        \"ShelterIndicator\": \"N\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"Description\": \"RP Industry Centre\",\n" +
                            "        \"Latitude\": 1.445015,\n" +
                            "        \"Longitude\": 103.783594,\n" +
                            "        \"RackType\": \"Racks\",\n" +
                            "        \"RackCount\": 10,\n" +
                            "        \"ShelterIndicator\": \"Y\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"Description\": \"RP E1 Building\",\n" +
                            "        \"Latitude\": 1.444404,\n" +
                            "        \"Longitude\": 103.785150,\n" +
                            "        \"RackType\": \"Yellow Box\",\n" +
                            "        \"RackCount\": 40,\n" +
                            "        \"ShelterIndicator\": \"N\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"Description\": \"Block 807 HDB Woodlands\",\n" +
                            "        \"Latitude\": 1.442301,\n" +
                            "        \"Longitude\": 103.787290,\n" +
                            "        \"RackType\": \"Racks\",\n" +
                            "        \"RackCount\": 5,\n" +
                            "        \"ShelterIndicator\": \"Y\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"Description\": \"Marsiling Industrial Estate\",\n" +
                            "        \"Latitude\": 1.441454, \n" +
                            "        \"Longitude\": 103.782897,\n" +
                            "        \"RackType\": \"Yellow Box\",\n" +
                            "        \"RackCount\": 40,\n" +
                            "        \"ShelterIndicator\": \"N\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"Description\": \"Al-Ameen Eating Corner\",\n" +
                            "        \"Latitude\": 1.439620,\n" +
                            "        \"Longitude\": 103.783261,\n" +
                            "        \"RackType\": \"Racks\",\n" +
                            "        \"RackCount\": 15,\n" +
                            "        \"ShelterIndicator\": \"Y\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"Description\": \"Riverside Secondary School\",\n" +
                            "        \"Latitude\": 1.440789,\n" +
                            "        \"Longitude\": 103.788637,\n" +
                            "        \"RackType\": \"Racks\",\n" +
                            "        \"RackCount\": 5,\n" +
                            "        \"ShelterIndicator\": \"N\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"Description\": \"Woodlands Community Club\",\n" +
                            "        \"Latitude\": 1.439910,\n" +
                            "        \"Longitude\": 103.788325,\n" +
                            "        \"RackType\": \"Racks\",\n" +
                            "        \"RackCount\": 27,\n" +
                            "        \"ShelterIndicator\": \"Y\"\n" +
                            "    }\n" +
                            "]");



                    for (int i = 0; i < parkingPlaces.length(); i++) {
                        JSONObject parkingPlace = (JSONObject) parkingPlaces.get(i);
                        LatLng parkingPlaceCoords = new LatLng(parkingPlace.getDouble("Latitude"), parkingPlace.getDouble("Longitude"));

                        Toast.makeText(this, parkingPlaceCoords.toString(), Toast.LENGTH_SHORT).show();

                        mMap.addMarker(new MarkerOptions().position(parkingPlaceCoords).title(parkingPlace.getString("Description")).icon(BitmapDescriptorFactory.fromResource(R.drawable.parking)));

                    }

                } catch (Exception ex) { ex.printStackTrace(); }


                //foodBtn.setImageResource(R.drawable.foodmap);
                //mallBtn.setImageResource(R.drawable.mallsmap);
                //parkBtn.setImageResource(R.drawable.parksmap_s);
                //trainBtn.setImageResource(R.drawable.trainmap);

                break;
        }
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

    private String getUrl(double latitude, double longitude, String str_nearbylocation){
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + latitude + "," + longitude);
        googleURL.append("&radius=" + proximityRadius);
        googleURL.append("&type=" + str_nearbylocation);
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + "AIzaSyCU_yfjpw71q88cIsNKyeNyEV8IALjxDqo");

        Log.d("GoogleMapsActivity", "url = " + googleURL.toString());
        return googleURL.toString();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String attraction = "attraction", restaurant = "restaurant", mall = "shopping_mall", park = "park", train = "train_station";
        Object transferData[]           = new Object[2];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();

        Intent mI = getIntent();
        int intValue = mI.getIntExtra("buttonID", 0);
        if(intValue == 0){
            // error handling
        } else{
            if(intValue == R.id.foodBtn){
                mMap.clear();
                String url      = getUrl(1.4360, 103.7860, restaurant);
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.setId("food");
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Showing nearby food.", Toast.LENGTH_SHORT).show();

                foodBtn.setImageResource(R.drawable.foodmap_s);
                mallBtn.setImageResource(R.drawable.mallsmap);
                parkBtn.setImageResource(R.drawable.parksmap);
                trainBtn.setImageResource(R.drawable.trainmap);
            }
            else if(intValue == R.id.mallBtn){
                mMap.clear();
                String url      = getUrl(1.4360, 103.7860, mall);
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.setId("mall");
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Showing nearby malls.", Toast.LENGTH_SHORT).show();

                foodBtn.setImageResource(R.drawable.foodmap);
                mallBtn.setImageResource(R.drawable.mallsmap_s);
                parkBtn.setImageResource(R.drawable.parksmap);
                trainBtn.setImageResource(R.drawable.trainmap);
            }
            else if(intValue == R.id.parkBtn){
                mMap.clear();
                String url      = getUrl(1.4360, 103.7860, park);
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.setId("park");
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Showing nearby parks.", Toast.LENGTH_SHORT).show();

                foodBtn.setImageResource(R.drawable.foodmap);
                mallBtn.setImageResource(R.drawable.mallsmap);
                parkBtn.setImageResource(R.drawable.parksmap_s);
                trainBtn.setImageResource(R.drawable.trainmap);
            }
            else {
                mMap.clear();
                String url      = getUrl(1.4360, 103.7860, train);
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.setId("train");
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Showing nearby train stations.", Toast.LENGTH_SHORT).show();

                foodBtn.setImageResource(R.drawable.foodmap);
                mallBtn.setImageResource(R.drawable.mallsmap);
                parkBtn.setImageResource(R.drawable.parksmap);
                trainBtn.setImageResource(R.drawable.trainmap_s);
            }
        }

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
        }
    }

    public boolean checkUserLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            } else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Request_User_Location_Code:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if(googleApiClient == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else{
                    Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

//    public void showParkingMarkers() {
//        JSONArray parkingPlaces = null;
//        try {
//            // can use the datamall api in place of this parkingPlaces JSONArray variable (via URL connection and BufferedReader)
//            parkingPlaces = new JSONArray("[\n" +
//                    "    {\n" +
//                    "        \"Description\": \"RP North Canteen\",\n" +
//                    "        \"Latitude\": 1.446865,\n" +
//                    "        \"Longitude\": 103.784696,\n" +
//                    "        \"RackType\": \"Racks\",\n" +
//                    "        \"RackCount\": 65,\n" +
//                    "        \"ShelterIndicator\": \"N\"\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "        \"Description\": \"Admiralty Park\",\n" +
//                    "        \"Latitude\": 1.448700,\n" +
//                    "        \"Longitude\": 103.778879,\n" +
//                    "        \"RackType\": \"Yellow Box\",\n" +
//                    "        \"RackCount\": 30,\n" +
//                    "        \"ShelterIndicator\": \"N\"\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "        \"Description\": \"RP Industry Centre\",\n" +
//                    "        \"Latitude\": 1.445015,\n" +
//                    "        \"Longitude\": 103.783594,\n" +
//                    "        \"RackType\": \"Racks\",\n" +
//                    "        \"RackCount\": 10,\n" +
//                    "        \"ShelterIndicator\": \"Y\"\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "        \"Description\": \"RP E1 Building\",\n" +
//                    "        \"Latitude\": 1.444404,\n" +
//                    "        \"Longitude\": 103.785150,\n" +
//                    "        \"RackType\": \"Yellow Box\",\n" +
//                    "        \"RackCount\": 40,\n" +
//                    "        \"ShelterIndicator\": \"N\"\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "        \"Description\": \"Block 807 HDB Woodlands\",\n" +
//                    "        \"Latitude\": 1.442301,\n" +
//                    "        \"Longitude\": 103.787290,\n" +
//                    "        \"RackType\": \"Racks\",\n" +
//                    "        \"RackCount\": 5,\n" +
//                    "        \"ShelterIndicator\": \"Y\"\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "        \"Description\": \"Marsiling Industrial Estate\",\n" +
//                    "        \"Latitude\": 1.441454, \n" +
//                    "        \"Longitude\": 103.782897,\n" +
//                    "        \"RackType\": \"Yellow Box\",\n" +
//                    "        \"RackCount\": 40,\n" +
//                    "        \"ShelterIndicator\": \"N\"\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "        \"Description\": \"Al-Ameen Eating Corner\",\n" +
//                    "        \"Latitude\": 1.439620,\n" +
//                    "        \"Longitude\": 103.783261,\n" +
//                    "        \"RackType\": \"Racks\",\n" +
//                    "        \"RackCount\": 15,\n" +
//                    "        \"ShelterIndicator\": \"Y\"\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "        \"Description\": \"Riverside Secondary School\",\n" +
//                    "        \"Latitude\": 1.440789,\n" +
//                    "        \"Longitude\": 103.788637,\n" +
//                    "        \"RackType\": \"Racks\",\n" +
//                    "        \"RackCount\": 5,\n" +
//                    "        \"ShelterIndicator\": \"N\"\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "        \"Description\": \"Woodlands Community Club\",\n" +
//                    "        \"Latitude\": 1.439910,\n" +
//                    "        \"Longitude\": 103.788325,\n" +
//                    "        \"RackType\": \"Racks\",\n" +
//                    "        \"RackCount\": 27,\n" +
//                    "        \"ShelterIndicator\": \"Y\"\n" +
//                    "    }\n" +
//                    "]");
//
//            for (int i = 0; i < parkingPlaces.length(); i++) {
//                JSONObject parkingPlace = (JSONObject) parkingPlaces.get(i);
//                LatLng parkingPlaceCoords = new LatLng(parkingPlace.getDouble("Latitude"), parkingPlace.getDouble("Longitude"));
//                mMap.addMarker(new MarkerOptions().position(parkingPlaceCoords).title(parkingPlace.getString("Description")).snippet("Rack Type: " + parkingPlace.getString("Racks") + "\nRack Count: " + parkingPlace.getInt("RackCount") + "\nShelter Indicator: " + parkingPlace.getString("ShelterIndicator")));
//            }
//
//        } catch (Exception ex) { ex.printStackTrace(); }
//    }




    // activate the qrcode reader after clicking on parking marker
    public void qrcode (View v)
    {
        Intent i = new Intent(this, ScanQRCode.class);
        startActivity(i);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}