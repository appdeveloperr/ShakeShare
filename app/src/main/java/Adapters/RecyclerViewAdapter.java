package Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.usmansh.ingrumidreal.HomeScreenAct;
import com.project.usmansh.ingrumidreal.R;

import java.util.ArrayList;

import Classes.Data;
import Fragments.IngrumBump;
import Fragments.UserProfile;

public class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    public static final String TAG = "RecyclerViewAdapter";
    public static int selectOption;
    //public ArrayList<String> mNames = new ArrayList<>();
    //public ArrayList<String> mImageUrls = new ArrayList<>();
    public ArrayList<Data> dataImgPath = new ArrayList<>();

    public Context mContext;

    Fragment mFragment;
    Bundle mBundle;



   /* public RecyclerViewAdapter(Context mContext, ArrayList<String> mImageUrls, ArrayList<String> mNames ) {

        this.mContext = mContext;
        this.mNames = mNames;
        this.mImageUrls = mImageUrls;

    }*/



    public RecyclerViewAdapter(Context mContext, ArrayList<Data> data) {

        this.mContext = mContext;
        this.dataImgPath = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG,"onCreateViewHolder: called");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        Log.d(TAG,"onBindViewHolder: called");

        Data all = dataImgPath.get(position);
        Log.d("Image Path: ", String.valueOf(all.imageId));
       // viewHolder.name.setText(all.txt);
        viewHolder.image.setImageResource(all.imageId);
        /*Glide.with(mContext)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(viewHolder.image);
*/
        //viewHolder.name.setText(mNames.get(position));


        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.d(TAG,"onClick: clicked on an image: "+mNames.get(position));
               // Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
                fragmentJump(position);

            }
        });
    }




    private void fragmentJump(int position) {

        if(position==0) {
            mFragment = new IngrumBump();
        }else if(position==1){
            mFragment = new UserProfile();
        }else {
            mFragment = new IngrumBump();
        }
        mBundle = new Bundle();
        mBundle.putInt("key",position);
        mFragment.setArguments(mBundle);
      //  Log.d("Ye kiyaa haiii...: ", String.valueOf(position));
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
        return dataImgPath.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

       // CircleImageView image;
       ImageView image;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.frnd_profilepic);
            //name  = itemView.findViewById(R.id.name);



        }
    }
}
