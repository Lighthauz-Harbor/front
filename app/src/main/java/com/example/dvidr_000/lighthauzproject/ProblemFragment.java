package com.example.dvidr_000.lighthauzproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProblemFragment extends Fragment implements View.OnClickListener {

    private EditText problem;
    private Button nextBtn;
    private Bundle ideaBundle;

    public ProblemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_problem, container, false);

        getActivity().setTitle("Idea Problem");
        ideaBundle = getArguments();
        problem = (EditText) v.findViewById(R.id.etIdeaProblemFill);
        nextBtn = (Button) v.findViewById(R.id.btnNextIdeaProblem);
        nextBtn.setOnClickListener(this);

        if (problem.getText().toString().isEmpty()){
            setDetails();
        }
        return v;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btnNextIdeaProblem) {
            if (validate()){
                ideaBundle.putString("PROBLEM",problem.getText().toString());

                SolutionFragment fragment = new SolutionFragment();
                fragment.setArguments(ideaBundle);

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.fragment_container_detail, fragment);
                tr.addToBackStack(null);
                tr.commit();
            }
            else {
                Toast.makeText(getContext(), R.string.EmptyField, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setDetails(){
        problem.setText(ideaBundle.getString("PROBLEM"));
    }

    public boolean validate(){
        if (problem.getText().toString().isEmpty()){
            problem.requestFocus();
            return false;
        }
        return true;
    }
}