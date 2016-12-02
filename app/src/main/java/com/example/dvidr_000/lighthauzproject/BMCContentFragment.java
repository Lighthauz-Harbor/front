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


    public BMCContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bmccontent, container, false);

        ImageView q1 = (ImageView) v.findViewById(R.id.QuestionMarkVP);
        q1.setOnClickListener(this);

        Button nextBtn = (Button) v.findViewById(R.id.btnNextIdeaBMCContent);
        nextBtn.setOnClickListener(this);

        String content= getActivity().getIntent().getStringExtra("EXTRA_CONTENT");



        if(content.equals("CREATE_IDEA")){
            getActivity().setTitle("Make BMC");

            nextBtn.setOnClickListener(this);
        }
        else {
            Bundle args = getArguments();

            getActivity().setTitle("The BMC");
            nextBtn.setVisibility(View.INVISIBLE);

            EditText vp = (EditText) v.findViewById(R.id.etBMCVPfill);
            EditText cs = (EditText) v.findViewById(R.id.etBMCCSfill);
            EditText kp = (EditText) v.findViewById(R.id.etBMCKPfill);
            EditText cost = (EditText) v.findViewById(R.id.etBMCCOSTfill);
            EditText rs = (EditText) v.findViewById(R.id.etBMCRSfill);
            EditText ka = (EditText) v.findViewById(R.id.etBMCKAfill);
            EditText kr = (EditText) v.findViewById(R.id.etBMCKRfill);
            EditText ch = (EditText) v.findViewById(R.id.etBMCChannelfill);
            EditText cr = (EditText) v.findViewById(R.id.etBMCCRfill);

            vp.setText(args.getString("VP"));
            cs.setText(args.getString("CS"));
            kp.setText(args.getString("KP"));
            cost.setText(args.getString("COST"));
            rs.setText(args.getString("RS"));
            ka.setText(args.getString("KA"));
            kr.setText(args.getString("KR"));
            ch.setText(args.getString("CH"));
            cr.setText(args.getString("CR"));

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


        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.QuestionMarkVP){
            AlertDialog.Builder hint = new AlertDialog.Builder(getActivity());
            hint.setMessage("Value Proposition\n\nWhat value do you offer?")
                    .setPositiveButton("CLOSE",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog alert = hint.create();
            alert.setTitle("Tips");
            alert.show();
        }
        else if(view.getId()==R.id.btnNextIdeaBMCContent){

            Bundle args = getArguments();
            View v = getView();

            EditText vp = (EditText) v.findViewById(R.id.etBMCVPfill);
            EditText cs = (EditText) v.findViewById(R.id.etBMCCSfill);
            EditText kp = (EditText) v.findViewById(R.id.etBMCKPfill);
            EditText cost = (EditText) v.findViewById(R.id.etBMCCOSTfill);
            EditText rs = (EditText) v.findViewById(R.id.etBMCRSfill);
            EditText ka = (EditText) v.findViewById(R.id.etBMCKAfill);
            EditText kr = (EditText) v.findViewById(R.id.etBMCKRfill);
            EditText ch = (EditText) v.findViewById(R.id.etBMCChannelfill);
            EditText cr = (EditText) v.findViewById(R.id.etBMCCRfill);

            args.putString("VP",vp.getText().toString());
            args.putString("CS",cs.getText().toString());
            args.putString("KP",kp.getText().toString());
            args.putString("COST",cost.getText().toString());
            args.putString("RS",rs.getText().toString());
            args.putString("KA",ka.getText().toString());
            args.putString("KR",kr.getText().toString());
            args.putString("CH",ch.getText().toString());
            args.putString("CR",cr.getText().toString());

            SWOTFragment fragment = new SWOTFragment();
            fragment.setArguments(args);

            FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
            tr.replace(R.id.fragment_container_detail,fragment);
            tr.addToBackStack(null);
            tr.commit();
        }
    }






}
