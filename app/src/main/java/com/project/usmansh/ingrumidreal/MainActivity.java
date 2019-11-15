package com.project.usmansh.ingrumidreal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import Classes.Mysingleton;
import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    EditText mainAct_uphoneEd,mainAct_upasswordEd,mainAct_uemailEd;
    Button mainAct_loginBt,mainAct_registerBt;
    TextView mainAct_forgetpasTv;
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //mainAct_uphoneEd     = (EditText)findViewById(R.id.mianAct_uphoneEd);
        mainAct_uemailEd     = (EditText)findViewById(R.id.mianAct_uemailEd);
        mainAct_upasswordEd = (EditText)findViewById(R.id.mainAct_upasswordEd);
        mainAct_loginBt     = (Button)findViewById(R.id.mainAct_Loginbt);
        mainAct_registerBt  = (Button)findViewById(R.id.mainAct_registerBt);
        mainAct_forgetpasTv = (TextView)findViewById(R.id.mianAct_forgetpasTv);

        checkUserLoginState();

        mainAct_registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,VerificationAct.class));
                CustomIntent.customType(MainActivity.this,"fadein-to-fadeout");
            }
        });



        mainAct_loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage("Internet Not Availale..!")
                            .setPositiveButton("OK", null).show();
                }else{
                    LoginWithVolley();
                }

            }

        });


        mainAct_forgetpasTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Go for forget password..!", Toast.LENGTH_SHORT).show();
            }
        });



    }



    private void checkUserLoginState() {


        SharedPreferences SharedPref = getSharedPreferences("User_Token", Context.MODE_PRIVATE);
        String user_token = SharedPref.getString("uToken","");

        if(!user_token.isEmpty() || !user_token.equals("")){

            //Going to HomeActivity
            Intent goHomeAct = new Intent(getApplicationContext(),TabHomeScreenAct.class);
            goHomeAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goHomeAct);
            finish();
            //startActivity(new Intent(MainActivity.this,HomeScreenAct.class));
            CustomIntent.customType(MainActivity.this,"fadein-to-fadeout");
        }


    }

    private void LoginWithVolley() {

        String url = "http://3.219.121.75:8080/api/auth/login/";
                         JSONObject jsonVal = new JSONObject();
        try {
            //jsonVal.put("phone_num",mainAct_uphoneEd.getText().toString().trim());
            jsonVal.put("email",mainAct_uemailEd.getText().toString().trim());
            jsonVal.put("password",mainAct_upasswordEd.getText().toString().trim());
            Log.d("Login Email: ",mainAct_uemailEd.getText().toString().trim());
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
                        //Saving User's Token into SharedPref
                        saveUserTokenInFile(response.getString("token"));
                        //Going to HomeActivity
                        Intent goHomeAct = new Intent(getApplicationContext(),TabHomeScreenAct.class);
                        goHomeAct.putExtra("email",mainAct_uemailEd.getText().toString().trim());
                        goHomeAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goHomeAct);
                        finish();
                        //startActivity(new Intent(MainActivity.this,HomeScreenAct.class));
                        CustomIntent.customType(MainActivity.this,"fadein-to-fadeout");

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

                       // Log.d("String Error: ",jsonError);
                        Log.d("Json Error: ",obj.get("message").toString());
                        Toast.makeText(MainActivity.this, "Error: "+obj.get("message").toString(), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });


        Mysingleton.getInstance(MainActivity.this).addToRequestque(request);
    }

    private void saveUserTokenInFile(String user_token) {

        SharedPreferences SharedPref = getSharedPreferences("User_Token", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = SharedPref.edit();
        editor.clear().apply();
        editor.putString("uToken", user_token);
        editor.apply();
        Log.d("Saved: ","Token Saved Success..!");


    }

    private void createUserAccountReteroFit(User user) {


        //Create Retrofit Instance

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://3.219.121.75:8080/api/auth/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        //get client and call object the request
        UserClient client =   retrofit.create(UserClient.class);
        Call<User> call =  client.createAccount(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                Toast.makeText(MainActivity.this, "Yeahh.! "+response.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Response: ",response.toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Something went wrong..!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
