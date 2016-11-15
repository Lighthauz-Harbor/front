package com.example.dvidr_000.lighthauzproject;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class IdeaInfoFragment extends Fragment implements View.OnClickListener{


    public IdeaInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_idea_info, container, false);

        getActivity().setTitle("Idea Information");

        Button nextBtn = (Button) v.findViewById(R.id.btnNextIdeaInfo);

        nextBtn.setOnClickListener(this);

        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnNextIdeaInfo){

            FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
            tr.replace(R.id.fragment_container_detail,new BackgroundFragment());
            tr.addToBackStack(null);
            tr.commit();
        }
    }
}
