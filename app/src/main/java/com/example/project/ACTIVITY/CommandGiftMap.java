package com.example.project.ACTIVITY;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.project.DATAS.BoutiqueUser;
import com.example.project.DATAS.GIFT;
import com.example.project.R;
import com.example.project.utils.GpsUtils;
import com.example.project.utils.Notification;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CommandGiftMap extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {


    // New variables for Current Place Picker
    private static final String TAG = "LocationPicker";

    private PlacesClient mPlacesClient;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location mLastKnownLocation;

    private final LatLng mDefaultLocation = null;
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private GoogleMap mMap;
    private boolean isGPS = false;
    Button selected;
    ImageView zoom;
    FirebaseDatabase database;
    DatabaseReference reference;
    private String uid;
    ArrayList<BoutiqueUser> boutiqueUsers = new ArrayList<>();
    public static GIFT gif;
    private BoutiqueUser currentBoutique;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (gif == null) return;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = preferences.getString("uid", null);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();


        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        selected = findViewById(R.id.selected);
        zoom = findViewById(R.id.zoom);


        // Initialize the Places client
        String apiKey = getString(R.string.key);
        Places.initialize(getApplicationContext(), apiKey);
        mPlacesClient = Places.createClient(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });



        zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGPS) {
                    new GpsUtils(CommandGiftMap.this).turnGPSOn(new GpsUtils.onGpsListener() {
                        @Override
                        public void gpsStatus(boolean isGPSEnable) {
                            // turn on GPS
                            isGPS = isGPSEnable;
                            if (isGPS) pickCurrentPlace();
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 500) {
                isGPS = true; // flag maintain before get location
            }
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        getLocationPermission();

        reference.child("BOUTIQUES").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boutiqueUsers.clear();
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    BoutiqueUser user = snapshot.getValue(BoutiqueUser.class);
                    if (user == null) continue;
                    boutiqueUsers.add(user);
                    LatLng latLong = new LatLng(Double.valueOf(user.getLat()), Double.valueOf(user.getLon()));
                    mMap.addMarker(new MarkerOptions()
                            .position(latLong)
                            .title(user.getDes()))
                            .setTag(i);
                    i += 1;
                }
                mMap.setOnMarkerClickListener(CommandGiftMap.this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (currentBoutique == null) {
            selected.setEnabled(false);
        } else {
            selected.setEnabled(true);
        }
        if (isGPS) {
            pickCurrentPlace();
        }
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker == null) return false;
        if (marker.getTag() != null && marker.getTag().equals("c")){
            currentBoutique = null;
            selected.setEnabled(false);
            return false;
        }
        if (marker.getTag() != null){
            Integer pos = (Integer) marker.getTag();
            currentBoutique = boutiqueUsers.get(pos);
            selected.setEnabled(true);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() == null) return;
                            mLastKnownLocation = task.getResult();
                            Log.d(TAG, "Latitude: " + mLastKnownLocation.getLatitude());
                            Log.d(TAG, "Longitude: " + mLastKnownLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new com.google.android.gms.maps.model.LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));


                            com.google.android.gms.maps.model.LatLng pos = new com.google.android.gms.maps.model.LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(pos).title("Me")).setTag("c");



                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        }

                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void pickCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            getDeviceLocation();
        } else {

            getLocationPermission();
        }
    }

    public void select(View v) {
        if (currentBoutique == null){
            Toast.makeText(CommandGiftMap.this, "Choose boutique", Toast.LENGTH_SHORT).show();
            return;
        }


        reference.child("USERS").child(uid).child("myGifts").child(gif.getQr_code()).child("status")
                .setValue("En attente");

        reference.child("BOUTIQUES").child(currentBoutique.getUid()).child("gifts")
                .child(gif.getQr_code()).setValue(gif);


        reference.child("BOUTIQUES").child(currentBoutique.getUid()).child("gifts")
                .child(gif.getQr_code()).child("status").setValue("En attente");

        Toast.makeText(CommandGiftMap.this, "Gift command seccussfuly", Toast.LENGTH_LONG).show();
        Notification notification = new Notification(CommandGiftMap.this);
        notification.notif(gif.getUid() ,gif.getUsername() + " Nouveau cadeaux" ,gif.getType());
        currentBoutique = null;
        gif = null;
        startActivity(new Intent(CommandGiftMap.this, Home.class));

    }

}