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
public class ProblemFragment extends Fragment implements View.OnClickListener {


    public ProblemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_problem, container, false);

        getActivity().setTitle("Idea Problem");

        Button nextBtn = (Button) v.findViewById(R.id.btnNextIdeaProblem);

        nextBtn.setOnClickListener(this);


        return v;
    }


    public void onClick(View view) {
        if (view.getId() == R.id.btnNextIdeaProblem) {

            Bundle args = getArguments();
            View v = getView();

            EditText problem = (EditText) v.findViewById(R.id.etIdeaProblemFill);

            args.putString("PROBLEM",problem.getText().toString());

            SolutionFragment fragment = new SolutionFragment();
            fragment.setArguments(args);

            FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
            tr.replace(R.id.fragment_container_detail, fragment);
            tr.addToBackStack(null);
            tr.commit();
        }
    }
}