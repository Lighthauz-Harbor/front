package com.example.dvidr_000.lighthauzproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class BMCFragment extends Fragment implements View.OnClickListener{


    public BMCFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bmc, container, false);

        getActivity().setTitle("Make BMC");

        Button nextBtn = (Button) v.findViewById(R.id.btnNextIdeaBMC);

        nextBtn.setOnClickListener(this);


        return v;
    }

    public void onClick(View view) {
        if(view.getId()==R.id.btnNextIdeaBMC){

            Bundle args = getArguments();

            BMCContentFragment fragment = new BMCContentFragment();
            fragment.setArguments(args);

            FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
            tr.replace(R.id.fragment_container_detail,fragment);
            tr.addToBackStack(null);
            tr.commit();
        }
    }


}
