package com.example.dvidr_000.lighthauzproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class VerificationActivity extends AppCompatActivity {

    String username;

    TextView notice;
    TextView link;
    Button backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        username = getIntent().getExtras().getString("username");

        notice = (TextView) findViewById(R.id.tvVerifNotice);
        link = (TextView) findViewById(R.id.tvVerifLink);
        backToLogin = (Button) findViewById(R.id.btnBackToLogin);

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        request();

    }

    protected void request(){
        String tag_json = "json_object_req";

        final String URL = "https://lighthauz.herokuapp.com/user/auth/generate-verif/";
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<>();
        params.put("username",username);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String feedback;

                        try {
                            if (response.isNull("message")){
                                feedback = response.getString("fail");

                                String verifLink = response.getString("verifLink");
                                link.setMovementMethod(LinkMovementMethod.getInstance());
                                String text = "<a href=\""+ verifLink + "\">Verification Link </a>";
                                link.setText(Html.fromHtml(text));
                                link.setVisibility(View.VISIBLE);
                                link.setClickable(true);
                            }
                            else {
                                feedback = response.getString("message");

                            }

                            notice.setText(feedback);
                            notice.setVisibility(View.VISIBLE);
                            backToLogin.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        // add the request object to the queue to be executed
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req,tag_json);
    }
}
