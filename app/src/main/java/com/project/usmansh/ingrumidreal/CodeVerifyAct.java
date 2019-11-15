package com.project.usmansh.ingrumidreal;

import android.content.Intent;
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

public class CodeVerifyAct extends AppCompatActivity {

    Button codeVeriAct_verifyBt;
    EditText codeVeriAct_ucodeEd;
    String phoneNum,enterOtp;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verify);

//        phoneNum = getIntent().getStringExtra("phoneNum");
       email = getIntent().getStringExtra("email");

        codeVeriAct_verifyBt = (Button)findViewById(R.id.codeVeriAct_verifyBt);
        codeVeriAct_ucodeEd  = (EditText)findViewById(R.id.codeVeriAct_ucodeEd);

            codeVeriAct_verifyBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     enterOtp = codeVeriAct_ucodeEd.getText().toString();
                    validateOTPCallingAPI(enterOtp);

                }
            });

         }





    private void validateOTPCallingAPI(final String enterCode) {

        String url = "http://3.219.121.75:8080/api/auth/register/step2";

        JSONObject jsonVal = new JSONObject();
        try {
            jsonVal.put("email",email);
            jsonVal.put("otp",enterCode);
            Log.d("OTP: ",enterCode);
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
                        //Going to HomeActivity
                        Intent goHomeAct = new Intent(getApplicationContext(),RegistrationAct.class);
                        goHomeAct.putExtra("email",email);
                        goHomeAct.putExtra("otp",enterCode);
                        startActivity(goHomeAct);
                        //startActivity(new Intent(MainActivity.this,HomeScreenAct.class));
                        CustomIntent.customType(CodeVerifyAct.this,"fadein-to-fadeout");

                    }else{
                        Toast.makeText(CodeVerifyAct.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
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
        });


        Mysingleton.getInstance(CodeVerifyAct.this).addToRequestque(request);


    }
}
