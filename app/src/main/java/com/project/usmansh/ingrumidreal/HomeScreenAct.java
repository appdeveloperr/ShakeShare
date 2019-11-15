package com.project.usmansh.ingrumidreal;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.session.MediaSession;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Placeholder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.RecyclerViewAdapter;
import Classes.Data;
import Classes.Mysingleton;
import Fragments.Friends;
import Fragments.IngrumBump;
import Fragments.Message;
import Fragments.UserProfile;


public class HomeScreenAct extends AppCompatActivity {


    ConstraintLayout homeAct_mainCl,ingrumCl,frindsCl,messageCl,mapCl,profileCl;
    Placeholder homeAct_placeholder;

    public ArrayList<String> mNames = new ArrayList<>();
    public ArrayList<String> mImageUrls = new ArrayList<>();

    ArrayList<Data> data= new ArrayList<>();

    //    RecyclerView recyclerView;
    private static final String TAG = "HomeActivity";


    ImageView homeAct_ingrumIV,homeAct_friendsIV,homeAct_mapIV,homeAct_profileIV,homeAct_messageIV;
    Fragment fragment;



    //Google getLocation API variables Declaration
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    private static final int REQUEST_CODE = 1000;
    public  static boolean onBumpScreen = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

//        homeAct_placeholder = (Placeholder)findViewById(R.id.homeAct_placeholder);
        homeAct_mainCl      = (ConstraintLayout)findViewById(R.id.homeAct_mainCl);
        homeAct_ingrumIV  = (ImageView)findViewById(R.id.homeAct_imgrumIV);
        homeAct_friendsIV = (ImageView)findViewById(R.id.homeAct_friendsIV);
        homeAct_mapIV     = (ImageView)findViewById(R.id.homeAct_mapIV);
        homeAct_profileIV = (ImageView)findViewById(R.id.homeAct_profileIV);
        homeAct_messageIV = (ImageView)findViewById(R.id.homeAct_messageIV);

        ingrumCl  = (ConstraintLayout)findViewById(R.id.ingrumCL);
        frindsCl  = (ConstraintLayout)findViewById(R.id.friendsCL);
        messageCl = (ConstraintLayout)findViewById(R.id.messageCL);
        mapCl     = (ConstraintLayout)findViewById(R.id.mapCL);
        profileCl = (ConstraintLayout)findViewById(R.id.profileCl);




        SharedPreferences SharedPref = getSharedPreferences("User_Token", Context.MODE_PRIVATE);
        String user_token = SharedPref.getString("uToken","");
        //Toast.makeText(this, "uToken: "+user_token, Toast.LENGTH_SHORT).show();



        ingrumCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBumpScreen = true;
      //          Toast.makeText(HomeScreenAct.this, "ingrum screen.!", Toast.LENGTH_SHORT).show();
                fragment = new IngrumBump();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
                transaction.add(R.id.fragment, fragment).commit();

