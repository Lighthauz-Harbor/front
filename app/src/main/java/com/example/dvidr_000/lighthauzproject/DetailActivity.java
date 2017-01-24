package com.example.dvidr_000.lighthauzproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;

public class DetailActivity extends AppCompatActivity {

    private FragmentTransaction tr;
    private FragmentManager fm;
    private SessionManager sessionManager;
    private HashMap<String,String> user;

    private ProgressDialog pDialog;
    private String newIdeaId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String content= getIntent().getStringExtra("EXTRA_CONTENT");
        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();
        fm = getSupportFragmentManager();
        tr = fm.beginTransaction();
        pDialog = new ProgressDialog(DetailActivity.this);
        pDialog.setCancelable(false);

        switch (content){
            case "IDEA_DETAIL":
                tr.replace(R.id.fragment_container_detail,new IdeaDetailFragment());
                break;
            case "EDIT_IDEA":
                Bundle ideaBundle = getIntent().getBundleExtra("IDEA_BUNDLE");
                IdeaInfoFragment fragment = new IdeaInfoFragment();
                fragment.setArguments(ideaBundle);
                tr.replace(R.id.fragment_container_detail,fragment);
                break;
            case "CREATE_IDEA":
                tr.replace(R.id.fragment_container_detail,new CreateIdeaFragment());
                break;
            case "VIEW_PROFILE":
                tr.replace(R.id.fragment_container_detail,new ViewProfileFragment());
                break;
            case "FIRST_LOGIN":
                tr.replace(R.id.fragment_container_detail,new FirstLoginFragment());
                disableUpBtn();
                break;
            case "SET_INTEREST":
                tr.replace(R.id.fragment_container_detail,new CategoryPickFragment());
                break;
            case "REQ_RECEIVED":
                tr.replace(R.id.fragment_container_detail,new RequestReceivedFragment());
                break;
            case "REQ_SENT":
                tr.replace(R.id.fragment_container_detail,new RequestSentFragment());
                break;
            case "COLLAB_INVITE":
                tr.replace(R.id.fragment_container_detail,new InvitePartnersFragment());
                disableUpBtn();
                break;
        }
        tr.addToBackStack(null);
        tr.commit();
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() == 1) {
            finish();
        }
        super.onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //up button management
            case android.R.id.home:
                if (fm.getBackStackEntryCount() > 1) {
                    fm.popBackStack();
                    return true;
                } else {
                    finish();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void disableUpBtn(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }
    }

    public void createIdea(Bundle idea, final String content){
        String title = idea.getString("TITLE");
        final String category  = idea.getString("CATEGORY");
        String oldCategory  = idea.getString("OLD_CATEGORY","");
        String description = idea.getString("DESCRIPTION");
        String publicity= idea.getString("VISIBILITY");
        String background = idea.getString("BACKGROUND");
        String problem = idea.getString("PROBLEM");
        String solution = idea.getString("SOLUTION");
        String valueProposition = idea.getString("VP");
        String customerSegments = idea.getString("CS");
        String customerRelationships = idea.getString("CR");
        String channels = idea.getString("CH");
        String keyActivities = idea.getString("KA");
        String keyResources = idea.getString("KR");
        String keyPartners = idea.getString("KP");
        String costStructure = idea.getString("COST");
        String revenueStreams = idea.getString("RS");
        String strengths = idea.getString("STRENGTH");
        String weaknesses = idea.getString("WEAKNESS");
        String opportunities = idea.getString("OPPORTUNITY");
        String threats = idea.getString("THREAT");
        String pic = idea.getString("PIC","");
        String extraLink = idea.getString("EXTRA_LINK");

        pDialog.setMessage("Creating idea...");
        pDialog.setCancelable(false);
        pDialog.show();

        // Tag used to cancel the request
        String tag_json = "json_object_req";
        String url="";
        String ideaId="";
        String defaultPic= "http://res.cloudinary.com/lighthauz-harbor/image/upload/v1479742560/default-idea-pic_wq1dzc.png";
        HashMap<String,String> params = new HashMap<>();
        int method=0;
        if (content.equals("CREATE_IDEA")){
            url = "http://lighthauz.herokuapp.com/api/ideas/create";
            method = Request.Method.POST;
        }
        else if (content.equals("EDIT_IDEA")){
            ideaId = idea.getString("IDEA_ID");
            url = "http://lighthauz.herokuapp.com/api/ideas/update/" + ideaId;
            method = Request.Method.PUT;
            params.put("oldCategory",oldCategory);
            params.put("oldAuthor",user.get(SessionManager.KEY_USERNAME));
        }

        params.put("id",ideaId);
        params.put("title",title);
        params.put("category",category);
        params.put("author",user.get(SessionManager.KEY_USERNAME));
        params.put("description",description);
        params.put("visibility",publicity);
        params.put("background",background);
        params.put("problem",problem);
        params.put("solution",solution);
        params.put("extraLink",extraLink);
        if (!pic.isEmpty())params.put("pic",pic);
        else params.put("pic",defaultPic);
        params.put("strengths",strengths);
        params.put("weaknesses",weaknesses);
        params.put("opportunities",opportunities);
        params.put("threats",threats);
        params.put("valueProposition",valueProposition);
        params.put("customerSegments",customerSegments);
        params.put("customerRelationships",customerRelationships);
        params.put("channels",channels);
        params.put("keyActivities",keyActivities);
        params.put("keyResources",keyResources);
        params.put("keyPartners",keyPartners);
        params.put("costStructure",costStructure);
        params.put("revenueStreams",revenueStreams);

        JsonObjectRequest req = new JsonObjectRequest(method, url,new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());

                        try{
                            String msg = response.getString("message");
                            newIdeaId = response.getString("id");
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                        catch (JSONException e){
                            Log.e("MYAPP", "unexpected JSON exception", e);
                        }
                        pDialog.dismiss();
                        if (content.equals("CREATE_IDEA")) {
                            Intent in = new Intent(getApplicationContext(), DetailActivity.class);
                            in.putExtra("EXTRA_CONTENT", "COLLAB_INVITE");
                            in.putExtra("CATEGORY", category);
                            in.putExtra("IDEA_ID", newIdeaId);
                            startActivity(in);
                        }
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(req, tag_json);

    }


}
