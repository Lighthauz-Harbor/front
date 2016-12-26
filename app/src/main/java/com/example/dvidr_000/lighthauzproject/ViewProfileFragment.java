package com.example.dvidr_000.lighthauzproject;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfileFragment extends Fragment implements MyAdapter.ItemClickCallback{

    private ProgressBar pb;
    private TextView name;
    private TextView about;
    private TextView email;
    private TextView connectionCount;
    private TextView ideaCount;
    private ImageView profilePic;
    private RelativeLayout layout;
    private MenuItem menuAdd;
    private MenuItem menuRremove;
    private MenuItem menucontact;
    private MenuItem menuUpdateProfile;
    private AlertDialog alertDialog;
    private ProgressDialog pDialog;
    private RecyclerView recViewConnection;
    private MyAdapter adapterConnection;
    private RecyclerView recViewIdea;
    private MyAdapter adapterIdea;
    private Dialog reportDialog;
    private EditText reportTitle;
    private EditText reportMessage;
    private Button reportSubmit;

    private String reportTitleStr;
    private String reportMessageStr;

    private String idStr;
    private String nameStr;
    private String aboutStr;
    private String emailStr;
    private String profilePicStr;

    private SessionManager sessionManager;
    private HashMap<String,String> user;

    private List<User> users;
    private List<Idea> ideas;

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        menuAdd = menu.findItem(R.id.menuAddUser);
        menuRremove = menu.findItem(R.id.menuRemoveUser);
        menucontact = menu.findItem(R.id.menuContact);
        menuUpdateProfile = menu.findItem(R.id.menuUpdateProfile);

        if (idStr.equals(user.get(SessionManager.KEY_ID))){
            menuUpdateProfile.setVisible(true);
        }
        else {
            isConnected(user.get(SessionManager.KEY_ID),idStr);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuContact:
                String[] address = {emailStr};
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, address);
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                break;
            case R.id.menuAddUser:
                addUser(user.get(SessionManager.KEY_ID),idStr);
                break;
            case R.id.menuRemoveUser:
                alertDialog.show();
                break;
            case R.id.menuUpdateProfile:
                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.fragment_container_detail,new EditProfileFragment());
                tr.addToBackStack(null);
                tr.commit();
                break;
            case R.id.menuReportUser:
                reportDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_view_profile, container, false);

        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        pb = (ProgressBar) v.findViewById(R.id.pBarViewProfile);
        name = (TextView) v.findViewById(R.id.tvUserProfileName);
        about = (TextView) v.findViewById(R.id.tvUserProfileAboutMeFIll);
        email = (TextView) v.findViewById(R.id.tvUserProfileEmailFill);
        connectionCount = (TextView) v.findViewById(R.id.tvUserProfileConnectionsCount);
        ideaCount = (TextView) v.findViewById(R.id.tvUserProfileIdeasCount);
        profilePic = (ImageView) v.findViewById(R.id.ic_view_profile);
        layout = (RelativeLayout) v.findViewById(R.id.view_profile_layout);
        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();
        users = new ArrayList<>();
        ideas = new ArrayList<>();

        getActivity().setTitle("User Profile");

        AlertDialog.Builder removeDialog = new AlertDialog.Builder(getActivity());
        removeDialog.setMessage(R.string.ConfirmRemoveUser)
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(R.string.Yes,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeUser(user.get(SessionManager.KEY_ID),idStr);
                        dialogInterface.dismiss();
                    }
                });
        alertDialog = removeDialog.create();
        alertDialog.setTitle("Confirm");

        reportDialog = new Dialog(getContext());
        reportDialog.setContentView(R.layout.dialog_report);
        reportTitle = (EditText) reportDialog.findViewById(R.id.et_report_title);
        reportMessage = (EditText) reportDialog.findViewById(R.id.et_report_message);
        reportSubmit = (Button) reportDialog.findViewById(R.id.btn_submit_dialog_report);
        reportSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportTitleStr = reportTitle.getText().toString().trim();
                reportMessageStr = reportMessage.getText().toString().trim();
                if (reportTitleStr.isEmpty()){
                    Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if (reportMessageStr.isEmpty()){
                    Toast.makeText(getContext(), "Message cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    submitReport();
                }
            }
        });

        try {
            idStr = getArguments().getString("USER_ID");
        }
        catch (Exception e){
            idStr = getActivity().getIntent().getStringExtra("USER_ID");
        }

        setHasOptionsMenu(true);

        requestProfile();

        LinearLayoutManager layout1 = new LinearLayoutManager(getContext());
        layout1.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager layout2 = new LinearLayoutManager(getContext());
        layout2.setOrientation(LinearLayoutManager.HORIZONTAL);

        recViewConnection = (RecyclerView) v.findViewById(R.id.rec_list_users_connection);
        recViewConnection.setLayoutManager(layout1);

        recViewIdea = (RecyclerView) v.findViewById(R.id.rec_list_users_idea);
        recViewIdea.setLayoutManager(layout2);

        adapterConnection = new MyAdapter(users, getActivity(), "USER_HORIZONTAL");
        adapterConnection.setItemClickCallback(this);

        adapterIdea = new MyAdapter(ideas, "IDEA_HORIZONTAL", getActivity());
        adapterIdea.setItemClickCallback(this);

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onItemClick(int p, View view) {
        String itemTag =(String) view.findViewById(R.id.card_item_simple_horizontal).getTag();
        Bundle args = new Bundle();
        FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
        if (itemTag.equals("USER_HORIZONTAL")){
            args.putString("USER_ID",users.get(p).getId());
            ViewProfileFragment fragment = new ViewProfileFragment();
            fragment.setArguments(args);
            tr.replace(R.id.fragment_container_detail,fragment);
        }
        else if (itemTag.equals("IDEA_HORIZONTAL")){
            args.putString("IDEA_ID",ideas.get(p).getId());
            IdeaDetailFragment fragment = new IdeaDetailFragment();
            fragment.setArguments(args);
            tr.replace(R.id.fragment_container_detail,fragment);
        }
        tr.addToBackStack(null);
        tr.commit();
    }

    public void setDetails(){
        name.setText(nameStr);
        about.setText(aboutStr);
        email.setText(emailStr);
        MyAdapter.imageLoader(profilePicStr,profilePic);
    }

    public void requestProfile(){
        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/users/get/" + idStr;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());

                        try{
                            nameStr = response.getString("name");
                            emailStr = response.getString("username");
                            aboutStr = response.getString("bio");
                            profilePicStr = response.getString("profilePic");

                            setDetails();
                            pb.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                            requestConnections();
                            requestIdeas();

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

    public void isConnected(final String user1, final String user2){
        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/connections/is-connected/" + user1 + "/" + user2;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());

                        try{
                            if(response.getBoolean("connected")){
                                menuRremove.setVisible(true);
                            }
                            else {
                                menuAdd.setVisible(true);
                            }
                            menucontact.setVisible(true);
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

    public void addUser(String user1, String user2){
        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/connections/request/";
        HashMap<String,String> params = new HashMap<>();
        params.put("fromId",user1);
        params.put("toId",user2);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());

                        try{
                            Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
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

    public void removeUser(String user1, String user2){
        pDialog.show();

        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/connections/remove";
        HashMap<String,String> params = new HashMap<>();
        params.put("fromId",user1);
        params.put("toId",user2);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());

                        try{
                            String msg = response.getString("message");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            if(msg.contains("Success")){
                                getActivity().onBackPressed();
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
                pDialog.dismiss();
            }
        });

        // Adding request to request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(req, tag_json);
    }

    public void requestIdeas(){

        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/ideas/list/" + idStr;

        JsonObjectRequest req = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());
                        JSONArray myArray;
                        ideas.clear();
                        try{
                            myArray = response.getJSONArray("ideas");
                            if (myArray.length()==0){
                            }
                            else {
                                for (int i = 0; i < myArray.length(); i++) {

                                    JSONObject idea = myArray.getJSONObject(i);
                                    Idea newIdea = new Idea(idea.getString("id"), idea.getString("title"),idea.getString("pic"));
                                    ideas.add(newIdea);
                                }
                                recViewIdea.setAdapter(adapterIdea);
                            }
                            ideaCount.setText(Integer.toString(myArray.length()));

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

    public void requestConnections(){
        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/connections/"+ idStr;

        JsonObjectRequest req = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        users.clear();
                        JSONArray myArray;
                        try{
                            myArray = response.getJSONArray("connections");
                            if (myArray.length()==0){
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
                                recViewConnection.setAdapter(adapterConnection);
                            }
                            connectionCount.setText(Integer.toString(myArray.length()));
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

    public void submitReport(){
        // Tag used to cancel the request
        String tag_json = "json_object_req";
        String url = "http://lighthauz.herokuapp.com/api/reports/create";

        HashMap<String,String> params = new HashMap<>();
        params.put("authorId",user.get(SessionManager.KEY_ID));
        params.put("targetId",idStr);
        params.put("title",reportTitleStr);
        params.put("message",reportMessageStr);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String msg = response.getString("message");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            reportDialog.dismiss();
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
