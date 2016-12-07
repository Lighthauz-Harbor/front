package com.example.dvidr_000.lighthauzproject;


import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    private int pos;
    SessionManager sessionManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menuLogout){
            requestLogout();
            sessionManager.logoutUser();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(getApplicationContext());

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,new NewsFeedFragment()).commit();
        pos = 1;

        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected( MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_home:
                                if(pos!=1) {
                                    NewsFeedFragment fragment1 = new NewsFeedFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, fragment1).commit();
                                    pos=1;
                                }
                                break;
                            case R.id.nav_idea:
                                if(pos!=2) {
                                    IdeaListFragment fragment2 = new IdeaListFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, fragment2).commit();
                                    pos=2;
                                }
                                break;
                            case R.id.nav_search:
                                if(pos!=3) {
                                    SearchFragment fragment3 = new SearchFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, fragment3).commit();
                                    pos=3;
                                }
                                break;

                            case R.id.nav_connection:
                                if(pos!=4) {
                                    ConnectionFragment fragment4 = new ConnectionFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, fragment4).commit();
                                    pos=4;
                                }
                                break;
                        }
                        return false;
                    }
                });
    }

    public void requestLogout(){

        // Tag used to cancel the request
        String tag_json = "json_object_req";
        String url = "http://lighthauz.herokuapp.com/user/auth/logout";


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Adding request to request queue
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req, tag_json);
    }


}