package Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.usmansh.ingrumidreal.FriendProfileAct;
import com.project.usmansh.ingrumidreal.HomeScreenAct;
import com.project.usmansh.ingrumidreal.R;

import java.util.ArrayList;

import Classes.FriendsData;
import Fragments.FriendProfile;
import Fragments.Message_Chat;

public class FriendListViewAdapter extends BaseAdapter {


    ImageView frnd_list_phoneIV,frnd_list_emailIV,frnd_list_facebookIV;
    ImageView frnd_list_instagramIV,frnd_list_twitterIV,frnd_list_linkedinIV,frnd_list_snapchatIV;



    public ArrayList<String> Names = new ArrayList<>();
    public ArrayList<String> Msg = new ArrayList<>();
    public ArrayList<FriendsData> frndArr = new ArrayList<>();

    public Context mContext;

    Fragment mFragment;
    Bundle mBundle;


    public FriendListViewAdapter(Context mContext, ArrayList<FriendsData> frnd ) {

        this.mContext = mContext;
        this.frndArr  = frnd;
        //this.Names = names;
        //this.Msg = msg;

    }



    @Override
    public int getCount() {
        return frndArr.size();
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View view = new View (mContext);

        LayoutInflater inflat= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (convertView==null){
            view = inflat.inflate(R.layout.friends_list_item,null);
        } else{
             view = convertView;
        }


        final TextView nametv = (TextView) view.findViewById(R.id.frnd_nametv);

        final ConstraintLayout friendlistCL = (ConstraintLayout)view.findViewById(R.id.friendlistCL);
        ImageView image       = (ImageView) view.findViewById(R.id.frnd_profilepic);
        frnd_list_phoneIV     = (ImageView)view.findViewById(R.id.frnd_list_phone);
        frnd_list_emailIV     = (ImageView)view.findViewById(R.id.frnd_list_email);
        frnd_list_facebookIV  = (ImageView)view.findViewById(R.id.frnd_list_facebook);
        frnd_list_instagramIV = (ImageView)view.findViewById(R.id.frnd_list_instagram);
        frnd_list_twitterIV   = (ImageView)view.findViewById(R.id.frnd_list_twitter);
        frnd_list_linkedinIV  = (ImageView)view.findViewById(R.id.frnd_list_linkedin);
        frnd_list_snapchatIV  = (ImageView)view.findViewById(R.id.frnd_list_snapchat);




        nametv.setText(frndArr.get(position).getName());

        friendlistCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FAdapter: ","NAme: "+frndArr.get(position).getName());
//                fragmentJump(position,frndArr.get(position));
                Intent gofrndAct = new Intent(mContext, FriendProfileAct.class);
                gofrndAct.putExtra("name",frndArr.get(position).getName());
                gofrndAct.putExtra("username",frndArr.get(position).getUsername());
                gofrndAct.putExtra("phone",frndArr.get(position).isPhone());
                gofrndAct.putExtra("email",frndArr.get(position).isEmaill());
                gofrndAct.putExtra("facebook",frndArr.get(position).isFacebook());
                gofrndAct.putExtra("instagram",frndArr.get(position).isInstagram());
                gofrndAct.putExtra("twitter",frndArr.get(position).isTwitter());
                gofrndAct.putExtra("linkedin",frndArr.get(position).isLinkedin());
                gofrndAct.putExtra("snapchat",frndArr.get(position).isSnapchat());


                Log.d("Frjhjasdasd: ", frndArr.get(position).getUsername());
                Log.d("Friadd: ", String.valueOf(frndArr.get(position).isTwitter()));

                mContext.startActivity(gofrndAct);
            }
        });




        setAdapterSocialIcons(frndArr.get(position).isPhone(),frndArr.get(position).isEmaill(),frndArr.get(position).isFacebook(),
                frndArr.get(position).isInstagram(),frndArr.get(position).isTwitter(),frndArr.get(position).isLinkedin(),
                frndArr.get(position).isSnapchat());

        return view;

    }




    private void setAdapterSocialIcons(boolean phone, boolean emaill, boolean facebook, boolean instagram, boolean twitter, boolean linkedin, boolean snapchat) {



        if(phone){
            frnd_list_phoneIV.setImageResource(R.drawable.dialer);
        }else{
            frnd_list_phoneIV.setImageResource(R.drawable.dialerwhite);
        }


        if(emaill){
            frnd_list_emailIV.setImageResource(R.drawable.email);
        }else{
            frnd_list_emailIV.setImageResource(R.drawable.emailwhite);
        }


        if(facebook){
            frnd_list_facebookIV.setImageResource(R.drawable.facebook);
        }else{
            frnd_list_facebookIV.setImageResource(R.drawable.facebookwhite);
        }


        if(instagram){
            frnd_list_instagramIV.setImageResource(R.drawable.instagram);
        }else{
            frnd_list_instagramIV.setImageResource(R.drawable.instagramwhite);
        }


        if(twitter){
            frnd_list_twitterIV.setImageResource(R.drawable.twitter);
        }else{
            frnd_list_twitterIV.setImageResource(R.drawable.twitterwhite);
        }


        if(linkedin){
            frnd_list_linkedinIV.setImageResource(R.drawable.linkedin);
        }else{
            frnd_list_linkedinIV.setImageResource(R.drawable.linkedinwhite);
        }


        if(snapchat){
            frnd_list_snapchatIV.setImageResource(R.drawable.snapchat);
        }else{
            frnd_list_snapchatIV.setImageResource(R.drawable.snapchatwhite);
        }




    }

    private void fragmentJump(int position, FriendsData friendsData) {


        mFragment = new FriendProfile();
        mBundle = new Bundle();
        mBundle.putInt("key",position);
        mBundle.putBoolean("phone",friendsData.isPhone());
        mBundle.putBoolean("facebook",friendsData.isPhone());
        mBundle.putBoolean("instagram",friendsData.isPhone());
        mBundle.putBoolean("twitter",friendsData.isPhone());
        mBundle.putBoolean("linkedin",friendsData.isPhone());
        mBundle.putBoolean("snapchat",friendsData.isPhone());
        mBundle.putString("name",friendsData.getName());
        mBundle.putString("email", friendsData.getEmail());

        mFragment.setArguments(mBundle);
        switchContent(R.id.fragment, mFragment);
    }

    public void switchContent(int id, Fragment fragment) {
        if (mContext == null)
            return;
        if (mContext instanceof HomeScreenAct) {
            HomeScreenAct homeActivity = (HomeScreenAct) mContext;
            Fragment frag = fragment;
            homeActivity.switchContent(id, frag);
        }

    }


}
