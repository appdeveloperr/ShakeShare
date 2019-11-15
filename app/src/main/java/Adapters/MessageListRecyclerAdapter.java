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

import com.project.usmansh.ingrumidreal.HomeScreenAct;
import com.project.usmansh.ingrumidreal.R;

import java.util.ArrayList;

import Classes.Data;
import Fragments.IngrumBump;
import Fragments.Message_Chat;
import Fragments.UserProfile;

public class MessageListRecyclerAdapter extends RecyclerView.Adapter<MessageListRecyclerAdapter.MsgViewHolder> {

        public static final String TAG = "MessagetRecyclerAdapter";

        public ArrayList<String> Names = new ArrayList<>();
        public ArrayList<String> Msg = new ArrayList<>();

        public Context mContext;

        Fragment mFragment;
        Bundle mBundle;


    public MessageListRecyclerAdapter(Context mContext, ArrayList<String> names, ArrayList<String> msg ) {

        this.mContext = mContext;
        this.Names = names;
        this.Msg = msg;

    }


        @NonNull
        @Override
        public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                Log.d(TAG,"onCreateViewHolder: called");
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_list_item, viewGroup, false);

                return new MessageListRecyclerAdapter.MsgViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull MsgViewHolder viewHolder, final int position) {


                viewHolder.name.setText(Names.get(position));
                viewHolder.msg.setText(Msg.get(position));

                viewHolder.messagelistCL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                fragmentJump(position);
                        }
                });

        }


        private void fragmentJump(int position) {

                mFragment = new Message_Chat();
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
                return Names.size();
        }

        public static class MsgViewHolder extends RecyclerView.ViewHolder{

                // CircleImageView image;
                ImageView image;
                TextView name,msg;
                ConstraintLayout messagelistCL;

                public MsgViewHolder(@NonNull View itemView) {
                        super(itemView);

                        //image = itemView.findViewById(R.id.frnd_profilepic);
                        name  = itemView.findViewById(R.id.nametv);
                        msg  = itemView.findViewById(R.id.msgtv);
                        messagelistCL = itemView.findViewById(R.id.messagelistCL);

                }
        }
}
