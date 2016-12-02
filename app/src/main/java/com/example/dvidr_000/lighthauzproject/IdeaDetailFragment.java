package com.example.dvidr_000.lighthauzproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class IdeaDetailFragment extends Fragment implements View.OnClickListener{

    private ProgressBar pb;
    private RelativeLayout layout;
    private TextView title;
    private TextView desc;
    private TextView category;
    private TextView createdBy;
    private TextView problem;
    private TextView background;
    private TextView solution;
    private TextView lastEdited;
    private ImageView pic;

    private String idStr;
    private String titleStr;
    private String descStr;
    private String categoryStr;
    private String problemStr;
    private String backgroundStr;
    private String solutionStr;

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



    public IdeaDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.menu_idea_detail, menu);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_idea_detail, container, false);
        setHasOptionsMenu(true);

        getActivity().setTitle("Idea Details");
        idStr = getActivity().getIntent().getStringExtra("IDEA_ID");

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
        pic = (ImageView) v.findViewById(R.id.ic_idea_detail);

        Button showBMC = (Button) v.findViewById(R.id.btnShowBMC);
        Button showSWOT = (Button) v.findViewById(R.id.btnShowSWOT);

        showBMC.setOnClickListener(this);
        showSWOT.setOnClickListener(this);
        createdBy.setOnClickListener(this);

        setDetails(v);

        return v;
    }

    public void setDetails(View v){
        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/ideas/get/";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url + idStr ,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());

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
                            lastEdited.setText(MyAdapter.setDate(timestamp));
                            createdBy.setText(name);

                            pb.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
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
    public void onClick(View view) {

        Bundle args = new Bundle();

        switch (view.getId()){
            case R.id.btnShowBMC:
                args.putString("VP",valueProposition);
                args.putString("CS",customerSegments);
                args.putString("KP",keyPartners);
                args.putString("COST",costStructure);
                args.putString("RS",revenueStreams);
                args.putString("KA",keyActivities);
                args.putString("KR",keyResources);
                args.putString("CH",channels);
                args.putString("CR",customerRelationships);

                BMCContentFragment fragment1 = new BMCContentFragment();
                fragment1.setArguments(args);

                FragmentTransaction tr1 = getActivity().getSupportFragmentManager().beginTransaction();
                tr1.replace(R.id.fragment_container_detail,fragment1);
                tr1.addToBackStack(null);
                tr1.commit();
                break;
            case R.id.btnShowSWOT:


                args.putString("STRENGTH",strengths);
                args.putString("WEAKNESS",weaknesses);
                args.putString("OPPORTUNITY",opportunities);
                args.putString("THREAT",threats);

                SWOTFragment fragment = new SWOTFragment();
                fragment.setArguments(args);

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.fragment_container_detail,fragment);
                tr.addToBackStack(null);
                tr.commit();
                break;
            case R.id.tv_idea_detail_created_text:
                args.putString("USER_ID",userId);

                ViewProfileFragment fragment2 = new ViewProfileFragment();
                fragment2.setArguments(args);

                FragmentTransaction tr2 = getActivity().getSupportFragmentManager().beginTransaction();
                tr2.replace(R.id.fragment_container_detail,fragment2);
                tr2.addToBackStack(null);
                tr2.commit();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuReport:
                Toast.makeText(getActivity(),"Reported",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuSuggestion:
                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.fragment_container_detail,new SuggestionFragment());
                tr.addToBackStack(null);
                tr.commit();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


}