    //            transaction.replace(R.id.fragment, fragment);
                //transaction.addToBackStack(null);
                //transaction.commit();

            }
        });



        frindsCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBumpScreen = false;
    //            Toast.makeText(HomeScreenAct.this, "Friends Screen..!", Toast.LENGTH_SHORT).show();
                fragment = new Friends();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
                transaction.add(R.id.fragment, fragment).commit();

                //transaction.replace(R.id.fragment, fragment);

                //  transaction.addToBackStack(null);
                //transaction.commit();

            }
        });


        mapCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBumpScreen = false;
                calllFusedApi();
                //Intent map = new Intent(getApplicationContext(),MapsActivity.class);
                //startActivity(map);
                //
                //              Toast.makeText(HomeScreenAct.this, "Map Screen..!", Toast.LENGTH_SHORT).show();
                //fragment = new Map();
                //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
                //transaction.add(R.id.fragment, fragment).commit();

         //       transaction.replace(R.id.fragment, fragment);

                //transaction.addToBackStack(null);
           //      transaction.commit();

            }
        });

        profileCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBumpScreen = false;
                Log.d("Sensor: ","Unregisted in user profile");
                //Toast.makeText(HomeScreenAct.this, "Profile Screen..!", Toast.LENGTH_SHORT).show();
                fragment = new UserProfile();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
                transaction.add(R.id.fragment, fragment).commit();

             //   transaction.replace(R.id.fragment, fragment);
                //transaction.addToBackStack(null);

                //transaction.commit();

            }
        });


        messageCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBumpScreen = false;
        //        Toast.makeText(HomeScreenAct.this, "Message Screen..!", Toast.LENGTH_SHORT).show();
                fragment = new Message();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
                transaction.add(R.id.fragment, fragment).commit();

                //transaction.addToBackStack(null);
                //transaction.replace(R.id.fragment, fragment);
                //transaction.commit();

            }
        });



        //check location permission runtime
        if(ActivityCompat.shouldShowRequestPermissionRationale(HomeScreenAct.this, Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(HomeScreenAct.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }else{
            //if permission is granted then call this function
           // Toast.makeText(getApplicationContext(), "Calling Location", Toast.LENGTH_SHORT).show();
            buildLocationRequest();
            buildLocationCallBack();
            //Create FusedProviderClient
            fusedLocationProviderClient  = LocationServices.getFusedLocationProviderClient(HomeScreenAct.this);
        }

        initRecyclerView();

//        getImages();

    }





    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("E:\\AllAndroidProjects\\IngrumIdReal\\app\\src\\main\\res\\drawable-hdpi\facebook.png");
        mNames.add("Havasu Falls");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Trondheim");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Portugal");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Rocky Mountain National Park");


        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Mahahual");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Frozen Lake");


        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("White Sands Desert");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("Austrailia");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("Washington");


        /*data.add(new Data( R.drawable.map, "Image 1"));
        data.add(new Data( R.drawable.friends, "Image 2"));
        data.add(new Data( R.drawable.ingrum, "Image 3"));
        data.add(new Data( R.drawable.profile, "Image 2"));
        data.add(new Data( R.drawable.messages, "Image 3"));
        initRecyclerView();*/

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

      /*  LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, data);
        recyclerView.setAdapter(adapter);
*/
    }



    public void switchContent(int id, Fragment fragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
        ft.replace(id, fragment, fragment.toString());
        ft.addToBackStack(null);
        ft.commit();
    }



    //Google Map Api getLocation Coding and Methods


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length >0){

                    if(grantResults.length == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(HomeScreenAct.this, "Permission Granted..!", Toast.LENGTH_SHORT).show();
                    }else if (grantResults.length == PackageManager.PERMISSION_DENIED){
                        Toast.makeText(HomeScreenAct.this, "Permission Denied..!", Toast.LENGTH_SHORT).show();
                    }
                }
        }

    }

    private void buildLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }


    private void buildLocationCallBack() {

        locationCallback = new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {

                for(Location location :locationResult.getLocations()){
                   // Toast.makeText(HomeScreenAct.this, "Logging", Toast.LENGTH_SHORT).show();
                    Log.d("LOCATIONSSS: ","Lat: "+String.valueOf(location.getLatitude())+"    Long:  "+String.valueOf(location.getLatitude()));

                    Intent goMapAct = new Intent(HomeScreenAct.this, MapsActivity.class);
                    goMapAct.putExtra("lat",location.getLatitude());
                    goMapAct.putExtra("lang",location.getLongitude());
                    startActivity(goMapAct);
                }


            }
        };
    }


    private void calllFusedApi() {

        if(ActivityCompat.checkSelfPermission(HomeScreenAct.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(HomeScreenAct.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(HomeScreenAct.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            return;
        }else {

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
           // Toast.makeText(HomeScreenAct.this, "In CallFusedApi", Toast.LENGTH_SHORT).show();
        }
    }





}
