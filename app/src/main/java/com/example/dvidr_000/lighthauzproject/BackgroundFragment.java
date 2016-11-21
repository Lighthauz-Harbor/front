package com.example.dvidr_000.lighthauzproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class BackgroundFragment extends Fragment implements View.OnClickListener {


    public BackgroundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_background, container, false);

        getActivity().setTitle("Idea Background");

        Button nextBtn = (Button) v.findViewById(R.id.btnNextIdeaBackground);

        nextBtn.setOnClickListener(this);


        return v;
    }

    public void onClick(View view) {
        if(view.getId()==R.id.btnNextIdeaBackground){

            Bundle args = getArguments();
            View v = getView();

            EditText background = (EditText) v.findViewById(R.id.etIdeaBackgroundFill);

            args.putString("BACKGROUND",background.getText().toString());

            ProblemFragment fragment = new ProblemFragment();
            fragment.setArguments(args);

            FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
            tr.replace(R.id.fragment_container_detail,fragment);
            tr.addToBackStack(null);
            tr.commit();
        }
    }




    }


