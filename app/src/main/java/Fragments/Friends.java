package Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.project.usmansh.ingrumidreal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.FriendLisRecyclertAdapter;
import Adapters.FriendListViewAdapter;
import Classes.FriendsData;
import Classes.Mysingleton;

public class Friends extends Fragment {

    private static Friends INSTANCE = null;

    ListView frndListView;
    View view;

    FriendListViewAdapter friendLisRecyclertAdapter;
    ArrayList<String> Names = new ArrayList<>();
    ArrayList<FriendsData> frndsArr = new ArrayList<>();

    public Friends() {
        // Required empty public constructor
    }

    public static Friends getInstance() {

        if (INSTANCE == null)
            INSTANCE = new Friends();
        return INSTANCE;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_friends, container, false);

        getAllFriends();

       // LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        frndListView = (ListView) view.findViewById(R.id.friendsListView);
        //frndListView.setLayoutManager(layoutManager);

        friendLisRecyclertAdapter = new FriendListViewAdapter(getActivity(),frndsArr);
        frndListView.setAdapter(friendLisRecyclertAdapter);
        Log.d("Adapter..: ","Frindss + "+Names.size());


        return view;
    }






        private void getAllFriends() {

            String url = "http://3.219.121.75:8080/api/crud/friends";

            SharedPreferences SharedPref = getActivity().getSharedPreferences("User_Token", Context.MODE_PRIVATE);
            final String user_token = SharedPref.getString("uToken","");



            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    //Getting Friends List
                    try {
                        String status_Code = response.getString("status");
                        String message     = response.getString("message");
                        Log.d("Responsee: ","Status: "+status_Code+" and "+"Message: "+message);
                        JSONArray friendList = response.getJSONArray("linked_user");
                        getFriendList(friendList);


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


            Mysingleton.getInstance(getActivity()).addToRequestque(request);


        }





    private void getFriendList(JSONArray friendList) {

        //Toast.makeText(getActivity(), "Getting friends", Toast.LENGTH_LONG).show();
        frndsArr.clear();
        JSONObject jsonObject;
        FriendsData friend;

            for (int i=0; i<friendList.length(); i++){

                try {
                    jsonObject = friendList.getJSONObject(i);
                    Log.d("FRNDSSS: "+i,jsonObject.toString());
                   // Toast.makeText(getActivity(), ""+jsonObject.toString(), Toast.LENGTH_LONG).show();
                    friend = new FriendsData();
                    friend.setPhone(jsonObject.getBoolean("phone"));
                    friend.setFacebook(jsonObject.getBoolean("facebook"));
                    friend.setInstagram(jsonObject.getBoolean("instagram"));
                    friend.setLinkedin(jsonObject.getBoolean("linkedin"));
                    friend.setTwitter(jsonObject.getBoolean("twitter"));
                    friend.setSnapchat(jsonObject.getBoolean("snapchat"));
                    friend.setEmaill(jsonObject.getBoolean("email"));
                    friend.setName(jsonObject.getString("name"));
                    friend.setUsername(jsonObject.getString("username"));

                    frndsArr.add(friend);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            Log.d("Arr Size: ", String.valueOf(frndsArr.size()));
            friendLisRecyclertAdapter.notifyDataSetChanged();
    }


}
