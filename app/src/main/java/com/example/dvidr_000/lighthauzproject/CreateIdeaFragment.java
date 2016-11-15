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
public class CreateIdeaFragment extends Fragment implements View.OnClickListener{


    public CreateIdeaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_idea, container, false);

        getActivity().setTitle("Add New Idea");

        Button nextBtn = (Button) v.findViewById(R.id.btnNextCreateIdea);

        nextBtn.setOnClickListener(this);

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnNextCreateIdea){

            FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
            tr.replace(R.id.fragment_container_detail,new IdeaInfoFragment());
            tr.addToBackStack(null);
            tr.commit();
        }
    }
}
