package Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.usmansh.ingrumidreal.HomeScreenAct;
import com.project.usmansh.ingrumidreal.R;

import java.util.ArrayList;

import Fragments.Message_Chat;

public class MessageListViewAdapter extends BaseAdapter {


    public static final String TAG = "MessagetRecyclerAdapter";

    public ArrayList<String> Names = new ArrayList<>();
    public ArrayList<String> Msg = new ArrayList<>();

    public Context mContext;

    Fragment mFragment;
    Bundle mBundle;


    public MessageListViewAdapter (Context mContext, ArrayList<String> names, ArrayList<String> msg ) {

        this.mContext = mContext;
        this.Names = names;
        this.Msg = msg;

    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View view = new View (mContext);

        LayoutInflater inflat= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView==null){
            view = inflat.inflate(R.layout.message_list_item,null);
        } else{
            view = convertView;
        }


        TextView name  = (TextView) view.findViewById(R.id.nametv);
        TextView msg  =  (TextView)view.findViewById(R.id.msgtv);
        ConstraintLayout messagelistCL = (ConstraintLayout)view.findViewById(R.id.messagelistCL);

        name.setText(Names.get(position));
        msg.setText(Msg.get(position));
        messagelistCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentJump(position);
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return Names.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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


}
