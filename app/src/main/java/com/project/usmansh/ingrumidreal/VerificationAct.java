package com.project.usmansh.ingrumidreal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import Classes.Mysingleton;
import maes.tech.intentanim.CustomIntent;

public class VerificationAct extends AppCompatActivity {

    EditText veriAct_uphoneEd,veriAct_uemailEd;
    Button veriAct_verifyBt;
    ImageView veriAct_linkedinIv,veriAct_googleIV,veriAct_facebookIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);


        //veriAct_uphoneEd    = (EditText)findViewById(R.id.veriAct_uphoneEd);
        veriAct_uemailEd   = (EditText)findViewById(R.id.veriAct_uemailEd);
        veriAct_verifyBt   = (Button)findViewById(R.id.veriAct_verifyBt);
        /*veriAct_linkedinIv = (ImageView)findViewById(R.id.veriAct_linkedinIV);
        veriAct_googleIV   = (ImageView)findViewById(R.id.veriAct_googleIV);
        veriAct_facebookIV = (ImageView)findViewById(R.id.veriAct_facebookIV);
*/

        veriAct_verifyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   startActivity(new Intent(getApplicationContext(),RegistrationAct.class));
               // CustomIntent.customType(VerificationAct.this,"fadein-to-fadeout");

                if(TextUtils.isEmpty(veriAct_uemailEd.getText().toString())){
                    Toast.makeText(VerificationAct.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
                }else{
                    getOTPCallingApi(veriAct_uemailEd.getText().toString());
                }
            }
        });

    }




    private void getOTPCallingApi(String email) {


        String url = "http://3.219.121.75:8080/api/auth/register/step1";

        JSONObject jsonVal = new JSONObject();
        try {
            jsonVal.put("email",email);
            Log.d("Email: ",email);
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
                        Intent goHomeAct = new Intent(getApplicationContext(),CodeVerifyAct.class);
                        goHomeAct.putExtra("email",response.getString("email"));
                        startActivity(goHomeAct);
                        CustomIntent.customType(VerificationAct.this,"fadein-to-fadeout");

                    }else{
                        Toast.makeText(VerificationAct.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
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


        Mysingleton.getInstance(VerificationAct.this).addToRequestque(request);


    }


    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(VerificationAct.this,"fadein-to-fadeout");

    }
}
