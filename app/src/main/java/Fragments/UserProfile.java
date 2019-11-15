package Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.LocaleDisplayNames;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.jgabrielfreitas.core.BlurImageView;
import com.project.usmansh.ingrumidreal.FriendProfileAct;
import com.project.usmansh.ingrumidreal.HomeScreenAct;
import com.project.usmansh.ingrumidreal.MainActivity;
import com.project.usmansh.ingrumidreal.R;
import com.project.usmansh.ingrumidreal.TabHomeScreenAct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import Classes.Mysingleton;
import Classes.UserSocial;
import maes.tech.intentanim.CustomIntent;

import static android.app.Activity.RESULT_OK;


public class UserProfile extends Fragment {

    private static final int MY_CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;


    View view;
    ImageView userpProf_editIv, userProf_twitterIV, userProf_phoneIV, userProf_emailIV, userProf_linkedinIV;
    ImageView userProf_facebookIV, userProf_instagramIV, userProf_snapchatIV, userpProf_socialEditIV,userpProf_logoutIV;

    //Social Popup component Declaration
    ImageView popupSoc_mainImgIV, popSocial_linkedinIV, popSocial_twitterIV, popSocial_facebookIV;
    ImageView popSocial_snapchatIV, popSocial_phoneIV, popSocial_emailIV, popSocial_instagramIV;
    TextView popSocial_socialUrl;
    Button popSocial_okBt;
    JSONArray accounts = new JSONArray();
    JSONArray usernames = new JSONArray();

    JSONArray accountsNew = new JSONArray();
    JSONArray usernamesNew = new JSONArray();
    String selectAccount = "";
    String previousUname = "";

    AlertDialog socialDialog, socialEditDialog;
    ConstraintLayout popup_social_mainCL;
    public static UserSocial userSocial;

    //EditProfile popup Components
    ImageView popup_editProf_mainIV;
    BlurImageView userpProf_blurIV;
    TextView userpProf_nameTv;



    //Edite Social Media PopUp
    String phone, facebook, instagram, twitter, linkedin, snapchat, email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        userpProf_logoutIV     = (ImageView) view.findViewById(R.id.userpProf_logoutIV);
        userpProf_socialEditIV = (ImageView) view.findViewById(R.id.userpProf_socialEditIV);
        userpProf_editIv       = (ImageView) view.findViewById(R.id.userpProf_editIv);
        userProf_twitterIV     = (ImageView) view.findViewById(R.id.userProf_twitterIV);
        userProf_linkedinIV    = (ImageView) view.findViewById(R.id.userProf_linkedinIV);
        userProf_facebookIV    = (ImageView) view.findViewById(R.id.userpProf_facebookIV);
        userProf_instagramIV   = (ImageView) view.findViewById(R.id.userProf_instagramIV);
        userProf_snapchatIV    = (ImageView) view.findViewById(R.id.userProf_snapchatIV);
        userProf_phoneIV       = (ImageView) view.findViewById(R.id.userpProf_phoneIV);
        userProf_emailIV       = (ImageView) view.findViewById(R.id.userpProf_emailIV);
        userpProf_nameTv       = (TextView)  view.findViewById(R.id.userpProf_nameTv);
        userpProf_blurIV       = (BlurImageView) view.findViewById(R.id.userpProf_blurIV);

        userpProf_blurIV.setBlur(2);
        getUserProfileDetails();

