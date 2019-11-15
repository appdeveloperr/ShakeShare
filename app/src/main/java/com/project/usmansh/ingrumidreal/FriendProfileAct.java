package com.project.usmansh.ingrumidreal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jgabrielfreitas.core.BlurImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Classes.FriendSocialMediaNames;
import Classes.Mysingleton;
import maes.tech.intentanim.CustomIntent;

import static Fragments.UserProfile.userSocial;

public class FriendProfileAct extends AppCompatActivity {


    boolean phone, email, facebook, instagram, twitter, linkedin, snapchat;
    String name, username;

    //Friend Profile Components Declaration
    ImageView frndProfAct_imgIV;
    TextView frndProfAct_nameTv, frndProfAct_desTv;
    ImageView frndProfAct_emailIV, frndProfAct_phoneIV, frndProfAct_facebookIV, frndProfAct_instagramIV;
    ImageView frndProfAct_snapchatIV, frndProfAct_linkedinIV, frndProfAct_twitterIV, frndProfAct_unfriendIV;
    Button frndProf_msgBt;
    BlurImageView frndProf_blurIV;
    FriendSocialMediaNames friendSocialMediaNames;

    //Unfriend PopUP Components
    ConstraintLayout popUnfriend_mainCL;
    AlertDialog unfriendDialog,friendSocialPopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);


        //Linking Components
        frndProfAct_unfriendIV  = (ImageView) findViewById(R.id.frndProf_unfriendIV);
        frndProfAct_imgIV       = (ImageView) findViewById(R.id.frndProf_imgIV);
        frndProfAct_phoneIV     = (ImageView) findViewById(R.id.frndProf_phoneIV);
        frndProfAct_facebookIV  = (ImageView) findViewById(R.id.frndProf_facebookIV);
        frndProfAct_instagramIV = (ImageView) findViewById(R.id.frndProf_instagramIV);
        frndProfAct_twitterIV   = (ImageView) findViewById(R.id.frndProf_twitterIV);
        frndProfAct_linkedinIV  = (ImageView) findViewById(R.id.frndProf_linkedinIV);
        frndProfAct_snapchatIV  = (ImageView) findViewById(R.id.frndProf_snapchatIV);
        frndProfAct_emailIV     = (ImageView) findViewById(R.id.frndProf_emailIV);
        frndProfAct_nameTv      = (TextView)  findViewById(R.id.frndProf_nameTv);
        frndProfAct_desTv       = (TextView)  findViewById(R.id.frndProf_desTv);
        frndProf_blurIV         = (BlurImageView)findViewById(R.id.frndProf_blurIV);
        frndProf_blurIV.setBlur(2);

        frndProfAct_unfriendIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openUnfriendDialog();
            }
        });


        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("username");
        phone = getIntent().getBooleanExtra("phone", false);
        email = getIntent().getBooleanExtra("email", false);
        facebook = getIntent().getBooleanExtra("facebook", false);
        instagram = getIntent().getBooleanExtra("instagram", false);
        twitter = getIntent().getBooleanExtra("twitter", false);
        linkedin = getIntent().getBooleanExtra("linkedin", false);
        snapchat = getIntent().getBooleanExtra("snapchat", false);


        frndProfAct_nameTv.setText(name);
        //Toast.makeText(this, "Name:"+username, Toast.LENGTH_SHORT).show();
        setAdapterSocialIcons(phone,email,facebook,instagram,twitter,linkedin,snapchat);

        getFriendProfileDetail(username);


        frndProfAct_phoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriendsSocialMediaPopUp("phone");
            }
        });


        frndProfAct_emailIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriendsSocialMediaPopUp("email");
            }
        });

        frndProfAct_facebookIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriendsSocialMediaPopUp("facebook");
            }
        });

        frndProfAct_instagramIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriendsSocialMediaPopUp("instagram");
            }
        });

        frndProfAct_twitterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriendsSocialMediaPopUp("twitter");
            }
        });

        frndProfAct_linkedinIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriendsSocialMediaPopUp("linkedin");
            }
        });

        frndProfAct_snapchatIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriendsSocialMediaPopUp("snapchat");
            }
        });
    }



    private void openUnfriendDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(FriendProfileAct.this);
        View promptView = layoutInflater.inflate(R.layout.popup_unfriend, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(FriendProfileAct.this);

        //Linking Popup_social Components
        popUnfriend_mainCL = (ConstraintLayout) promptView.findViewById(R.id.popUnfriend_mainCL);
        Button popUnfriend_disconnectBt = (Button)promptView.findViewById(R.id.popUnfriend_disconnectBt);

        popUnfriend_disconnectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Unfriend: ","Calling UnFriend");
                unfriendApiCalling(username);
            }
        });

        popUnfriend_mainCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Call Unfriend API
                unfriendDialog.dismiss();
            }
        });

        //Show Alert Code
        builder.setView(promptView);
        unfriendDialog = builder.create();
        unfriendDialog.show();
        unfriendDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.getWindow().setLayout(650, 700); //Controlling width and

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

    }


    private void setAdapterSocialIcons(boolean phone, boolean emaill, boolean facebook, boolean instagram,
                                       boolean twitter, boolean linkedin, boolean snapchat) {


        if (phone) {
            frndProfAct_phoneIV.setImageResource(R.drawable.dialer);
        } else {
            frndProfAct_phoneIV.setImageResource(R.drawable.dialerwhite);
        }


        if (emaill) {
            frndProfAct_emailIV.setImageResource(R.drawable.email);
        } else {
            frndProfAct_emailIV.setImageResource(R.drawable.emailwhite);
        }


        if (facebook) {
            frndProfAct_facebookIV.setImageResource(R.drawable.facebook);
        } else {
            frndProfAct_facebookIV.setImageResource(R.drawable.facebookwhite);
        }


        if (instagram) {
            frndProfAct_instagramIV.setImageResource(R.drawable.instagram);
        } else {
            frndProfAct_instagramIV.setImageResource(R.drawable.instagramwhite);
        }


        if (twitter) {

            Log.d("Fininin: ", String.valueOf(twitter));

            frndProfAct_twitterIV.setImageResource(R.drawable.twitter);
        } else {
            frndProfAct_twitterIV.setImageResource(R.drawable.twitterwhite);
        }


        if (linkedin) {
            frndProfAct_linkedinIV.setImageResource(R.drawable.linkedin);
        } else {
            frndProfAct_linkedinIV.setImageResource(R.drawable.linkedinwhite);
        }


        if (snapchat) {
            frndProfAct_snapchatIV.setImageResource(R.drawable.snapchat);
        } else {
            frndProfAct_snapchatIV.setImageResource(R.drawable.snapchatwhite);
        }


    }

    private void getFriendProfileDetail(String username) {

        String url = "http://3.219.121.75:8080/api/crud/friends_accounts";

        SharedPreferences SharedPref = getSharedPreferences("User_Token", Context.MODE_PRIVATE);
        final String user_token = SharedPref.getString("uToken","");

        JSONObject jsonval = new JSONObject();
        try {
            jsonval.put("username",username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,jsonval, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Getting Friends List
                try {
                    String status_Code = response.getString("status");
                    String message     = response.getString("message");
                    Log.d("Responsee: ","Status: "+status_Code+" and "+"Message: "+message);
                    JSONArray friendUnameList = response.getJSONArray("linked_user");
                    //getFriendList(friendList);
                    Log.d("Responseeeeee: ",friendUnameList.toString());
                    setFrndSocialMediaNames(friendUnameList);


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


        Mysingleton.getInstance(FriendProfileAct.this).addToRequestque(request);


    }


    private void setFrndSocialMediaNames(JSONArray friendUnameList) {

        try {
            JSONObject  frndUnameObj = friendUnameList.getJSONObject(0);

            friendSocialMediaNames = new FriendSocialMediaNames();
            friendSocialMediaNames.setPhone(frndUnameObj.getString("phone"));
            friendSocialMediaNames.setEmail(frndUnameObj.getString("email"));
            friendSocialMediaNames.setFacebook(frndUnameObj.getString("facebook"));
            friendSocialMediaNames.setInstagram(frndUnameObj.getString("instagram"));
            friendSocialMediaNames.setTwitter(frndUnameObj.getString("twitter"));
            friendSocialMediaNames.setLinkedin(frndUnameObj.getString("linkedin"));
            friendSocialMediaNames.setSnapchat(frndUnameObj.getString("snapchat"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void unfriendApiCalling(String username) {

        String urll = "http://3.219.121.75:8080/api/crud/remove_friend";

        SharedPreferences SharedPref = getSharedPreferences("User_Token", Context.MODE_PRIVATE);
        final String user_token = SharedPref.getString("uToken","");

        JSONObject jsonval = new JSONObject();
        try {
            jsonval.put("username",username);
            Log.d("UnFrndSending ", String.valueOf(jsonval));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urll,
                jsonval, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Successfull Response
                Log.d("Response: ",response.toString());

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


        Mysingleton.getInstance(FriendProfileAct.this).addToRequestque(request);


    }


    private void openFriendsSocialMediaPopUp(String imgid){

        LayoutInflater layoutInflater = LayoutInflater.from(FriendProfileAct.this);
        View promptView = layoutInflater.inflate(R.layout.popup_frnd_socialmedia, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(FriendProfileAct.this);

        //Declaring Components
        final ImageView friendSocialPopup_mainImgIV,friendSocialPopup_linkedinIV,friendSocialPopup_facebookIV,friendSocialPopup_instagramIV;
        ImageView friendSocialPopup_snapchatIV,friendSocialPopup_twitterIV,friendSocialPopup_phoneIV,friendSocialPopup_emailIV;
        final TextView friendSocialPopup_socialUrl;
        Button friendSocialPopup_okBt;
        ConstraintLayout friendSocialPopup_mainCL;

        //Linking Popup_social Components
        friendSocialPopup_mainImgIV     = (ImageView) promptView.findViewById(R.id.popup_frndSocial_mainImgIV);
        friendSocialPopup_linkedinIV    = (ImageView) promptView.findViewById(R.id.popup_frndSocial_linkedinIV);
        friendSocialPopup_facebookIV    = (ImageView) promptView.findViewById(R.id.popup_frndSocial_facebookIV);
        friendSocialPopup_twitterIV     = (ImageView) promptView.findViewById(R.id.popup_frndSocial_twitterIV);
        friendSocialPopup_snapchatIV    = (ImageView) promptView.findViewById(R.id.popup_frndSocial_snapchatIV);
        friendSocialPopup_instagramIV   = (ImageView) promptView.findViewById(R.id.popup_frndSocial_instagramIV);
        friendSocialPopup_emailIV       = (ImageView) promptView.findViewById(R.id.popup_frndSocial_emailIV);
        friendSocialPopup_phoneIV       = (ImageView) promptView.findViewById(R.id.popup_frndSocial_phoneIV);
        friendSocialPopup_socialUrl     = (TextView) promptView.findViewById(R.id.popup_frndSocial_urlTv);
        friendSocialPopup_okBt          = (Button) promptView.findViewById(R.id.popup_frndSocial_okBt);
        friendSocialPopup_mainCL        = (ConstraintLayout) promptView.findViewById(R.id.popup_frndSocial_mainCL);


            //SettingSelectedImage
        //Comparing Img Id to set Selected Image on Main ImageView
        if (imgid.equals("email")) {
           friendSocialPopup_mainImgIV.setImageResource(R.drawable.emailclr);
           friendSocialPopup_socialUrl.setText(friendSocialMediaNames.getEmail());

        } else if (imgid.equals("phone")) {
           friendSocialPopup_mainImgIV.setImageResource(R.drawable.phoneclr);
            friendSocialPopup_socialUrl.setText(friendSocialMediaNames.getPhone());
        } else if (imgid.equals("instagram")) {
            friendSocialPopup_mainImgIV.setImageResource(R.drawable.instagramclr);
            friendSocialPopup_socialUrl.setText(friendSocialMediaNames.getInstagram());
        } else if (imgid.equals("snapchat")) {
            friendSocialPopup_mainImgIV.setImageResource(R.drawable.snapchatclr);
            friendSocialPopup_socialUrl.setText(friendSocialMediaNames.getSnapchat());
        } else if (imgid.equals("facebook")) {
            friendSocialPopup_mainImgIV.setImageResource(R.drawable.facebookclr);
            friendSocialPopup_socialUrl.setText("www.facebook.com/"+friendSocialMediaNames.getFacebook());
        } else if (imgid.equals("linkedin")) {
            friendSocialPopup_mainImgIV.setImageResource(R.drawable.linkedinclr);
            friendSocialPopup_socialUrl.setText(friendSocialMediaNames.getLinkedin());
        } else if (imgid.equals("twitter")) {
            friendSocialPopup_mainImgIV.setImageResource(R.drawable.twitterclr);
            friendSocialPopup_socialUrl.setText(friendSocialMediaNames.getTwitter());
        }



        //Changing Image After Clicking on ImageView from Social Popup
        friendSocialPopup_linkedinIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendSocialPopup_mainImgIV.setImageResource(R.drawable.linkedinclr);
                friendSocialPopup_socialUrl.setText(friendSocialMediaNames.getLinkedin());
                //SetURL
            }
        });

        friendSocialPopup_twitterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendSocialPopup_mainImgIV.setImageResource(R.drawable.twitterclr);
                friendSocialPopup_socialUrl.setText(friendSocialMediaNames.getTwitter());
            }
        });

        friendSocialPopup_facebookIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               friendSocialPopup_mainImgIV.setImageResource(R.drawable.facebookclr);
                friendSocialPopup_socialUrl.setText(friendSocialMediaNames.getFacebook());

            }
        });

        friendSocialPopup_snapchatIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendSocialPopup_mainImgIV.setImageResource(R.drawable.snapchatclr);
                friendSocialPopup_socialUrl.setText(friendSocialMediaNames.getSnapchat());

            }
        });

        friendSocialPopup_instagramIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendSocialPopup_mainImgIV.setImageResource(R.drawable.instagramclr);
                friendSocialPopup_socialUrl.setText(friendSocialMediaNames.getInstagram());

            }
        });

        friendSocialPopup_phoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendSocialPopup_mainImgIV.setImageResource(R.drawable.phoneclr);
                friendSocialPopup_socialUrl.setText(friendSocialMediaNames.getPhone());

            }
        });

        friendSocialPopup_emailIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendSocialPopup_mainImgIV.setImageResource(R.drawable.emailclr);
                friendSocialPopup_socialUrl.setText(friendSocialMediaNames.getEmail());

            }
        });


        friendSocialPopup_okBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //OpenURL in Browser
                String url = friendSocialPopup_socialUrl.getText().toString().trim();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                friendSocialPopup.dismiss();
            }
        });

        friendSocialPopup_mainCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendSocialPopup.dismiss();
            }
        });


        //Show Alert Code
        builder.setView(promptView);
        friendSocialPopup = builder.create();
        friendSocialPopup.show();
        friendSocialPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // socialDialog.getWindow().setLayout(650, 700); //Controlling width and


    }
}

