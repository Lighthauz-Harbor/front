package com.example.dvidr_000.lighthauzproject;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        String content= getIntent().getStringExtra("EXTRA_CONTENT");

        switch (content){
            case "IDEA_DETAIL":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_detail,new IdeaDetailFragment()).commit();
                break;
            case "CREATE_IDEA":
                FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.fragment_container_detail,new CreateIdeaFragment());
                tr.addToBackStack(null);
                tr.commit();
                break;
        }



    }

// biar balik ke fragment idea list
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
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
}
