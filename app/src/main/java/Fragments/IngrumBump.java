package Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.project.usmansh.ingrumidreal.HomeScreenAct;
import com.project.usmansh.ingrumidreal.MapsActivity;
import com.project.usmansh.ingrumidreal.R;
import com.project.usmansh.ingrumidreal.ScanCodeAct;
import com.project.usmansh.ingrumidreal.TabHomeScreenAct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import Classes.Mysingleton;

import static android.content.Context.LOCATION_SERVICE;


public class IngrumBump extends Fragment {


    private static final int REQUEST_CODE = 1000;
    private static final int CAMERA_REQUEST_CODE = 1001;
    public final static int QRcodeWidth = 500;

    private static IngrumBump INSTANCE = null;
    View view;

    ImageView popBump_crossIV, popBump_mainImgIV,popBump_phoneIV, popBump_facebookIV,  popBump_instagramIV;
    ImageView popBump_twitterIV, popBump_linkedinIV, popBump_snapchatIV, popBump_emailIV;
    TextView popBump_usernameTv;
    Button popBump_connectBt;
    ImageView frag_bump_mapIV,frag_bump_searchIV;
    EditText frag_bump_searchEd;
    JSONArray accounts = new JSONArray();
    JSONArray usersList = new JSONArray();
    JSONArray searchUsersList = new JSONArray();
    ArrayAdapter<String> userListAdapter,searchUserListAdapter;
    final ArrayList<String> userNames = new ArrayList<>();
    ArrayList<String> searchUserNames = new ArrayList<>();
    LocationManager mLocationManager;
    public  SensorManager sm;

    private float acelVal;
    private float acelLast;
    private float shake;
    double userLat,userLong;
    float userAcuracy;
    int seachPage = 1;
    int threadCalltimes = 0;
    boolean isUListDialogOpen = false;
    boolean isSearchUListDialogOpen = false;
    LocationManager locationManager ;
    boolean GpsStatus ;

    String utcYear,utcMonth,utcDay,utcHour,utcMinute,utcSecond;

    //BumpPopUp
    boolean phoneClicked = false;
    boolean emailClicked = false;
    boolean facebookClicked = false;
    boolean instagramClicked = false;
    boolean linkedinClicked = false;
    boolean twitterClicked = false;
    boolean snapchatClicked = false;

    //Google getLocation API variables Declaration
    FusedLocationProviderClient fusedLocationProviderClient,mapfusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    //Components

    ImageView frag_bump_qrIV;
    AlertDialog bumpAlertDialog, userslistAlertDialog, qrAlertDialog,gpsAlertDialog,searchListAlertDialog;

    //Constructor
    public IngrumBump() {
        // Required empty public constructor
    }
    public static IngrumBump getInstance() {

        if (INSTANCE == null)
            INSTANCE = new IngrumBump();
        return INSTANCE;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ingrum_bump, container, false);

        frag_bump_qrIV      = (ImageView)view.findViewById(R.id.frag_bump_qrIV);
        frag_bump_mapIV     = (ImageView)view.findViewById(R.id.frag_bump_mapIV);
        frag_bump_searchEd  = (EditText) view.findViewById(R.id.frag_bump_searchEd);
        frag_bump_searchIV  = (ImageView)view.findViewById(R.id.frag_bump_searchIV);


