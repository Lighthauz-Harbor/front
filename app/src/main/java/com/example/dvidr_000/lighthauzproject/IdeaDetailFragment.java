package com.example.dvidr_000.lighthauzproject;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
public class IdeaDetailFragment extends Fragment implements View.OnClickListener{

    private AlertDialog alertDialog;
    private ProgressBar pb;
    private ProgressDialog pDialog;
    private RelativeLayout layout;
    private TextView title;
    private TextView desc;
    private TextView category;
    private TextView createdBy;
    private TextView problem;
    private TextView background;
    private TextView solution;
    private TextView lastEdited;
    private TextView likes;
    private TextView comments;
    private ImageView pic;
    private ImageView profPic;
    private ImageButton likeBtn;
    private Dialog reportDialog;
    private EditText reportTitle;
    private EditText reportMessage;
    private Button reportSubmit;

    private String idStr;
    private String titleStr;
    private String descStr;
    private String categoryStr;
    private String problemStr;
    private String backgroundStr;
    private String solutionStr;
    private boolean likeStatus;
    private int likeCount;
    private String reportTitleStr;
    private String reportMessageStr;

    private String valueProposition;
    private String customerSegments;
    private String customerRelationships;
    private String channels;
    private String costStructure;
    private String revenueStreams;
    private String keyActivities;
    private String keyResources;
    private String keyPartners;
    private String strengths;
    private String weaknesses;
    private String opportunities;
    private String threats;
    private String extraLink;
    private String picStr;
    private int visibility;
    private Long timestamp;

    private String name;
    private String userId;
    private String email;
    private String profilePicStr;

    private SessionManager sessionManager;
    private HashMap<String,String> user;
    private Bundle ideaBundle;

    private MenuItem menuEditIdea;
    private MenuItem menuSuggestion;
    private MenuItem menuReport;
    private MenuItem menuDeleteIdea;

    private List<User> likers;

