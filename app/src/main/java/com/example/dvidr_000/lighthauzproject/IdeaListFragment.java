package com.example.dvidr_000.lighthauzproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class IdeaListFragment extends Fragment implements MyAdapter.ItemClickCallback{

    private RecyclerView recView;
    private MyAdapter adapter;
    int loginIndex=0;
    private String idStr;
    SessionManager sessionManager;
    HashMap<String,String> user;
    ArrayList<Idea> ideas;


    public IdeaListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {

        request();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_idea_list, container, false);

        loginIndex = getActivity().getIntent().getIntExtra("LOGIN_INDEX",0);
        ideas = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("EXTRA_CONTENT","CREATE_IDEA");
                startActivity(intent);
            }
        });

        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();
        idStr = user.get(SessionManager.KEY_ID);

        recView = (RecyclerView) v.findViewById(R.id.rec_list_idea);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MyAdapter(ideas, "IDEA", getActivity());
        adapter.setItemClickCallback(this);

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onItemClick(int p,View view) {
        //Open idea detail
        Intent intent = new Intent(getActivity(),DetailActivity.class);
        intent.putExtra("EXTRA_CONTENT","IDEA_DETAIL");
        intent.putExtra("IDEA_ID",ideas.get(p).getId());


        startActivity(intent);
    }

    public void request(){

        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/ideas/list/";

        JsonObjectRequest req = new JsonObjectRequest(url + idStr,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());
                        JSONArray myArray;
                        ideas.clear();
                        try{
                            myArray = response.getJSONArray("ideas");
                            for(int i=0;i<myArray.length();i++){

                                JSONObject idea = myArray.getJSONObject(i);
                                Idea newIdea = new Idea(idea.getString("id"),idea.getString("title"));
                                ideas.add(newIdea);
                            }

                            recView.setAdapter(adapter);

                        }
                        catch (JSONException e){
                            Log.e("MYAPP", "unexpected JSON exception", e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

// Adding request to request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(req, tag_json);
    }
}
