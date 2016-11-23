package com.example.dvidr_000.lighthauzproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class IdeaDetailFragment extends Fragment implements View.OnClickListener{

    int loginIndex = 0;
    int ideaId;
    ArrayList<Idea> selected = new ArrayList<>();

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
        loginIndex = getActivity().getIntent().getIntExtra("LOGIN_INDEX",0);
        ideaId = getActivity().getIntent().getIntExtra("IDEA_ID",0);

        Button showBMC = (Button) v.findViewById(R.id.btnShowBMC);
        Button showSWOT = (Button) v.findViewById(R.id.btnShowSWOT);

        showBMC.setOnClickListener(this);
        showSWOT.setOnClickListener(this);

        setDetails(v);

        return v;
    }

    public void setDetails(View v){

        TextView title = (TextView) v.findViewById(R.id.tv_idea_detail_title);
        TextView desc = (TextView) v.findViewById(R.id.tv_idea_detail_desc_text);
        TextView category = (TextView) v.findViewById(R.id.tv_idea_detail_category_text);
        TextView createdBy = (TextView) v.findViewById(R.id.tv_idea_detail_created_text);
        TextView problem = (TextView) v.findViewById(R.id.tv_idea_detail_problem_text);
        TextView background = (TextView) v.findViewById(R.id.tv_idea_detail_background_text);
        TextView solution = (TextView) v.findViewById(R.id.tv_idea_detail_solution_text);
        TextView edit = (TextView) v.findViewById(R.id.tv_idea_detail_lastedited_text);

        Idea idea = Idea.getIdeas().get(ideaId);


        title.setText(idea.getTitle());
        desc.setText(idea.getDescription());
        category.setText(idea.getCategory());
        createdBy.setText(User.getUsers().get(loginIndex).getName());
        edit.setText("-");
        problem.setText(idea.getProblem());
        background.setText(idea.getBackground());
        solution.setText(idea.getSolution());

    }

    @Override
    public void onClick(View view) {

        Idea idea = Idea.getIdeas().get(ideaId);
        Bundle args = new Bundle();

        switch (view.getId()){
            case R.id.btnShowBMC:
                args.putString("VP",idea.getValueProposition());
                args.putString("CS",idea.getCustomerSegment());
                args.putString("KP",idea.getKeyPartner());
                args.putString("COST",idea.getCostStructure());
                args.putString("RS",idea.getRevenueStream());
                args.putString("KA",idea.getKeyActivities());
                args.putString("KR",idea.getKeyResources());
                args.putString("CH",idea.getChannel());
                args.putString("CR",idea.getCustomerRelationship());

                BMCContentFragment fragment1 = new BMCContentFragment();
                fragment1.setArguments(args);

                FragmentTransaction tr1 = getActivity().getSupportFragmentManager().beginTransaction();
                tr1.replace(R.id.fragment_container_detail,fragment1);
                tr1.addToBackStack(null);
                tr1.commit();
                break;
            case R.id.btnShowSWOT:


                args.putString("STRENGTH",idea.getStrength());
                args.putString("WEAKNESS",idea.getWeakness());
                args.putString("OPPORTUNITY",idea.getOpportuniities());
                args.putString("THREAT",idea.getThreat());

                SWOTFragment fragment = new SWOTFragment();
                fragment.setArguments(args);

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.fragment_container_detail,fragment);
                tr.addToBackStack(null);
                tr.commit();

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
