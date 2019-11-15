package com.project.usmansh.ingrumidreal;

import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

public class MapsActivity extends android.support.v4.app.FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this,R.raw.mapstyle));

            if (!success) {
                Log.e("TAG: ", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("TAG: ", "Can't find style. Error: ", e);
        }
        loadLocationForThisUser();
    }





    private void loadLocationForThisUser() {


        double mylat  = getIntent().getDoubleExtra("lat",0);
        double mylang = getIntent().getDoubleExtra("lang",0);

       double u1lat = mylat+0.002;
       double u1long = mylang+0.002;

       double u2lat = mylat-0.001;
       double u2long = mylang-0.002;

        //Add marker for friend location
        LatLng u1 = new LatLng(u1lat,u1long);
        LatLng u2 = new LatLng(u2lat,u2long);


        //Create Location from user coordinates
        Location currentUser = new Location("");
        currentUser.setLatitude(mylat);
        currentUser.setLongitude(mylang);

        //Create Location from user1 coordinates
        Location friend1= new Location("");
        friend1.setLatitude(u1lat);
        friend1.setLongitude(u1long);


        //Create Location from user2 coordinates
        Location friend2= new Location("");
        friend2.setLatitude(u2lat);
        friend2.setLongitude(u2long);

        //Create Function which will calculate the distance between friend and user
        //distance(currentUser,friend);


        //Clear All  old Marker
        mMap.clear();

        //Add friend marker on map
        mMap.addMarker(new MarkerOptions()
                .position(u1)
                .title("Friend 1")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        //Add friend marker on map
        mMap.addMarker(new MarkerOptions()
                .position(u2)
                .title("Friend 2")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        //Create Marker for Current User
        LatLng current = new LatLng(mylat,mylang);
        mMap.addMarker(new MarkerOptions()
                .position(current)
                .title("My Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));



        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mylat,mylang),16.0f));




    }


}
