package Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.usmansh.ingrumidreal.HomeScreenAct;
import com.project.usmansh.ingrumidreal.R;

import java.util.ArrayList;

import Classes.Data;
import Fragments.FriendProfile;
import Fragments.IngrumBump;
import Fragments.UserProfile;

public class FriendLisRecyclertAdapter extends RecyclerView.Adapter<FriendLisRecyclertAdapter.FrndViewHolder> {

    public static final String TAG = "RecyclerViewAdapter";
    public static int selectOption;
    public ArrayList<String> mNames = new ArrayList<>();
    //public ArrayList<String> mImageUrls = new ArrayList<>();

    public Context mContext;

    Fragment mFragment;
    Bundle mBundle;



    public FriendLisRecyclertAdapter(Context mContext, ArrayList<String> name) {

        this.mContext = mContext;
        this.mNames = name;
        //this.mImageUrls = mImageUrls;

    }


    @NonNull
    @Override
    public FrndViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG,"onCreateViewHolder: called");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friends_list_item, viewGroup, false);

        return new FrndViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FrndViewHolder viewHolder, final int position) {

        Log.d(TAG,"onBindViewHolder: called");

         viewHolder.name.setText(mNames.get(position));
        //viewHolder.image.setImageResource(all.imageId);
        /*Glide.with(mContext)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(viewHolder.image);
*/
        //viewHolder.name.setText(mNames.get(position));


        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Profile Pic", Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.friendlistCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display Clicked Friend Profile
                fragmentJump(position);
            }
        });

    }




    private void fragmentJump(int position) {

        /*if(position==0) {
            mFragment = new IngrumBump();
        }else if(position==1){
            mFragment = new UserProfile();
        }else {
            mFragment = new IngrumBump();
        }*/

        mFragment = new FriendProfile();
        mBundle = new Bundle();
        mBundle.putInt("key",position);
        mFragment.setArguments(mBundle);
        switchContent(R.id.fragment, mFragment);
    }


    public void switchContent(int id, Fragment fragment) {
        if (mContext == null)
            return;
        if (mContext instanceof HomeScreenAct) {
            HomeScreenAct mainActivity = (HomeScreenAct) mContext;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }

    }








    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class FrndViewHolder extends RecyclerView.ViewHolder{

        // CircleImageView image;
        ImageView image;
        TextView name;
        ConstraintLayout friendlistCL;

        public FrndViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.frnd_profilepic);
            name  = itemView.findViewById(R.id.frnd_nametv);
            friendlistCL = itemView.findViewById(R.id.friendlistCL);

        }
    }
}
