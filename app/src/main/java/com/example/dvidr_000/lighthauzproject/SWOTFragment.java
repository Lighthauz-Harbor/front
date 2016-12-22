package com.example.dvidr_000.lighthauzproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SWOTFragment extends Fragment implements View.OnClickListener{

    private EditText strength;
    private EditText weakness;
    private EditText opportunity;
    private EditText threat;
    private Bundle ideaBundle;
    private String content;

    public SWOTFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_swot, container, false);

        content = getActivity().getIntent().getStringExtra("EXTRA_CONTENT");
        ideaBundle = getArguments();

        ImageView q1;
        ImageView q2;
        ImageView q3;
        ImageView q4;
        q1 = (ImageView) v.findViewById(R.id.QuestionMarkStrenght);
        q1.setOnClickListener(this);
        q2 = (ImageView) v.findViewById(R.id.QuestionMarkWeakness);
        q2.setOnClickListener(this);
        q3 = (ImageView) v.findViewById(R.id.QuestionMarkOpportunities);
        q3.setOnClickListener(this);
        q4 = (ImageView) v.findViewById(R.id.QuestionMarkThreat);
        q4.setOnClickListener(this);

        Button nextBtn;
        nextBtn = (Button) v.findViewById(R.id.btnNextIdeaSWOT);
        nextBtn.setOnClickListener(this);
        strength = (EditText) v.findViewById(R.id.etStrengthFill);
        weakness = (EditText) v.findViewById(R.id.etWeaknessfill);
        opportunity = (EditText) v.findViewById(R.id.etOpportunitiesfill);
        threat = (EditText) v.findViewById(R.id.etThreatfill);

        if(content.equals("CREATE_IDEA")){
            getActivity().setTitle(R.string.KnowYourSwot);
        }
        else {
            getActivity().setTitle("The SWOT");

            strength.setText(ideaBundle.getString("STRENGTH"));
            weakness.setText(ideaBundle.getString("WEAKNESS"));
            opportunity.setText(ideaBundle.getString("OPPORTUNITY"));
            threat.setText(ideaBundle.getString("THREAT"));

            if (!content.equals("EDIT_IDEA")) {
                nextBtn.setVisibility(View.INVISIBLE);
                weakness.setEnabled(false);
                strength.setEnabled(false);
                opportunity.setEnabled(false);
                threat.setEnabled(false);
            }
        }
        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnNextIdeaSWOT){
            if (validate()){
                ideaBundle.putString("STRENGTH",strength.getText().toString());
                ideaBundle.putString("WEAKNESS",weakness.getText().toString());
                ideaBundle.putString("OPPORTUNITY",opportunity.getText().toString());
                ideaBundle.putString("THREAT",threat.getText().toString());

                //call create idea function in DetailActivity
                ((DetailActivity)getActivity()).createIdea(ideaBundle,content);
            }
            else {
                Toast.makeText(getContext(), R.string.EmptyField, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            AlertDialog.Builder hint = new AlertDialog.Builder(getActivity());
            AlertDialog alert;
            String title="";
            switch (view.getId()) {
                case R.id.QuestionMarkStrenght:
                    hint.setMessage(R.string.StrengthText);
                    title = "Strengths";
                    break;
                case R.id.QuestionMarkWeakness:
                    hint.setMessage(R.string.WeaknessText);
                    title = "Weaknesses";
                    break;
                case R.id.QuestionMarkOpportunities:
                    hint.setMessage(R.string.OpportunitiesText);
                    title = "Opportunities";
                    break;
                case R.id.QuestionMarkThreat:
                    hint.setMessage(R.string.ThreatText);
                    title = "Threats";
                    break;
            }
            hint.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert = hint.create();
            alert.setTitle(title);
            alert.show();
        }
    }

    public boolean validate(){
        if (strength.getText().toString().isEmpty()){
            strength.requestFocus();
            return false;
        }
        else if (weakness.getText().toString().isEmpty()){
            weakness.requestFocus();
            return false;
        }
        else if (opportunity.getText().toString().isEmpty()){
            opportunity.requestFocus();
            return false;
        }
        else if (threat.getText().toString().isEmpty()){
            threat.requestFocus();
            return false;
        }
        return true;
    }
}
