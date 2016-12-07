package com.example.dvidr_000.lighthauzproject;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    int loginIndex = 0;

    FragmentTransaction tr;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getIntent().getIntExtra("LOGIN_INDEX",loginIndex);
        String content= getIntent().getStringExtra("EXTRA_CONTENT");

        fm = getSupportFragmentManager();
        tr = fm.beginTransaction();

        switch (content){
            case "IDEA_DETAIL":
                tr.replace(R.id.fragment_container_detail,new IdeaDetailFragment());
                tr.addToBackStack(null);
                tr.commit();
                break;
            case "CREATE_IDEA":
                tr.replace(R.id.fragment_container_detail,new CreateIdeaFragment());
                tr.addToBackStack(null);
                tr.commit();
                break;
            case "VIEW_PROFILE":
                tr.replace(R.id.fragment_container_detail,new ViewProfileFragment());
                tr.addToBackStack(null);
                tr.commit();
                break;
            case "FIRST_LOGIN":

                tr.replace(R.id.fragment_container_detail,new FirstLoginFragment());
                tr.addToBackStack(null);
                tr.commit();
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setHomeButtonEnabled(false); // disable the button
                    actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
                    actionBar.setDisplayShowHomeEnabled(false); // remove the icon
                }
                break;
        }
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
                    Intent parentIntent = NavUtils.getParentActivityIntent(this);
                    parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(parentIntent);
                    finish();
                    return true;
                }


        }
        return super.onOptionsItemSelected(item);
    }

    public void createIdea(Bundle idea){
        String title = idea.getString("TITLE");
        String category  = idea.getString("CATEGORY");
        String  description = idea.getString("DESCRIPTION");
        int  publicity=0;
        String  background = idea.getString("BACKGROUND");
        String  problem = idea.getString("PROBLEM");
        String  solution = idea.getString("SOLUTION");
        String  valueProposition = idea.getString("VP");
        String  customerSegment = idea.getString("CS");
        String  customerRelationship = idea.getString("CR");
        String  channel = idea.getString("CH");
        String  keyActivities = idea.getString("KA");
        String  keyResources = idea.getString("KR");
        String  keyPartner = idea.getString("KP");
        String  costStructure = idea.getString("COST");
        String  revenueStream = idea.getString("RS");
        String  strength = idea.getString("STRENGTH");
        String  weakness = idea.getString("WEAKNESS");
        String  opportunities = idea.getString("OPPORTUNITY");
        String  threat = idea.getString("THREAT");

        Idea newIdea = new Idea(title,category,description,new Date().getTime(),publicity,background,problem,solution,valueProposition,customerSegment,customerRelationship,channel,keyActivities,keyResources,keyPartner,costStructure,revenueStream,strength,weakness,opportunities,threat,"","");

        Idea.getIdeas().add(newIdea);
        User.getUsers().get(loginIndex).getIdea().add(Idea.getIdeas().size()-1);

        Toast.makeText(this,"New Idea Added",Toast.LENGTH_SHORT).show();

    }


}
