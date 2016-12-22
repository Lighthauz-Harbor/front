package com.example.dvidr_000.lighthauzproject;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionFragment extends Fragment implements MyAdapter.ItemClickCallback, View.OnClickListener{
    private RecyclerView recView;
    public MyAdapter adapter;
    private TextView notice;
    private ProgressBar pb;
    private SessionManager sessionManager;
    private HashMap<String,String> user;
    private List<User> users;
    private String idStr;
    private Button btnSent;
    private Button btnReceived;

    public ConnectionFragment(){
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        notice.setVisibility(View.GONE);
        requestList();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_connection,container,false);

        setHasOptionsMenu(true);

        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();
        idStr = user.get(SessionManager.KEY_ID);

        users = new ArrayList<>();

        notice = (TextView) v.findViewById(R.id.connectionNotice);
        pb = (ProgressBar) v.findViewById(R.id.pBarConnection);
        btnSent = (Button) v.findViewById(R.id.btnRequestSent);
        btnReceived = (Button) v.findViewById(R.id.btnRequestReceived);
        btnSent.setOnClickListener(this);
        btnReceived.setOnClickListener(this);
        recView = (RecyclerView) v.findViewById(R.id.rec_list_connection);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));

        requestList();

        adapter = new MyAdapter(users, getActivity(), "USER");
        adapter.setItemClickCallback(this);
        return v;
    }

    public void requestList(){
        pb.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/connections/"+idStr;

        JsonObjectRequest req = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        users.clear();
                        JSONArray myArray;
                        try{
                            myArray = response.getJSONArray("connections");
                            if (myArray.length()==0){
                                notice.setVisibility(View.VISIBLE);
                            }
                            else {
                                for (int i = 0; i < myArray.length(); i++) {
                                    String userId = myArray.getJSONObject(i).getString("id");
                                    String name = myArray.getJSONObject(i).getString("name");
                                    String bio = myArray.getJSONObject(i).getString("bio");
                                    String profilePic = myArray.getJSONObject(i).getString("profilePic");

                                    User newUser = new User(userId, name, profilePic);
                                    users.add(newUser);
                                }
                                recView.setAdapter(adapter);
                            }
                        }
                        catch (JSONException e){
                            Log.e("MYAPP", "unexpected JSON exception", e);
                        }
                        pb.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pb.setVisibility(View.GONE);
            }
        });

// Adding request to request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(req, tag_json);
    }

    @Override
    public void onItemClick(int p, View view) {
        Intent intent = new Intent(getActivity(),DetailActivity.class);
        intent.putExtra("EXTRA_CONTENT","VIEW_PROFILE");
        intent.putExtra("USER_ID",users.get(p).getId());
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnRequestReceived){
            Intent in = new Intent(getActivity(),DetailActivity.class);
            in.putExtra("EXTRA_CONTENT","REQ_RECEIVED");
            startActivity(in);
        }
        else if (view.getId()==R.id.btnRequestSent){
            Intent in = new Intent(getActivity(),DetailActivity.class);
            in.putExtra("EXTRA_CONTENT","REQ_SENT");
            startActivity(in);
        }
    }
}
