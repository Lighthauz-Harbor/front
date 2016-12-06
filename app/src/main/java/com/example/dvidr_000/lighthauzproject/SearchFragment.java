package com.example.dvidr_000.lighthauzproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TabHost;
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
import java.util.List;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener,MyAdapter.ItemClickCallback{

    private RecyclerView recView;
    private MyAdapter adapter;
    private List<User> users;
    private List<Idea> ideas;
    private ProgressBar pb;
    private TextView notice;
    private TabHost host;
    private RadioGroup myRadioGroup;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Place an action bar item for searching.
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(searchItem);
        sv.setOnQueryTextListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_search, container, false);
        setHasOptionsMenu(true);

        ideas= new ArrayList<>();
        users= new ArrayList<>();

        myRadioGroup = (RadioGroup) v.findViewById(R.id.rgSearch);

        host = (TabHost)v.findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab User");
        spec.setContent(R.id.User);
        spec.setIndicator("User");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Idea");
        spec.setContent(R.id.Idea);
        spec.setIndicator("Idea");
        host.addTab(spec);

        setTabView(v,R.id.pBarSearchUser,R.id.rec_list_search_user,R.id.searchUserNotice);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("Tab User")) {
                    setTabView(v,R.id.pBarSearchUser,R.id.rec_list_search_user,R.id.searchUserNotice);
                }
                if(tabId.equals("Tab Idea")) {
                    setTabView(v,R.id.pBarSearchIdea,R.id.rec_list_search_idea,R.id.searchIdeaNotice);
                }
            }});

        return v;
    }

    public void setTabView(View v, int pbId, int recViewId, int guideId){
        pb = (ProgressBar) v.findViewById(pbId);
        notice = (TextView) v.findViewById(guideId);
        recView = (RecyclerView) v.findViewById(recViewId);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        notice.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        request(query);

        if(host.getCurrentTab()==0){
            adapter = new MyAdapter(users,getActivity(),"USER_DETAIL");

        }
        else {
            adapter = new MyAdapter(ideas,"IDEA_DETAIL",getActivity());

        }

        adapter.setItemClickCallback(this);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void request(String query){

        // Tag used to cancel the request
        final int code;
        String tag_json = "json_object_req";
        String url = "http://lighthauz.herokuapp.com/api/";
        int index = myRadioGroup.indexOfChild(getView().findViewById(myRadioGroup.getCheckedRadioButtonId()));

        if(host.getCurrentTab()==0){
            url = url + "users/search/";
            code=1;
            users.clear();
        }
        else if (index==0){
            url = url + "ideas/search/title/";
            code=2;
            ideas.clear();
        }
        else {
            url = url + "ideas/search/category/";
            code=2;
            ideas.clear();
        }

        url = url + query;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray myArray;
                        try{
                            myArray = response.getJSONArray("results");
                            for(int i=0;i<myArray.length();i++){
                                switch(code){
                                    case 1:
                                        String userId = myArray.getJSONObject(i).getString("id");
                                        String name = myArray.getJSONObject(i).getString("name");
                                        String username = myArray.getJSONObject(i).getString("username");
                                        Long createdAt = myArray.getJSONObject(i).getLong("createdAt");
                                        String profilePic = myArray.getJSONObject(i).getString("profilePic");

                                        User newUser = new User(userId,username,name,createdAt,profilePic);
                                        users.add(newUser);

                                        break;
                                    case 2:
                                        String ideaId = myArray.getJSONObject(i).getString("id");
                                        String title = myArray.getJSONObject(i).getString("title");
                                        String description = myArray.getJSONObject(i).getString("description");
                                        String pic = myArray.getJSONObject(i).getString("pic");
                                        String category = myArray.getJSONObject(i).getString("category");

                                        Idea newIdea = new Idea(ideaId,title,description,pic,category);
                                        ideas.add(newIdea);

                                        break;
                                }
                            }

                            if(host.getCurrentTab()==0){
                                if (users.isEmpty()){
                                    notice.setText(R.string.noResultsFound);
                                    notice.setVisibility(View.VISIBLE);
                                }
                                else {
                                    notice.setVisibility(View.GONE);
                                }
                            }
                            else {
                                if (ideas.isEmpty()){
                                    notice.setText(R.string.noResultsFound);
                                    notice.setVisibility(View.VISIBLE);
                                }
                                else {
                                    notice.setVisibility(View.GONE);
                                }
                            }

                            recView.setAdapter(adapter);
                            pb.setVisibility(View.GONE);
                            recView.setVisibility(View.VISIBLE);

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

    @Override
    public void onItemClick(int p, View view) {
        switch (view.getId()){
            //Idea
            case R.id.card_item_plain:

                Intent intent1 = new Intent(getActivity(),DetailActivity.class);
                intent1.putExtra("EXTRA_CONTENT","IDEA_DETAIL");
                intent1.putExtra("IDEA_ID",ideas.get(p).getId());
                startActivity(intent1);
                break;

            //User
            case R.id.card_item_simple:

                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("EXTRA_CONTENT","VIEW_PROFILE");
                intent.putExtra("USER_ID",users.get(p).getId());
                startActivity(intent);
                break;
        }
    }
}
