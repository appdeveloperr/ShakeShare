package Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.usmansh.ingrumidreal.R;

import Classes.FriendsData;


public class FriendProfile extends Fragment {

    View view;

    boolean phone, facebook, instagram, twitter, linkedin, snapchat;
    String name, email;

    //Friend Profile Components Declaration
    ImageView frndProf_imgIV;
    TextView  frndProf_nameTv, frndProf_desTv;
    ImageView frndProf_emailIV, frndProf_phoneIV, frndProf_facebookIV, frndProf_instagramIV;
    ImageView frndProf_snapchatIV, frndProf_linkedinIV, frndProf_twitterIV, frndProf_unfriendIV;
    Button frndProf_msgBt;


    //Unfriend PopUP Components
    ConstraintLayout popUnfriend_mainCL;
    AlertDialog unfriendDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_friend_profile, container, false);

        //Recieving Data coming from previous fragment Friend List Adapter
       /* Bundle bundle = this.getArguments();
        if (bundle != null) {

            phone     = bundle.getBoolean("phone");
            facebook  = bundle.getBoolean("facebook");
            instagram = bundle.getBoolean("instagram");
            twitter   = bundle.getBoolean("twitter");
            linkedin  = bundle.getBoolean("linkedin");
            snapchat  = bundle.getBoolean("snapchat");
            name      = bundle.getString("name");
            email     = bundle.getString("email");
        }


        //Linking Components
        frndProf_unfriendIV   = (ImageView)view.findViewById(R.id.frndProf_unfriendIV);
        frndProf_imgIV        = (ImageView)view.findViewById(R.id.frndProf_imgIV);
        frndProf_phoneIV      = (ImageView)view.findViewById(R.id.frndProf_phoneIV);
        frndProf_facebookIV   = (ImageView)view.findViewById(R.id.frndProf_facebookIV);
        frndProf_instagramIV  = (ImageView)view.findViewById(R.id.frndProf_instagramIV);
        frndProf_twitterIV    = (ImageView)view.findViewById(R.id.frndProf_twitterIV);
        frndProf_linkedinIV   = (ImageView)view.findViewById(R.id.frndProf_linkedinIV);
        frndProf_snapchatIV   = (ImageView)view.findViewById(R.id.frndProf_snapchatIV);
        frndProf_emailIV      = (ImageView)view.findViewById(R.id.frndProf_emailIV);
        frndProf_nameTv       = (TextView)view.findViewById(R.id.frndProf_nameTv);
        frndProf_desTv        = (TextView)view.findViewById(R.id.frndProf_desTv);


        //Setting Data onto Components
        frndProf_nameTv.setText(name);
        frndProf_unfriendIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openUnfriendDialog();
            }
        });*/


        return view;
    }







    private void openUnfriendDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.popup_unfriend, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Linking Popup_social Components
        popUnfriend_mainCL = (ConstraintLayout)promptView.findViewById(R.id.popUnfriend_mainCL);




        popUnfriend_mainCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        /*lp.copyFrom(unfriendDialog.getWindow().getAttributes());
        l*p.width = 650;
        lp.height = 720;
        lp.x=0;
        lp.y=-150;
        unfriendDialog.getWindow().setAttributes(lp);*/



    }



}
