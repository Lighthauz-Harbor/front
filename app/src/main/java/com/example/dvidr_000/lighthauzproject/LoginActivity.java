package com.example.dvidr_000.lighthauzproject;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
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

import static com.android.volley.VolleyLog.TAG;

public class LoginActivity extends AppCompatActivity {


    private ProgressBar pb;
    private String msg="";
    private Integer imgRes;
    private EditText email;
    private EditText password;
    private String emailStr;
    private String passwordStr;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(getApplicationContext());
        pb = (ProgressBar) findViewById(R.id.pBarLogin);

        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);

        ArrayList<User> users = (ArrayList) User.getUsers();

        User newUser;

        newUser = new User("david@lighthauz.com","david","David Long","01/01/1970");
        newUser.setInterest("Food & Beverage");
        imgRes = R.drawable.man1;
        newUser.setProfilePic(imgRes);
        users.add(newUser);

        newUser = new User("bryan@lighthauz.com","bryan","Bryan Tyler","01/01/1970");
        newUser.setInterest("Restaurant");
        imgRes = R.drawable.man2;
        newUser.setProfilePic(imgRes);
        users.add(newUser);

        newUser = new User("andrew@lighthauz.com","andrew","Andrew Jason","01/01/1970");
        newUser.setInterest("Tech");
        imgRes = R.drawable.man3;
        newUser.setProfilePic(imgRes);
        users.add(newUser);

        final TextView registerLink = (TextView) findViewById(R.id.tvRegister);
        final Button login = (Button) findViewById(R.id.btnLogin);


        registerLink.setOnClickListener (new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }

        });

        login.setOnClickListener (new View.OnClickListener()
        {
            public void onClick(View v)
            {
                emailStr = email.getText().toString().trim();
                passwordStr = password.getText().toString().trim();

                validate();

            }

        });

    }


    public void validate(){
        boolean fail=false;
        msg="";

        if (passwordStr.isEmpty()){
            fail=true;
            msg="Please enter password";
        } else if(passwordStr.length()<8 && passwordStr.length()>12) {
            fail = true;
            msg = "Invalid Password";
        }

        if (emailStr.isEmpty()) {
            fail =true;
            msg="Please enter email";
        } else {
            fail= !android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches();
            if(fail){
                msg="Invalid email";
            }
        }

        if(!fail){
            request();
        } else {
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
        }

    }

    protected void request(){
        // Tag used to cancel the request
        String tag_json = "json_object_req";

        final String URL = "https://lighthauz.herokuapp.com/user/auth/login";
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<>();
        params.put("username", emailStr);
        params.put("password", passwordStr);

        pb.setVisibility(View.VISIBLE);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            VolleyLog.d(response.toString());
                            if (response.isNull("token")){
                                msg = response.getString("fail");
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String token = response.getString("token");
                                String username = response.getString("username");
                                String id = response.getString("id");

                                sessionManager.createLoginSession(id,username,token);
                                requestCategoryPref();

                            }
                            pb.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                msg = "Error occured. Please try again.";
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
        });

        // add the request object to the queue to be executed
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req,tag_json);

    }

    public void requestCategoryPref(){
        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/category/prefer/list";
        HashMap<String,String> params = new HashMap<>();
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String> user = sessionManager.getUserDetails();
        params.put("userId",user.get(SessionManager.KEY_ID));

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());
                        try{
                            if (response.isNull("list")){
                                Toast.makeText(getApplicationContext(), response.getString("fail"), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                JSONArray myArray = response.getJSONArray("list");
                                Intent in;
                                if (myArray.length()==0){
                                    in = new Intent(LoginActivity.this,DetailActivity.class);
                                    in.putExtra("EXTRA_CONTENT","FIRST_LOGIN");
                                }
                                else {
                                    in = new Intent(LoginActivity.this,HomeActivity.class);
                                }
                                startActivity(in);
                                finish();
                            }
                        }
                        catch (JSONException e){
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req, tag_json);
    }



}
