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
public class SolutionFragment extends Fragment implements View.OnClickListener{


    public SolutionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_solution, container, false);

        getActivity().setTitle("Idea Solution");

        Button nextBtn = (Button) v.findViewById(R.id.btnNextIdeaSolution);

        nextBtn.setOnClickListener(this);


        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnNextIdeaSolution){

            Bundle args = getArguments();
            View v = getView();

            EditText solution = (EditText) v.findViewById(R.id.etIdeaSolutionFill);

            args.putString("SOLUTION",solution.getText().toString());

            BMCFragment fragment = new BMCFragment();
            fragment.setArguments(args);

            FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
            tr.replace(R.id.fragment_container_detail,fragment);
            tr.addToBackStack(null);
            tr.commit();
        }
    }
}
