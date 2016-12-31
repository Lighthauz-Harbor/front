package com.example.dvidr_000.lighthauzproject;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryPickFragment extends Fragment implements MyAdapter.ItemClickCallback{
    private RecyclerView recView;
    private MyAdapter adapter;
    private List<Category> categoryList;
    private List<String> selectedList;
    private SessionManager sessionManager;
    private HashMap<String,String> user;
    private ProgressDialog pDialog;

    public CategoryPickFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_pick, container, false);

        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();

        getActivity().setTitle("Choose Category");
        categoryList = new ArrayList<>();
        selectedList = new ArrayList<>();
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        recView = (RecyclerView) v.findViewById(R.id.rec_list_category);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button next = (Button) v.findViewById(R.id.btnNextCategoryPick);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray list = new JSONArray();
                List<Category> catList = adapter.getListData();
                Category c;

                for (int i=0;i<catList.size();i++){
                    c = catList.get(i);
                    if (c.isSelected()){
                        try {
                            list.put(c.getName());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                if (list.length()==0){
                    Toast.makeText(getContext(), R.string.EmptySelection, Toast.LENGTH_SHORT).show();
                }
                else postCategory(list);
            }
        });

        requestCategoryPref();

        adapter = new MyAdapter(getActivity(),categoryList,"CATEGORY_LIST");
        adapter.setItemClickCallback(this);

        // Inflate the layout for this fragment
        return v;
    }

    public void requestCategoryPref(){

        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/category/prefer/list";
        HashMap<String,String> params = new HashMap<>();
        params.put("userId",user.get(SessionManager.KEY_ID));

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray myArray;
                        VolleyLog.d(response.toString());

                        try{
                            if (response.isNull("list")){
                                Toast.makeText(getContext(), response.getString("fail"), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                myArray = response.getJSONArray("list");
                                for(int i=0;i<myArray.length();i++){
                                    String selected = myArray.get(i).toString();
                                    selectedList.add(selected);

                                }
                                requestCategoryList();
                            }
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

    public void requestCategoryList(){

        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/category/list";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray myArray;
                        VolleyLog.d(response.toString());
                        try{
                            myArray = response.getJSONArray("list");
                            for(int i=0;i<myArray.length();i++){
                                Category newCat = new Category(myArray.get(i).toString(),false);
                                for (int j=0;j<selectedList.size();j++){
                                    if (selectedList.get(j).equals(newCat.getName())){
                                        newCat.setSelected(true);
                                        break;
                                    }
                                }
                                categoryList.add(newCat);

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

    public void postCategory(JSONArray list){
        pDialog.show();

        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/category/prefer";

        HashMap<String,String> params = new HashMap<>();
        params.put("userId",user.get(SessionManager.KEY_ID));
        JSONObject obj = new JSONObject(params);

        try {
            obj.putOpt("categories",list);
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
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            }) {

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    int mStatusCode = response.statusCode;
                    if (mStatusCode== HttpURLConnection.HTTP_OK){
                        Log.d(TAG,"Success");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    pDialog.dismiss();
                                    proceed();

                            }
                        });

                    }
                    return super.parseNetworkResponse(response);
                }
            };

        // Adding request to request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(req, tag_json);
    }

    @Override
    public void onItemClick(int p, View view) {
        switch (view.getId()){
            case R.id.checkBox_list_simple_checkbox:

            case R.id.list_simple_checkbox_root:
                Category data = (Category) adapter.getListData().get(p);
                if (data.isSelected()) {
                    data.setSelected(false);
                } else {
                    data.setSelected(true);
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    public void proceed(){
        Intent in = new Intent(getActivity(),HomeActivity.class);
        startActivity(in);
        getActivity().finish();
    }



}
