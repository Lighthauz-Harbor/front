package com.example.dvidr_000.lighthauzproject;


import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;

public class HomeActivity extends AppCompatActivity {

    private int pos;
    SessionManager sessionManager;
    private HashMap<String,String> user;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                requestLogout();
                break;
            case R.id.menuSetInterest:
                Intent in = new Intent(HomeActivity.this,DetailActivity.class);
                in.putExtra("EXTRA_CONTENT","SET_INTEREST");
                startActivity(in);
                break;
            case R.id.menuProfile:
                Intent intent = new Intent(HomeActivity.this,DetailActivity.class);
                intent.putExtra("EXTRA_CONTENT","VIEW_PROFILE");
                intent.putExtra("USER_ID",user.get(SessionManager.KEY_ID));
                startActivity(intent);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();

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
                        return true;
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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                if (mStatusCode== HttpURLConnection.HTTP_NO_CONTENT){
                    Log.d(TAG,"Success");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sessionManager.logoutUser();
                            finish();
                        }
                    });

                }
                return super.parseNetworkResponse(response);
            }
        };

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(req, tag_json);
    }


}