        frag_bump_qrIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openQRDialog();
            }
        });


        frag_bump_mapIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openMap();

            }
        });

        frag_bump_searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchVal = frag_bump_searchEd.getText().toString().trim();

                if(searchVal.length() > 2){
                    callSearchApi(searchVal);
                }else{
                    Toast.makeText(getActivity(), "Enter minimum 3 characters..!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        frag_bump_searchEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        CheckGpsStatus();
        if(!GpsStatus){
            openEnableGPSAlert();
        }else{
            Log.d("GPS: ","ALready On");
        }




        //Sensors Coding Starting
        sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        startListening();

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

        //check location permission runtime
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }else{
            //if permission is granted then call this function
            buildLocationRequest();
            buildLocationCallBack();
            //Create FusedProviderClient
            fusedLocationProviderClient  = LocationServices.getFusedLocationProviderClient(getActivity());
            //GetUser'sLocation
            calllFusedApi();

        }


        checkCameraPermission();
        return view;
    }



    private void callSearchApi(String searchValue) {

        String url = "http://3.219.121.75:8080/api/crud/search";

        SharedPreferences SharedPref = getActivity().getSharedPreferences("User_Token", Context.MODE_PRIVATE);
        final String user_token = SharedPref.getString("uToken","");



        JSONObject jsonVal = new JSONObject();

        try {
            jsonVal.put("searchString", searchValue);
            jsonVal.put("page",String.valueOf(seachPage));

            Log.d("Search Sending Data: ",""+jsonVal.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonVal, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Successfully Logged In
                Log.d("Response: ",response.toString());
                try {
                    if(response.getString("status").equals("200")){
                        Log.d("Responseeee: ",response.toString());


                       searchUsersList = response.getJSONArray("users");
                        if(searchUsersList.length()>0) {

                            if(!isSearchUListDialogOpen) {
                                openSearchDialog(searchUsersList);

                            }else{
                                searchUserNames.clear();
                                //Getting UserNames from JSON Array
                                for(int i=0; i<searchUsersList.length(); i++){
                                    try {
                                        searchUserNames.add(searchUsersList.getJSONObject(i).getString("username"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                searchUserListAdapter.notifyDataSetChanged();

                            }
                            //callingThreadToGetUserAgain();
                        }
                        else{
                            Toast.makeText(getActivity(), "No Search User Found..!", Toast.LENGTH_SHORT).show();

                        }
                        //openBumpDialog(response.getJSONArray("users").getJSONObject(0).getString("username"));





                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Converting Error Response Into JSON Object
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {

                    String jsonError = new String(networkResponse.data);
                    // Print Error!
                    try {
                        JSONObject obj = new JSONObject(jsonError);
                        Log.d("String Error: ",jsonError);
                        //Log.d("Json Error: ",obj.get("message").toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+ user_token);
                return headers;
            }
        };


        Mysingleton.getInstance(getActivity()).addToRequestque(request);



    }

    private void openSearchDialog(JSONArray searchUsersList){

        isSearchUListDialogOpen = true;

        //Getting UserNames from JSON Array
        for(int i=0; i<searchUsersList.length(); i++){
            try {
                searchUserNames.add(searchUsersList.getJSONObject(i).getString("username"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.popup_search_list, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Linking Popup_social Components
        ListView popup_searchlist_listV      = (ListView)promptView.findViewById(R.id.popup_searchlist_listV);
        Button popup_searchlist_backBt       = (Button)promptView.findViewById(R.id.popup_searchlist_backBt);
        Button popup_searchlist_nextBt       = (Button)promptView.findViewById(R.id.popup_searchlist_nextBt);
        EditText popup_searchlist_searchEd   = (EditText)promptView.findViewById(R.id.popup_searchlist_searchEd);
        ImageView popup_searchlist_searchIV  = (ImageView)promptView.findViewById(R.id.popup_searchlist_searchIV);

        popup_searchlist_searchEd.setVisibility(View.INVISIBLE);
        popup_searchlist_nextBt.setVisibility(View.INVISIBLE);
        popup_searchlist_backBt.setVisibility(View.INVISIBLE);
        popup_searchlist_searchIV.setVisibility(View.INVISIBLE);


        searchUserListAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, searchUserNames){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setGravity(Gravity.CENTER);

                return view;
            }
        };
        popup_searchlist_listV.setAdapter(searchUserListAdapter);



        popup_searchlist_nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seachPage++;
                callSearchApi(frag_bump_searchEd.getText().toString().trim());
            }
        });


        popup_searchlist_backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seachPage--;
                callSearchApi(frag_bump_searchEd.getText().toString().trim());
            }
        });

        /*popup_searchlist_listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBumpDialog(searchUserNames.get(position));
            }
        });*/


        //Show Alert Code
        builder.setView(promptView);
        searchListAlertDialog = builder.create();
        searchListAlertDialog.show();
        searchListAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        searchListAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d("DIALOGGG: ","DISSMISSSs");
                //Registered Sensor Listener to start listening again
                isUListDialogOpen = false;
                Log.d("UserListDialog Reset: ","YEse ");

            }
        });



    }


    private void openEnableGPSAlert() {



        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.popup_enable_gps, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Linking Popup_social Components
        Button popupGps_yesBt = (Button)promptView.findViewById(R.id.popup_gps_yesBt);

        popupGps_yesBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                gpsAlertDialog.dismiss();
            }
        });

        //Show Alert Code
        builder.setView(promptView);
        gpsAlertDialog = builder.create();
        gpsAlertDialog.show();
        gpsAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //bu.getWindow().setLayout(650, 700); //Controlling width and

        /*WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(bumpAlertDialog.getWindow().getAttributes());
        lp.width = 950;
        lp.height = 720;
        lp.x=0;
        lp.y=-150;
        bumpAlertDialog.getWindow().setAttributes(lp);
*/

    }

    private void checkCameraPermission() {

        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA)){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);
        }

    }


    private void openQRDialog() {


        Bitmap bitmap = null;
        String inputValue = "https://www.ingrumid.com/"+TabHomeScreenAct.username;
        Log.d("QR CODEEEE: ","Input is: "+inputValue);
        
        if(inputValue.length() > 0){
            try {
                bitmap = TextToImageEncode(inputValue);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

        ImageView popupQr_imgIV,popupQr_crossIV,popupQr_cameraIV;
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.popup_qrcode, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Linking Popup_social Components

        popupQr_crossIV     = (ImageView)promptView.findViewById(R.id.popupQr_crossIV);
        popupQr_imgIV       = (ImageView)promptView.findViewById(R.id.popupQr_imgIV);
        popupQr_cameraIV    = (ImageView)promptView.findViewById(R.id.popupQr_cameraIV);
    
            popupQr_imgIV.setImageBitmap(bitmap);
        popupQr_crossIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrAlertDialog.dismiss();
            }
        });


        popupQr_imgIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "QR code", Toast.LENGTH_SHORT).show();
                addContact();
            }
        });


        popupQr_cameraIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScanCodeAct.class));

            }
        });


        //Show Alert Code
        builder.setView(promptView);
        qrAlertDialog = builder.create();
        qrAlertDialog.show();
        qrAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.getWindow().setLayout(650, 700); //Controlling width and


       /* WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = 650;
        lp.height = 720;
        lp.x=0;
        lp.y=-150;
        alertDialog.getWindow().setAttributes(lp);*/

    }

    private void addContact() {


        Intent intent =  new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType((ContactsContract.RawContacts.CONTENT_TYPE));

        String email = "usman@gmail.com";
        String fname = "usman";
        String lname = "sh";
        String phoneno = "032246682466";


        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email)
                .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .putExtra(ContactsContract.Intents.Insert.PHONE,phoneno)
                .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .putExtra(ContactsContract.Intents.Insert.NAME, fname+" "+lname);


        startActivity(intent);
        Log.d("Contacts: ","Addedd Success..!");
    }


    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];


        int color_black = getResources().getColor(R.color.colorPrimary);
        int color_white = getResources().getColor(R.color.white);

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        color_black : color_white;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public SensorEventListener shakeListner = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = acelVal - acelLast;
            shake = shake * 0.9f + delta;

            if (shake > 7) {

//                Toast.makeText(getActivity(), "Shake Triggered..!", Toast.LENGTH_SHORT).show();
                stopListening();
                Log.d("Sensor UNREGISTERED: ","YESSS");
             //   calllFusedApi();
                sendBumpData(userLat,userLong,userAcuracy);

            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {


        }
    };

    private void sendBumpData(double latitude, double longitude, float accuracy) {

        int lat = (int) (latitude*1000000);
        int lang = (int) (longitude*1000000);
        int accu = (int)accuracy;

        String url = "http://3.219.121.75:8080/api/bump/data";
        SharedPreferences SharedPref = getActivity().getSharedPreferences("User_Token", Context.MODE_PRIVATE);
        final String user_token = SharedPref.getString("uToken","");


        setUTCtime();

        //Setting Timestamp Data in JSON Object
        JSONObject timestamp = new JSONObject();
        try {
            timestamp.put("year",utcYear);
            timestamp.put("month",utcMonth);
            timestamp.put("date",utcDay);
            timestamp.put("hours",utcHour);
            timestamp.put("minutes",utcMinute);
            timestamp.put("seconds",utcSecond);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        //Creating JSON MAIN OBJECT
        JSONObject jsonVal = new JSONObject();
        try {
            jsonVal.put("lat",String.valueOf(lat));
            jsonVal.put("long", String.valueOf(lang));
            jsonVal.put("accuracy",String.valueOf(accu));
            jsonVal.put("timestamp",timestamp);

            Log.d("JSON OBject DATA SENT: ",jsonVal.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Calling Request API
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonVal, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Successfully Logged In
                Log.d("Response: ",response.toString());
                try {
                    if(response.getString("status").equals("200")){
                        Log.d("Responseeee: ",response.toString());
                        getBumpUsers();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Converting Error Response Into JSON Object
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {

                    String jsonError = new String(networkResponse.data);
                    // Print Error!
                    try {
                        JSONObject obj = new JSONObject(jsonError);
                        Log.d("String Error: ",jsonError);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+ user_token);
                return headers;
            }
        };


        Mysingleton.getInstance(getActivity()).addToRequestque(request);



    }

    private void setUTCtime() {


        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.d("DateUTC: ",outputFmt.format(time)) ;
        String utcTime = outputFmt.format(time);

        utcYear   = utcTime.substring(0,4);
        utcMonth  = utcTime.substring(5,7);
        utcDay    = utcTime.substring(8,10);
        utcHour   = utcTime.substring(11,13);
        utcMinute = utcTime.substring(14,16);
        utcSecond = utcTime.substring(17,19);

        Log.d("Year  :",utcYear);
        Log.d("Month  :",utcMonth);
        Log.d("Day  :",utcDay);
        Log.d("Hour  :",utcHour);
        Log.d("Minute  :",utcMinute);
        Log.d("Second  :",utcSecond);

    }

    private void getBumpUsers() {

        String url = "http://3.219.121.75:8080/api/bump/users";
        SharedPreferences SharedPref = getActivity().getSharedPreferences("User_Token", Context.MODE_PRIVATE);
        final String user_token = SharedPref.getString("uToken","");


        //Setting Timestamp Data in JSON Object
        JSONObject timestamp = new JSONObject();
        try {
            timestamp.put("year","2019");
            timestamp.put("month","09");
            timestamp.put("date","23");
            timestamp.put("hours","11");
            timestamp.put("minutes","57");
            timestamp.put("seconds","10");

        } catch (JSONException ex) {
            ex.printStackTrace();
        }


        //Calling Request API
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Successfully Logged In
                Log.d("Response: ",response.toString());
                try {
                    if(response.getString("status").equals("200")){
                        Log.d("Responseeee: ",response.toString());

                        usersList = response.getJSONArray("users");
                        if(usersList.length()>0) {

                            if(!isUListDialogOpen) {
                                openUserListDialog(usersList);
                            }else{

                                //clearing UserNames and add Again from JSON ARRAY
                                userNames.clear();
                                //Getting UserNames from JSON Array
                                for(int i=0; i<usersList.length(); i++){
                                    try {
                                        userNames.add(usersList.getJSONObject(i).getString("username"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                userListAdapter.notifyDataSetChanged();
                            }
                            //callingThreadToGetUserAgain();
                        }
                        else{
                            Toast.makeText(getActivity(), "No user Bumped..!", Toast.LENGTH_SHORT).show();

                        }
                        //openBumpDialog(response.getJSONArray("users").getJSONObject(0).getString("username"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Converting Error Response Into JSON Object
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {

                    String jsonError = new String(networkResponse.data);
                    // Print Error!
                    try {
                        JSONObject obj = new JSONObject(jsonError);
                        Log.d("String Error: ",jsonError);
                        //No Bumped User Found Error
                            startListening();
                        //callingThreadToGetUserAgain();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+ user_token);
                return headers;
            }
        };


        Mysingleton.getInstance(getActivity()).addToRequestque(request);


    }

    private void callingThreadToGetUserAgain() {

        final Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                threadCalltimes++;
                if(threadCalltimes>3) {
                    handler.removeCallbacks(this);
                    userslistAlertDialog.setCanceledOnTouchOutside(true);
                    Log.d("Handler: ","Thread Stops");
                } else{
                    Log.d("Handler: ","Hello Handler"+threadCalltimes);
                    getBumpUsers();
                   // userListAdapter.notifyDataSetChanged();
                    handler.postDelayed(this, 1000);
                }

            }
        };
        handler.postDelayed(r, 1000);

    }

    private void openUserListDialog(final JSONArray usersList) {

        isUListDialogOpen = true;
        callingThreadToGetUserAgain();

        //final ArrayList<String> userNames = new ArrayList<>();
        final ListView popup_users_listV;
        ConstraintLayout popup_user_listMainCl;
        EditText  popup_bumpUlist_searchEd;

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.popup_users_list, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        popup_bumpUlist_searchEd = (EditText)promptView.findViewById(R.id.popup_bumpUlist_searchEd);
        popup_users_listV = (ListView)promptView.findViewById(R.id.popup_users_listV);
        popup_user_listMainCl = (ConstraintLayout)promptView.findViewById(R.id.popup_user_listMainCl);

        //Getting UserNames from JSON Array
        for(int i=0; i<usersList.length(); i++){
            try {
                userNames.add(usersList.getJSONObject(i).getString("username"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        userListAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, userNames){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setGravity(Gravity.CENTER);

                return view;
            }
        };

        popup_users_listV.setAdapter(userListAdapter);


        //Linking Popup_social Components
        popup_users_listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBumpDialog(userNames.get(position));
            }
        });

        popup_user_listMainCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userslistAlertDialog.dismiss();
            }
        });


        //Searching Function coding
        popup_bumpUlist_searchEd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //When user changed the Text

                ArrayList<String> secondarr = new ArrayList<String>();

                for (int i = 0; i < userNames.size(); i++) {
                    String name = userNames.get(i);
                    if (name.contains(cs)) {
                        secondarr.add(name);
                    }
                }
                popup_users_listV.setAdapter(new ArrayAdapter<String>
                        (getActivity(), android.R.layout.simple_list_item_1, userNames));
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });


        //Show Alert Code
        builder.setView(promptView);
        userslistAlertDialog = builder.create();
        userslistAlertDialog.setCanceledOnTouchOutside(false);
        userslistAlertDialog.show();
        userslistAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.getWindow().setLayout(650, 700); //Controlling width and


      /*  WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(userslistAlertDialog.getWindow().getAttributes());
        lp.width = 500;
        lp.height = 700;
        lp.x=0;
        lp.y=-100;
        userslistAlertDialog.getWindow().setAttributes(lp);
*/

        userslistAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d("DIALOGGG: ","DISSMISSSs");
                //Registered Sensor Listener to start listening again
                startListening();
                Log.d("Sensor REGISTEREDDD: ","YESeahh");
                threadCalltimes=0;
                Log.d("Thread Reset: ","YEse "+threadCalltimes);
                isUListDialogOpen = false;
                Log.d("UserListDialog Reset: ","YEse ");

            }
        });

    }

    private void openBumpDialog(final String username) {

        //Open PopUP Clear Previous Data in Account Array
        accounts = new JSONArray();

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.popup_bump, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Linking Popup_social Components
        popBump_usernameTv  = (TextView)promptView.findViewById(R.id.popBump_usernameTv);
        popBump_crossIV     = (ImageView)promptView.findViewById(R.id.popBump_crossIV);
        popBump_mainImgIV   = (ImageView)promptView.findViewById(R.id.popBump_mainImgIV);
        popBump_phoneIV     = (ImageView)promptView.findViewById(R.id.popBump_phoneIV);
        popBump_facebookIV  = (ImageView)promptView.findViewById(R.id.popBump_facebookIV);
        popBump_instagramIV = (ImageView)promptView.findViewById(R.id.popBump_instagramIV);
        popBump_twitterIV   = (ImageView)promptView.findViewById(R.id.popBump_twitterIV);
        popBump_linkedinIV  = (ImageView)promptView.findViewById(R.id.popBump_linkedinIV);
        popBump_snapchatIV  = (ImageView)promptView.findViewById(R.id.popBump_snapchatIV);
        popBump_emailIV     = (ImageView)promptView.findViewById(R.id.popBump_emailIV);
        popBump_connectBt   = (Button)promptView.findViewById(R.id.popBump_connectBt);


        popBump_usernameTv.setText(username);
        popBump_crossIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bumpAlertDialog.dismiss();
            }
        });
        popBump_mainImgIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Log.d("Main: ","MainIMg With UName: "+username);
            }
        });

        popBump_phoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!phoneClicked){
                    popBump_phoneIV.setImageResource(R.drawable.dialer);
                    accounts.put("phone");
                    phoneClicked = true;
                }else {
                    popBump_phoneIV.setImageResource(R.drawable.dialerwhite);

                    try {
                        removeValueFromJsonArray("phone");
                        phoneClicked = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("Main: ","Phonee");
            }
        });

        popBump_facebookIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!facebookClicked){
                    popBump_facebookIV.setImageResource(R.drawable.facebook);
                    accounts.put("facebook");
                    facebookClicked = true;
                }else {
                    popBump_facebookIV.setImageResource(R.drawable.facebookwhite);
                    try {
                        removeValueFromJsonArray("facebook");
                        facebookClicked = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("Main: ","Facebook");
            }
        });

        popBump_instagramIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!instagramClicked){
                    popBump_instagramIV.setImageResource(R.drawable.instagram);
                    accounts.put("instagram");
                    instagramClicked = true;
                }else {
                    popBump_instagramIV.setImageResource(R.drawable.instagramwhite);
                    try {
                        removeValueFromJsonArray("instagram");
                        instagramClicked = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("Main: ","Instagram");

            }
        });

        popBump_twitterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!twitterClicked){
                    popBump_twitterIV.setImageResource(R.drawable.twitter);
                    accounts.put("twitter");
                    twitterClicked = true;
                }else {
                    popBump_twitterIV.setImageResource(R.drawable.twitterwhite);
                    try {
                        removeValueFromJsonArray("twitter");
                        twitterClicked = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("Main: ","Twiiter");


            }
        });

        popBump_linkedinIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!linkedinClicked){
                    popBump_linkedinIV.setImageResource(R.drawable.linkedin);
                    accounts.put("linkedin");
                    linkedinClicked = true;
                }else {
                    popBump_linkedinIV.setImageResource(R.drawable.linkedinwhite);
                    try {
                        removeValueFromJsonArray("linkedin");
                        linkedinClicked = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("Main: ","Linkedin");
            }
        });

        popBump_snapchatIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!snapchatClicked){
                    popBump_snapchatIV.setImageResource(R.drawable.snapchat);
                    accounts.put("snapchat");
                    snapchatClicked = true;
                }else {
                    popBump_snapchatIV.setImageResource(R.drawable.snapchatwhite);
                    try {
                        removeValueFromJsonArray("snapchat");
                        snapchatClicked = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("Main: ","Snapchat");
            }
        });

        popBump_emailIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!emailClicked){
                    popBump_emailIV.setImageResource(R.drawable.email);
                    accounts.put("email");
                    emailClicked = true;
                }else {
                    popBump_emailIV.setImageResource(R.drawable.emailwhite);
                    try {
                        removeValueFromJsonArray("email");
                        emailClicked = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("Main: ","Email");

            }
        });


        popBump_connectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneClicked     = false;
                emailClicked     = false;
                facebookClicked  = false;
                instagramClicked = false;
                twitterClicked   = false;
                linkedinClicked  = false;
                snapchatClicked  = false;
                Log.d("Account Added Arrayyy: ", String.valueOf(accounts.length()));
                bumpAlertDialog.dismiss();
                Log.d("Main: ","Request has sent Successfully..!");
                exchange_Request(username);
            }
        });



        //Show Alert Code
        builder.setView(promptView);
        bumpAlertDialog = builder.create();
        bumpAlertDialog.show();
        bumpAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //bu.getWindow().setLayout(650, 700); //Controlling width and

        /*WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(bumpAlertDialog.getWindow().getAttributes());
        lp.width = 950;
        lp.height = 720;
        lp.x=0;
        lp.y=-150;
        bumpAlertDialog.getWindow().setAttributes(lp);
*/

    }

    private void removeValueFromJsonArray(String value) throws JSONException {
        for (int i = 0; i < accounts.length(); i++) {
            if (accounts.get(i).equals(value)) {
                accounts.remove(i);
            }
        }

        Log.d("AccountRemoved: ",value);

    }

    private void exchange_Request(String username) {

        String url = "http://3.219.121.75:8080/api/bump/exchange_request";

        SharedPreferences SharedPref = getActivity().getSharedPreferences("User_Token", Context.MODE_PRIVATE);
        final String user_token = SharedPref.getString("uToken","");


        String targetEmail = username;
        //ArrayList<String> accountss = new ArrayList<String>();
        //accountss.add("facebook");
        //accountss.add("snapchat");
        //accountss.add("phone");
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("facebook");
        jsonArray.put("snapchat");
        jsonArray.put("phone");



        JSONObject jsonVal = new JSONObject();

        try {
            jsonVal.put("username",username);
            jsonVal.put("accounts",accounts);

            Log.d("Arrayyy: ",""+jsonVal.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonVal, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Successfully Logged In
                Log.d("Response: ",response.toString());
                try {
                    if(response.getString("status").equals("200")){
                        Log.d("Responseeee: ",response.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Converting Error Response Into JSON Object
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {

                    String jsonError = new String(networkResponse.data);
                    // Print Error!
                    try {
                        JSONObject obj = new JSONObject(jsonError);
                        Log.d("String Error: ",jsonError);
                        //Log.d("Json Error: ",obj.get("message").toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+ user_token);
                return headers;
            }
        };


        Mysingleton.getInstance(getActivity()).addToRequestque(request);

        //accounts.clear();
        //Toast.makeText(getActivity(), "After Sent: "+accounts.size(), Toast.LENGTH_SHORT).show();
    }

    private void locationMange() {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }


        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // Can't get location by any way
            Toast.makeText(getActivity(), "Can't get location..!", Toast.LENGTH_LONG).show();
        } else {

            // get location from GPS
            Toast.makeText(getActivity(), "Getting Location..!", Toast.LENGTH_SHORT).show();
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {


                    final double latitude = location.getLatitude();
                    final double longitude = location.getLongitude();

                    Log.d("User Lat: ", String.valueOf(latitude));
                    Log.d("User Long: ", String.valueOf(longitude));
                    Toast.makeText(getActivity(), "Location From GPS: " + "\nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                    double difLat = (25+(location.getAccuracy()/2) * 0.0002778)/30.87;
                    double difLong = (25+(location.getAccuracy()/2) * 0.0002778)/20.250;

                    double latMax  = latitude+difLat;
                    double latMin  = latitude-difLat;
                    double longMax = longitude+difLong;
                    double longMin = longitude-difLong;

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });


        }



    }


    //Google Map Api getLocation Coding and Methods
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length >0){
                     if(grantResults.length == PackageManager.PERMISSION_GRANTED){
                         Toast.makeText(getActivity(), "Location Permission Granted..!", Toast.LENGTH_SHORT).show();
                     }else if (grantResults.length == PackageManager.PERMISSION_DENIED){
                         Toast.makeText(getActivity(), "Location Permission Denied..!", Toast.LENGTH_SHORT).show();
                     }
                }
                break;

            case CAMERA_REQUEST_CODE:
                if(grantResults.length >0){
                    if(grantResults.length == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(getActivity(), "Camera Permission Granted..!", Toast.LENGTH_SHORT).show();
                    }else if (grantResults.length == PackageManager.PERMISSION_DENIED){
                        Toast.makeText(getActivity(), "Camera Permission Denied..!", Toast.LENGTH_SHORT).show();
                    }
                }
        break;
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
                   // Toast.makeText(getActivity(), "setting Location Params", Toast.LENGTH_SHORT).show();
                   Log.d("LOCATIONSSS: ","Lat: "+String.valueOf(location.getLatitude())+"    Long:  "+String.valueOf(location.getLatitude()));
                    userLat     = location.getLatitude();
                    userLong    = location.getLongitude();
                    userAcuracy = location.getAccuracy();
       //            sendBumpData(location.getLatitude(),location.getLongitude(),location.getAccuracy());
                }

            }
        };
    }

    private void calllFusedApi() {

        if(ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            return;
        }else {

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
           //Toast.makeText(getActivity(), "In CallFusedApi", Toast.LENGTH_SHORT).show();
        }
    }

    private void startListening(){
        sm.registerListener(shakeListner, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        Log.d("SensorStar: "," Start Listening");
    }

    private void stopListening(){
        sm.unregisterListener(shakeListner);
        Log.d("SensorStop: ","Stop Listening");
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible

        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (!isVisibleToUser) {
                Log.d("BUMPFragment", "Not visible anymore.  Stopping ShakeSensor");
                stopListening();
            }else{
                Log.d("BUMPFragment", "Again We Are Visible NOW STARTING ShakeSensor");
                startListening();

            }
        }
    }


    private void openMap() {

        Intent goMapAct = new Intent(getActivity(), MapsActivity.class);
        goMapAct.putExtra("lat",userLat);
        goMapAct.putExtra("lang",userLong);
        startActivity(goMapAct);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("onSTART: ","Called");
        }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume: ","Calledd");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("onPAUSE: ","Called");
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d("onSTOP: ","CALEDD");
    }



    public void CheckGpsStatus(){

        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
