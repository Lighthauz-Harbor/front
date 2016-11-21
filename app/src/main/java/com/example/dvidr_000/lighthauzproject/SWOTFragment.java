package com.example.dvidr_000.lighthauzproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class SWOTFragment extends Fragment implements View.OnClickListener{


    public SWOTFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_swot, container, false);

        String content= getActivity().getIntent().getStringExtra("EXTRA_CONTENT");

        Button nextBtn = (Button) v.findViewById(R.id.btnNextIdeaSWOT);

        if(content.equals("CREATE_IDEA")){
            getActivity().setTitle(R.string.KnowYourSwot);

            nextBtn.setOnClickListener(this);
        }
        else {
            Bundle args = getArguments();

            getActivity().setTitle("The SWOT");
            nextBtn.setVisibility(View.INVISIBLE);

            EditText strength = (EditText) v.findViewById(R.id.etStrengthFill);
            EditText weakness = (EditText) v.findViewById(R.id.etWeaknessfill);
            EditText opportunity = (EditText) v.findViewById(R.id.etOpportunitiesfill);
            EditText threat = (EditText) v.findViewById(R.id.etThreatfill);

            strength.setText(args.getString("STRENGTH"));
            strength.setEnabled(false);
            weakness.setText(args.getString("WEAKNESS"));
            weakness.setEnabled(false);
            opportunity.setText(args.getString("OPPORTUNITY"));
            opportunity.setEnabled(false);
            threat.setText(args.getString("THREAT"));
            threat.setEnabled(false);


        }



        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnNextIdeaSWOT){

            Bundle args = getArguments();
            View v = getView();

            EditText strength = (EditText) v.findViewById(R.id.etStrengthFill);
            EditText weakness = (EditText) v.findViewById(R.id.etWeaknessfill);
            EditText opportunity = (EditText) v.findViewById(R.id.etOpportunitiesfill);
            EditText threat = (EditText) v.findViewById(R.id.etThreatfill);

            args.putString("STRENGTH",strength.getText().toString());
            args.putString("WEAKNESS",weakness.getText().toString());
            args.putString("OPPORTUNITY",opportunity.getText().toString());
            args.putString("THREAT",threat.getText().toString());

            ((DetailActivity)getActivity()).createIdea(args);

            Intent parentIntent = NavUtils.getParentActivityIntent(getActivity());
            parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(parentIntent);
            getActivity().finish();

        }
    }
}
