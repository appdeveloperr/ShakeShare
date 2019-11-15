package com.project.usmansh.ingrumidreal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegistrationAct extends AppCompatActivity {

    EditText regAct_unameEd,regAct_uemailEd,regAct_upasswordEd,regAct_ufullnameEd;
    String phoneNum,otp;
    String email;

    Button regAct_signupBt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


       // phoneNum              = getIntent().getStringExtra("phoneNum");
        email                 = getIntent().getStringExtra("email");
        otp                   = getIntent().getStringExtra("otp");

        regAct_unameEd        = (EditText)findViewById(R.id.regAct_unameEd);
        //regAct_uemailEd       = (EditText)findViewById(R.id.regAct_uemailEd);
        regAct_upasswordEd    = (EditText)findViewById(R.id.regAct_upasswordEd);
        regAct_ufullnameEd    = (EditText)findViewById(R.id.regAct_ufullnameEd);
        regAct_signupBt       = (Button)findViewById(R.id.regAct_signupBt);



        regAct_signupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Getting Registration Data from Input Fields
                String uName     = regAct_unameEd.getText().toString().trim();
                //String uEmail    = regAct_uemailEd.getText().toString().trim();
                String uPassword = regAct_upasswordEd.getText().toString().trim();
                String ufullName = regAct_ufullnameEd.getText().toString().trim();


                if(uName.isEmpty()){
                    Toast.makeText(RegistrationAct.this, "Error: Enter UserName", Toast.LENGTH_SHORT).show();
                }else if(uName.length() < 6){
                    Toast.makeText(RegistrationAct.this, "Error: User Name must contain at least 6 characters ", Toast.LENGTH_SHORT).show();
                }else if(uPassword.isEmpty()){
                    Toast.makeText(RegistrationAct.this, "Error: Enter Password", Toast.LENGTH_SHORT).show();
                }else if(uPassword.length() < 6){
                    Toast.makeText(RegistrationAct.this, "Error: Password must contain at least 6 characters ", Toast.LENGTH_SHORT).show();
                }else if(ufullName.isEmpty()){
                    Toast.makeText(RegistrationAct.this, "Error: Enter Full Name", Toast.LENGTH_SHORT).show();
                }else if(ufullName.length() < 6){
                    Toast.makeText(RegistrationAct.this, "Error: Full Name must contain at least 6 characters ", Toast.LENGTH_SHORT).show();
                }else{
                    createAccountWithVolley();
                }

            }
        });

    }



    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(RegistrationAct.this,"fadein-to-fadeout");

    }




    private void createAccountWithVolley() {

        saveUserNameInSPref(regAct_unameEd.getText().toString().trim());
        String url = "http://3.219.121.75:8080/api/auth/register/step3";

        //Creating JSON object with user's data
        JSONObject jsonVal = new JSONObject();
        try {
            jsonVal.put("email",email);
            jsonVal.put("otp",otp);
            jsonVal.put("name",regAct_ufullnameEd.getText().toString().trim());
            jsonVal.put("username",regAct_unameEd.getText().toString().trim());
            jsonVal.put("password",regAct_upasswordEd.getText().toString().trim());
            //jsonVal.put("phone_num",phoneNum);
            Log.d("DATA SENT: ", String.valueOf(jsonVal));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonVal, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Successfully Logged In
                Log.d("Response: ",response.toString());
                try {

                    if(response.getString("status").equals("true")){
                        Toast.makeText(RegistrationAct.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                       //Going to LoginActivity
                        Intent goHomeAct = new Intent(getApplicationContext(),MainActivity.class);
                        goHomeAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goHomeAct);
                        finish();
                        //startActivity(new Intent(MainActivity.this,HomeScreenAct.class));
                        CustomIntent.customType(RegistrationAct.this,"fadein-to-fadeout");
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
                        Toast.makeText(RegistrationAct.this, "Error: "+obj.get("message").toString(), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });


        Mysingleton.getInstance(RegistrationAct.this).addToRequestque(request);



    }


    private void saveUserNameInSPref(String uname) {

        SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
        editor.putString("username", uname);
        editor.apply();
        Log.d("USNAMEEE: ","SAVEDDD:  "+uname);
    }
}
