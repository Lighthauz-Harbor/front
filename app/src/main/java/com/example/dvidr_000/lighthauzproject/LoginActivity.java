package com.example.dvidr_000.lighthauzproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
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
    private boolean status;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(getApplicationContext());
        pb = (ProgressBar) findViewById(R.id.pBarLogin);

        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);

        ArrayList<Idea> ideas = (ArrayList) Idea.getIdeas();
        ArrayList<User> users = (ArrayList) User.getUsers();

        User newUser;
        newUser = new User("admin@admin.com","admin","Admin","01/01/1970","0123456789");
        users.add(newUser);

        newUser = new User("david@lighthauz.com","david","David Long","01/01/1970","0123456789");
        newUser.setInterest("Food & Beverage");
        imgRes = R.drawable.man1;
        newUser.setProfilePic(imgRes);
        users.add(newUser);

        newUser = new User("bryan@lighthauz.com","bryan","Bryan Tyler","01/01/1970","0123456789");
        newUser.setInterest("Restaurant");
        imgRes = R.drawable.man2;
        newUser.setProfilePic(imgRes);
        users.add(newUser);

        newUser = new User("andrew@lighthauz.com","andrew","Andrew Jason","01/01/1970","0123456789");
        newUser.setInterest("Tech");
        imgRes = R.drawable.man3;
        newUser.setProfilePic(imgRes);
        users.add(newUser);

        Idea newIdea = new Idea("asd","Some category","Some description\n2\n3\n4asddsadasadadadadasadsad",new Date().getTime(),2,"Some background","Some problem","Some solution","VP","CS","CR","CH","KA","KR","KP","COST","RS","Some strength","Some weakness","Some opportunities","Some threats","","");
        ideas.add(newIdea);

        users.get(0).getIdea().add(ideas.size()-1);

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

        ArrayList<User> users = (ArrayList) User.getUsers();

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


        /*int index=0;
        fail=true;
        do{
            if(email.equals(users.get(index).getEmail())){
                if(password.equals(users.get(index).getPassword())){
                    fail=false;
                    loginIndex=index;
                }
                else{
                    msg="Wrong password";
                }
            }else {
                msg="Email isn't registered";
            }

            index++;
        }while(index<users.size());*/

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


                                proceed();

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

    protected void proceed(){
        Intent login = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(login);
        finish();
    }



}
