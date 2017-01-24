package com.example.dvidr_000.lighthauzproject;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class RequestReceivedFragment extends Fragment implements DataAdapter.ItemClickCallback,View.OnClickListener{
    private RecyclerView recView;
    private DataAdapter adapter;
    private SessionManager sessionManager;
    private HashMap<String,String> user;
    private List<User> users;
    private String idStr;

    private Dialog myDialog;
    private ImageView pic;
    private TextView name;
    private TextView bio;
    private ImageButton accept;
    private ImageButton reject;
    private String selectedUserId;

    private TextView notice;
    private ProgressBar pb;
    private ProgressDialog pDialog;

    public RequestReceivedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_request_received,container,false);

        getActivity().setTitle(R.string.RequestsReceived);

        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();
        idStr = user.get(SessionManager.KEY_ID);

        users = new ArrayList<>();

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        notice = (TextView) v.findViewById(R.id.reqReceivedNotice);
        pb = (ProgressBar) v.findViewById(R.id.pBarReqReceived);
        recView = (RecyclerView) v.findViewById(R.id.rec_list_req_received);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));

        requestList();

        adapter = new DataAdapter(users, getActivity(), "USER");
        adapter.setItemClickCallback(this);

        myDialog = new Dialog(getContext(),R.style.CustomDialogTheme);
        myDialog.setContentView(R.layout.dialog_request);
        pic = (ImageView) myDialog.findViewById(R.id.ic_dialog_request);
        name = (TextView) myDialog.findViewById(R.id.tv_name_dialog_request);
        bio = (TextView) myDialog.findViewById(R.id.tv_bio_dialog_request);
        accept = (ImageButton) myDialog.findViewById(R.id.btn_accept_dialog_req);
        reject = (ImageButton) myDialog.findViewById(R.id.btn_reject_dialog_req);
        container = (LinearLayout) myDialog.findViewById(R.id.dialog_req_root);
        container.setOnClickListener(this);
        accept.setOnClickListener(this);
        reject.setOnClickListener(this);

        return v;
    }

    @Override
    public void onItemClick(int p, View view) {
        User selected = users.get(p);
        selectedUserId = selected.getId();
        name.setText(selected.getName());
        bio.setText(selected.getBio());
        DataAdapter.imageLoader(selected.getProfPic(),pic);
        myDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_req_root:
                Intent intent1 = new Intent(getActivity(),DetailActivity.class);
                intent1.putExtra("EXTRA_CONTENT","VIEW_PROFILE");
                intent1.putExtra("USER_ID",selectedUserId);
                startActivity(intent1);
                break;
            case R.id.btn_accept_dialog_req:
                userAction("accept",selectedUserId,idStr);
                break;
            case R.id.btn_reject_dialog_req:
                userAction("remove",selectedUserId,idStr);
                break;
        }
    }

    public void requestList(){
        pb.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/connections/requests/received/" + idStr;

        JsonObjectRequest req = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());
                        users.clear();
                        JSONArray myArray;
                        try{
                            myArray = response.getJSONArray("receivedByUser");
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
                                    newUser.setBio(bio);
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
        AppSingleton.getInstance(getContext()).addToRequestQueue(req, tag_json);
    }

    public void userAction(String action, String user1, String user2){
        pDialog.show();

        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/connections/" + action;
        HashMap<String,String> params = new HashMap<>();
        params.put("fromId",user1);
        params.put("toId",user2);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String msg = response.getString("message");
                            for (int i=0;i<users.size();i++){
                                if (users.get(i).getId().equals(selectedUserId))users.remove(i);
                            }
                            myDialog.dismiss();
                            adapter.notifyDataSetChanged();
                            if (users.isEmpty())notice.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                        catch (JSONException e){
                            Log.e("MYAPP", "unexpected JSON exception", e);
                        }
                        pDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppSingleton.getInstance(getContext()).addToRequestQueue(req, tag_json);
    }
}
