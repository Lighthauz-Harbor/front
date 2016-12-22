package com.example.dvidr_000.lighthauzproject;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
public class BMCContentFragment extends Fragment implements View.OnClickListener{
    private EditText vp;
    private EditText cs;
    private EditText kp;
    private EditText cost;
    private EditText rs;
    private EditText ka;
    private EditText kr;
    private EditText ch;
    private EditText cr;
    private Bundle ideaBundle;

    public BMCContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bmccontent, container, false);

        ImageView q1;
        ImageView q2;
        ImageView q3;
        ImageView q4;
        ImageView q5;
        ImageView q6;
        ImageView q7;
        ImageView q8;
        ImageView q9;

        q1 = (ImageView) v.findViewById(R.id.QuestionMarkVP);
        q1.setOnClickListener(this);
        q2 = (ImageView) v.findViewById(R.id.QuestionMarkChannel);
        q2.setOnClickListener(this);
        q3 = (ImageView) v.findViewById(R.id.QuestionMarkCost);
        q3.setOnClickListener(this);
        q4 = (ImageView) v.findViewById(R.id.QuestionMarkCR);
        q4.setOnClickListener(this);
        q5 = (ImageView) v.findViewById(R.id.QuestionMarkCS);
        q5.setOnClickListener(this);
        q6 = (ImageView) v.findViewById(R.id.QuestionMarkKA);
        q6.setOnClickListener(this);
        q7 = (ImageView) v.findViewById(R.id.QuestionMarkKP);
        q7.setOnClickListener(this);
        q8 = (ImageView) v.findViewById(R.id.QuestionMarkKR);
        q8.setOnClickListener(this);
        q9 = (ImageView) v.findViewById(R.id.QuestionMarkRS);
        q9.setOnClickListener(this);

        Button nextBtn;
        nextBtn = (Button) v.findViewById(R.id.btnNextIdeaBMCContent);
        nextBtn.setOnClickListener(this);

        vp = (EditText) v.findViewById(R.id.etBMCVPfill);
        cs = (EditText) v.findViewById(R.id.etBMCCSfill);
        kp = (EditText) v.findViewById(R.id.etBMCKPfill);
        cost = (EditText) v.findViewById(R.id.etBMCCOSTfill);
        rs = (EditText) v.findViewById(R.id.etBMCRSfill);
        ka = (EditText) v.findViewById(R.id.etBMCKAfill);
        kr = (EditText) v.findViewById(R.id.etBMCKRfill);
        ch = (EditText) v.findViewById(R.id.etBMCChannelfill);
        cr = (EditText) v.findViewById(R.id.etBMCCRfill);

        String content = getActivity().getIntent().getStringExtra("EXTRA_CONTENT");
        ideaBundle = getArguments();

        if (content.equals("CREATE_IDEA")) {
            getActivity().setTitle("Make BMC");
        } else {
            vp.setText(ideaBundle.getString("VP"));
            cs.setText(ideaBundle.getString("CS"));
            kp.setText(ideaBundle.getString("KP"));
            cost.setText(ideaBundle.getString("COST"));
            rs.setText(ideaBundle.getString("RS"));
            ka.setText(ideaBundle.getString("KA"));
            kr.setText(ideaBundle.getString("KR"));
            ch.setText(ideaBundle.getString("CH"));
            cr.setText(ideaBundle.getString("CR"));

            if (!content.equals("EDIT_IDEA")) {
                getActivity().setTitle("The BMC");
                nextBtn.setVisibility(View.INVISIBLE);
                vp.setEnabled(false);
                cs.setEnabled(false);
                kp.setEnabled(false);
                cost.setEnabled(false);
                rs.setEnabled(false);
                ka.setEnabled(false);
                kr.setEnabled(false);
                ch.setEnabled(false);
                cr.setEnabled(false);
            }
        }

        return v;
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.btnNextIdeaBMCContent) {
            if (validate()) {
                ideaBundle.putString("VP", vp.getText().toString());
                ideaBundle.putString("CS", cs.getText().toString());
                ideaBundle.putString("KP", kp.getText().toString());
                ideaBundle.putString("COST", cost.getText().toString());
                ideaBundle.putString("RS", rs.getText().toString());
                ideaBundle.putString("KA", ka.getText().toString());
                ideaBundle.putString("KR", kr.getText().toString());
                ideaBundle.putString("CH", ch.getText().toString());
                ideaBundle.putString("CR", cr.getText().toString());

                SWOTFragment fragment = new SWOTFragment();
                fragment.setArguments(ideaBundle);

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.fragment_container_detail, fragment);
                tr.addToBackStack(null);
                tr.commit();
            } else {
                Toast.makeText(getContext(), R.string.EmptyField, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            AlertDialog.Builder hint = new AlertDialog.Builder(getActivity());
            AlertDialog alert;
            String title="";
            switch (view.getId()){
                case R.id.QuestionMarkVP:
                    hint.setMessage(R.string.ValuePropositionText);
                    title = "Value Propositions";
                    break;
                case R.id.QuestionMarkChannel:
                    hint.setMessage(R.string.ChannelText);
                    title = "Channels";
                    break;
                case R.id.QuestionMarkCost:
                    hint.setMessage(R.string.CostStructureText);
                    title = "Cost Structure";
                    break;
                case R.id.QuestionMarkCR:
                    hint.setMessage(R.string.CustomerRelationshipText);
                    title = "Customer Relationships";
                    break;
                case R.id.QuestionMarkCS:
                    hint.setMessage(R.string.CustomerSegmentText);
                    title = "Customer Segments";
                    break;
                case R.id.QuestionMarkKA:
                    hint.setMessage(R.string.KeyActivitiesText);
                    title = "Key Activities";
                    break;
                case R.id.QuestionMarkKP:
                    hint.setMessage(R.string.KeyPartnersText);
                    title = "Key Partnerships";
                    break;
                case R.id.QuestionMarkKR:
                    hint.setMessage(R.string.KeyResourcesText);
                    title = "Key Resources";
                    break;
                case R.id.QuestionMarkRS:
                    hint.setMessage(R.string.RevenueStreamsText);
                    title = "Revenue Streams";
                    break;

            }
            hint.setPositiveButton("CLOSE",new DialogInterface.OnClickListener() {
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
        if (vp.getText().toString().isEmpty()){
            vp.requestFocus();
            return false;
        }
        else if (cs.getText().toString().isEmpty()){
            cs.requestFocus();
            return false;
        }
        else if (kp.getText().toString().isEmpty()){
            kp.requestFocus();
            return false;
        }
        else if (cost.getText().toString().isEmpty()){
            cost.requestFocus();
            return false;
        }
        else if (rs.getText().toString().isEmpty()){
            rs.requestFocus();
            return false;
        }
        else if (ka.getText().toString().isEmpty()){
            ka.requestFocus();
            return false;
        }
        else if (kr.getText().toString().isEmpty()){
            kr.requestFocus();
            return false;
        }
        else if (ch.getText().toString().isEmpty()){
            ch.requestFocus();
            return false;
        }
        else if (cr.getText().toString().isEmpty()){
            cr.requestFocus();
            return false;
        }
        return true;
    }

}
