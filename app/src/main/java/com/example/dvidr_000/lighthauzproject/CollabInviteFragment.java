package com.example.dvidr_000.lighthauzproject;


import android.app.ProgressDialog;
import android.content.DialogInterface;
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
public class CollabInviteFragment extends Fragment implements MyAdapter.ItemClickCallback, View.OnClickListener{
    private RecyclerView recView;
    private MyAdapter adapter;
    private List<User> users;
    private boolean[] selected;
    private SessionManager sessionManager;
    private HashMap<String,String> user;
    private AlertDialog alertDialog;
    private ProgressDialog pDialog;
    private ProgressBar pb;
    private TextView notice;
    private String category;
    private String ideaId;

    public CollabInviteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_collab_invite, container, false);

        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();

        getActivity().setTitle("Invite Partners");
        ideaId = getActivity().getIntent().getStringExtra("IDEA_ID");
        category = getActivity().getIntent().getStringExtra("CATEGORY");
        users = new ArrayList<>();
        pb = (ProgressBar) v.findViewById(R.id.pBarCollabInvite);
        notice = (TextView) v.findViewById(R.id.CollabInviteNotice);
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        recView = (RecyclerView) v.findViewById(R.id.rec_list_collab_invite);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button next = (Button) v.findViewById(R.id.btnNextCollabInvite);
        next.setOnClickListener(this);

        Button skip = (Button) v.findViewById(R.id.btnSkipCollabInvite);
        skip.setOnClickListener(this);

        AlertDialog.Builder removeDialog = new AlertDialog.Builder(getActivity());
        removeDialog.setMessage(R.string.ConfirmSkipCollabInvite)
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(R.string.Yes,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getActivity().finish();
                    }
                });
        alertDialog = removeDialog.create();
        alertDialog.setTitle("Confirm");

        getSuggestion(1);

        adapter = new MyAdapter(users,getActivity(),"COLLAB_INVITE");
        adapter.setItemClickCallback(this);

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnNextCollabInvite:
                JSONArray list = new JSONArray();

                for (int i=0;i<selected.length;i++){
                    if (selected[i]){
                        try {
                            list.put(users.get(i).getId());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                if (list.length()==0){
                    Toast.makeText(getContext(), R.string.EmptySelection, Toast.LENGTH_SHORT).show();
                }
                else invitePartners(list);
                break;
            case R.id.btnSkipCollabInvite:
                alertDialog.show();
                break;
        }
    }

    @Override
    public void onItemClick(int p, View view) {
        switch (view.getId()){
            case R.id.checkBox_list_simple_checkbox:
                if (selected[p]) {
                    selected[p] = false;
                } else {
                    selected[p] = true;
                }
                break;

            case R.id.list_simple_checkbox_root:
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("EXTRA_CONTENT","VIEW_PROFILE");
                intent.putExtra("USER_ID",users.get(p).getId());
                startActivity(intent);
                break;
        }
    }

    public void getSuggestion(final int route){
        notice.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String tag_json = "json_object_req";
        String url="";
        JSONObject obj = new JSONObject();
        try {
            if (route==1){
                url = "http://lighthauz.herokuapp.com/api/recommend/ideas/partners";
                obj.put("userId",user.get(SessionManager.KEY_ID));
            }
            else if (route==2) {
                url = "http://lighthauz.herokuapp.com/api/category/recommend/users/";
                obj.put("num", 4);
            }
            obj.put("category",category);
        } catch (JSONException e){

        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,url,obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());
                        users.clear();
                        JSONArray myArray;
                        try{
                            if (response.isNull("fail")){
                                myArray = response.getJSONArray("recommended");
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
                                    selected = new boolean[users.size()];
                                    for (int i=0;i<selected.length;i++){
                                        selected[i] = false;
                                    }
                                    recView.setAdapter(adapter);
                                }
                            }
                            else {
                                if (route==1){
                                    getSuggestion(2);
                                }
                                else {
                                    notice.setText(response.getString("fail"));
                                    notice.setVisibility(View.VISIBLE);
                                }
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

    public void invitePartners(JSONArray list){
        pDialog.show();

        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/ideas/invite-partners";

        JSONObject obj = new JSONObject();

        try {
            obj.putOpt("partnerIds",list);
            obj.putOpt("ideaId",ideaId);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,url,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());
                        try{
                            if (response.isNull("fail")){
                                Toast.makeText(getContext(),response.getString("message") , Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }
                            else {
                                Toast.makeText(getContext(),response.getString("fail") , Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (JSONException e){
                            Log.e("MYAPP", "unexpected JSON exception", e);
                        }
                        pDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: ", error.toString());
                pDialog.dismiss();
            }
        });

        // Adding request to request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(req, tag_json);
    }
}