    public IdeaDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.menu_idea_detail, menu);
        menuEditIdea = (MenuItem) menu.findItem(R.id.menuEditIdea);
        menuSuggestion = (MenuItem) menu.findItem(R.id.menuSuggestion);
        menuReport =(MenuItem) menu.findItem(R.id.menuReportIdea);
        menuDeleteIdea =(MenuItem) menu.findItem(R.id.menuDeleteIdea);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_idea_detail, container, false);
        setHasOptionsMenu(true);

        likers = new ArrayList<>();
        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();
        getActivity().setTitle("Idea Details");

        try {
            idStr = getArguments().getString("IDEA_ID");
        }
        catch (Exception e){
            idStr = getActivity().getIntent().getStringExtra("IDEA_ID");
        }

        ideaBundle = new Bundle();

        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        layout = (RelativeLayout) v.findViewById(R.id.idea_detail_layout);
        layout.setVisibility(View.GONE);
        pb = (ProgressBar) v.findViewById(R.id.pBarIdeaDetail);
        title = (TextView) v.findViewById(R.id.tv_idea_detail_title);
        desc = (TextView) v.findViewById(R.id.tv_idea_detail_desc_text);
        category = (TextView) v.findViewById(R.id.tv_idea_detail_category_text);
        createdBy = (TextView) v.findViewById(R.id.tv_idea_detail_created_text);
        problem = (TextView) v.findViewById(R.id.tv_idea_detail_problem_text);
        background = (TextView) v.findViewById(R.id.tv_idea_detail_background_text);
        solution = (TextView) v.findViewById(R.id.tv_idea_detail_solution_text);
        lastEdited = (TextView) v.findViewById(R.id.tv_idea_detail_lastedited_text);
        likes = (TextView) v.findViewById(R.id.tv_idea_detail_like);
        comments = (TextView) v.findViewById(R.id.tv_idea_detail_comment);
        pic = (ImageView) v.findViewById(R.id.ic_idea_detail);
        profPic = (ImageView) v.findViewById(R.id.ic_idea_detail_prof);
        Button showBMC = (Button) v.findViewById(R.id.btnShowBMC);
        Button showSWOT = (Button) v.findViewById(R.id.btnShowSWOT);
        likeBtn = (ImageButton) v.findViewById(R.id.ic_like_button);

        reportDialog = new Dialog(getContext());
        reportDialog.setContentView(R.layout.dialog_report);
        reportTitle = (EditText) reportDialog.findViewById(R.id.et_report_title);
        reportMessage = (EditText) reportDialog.findViewById(R.id.et_report_message);
        reportSubmit = (Button) reportDialog.findViewById(R.id.btn_submit_dialog_report);

        showBMC.setOnClickListener(this);
        showSWOT.setOnClickListener(this);
        createdBy.setOnClickListener(this);
        profPic.setOnClickListener(this);
        likeBtn.setOnClickListener(this);
        likes.setOnClickListener(this);
        comments.setOnClickListener(this);
        reportSubmit.setOnClickListener(this);

        AlertDialog.Builder removeDialog = new AlertDialog.Builder(getActivity());
        removeDialog.setMessage(R.string.ConfirmRemoveIdea)
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(R.string.Yes,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        JSONArray list = new JSONArray();
                        list.put(idStr);
                        deleteIdea(list);
                        dialogInterface.dismiss();
                    }
                });
        alertDialog = removeDialog.create();
        alertDialog.setTitle("Confirm");
        likes.setText("likes");
        comments.setText("comments");

        getLike();
        getComment();
        setDetails();

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnShowBMC:

                BMCContentFragment fragment1 = new BMCContentFragment();
                fragment1.setArguments(ideaBundle);

                FragmentTransaction tr1 = getActivity().getSupportFragmentManager().beginTransaction();
                tr1.replace(R.id.fragment_container_detail,fragment1);
                tr1.addToBackStack(null);
                tr1.commit();
                break;
            case R.id.btnShowSWOT:

                SWOTFragment fragment = new SWOTFragment();
                fragment.setArguments(ideaBundle);

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.fragment_container_detail,fragment);
                tr.addToBackStack(null);
                tr.commit();
                break;
            case R.id.ic_idea_detail_prof:
            case R.id.tv_idea_detail_created_text:
                Bundle args = new Bundle();
                args.putString("USER_ID",userId);

                ViewProfileFragment fragment2 = new ViewProfileFragment();
                fragment2.setArguments(args);

                FragmentTransaction tr2 = getActivity().getSupportFragmentManager().beginTransaction();
                tr2.replace(R.id.fragment_container_detail,fragment2);
                tr2.addToBackStack(null);
                tr2.commit();
                break;
            case R.id.ic_like_button:
                if (likeStatus){
                like("unlike");
            }
                else {
                    like("like");
                }
                break;
            case R.id.tv_idea_detail_like:
                Bundle args3 = new Bundle();
                args3.putString("IDEA_ID",idStr);

                LikeListFragment fragment3 = new LikeListFragment();
                fragment3.setArguments(args3);

                FragmentTransaction tr3 = getActivity().getSupportFragmentManager().beginTransaction();
                tr3.replace(R.id.fragment_container_detail,fragment3);
                tr3.addToBackStack(null);
                tr3.commit();
                break;
            case R.id.tv_idea_detail_comment:
                Bundle args4 = new Bundle();
                args4.putString("IDEA_ID",idStr);

                CommentFragment fragment4 = new CommentFragment();
                fragment4.setArguments(args4);

                FragmentTransaction tr4 = getActivity().getSupportFragmentManager().beginTransaction();
                tr4.replace(R.id.fragment_container_detail,fragment4);
                tr4.addToBackStack(null);
                tr4.commit();
                break;
            case R.id.btn_submit_dialog_report:
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
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuReportIdea:
                reportDialog.show();
                break;
            case R.id.menuSuggestion:
                Bundle args = new Bundle();
                args.putString("CATEGORY",categoryStr);

                SuggestionFragment fragment = new SuggestionFragment();
                fragment.setArguments(args);

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.fragment_container_detail,fragment);
                tr.addToBackStack(null);
                tr.commit();
                break;
            case R.id.menuEditIdea:
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("EXTRA_CONTENT","EDIT_IDEA");
                intent.putExtra("IDEA_BUNDLE",ideaBundle);
                startActivity(intent);
                break;
            case R.id.menuDeleteIdea:
                alertDialog.show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void setDetails(){
        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/ideas/get/";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url + idStr ,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject ideaObj = response.getJSONObject("idea");
                            JSONObject author = response.getJSONObject("author");
                            titleStr = ideaObj.getString("title");
                            categoryStr = response.getString("category");
                            descStr = ideaObj.getString("description");
                            timestamp = response.getLong("timestamp");
                            backgroundStr = ideaObj.getString("background");
                            problemStr = ideaObj.getString("problem");
                            solutionStr = ideaObj.getString("solution");
                            valueProposition = ideaObj.getString("valueProposition");
                            customerSegments = ideaObj.getString("customerSegments");
                            customerRelationships = ideaObj.getString("customerRelationships");
                            channels = ideaObj.getString("channels");
                            keyActivities = ideaObj.getString("keyActivities");
                            keyResources = ideaObj.getString("keyResources");
                            keyPartners = ideaObj.getString("keyPartners");
                            costStructure = ideaObj.getString("costStructure");
                            revenueStreams = ideaObj.getString("revenueStreams");
                            strengths = ideaObj.getString("strengths");
                            weaknesses = ideaObj.getString("weaknesses");
                            opportunities = ideaObj.getString("opportunities");
                            threats = ideaObj.getString("threats");
                            extraLink = ideaObj.getString("extraLink");
                            picStr = ideaObj.getString("pic");
                            visibility = response.getInt("visibility");

                            userId = author.getString("id");
                            name = author.getString("name");
                            email = author.getString("email");
                            profilePicStr = author.getString("profilePic");

                            title.setText(titleStr);
                            desc.setText(descStr);
                            category.setText(categoryStr);
                            createdBy.setText(titleStr);
                            lastEdited.setText("-");
                            problem.setText(problemStr);
                            background.setText(backgroundStr);
                            solution.setText(solutionStr);
                            MyAdapter.imageLoader(picStr,pic);
                            MyAdapter.imageLoader(profilePicStr,profPic);
                            lastEdited.setText(MyAdapter.setDate(timestamp));
                            createdBy.setText(name);

                            pb.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                            if (userId.equals(user.get(SessionManager.KEY_ID))){
                                menuEditIdea.setVisible(true);
                                menuSuggestion.setVisible(true);
                                menuDeleteIdea.setVisible(true);

                                ideaBundle.putString("IDEA_ID",idStr);
                                ideaBundle.putString("PIC",picStr);
                                ideaBundle.putString("TITLE",titleStr);
                                ideaBundle.putString("CATEGORY",categoryStr);
                                ideaBundle.putString("DESCRIPTION",descStr);
                                ideaBundle.putString("BACKGROUND",backgroundStr);
                                ideaBundle.putString("PROBLEM",problemStr);
                                ideaBundle.putString("SOLUTION",solutionStr);
                                ideaBundle.putString("EXTRA_LINK",extraLink);
                                ideaBundle.putInt("VISIBILITY",visibility);

                            }
                            else {
                            }
                            ideaBundle.putString("VP",valueProposition);
                            ideaBundle.putString("CS",customerSegments);
                            ideaBundle.putString("KP",keyPartners);
                            ideaBundle.putString("COST",costStructure);
                            ideaBundle.putString("RS",revenueStreams);
                            ideaBundle.putString("KA",keyActivities);
                            ideaBundle.putString("KR",keyResources);
                            ideaBundle.putString("CH",channels);
                            ideaBundle.putString("CR",customerRelationships);
                            ideaBundle.putString("STRENGTH",strengths);
                            ideaBundle.putString("WEAKNESS",weaknesses);
                            ideaBundle.putString("OPPORTUNITY",opportunities);
                            ideaBundle.putString("THREAT",threats);

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

    public void deleteIdea(JSONArray list){
        pDialog.setMessage("Deleting...");
        pDialog.show();

        // Tag used to cancel the request
        String tag_json = "json_object_req";
        String url = "http://lighthauz.herokuapp.com/api/ideas/delete";

        JSONObject obj = new JSONObject();

        try {
            obj.putOpt("ids",list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String msg = response.getString("message");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
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

    public void like(final String like){
        // Tag used to cancel the request
        String tag_json = "json_object_req";
        String url = "http://lighthauz.herokuapp.com/api/" + like;

        HashMap<String,String> params = new HashMap<>();
        params.put("userId",user.get(SessionManager.KEY_ID));
        params.put("ideaId",idStr);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String msg = response.getString("fail");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
        }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                if (mStatusCode==HttpURLConnection.HTTP_OK){
                    Log.d(TAG,"Success");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String likeStr;
                            if (like.equals("like")){
                                likeBtn.setImageResource(R.drawable.ic_heart_red);
                                likeCount++;
                                likeStatus=true;
                            }
                            else {
                                likeBtn.setImageResource(R.drawable.ic_heart);
                                likeCount--;
                                likeStatus=false;
                            }

                            likeStr = likeCount + " likes";
                            likes.setText(likeStr);
                        }
                    });

                }
                return super.parseNetworkResponse(response);
            }
        };

        // Adding request to request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(req, tag_json);
    }

    public void getLike(){
        // Tag used to cancel the request
        String tag_json = "json_object_req";
        String url = "http://lighthauz.herokuapp.com/api/like/list/"+idStr;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());
                        JSONArray myArray;
                        likers.clear();
                        if (response.isNull("fail")) {
                            try {
                                myArray = response.getJSONArray("list");
                                if (myArray.length()==0){
                                }
                                else {
                                    for (int i = 0; i < myArray.length(); i++) {
                                        String userId = myArray.getJSONObject(i).getString("id");
                                        String name = myArray.getJSONObject(i).getString("name");
                                        String profilePic = myArray.getJSONObject(i).getString("profilePic");

                                        User newUser = new User(userId, name, profilePic);
                                        likers.add(newUser);
                                    }
                                }

                                likeCount = likers.size();
                                String likeStr =  likeCount+ " likes";
                                likes.setText(likeStr);
                                likeStatus=false;

                                for (int i=0;i<likers.size();i++){
                                    if (likers.get(i).getId().equals(user.get(SessionManager.KEY_ID))){
                                        likeStatus=true;
                                        likeBtn.setImageResource(R.drawable.ic_heart_red);
                                        break;
                                    }
                                }

                            } catch (JSONException e) {
                                Log.e("MYAPP", "unexpected JSON exception", e);
                            }
                        }
                        else {
                            try {
                                String msg = response.getString("fail");
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Log.e("MYAPP", "unexpected JSON exception", e);
                            }
                        }

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

    public void getComment(){
        // Tag used to cancel the request
        String tag_json = "json_object_req";
        String url = "http://lighthauz.herokuapp.com/api/comment/list/"+idStr;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray myArray;
                        if (response.isNull("fail")) {
                            try {
                                myArray = response.getJSONArray("list");
                                String commentStr = myArray.length() + " comments";
                                comments.setText(commentStr);

                            } catch (JSONException e) {
                                Log.e("MYAPP", "unexpected JSON exception", e);
                            }
                        }
                        else {
                            try {
                                String msg = response.getString("fail");
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Log.e("MYAPP", "unexpected JSON exception", e);
                            }
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