        userpProf_nameTv.setText(TabHomeScreenAct.username);
        userpProf_socialEditIV.setVisibility(View.INVISIBLE);
        userpProf_logoutIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserLogOutState();
            }
        });

        userpProf_socialEditIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMediaEditPopUp();
            }
        });



        userpProf_editIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openEditProDialog();
            }
        });


        userProf_twitterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMediaDialog("twitter");
            }
        });

        userProf_linkedinIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMediaDialog("linkedin");
            }
        });

        userProf_instagramIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMediaDialog("instagram");
            }
        });


        userProf_snapchatIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMediaDialog("snapchat");
            }
        });

        userProf_facebookIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMediaDialog("facebook");
            }
        });


        userProf_phoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMediaDialog("phone");
            }
        });

        userProf_emailIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMediaDialog("email");
            }
        });


        return view;
    }


    private void getUserProfileDetails() {

        String url = "http://3.219.121.75:8080/api/settings/accounts";

        SharedPreferences SharedPref = getActivity().getSharedPreferences("User_Token", Context.MODE_PRIVATE);
        final String user_token = SharedPref.getString("uToken", "");

        JSONObject jsonVal = new JSONObject();
        try {
            jsonVal.put("token", user_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response: ", response.toString());

                try {
                    JSONObject jsonObject = response.getJSONObject("user");
                    setSocialData(jsonObject);
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

                        Log.d("String Error: ", jsonError);
                        Log.d("Json Error: ", obj.get("message").toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + user_token);
                return headers;
            }
        };


        Mysingleton.getInstance(getActivity()).addToRequestque(request);

    }


    private void setSocialData(JSONObject jsonObject) {

        userSocial = new UserSocial();
        try {
            userSocial.setPhone(jsonObject.getString("phone"));
            userSocial.setEmail(jsonObject.getString("email"));
            userSocial.setFacebook(jsonObject.getString("facebook"));
            userSocial.setInstagram(jsonObject.getString("instagram"));
            userSocial.setTwitter(jsonObject.getString("twitter"));
            userSocial.setLinkedin(jsonObject.getString("linkedin"));
            userSocial.setSnapchat(jsonObject.getString("snapchat"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void openEditProDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.dialog_view_editprofile, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Declaring socialPopUp components Valriable
        Button popup_editProf_changePicBt;

        popup_editProf_mainIV      = (ImageView)promptView.findViewById(R.id.popup_editProf_mainIV);
        popup_editProf_changePicBt = (Button) promptView.findViewById(R.id.popup_editProf_changePicBt);


        popup_editProf_changePicBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Opening Camera
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, MY_CAMERA_REQUEST);
                }
            }
        });


        //Show Alert Code
        builder.setView(promptView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //alertDialog.getWindow().setLayout(650, 800); //Controlling width and

        /*WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = 650;
        lp.height = 1000;
        lp.x=0;
        lp.y=-80;
        alertDialog.getWindow().setAttributes(lp);*/

    }

    private void openSocialMediaDialog(String imgid) {


        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.popup_social_media, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Linking Popup_social Components
        popupSoc_mainImgIV = (ImageView) promptView.findViewById(R.id.popupSoc_mainImgIV);
        popSocial_linkedinIV = (ImageView) promptView.findViewById(R.id.popSocial_linkedinIV);
        popSocial_facebookIV = (ImageView) promptView.findViewById(R.id.popSocial_facebookIV);
        popSocial_twitterIV = (ImageView) promptView.findViewById(R.id.popSocial_twitterIV);
        popSocial_snapchatIV = (ImageView) promptView.findViewById(R.id.popSocial_snapchatIV);
        popSocial_instagramIV = (ImageView) promptView.findViewById(R.id.popSocial_instagramIV);
        popSocial_emailIV = (ImageView) promptView.findViewById(R.id.popSocial_emailIV);
        popSocial_phoneIV = (ImageView) promptView.findViewById(R.id.popSocial_phoneIV);
        popSocial_socialUrl = (TextView) promptView.findViewById(R.id.popSocial_urlTv);
        popSocial_okBt = (Button) promptView.findViewById(R.id.popSocial_okBt);
        popup_social_mainCL = (ConstraintLayout) promptView.findViewById(R.id.popup_social_mainCL);

        //Comparing Img Id to set Selected Image on Main ImageView
        if (imgid.equals("email")) {
            popupSoc_mainImgIV.setImageResource(R.drawable.emailclr);
            popSocial_socialUrl.setText(userSocial.getEmail());
            previousUname = userSocial.getEmail();
            //previousUname = popSocial_socialUrl.getText().toString();
            selectAccount = "email";
        } else if (imgid.equals("phone")) {
            popupSoc_mainImgIV.setImageResource(R.drawable.phoneclr);
            popSocial_socialUrl.setText(userSocial.getPhone());
            previousUname = userSocial.getPhone();
           // previousUname = popSocial_socialUrl.getText().toString();
            selectAccount = "phone";

        } else if (imgid.equals("instagram")) {
            popupSoc_mainImgIV.setImageResource(R.drawable.instagramclr);
            popSocial_socialUrl.setText(userSocial.getInstagram());
            previousUname = userSocial.getInstagram();
           // previousUname = popSocial_socialUrl.getText().toString();
            selectAccount = "instagram";

        } else if (imgid.equals("snapchat")) {
            popupSoc_mainImgIV.setImageResource(R.drawable.snapchatclr);
            popSocial_socialUrl.setText(userSocial.getSnapchat());
            previousUname = userSocial.getSnapchat() ;
            //previousUname = popSocial_socialUrl.getText().toString();
            selectAccount = "snapchat";

        } else if (imgid.equals("facebook")) {
            popupSoc_mainImgIV.setImageResource(R.drawable.facebookclr);
            popSocial_socialUrl.setText(userSocial.getFacebook());
            previousUname = userSocial.getFacebook();
            //previousUname = popSocial_socialUrl.getText().toString();
            selectAccount = "facebook";

        } else if (imgid.equals("linkedin")) {
            popupSoc_mainImgIV.setImageResource(R.drawable.linkedinclr);
            popSocial_socialUrl.setText(userSocial.getLinkedin());
            previousUname = userSocial.getLinkedin();
            //previousUname = popSocial_socialUrl.getText().toString();
            selectAccount = "linkedin";

        } else if (imgid.equals("twitter")) {
            popupSoc_mainImgIV.setImageResource(R.drawable.twitterclr);
            popSocial_socialUrl.setText(userSocial.getTwitter());
            previousUname = userSocial.getTwitter();
           // previousUname = popSocial_socialUrl.getText().toString();
            selectAccount = "twitter";
        }

        Log.d("Social Media Selected: ","Is: "+previousUname);

        //Changing Image After Clicking on ImageView from Social Popup
        popSocial_linkedinIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSoc_mainImgIV.setImageResource(R.drawable.linkedinclr);
                compareSocialAccountsUrls(previousUname,popSocial_socialUrl.getText().toString(),selectAccount);
                popSocial_socialUrl.setText(userSocial.getLinkedin());
                previousUname = popSocial_socialUrl.getText().toString();
                selectAccount = "linkedin";

            }
        });

        popSocial_twitterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSoc_mainImgIV.setImageResource(R.drawable.twitterclr);
                compareSocialAccountsUrls(previousUname,popSocial_socialUrl.getText().toString(),selectAccount);
                popSocial_socialUrl.setText(userSocial.getTwitter());
                previousUname = popSocial_socialUrl.getText().toString();
                selectAccount = "twitter";

            }
        });

        popSocial_facebookIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSoc_mainImgIV.setImageResource(R.drawable.facebookclr);
                compareSocialAccountsUrls(previousUname,popSocial_socialUrl.getText().toString(),selectAccount);
                popSocial_socialUrl.setText(userSocial.getFacebook());
                previousUname = popSocial_socialUrl.getText().toString();
                selectAccount = "facebook";

            }
        });

        popSocial_snapchatIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSoc_mainImgIV.setImageResource(R.drawable.snapchatclr);
                compareSocialAccountsUrls(previousUname,popSocial_socialUrl.getText().toString(),selectAccount);
                popSocial_socialUrl.setText(userSocial.getSnapchat());
                previousUname = popSocial_socialUrl.getText().toString();
                selectAccount = "snapchat";

            }
        });

        popSocial_instagramIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSoc_mainImgIV.setImageResource(R.drawable.instagramclr);
                compareSocialAccountsUrls(previousUname,popSocial_socialUrl.getText().toString(),selectAccount);
                popSocial_socialUrl.setText(userSocial.getInstagram());
                previousUname = popSocial_socialUrl.getText().toString();
                selectAccount = "instagram";

            }
        });

        popSocial_phoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSoc_mainImgIV.setImageResource(R.drawable.phoneclr);
                compareSocialAccountsUrls(previousUname,popSocial_socialUrl.getText().toString(),selectAccount);
                popSocial_socialUrl.setText(userSocial.getPhone());
                previousUname = popSocial_socialUrl.getText().toString();
                selectAccount = "phone";

            }
        });

        popSocial_emailIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSoc_mainImgIV.setImageResource(R.drawable.emailclr);
                compareSocialAccountsUrls(previousUname,popSocial_socialUrl.getText().toString(),selectAccount);
                popSocial_socialUrl.setText(userSocial.getEmail());
                previousUname = popSocial_socialUrl.getText().toString();
                selectAccount = "email";

            }
        });


        popSocial_okBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                compareSocialAccountsUrls(previousUname,popSocial_socialUrl.getText().toString(),selectAccount);
                socialDialog.dismiss();
            }
        });

        popup_social_mainCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socialDialog.dismiss();
            }
        });


        //Show Alert Code
        builder.setView(promptView);
        socialDialog = builder.create();
        socialDialog.show();
        socialDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // socialDialog.getWindow().setLayout(650, 700); //Controlling width and


    }

    private void compareSocialAccountsUrls(String previousUname, String newName,String selectAcount) {


        Log.d("URLssssssssssS: ","previous: "+previousUname+"   New: "+newName+" Account: "+selectAcount);

        if(!previousUname.equalsIgnoreCase(newName)){

            Log.d("API: ","Calling APi");
            updateUserSocialAccounts(newName,selectAcount);
        }
        else{
            Log.d("API: ","NOT Calling APi");

        }

    }


    private void openSocialMediaEditPopUp() {


        //Declaring socialPopUp components Valriable


        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.popup_socialmedia_edit, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Linking Components
        final EditText popup_socialEd_phoneEd, popup_socialEd_facebookEd, popup_socialEd_instagramEd, popup_socialEd_twitterEd;
        final EditText popup_socialEd_linkedinEd, popup_socialEd_snapchatEd, popup_socialEd_emailEd;
        Button popup_socialEd_saveBt;

        popup_socialEd_phoneEd = (EditText) promptView.findViewById(R.id.popup_socialEd_phoneEd);
        popup_socialEd_facebookEd = (EditText) promptView.findViewById(R.id.popup_socialEd_facebookEd);
        popup_socialEd_instagramEd = (EditText) promptView.findViewById(R.id.popup_socialEd_instagramEd);
        popup_socialEd_twitterEd = (EditText) promptView.findViewById(R.id.popup_socialEd_twitterEd);
        popup_socialEd_linkedinEd = (EditText) promptView.findViewById(R.id.popup_socialEd_linkedinEd);
        popup_socialEd_snapchatEd = (EditText) promptView.findViewById(R.id.popup_socialEd_snapchatEd);
        popup_socialEd_emailEd = (EditText) promptView.findViewById(R.id.popup_socialEd_emailEd);
        popup_socialEd_saveBt = (Button) promptView.findViewById(R.id.popup_socialEd_saveBt);


        popup_socialEd_phoneEd.setText(userSocial.getPhone());
        popup_socialEd_facebookEd.setText(userSocial.getFacebook());
        popup_socialEd_instagramEd.setText(userSocial.getInstagram());
        popup_socialEd_twitterEd.setText(userSocial.getTwitter());
        popup_socialEd_linkedinEd.setText(userSocial.getLinkedin());
        popup_socialEd_snapchatEd.setText(userSocial.getSnapchat());
        popup_socialEd_emailEd.setText("Email");


        popup_socialEd_saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone = popup_socialEd_phoneEd.getText().toString().trim();
                facebook = popup_socialEd_facebookEd.getText().toString().trim();
                instagram = popup_socialEd_instagramEd.getText().toString().trim();
                twitter = popup_socialEd_twitterEd.getText().toString().trim();
                linkedin = popup_socialEd_linkedinEd.getText().toString().trim();
                snapchat = popup_socialEd_snapchatEd.getText().toString().trim();
                email = popup_socialEd_emailEd.getText().toString().trim();

                setUserSocialDataInJSON(phone, facebook, instagram, twitter, linkedin, snapchat, email);

            }
        });


        //Show Alert Code
        builder.setView(promptView);
        socialEditDialog = builder.create();
        socialEditDialog.show();
        socialEditDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //alertDialog.getWindow().setLayout(650, 800); //Controlling width and

        /*WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(socialEditDialog.getWindow().getAttributes());
        lp.width = 650;
        lp.height = 1000;
        lp.x=0;
        lp.y=-100;
        socialEditDialog.getWindow().setAttributes(lp);
*/


    }


    private void setUserSocialDataInJSON(String phone, String facebook, String instagram,
                                         String twitter, String linkedin, String snapchat, String email) {


        //Setting JSON Accounts Array
        accounts.put("Phone");
        accounts.put("Email");
        accounts.put("Facebook");
        accounts.put("Instagram");
        accounts.put("Twitter");
        accounts.put("Linkedin");
        accounts.put("Snapchat");

        //Settins JSON Username Array
        usernames.put(phone);
        usernames.put(email);
        usernames.put(facebook);
        usernames.put(instagram);
        usernames.put(twitter);
        usernames.put(linkedin);
        usernames.put(snapchat);

        Log.d("Accounts ARR: ", String.valueOf(accounts.length()));
        Log.d("Usernames ARR: ", String.valueOf(usernames.length()));
        Log.d("API: ", "Calling API");

        //Calling API update User Social Accounts Data
       // updateUserSocialAccounts();

    }


    private void updateUserSocialAccounts(String newUName, String selectAccount) {
//        socialEditDialog.dismiss();

        String url = "http://3.219.121.75:8080/api/settings/accounts";

        SharedPreferences SharedPref = getActivity().getSharedPreferences("User_Token", Context.MODE_PRIVATE);
        final String user_token = SharedPref.getString("uToken", "");


        /*JSONArray jsonArray = new JSONArray();
        jsonArray.put("facebook");
        jsonArray.put("snapchat");
        jsonArray.put("phone");
        */

        accounts.put(selectAccount);
        usernames.put(newUName);

        JSONObject jsonVal = new JSONObject();

        try {
            jsonVal.put("accounts", accounts);
            jsonVal.put("usernames", usernames);
            Log.d("Arrayy GOINGG: ", "" + jsonVal.toString());
            accounts = new JSONArray();
            usernames = new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonVal, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Successfully Logged In
                Log.d("Response: ", response.toString());
                try {
                    if (response.getString("status").equals("200")) {
                        Log.d("Responseeee: ", response.toString());
                        getUserProfileDetails();
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
                        Log.d("String Error: ", jsonError);
                        //Log.d("Json Error: ",obj.get("message").toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + user_token);
                return headers;
            }
        };


        Mysingleton.getInstance(getActivity()).addToRequestque(request);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_SHORT).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, MY_CAMERA_REQUEST);
            } else {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == MY_CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            popup_editProf_mainIV.setImageBitmap(imageBitmap);

            Uri tempUri = getImageUri(getActivity(), imageBitmap);
            Log.d("DATATTTT: ","Chek: "+imageBitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();
            String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

            Log.d("Encoded Image: ","CODE: "+encodedImage);
            UploadImageStringOnServer(encodedImage);

        }

    }



    private void UploadImageStringOnServer(String encodedImage) {

        String urll = "http://3.219.121.75:8080/api/settings/image";

        SharedPreferences SharedPref = getActivity().getSharedPreferences("User_Token", Context.MODE_PRIVATE);
        final String user_token = SharedPref.getString("uToken","");

        JSONObject jsonval = new JSONObject();
        try {
            jsonval.put("image",encodedImage);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urll, jsonval, new com.android.volley.Response.Listener<JSONObject>() {
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


        Mysingleton.getInstance(getActivity()).addToRequestque(request);




    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /*public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }*/


    private void setUserLogOutState() {


        SharedPreferences SharedPref = getActivity().getSharedPreferences("User_Token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SharedPref.edit();
        editor.clear().apply();
        editor.putString("uToken", "");
        editor.apply();
        Log.d("Saved: ","Token Logout Success..!");

            //Going to HomeActivity
            Intent goHomeAct = new Intent(getActivity(), MainActivity.class);
            goHomeAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goHomeAct);
            getActivity().finish();
            //startActivity(new Intent(MainActivity.this,HomeScreenAct.class));
            CustomIntent.customType(getActivity(),"fadein-to-fadeout");
        }


    }

