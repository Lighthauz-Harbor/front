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
public class SolutionFragment extends Fragment implements View.OnClickListener{
    private EditText solution;
    private Button nextBtn;
    private Bundle ideaBundle;

    public SolutionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_solution, container, false);

        getActivity().setTitle("Idea Solution");
        ideaBundle = getArguments();
        solution = (EditText) v.findViewById(R.id.etIdeaSolutionFill);
        nextBtn = (Button) v.findViewById(R.id.btnNextIdeaSolution);
        nextBtn.setOnClickListener(this);

        if (solution.getText().toString().isEmpty()){
            setDetails();
        }
        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnNextIdeaSolution){
            if (validate()){
                ideaBundle.putString("SOLUTION",solution.getText().toString());

                BMCFragment fragment = new BMCFragment();
                fragment.setArguments(ideaBundle);

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.fragment_container_detail,fragment);
                tr.addToBackStack(null);
                tr.commit();
            }
            else {
                Toast.makeText(getContext(), R.string.EmptyField, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setDetails(){
        solution.setText(ideaBundle.getString("SOLUTION"));
    }

    public boolean validate(){
        if (solution.getText().toString().isEmpty()){
            solution.requestFocus();
            return false;
        }
        return true;
    }
}
