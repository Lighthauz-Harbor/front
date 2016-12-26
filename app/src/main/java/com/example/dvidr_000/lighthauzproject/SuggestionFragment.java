package com.example.dvidr_000.lighthauzproject;


import android.content.ClipData;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class SuggestionFragment extends Fragment implements MyAdapter.ItemClickCallback{

    private RecyclerView recView;
    private MyAdapter adapter;
    private TextView notice;
    private ProgressBar pb;
    private SessionManager sessionManager;
    private HashMap<String,String> user;
    private List<User> users;
    private String category;

    public SuggestionFragment() {
        // Required empty public constructor
    }

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_suggestion, container, false);

            getActivity().setTitle("Suggestions");
            category = getArguments().getString("CATEGORY");

            sessionManager = new SessionManager(getContext());
            user = sessionManager.getUserDetails();

            users = new ArrayList<>();

            notice = (TextView) rootView.findViewById(R.id.suggestionNotice);
            pb = (ProgressBar) rootView.findViewById(R.id.pBarSuggestion);
            recView = (RecyclerView) rootView.findViewById(R.id.rec_list_suggestion);
            recView.setLayoutManager(new LinearLayoutManager(getActivity()));

            getSuggestion();

            adapter = new MyAdapter(users, getActivity(),"USER");
            adapter.setItemClickCallback(this);

        // Inflate the layout for this fragment
        return rootView;
    }

    public void getSuggestion(){
        notice.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String tag_json = "json_object_req";
        String url = "http://lighthauz.herokuapp.com/api/recommend/ideas/partners";
        HashMap<String,String> params = new HashMap<>();
        params.put("userId",user.get(SessionManager.KEY_ID));
        params.put("category",category);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(params),
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
                                    recView.setAdapter(adapter);
                                }
                            }
                            else {
                                notice.setText(response.getString("fail"));
                                notice.setVisibility(View.VISIBLE);
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
        Bundle args = new Bundle();
        args.putString("USER_ID",users.get(p).getId());

        ViewProfileFragment fragment = new ViewProfileFragment();
        fragment.setArguments(args);

        FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.fragment_container_detail,fragment);
        tr.addToBackStack(null);
        tr.commit();
    }
}
