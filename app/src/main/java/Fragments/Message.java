package Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.project.usmansh.ingrumidreal.R;

import java.util.ArrayList;

import Adapters.FriendLisRecyclertAdapter;
import Adapters.MessageListRecyclerAdapter;
import Adapters.MessageListViewAdapter;

public class Message extends Fragment {



    private static Message INSTANCE = null;

    ListView messageListView;
    View view;


    MessageListViewAdapter messageListRecyclerAdapter;
    ArrayList<String> Names = new ArrayList<>();
    ArrayList<String> Msg = new ArrayList<>();


    public Message() {
        // Required empty public constructor
    }

    public static Message getInstance() {

        if (INSTANCE == null)
            INSTANCE = new Message();
        return INSTANCE;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_message, container, false);



        Names.add("Usman");
        Names.add("Fahad");
        Names.add("Ali");
        Names.add("Bilal");
        Names.add("Umar");

        Msg.add("Take care brother..!");
        Msg.add("I'm at shopping mall :D");
        Msg.add("How are you ?");
        Msg.add("What are you doing man");
        Msg.add("be a champ man..!");

        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        messageListView = (ListView) view.findViewById(R.id.messageRecyclerView);
      //  messageRecyclerView.setLayoutManager(layoutManager);
        messageListRecyclerAdapter = new MessageListViewAdapter(getActivity(),Names,Msg);
        messageListView.setAdapter(messageListRecyclerAdapter);

        return view;
    }

}
