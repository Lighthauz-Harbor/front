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
public class BackgroundFragment extends Fragment implements View.OnClickListener {

    private EditText background;
    private Button nextBtn;
    private Bundle ideaBundle;

    public BackgroundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_background, container, false);

        getActivity().setTitle("Idea Background");
        ideaBundle = getArguments();
        background = (EditText) v.findViewById(R.id.etIdeaBackgroundFill);
        nextBtn = (Button) v.findViewById(R.id.btnNextIdeaBackground);
        nextBtn.setOnClickListener(this);

        if (background.getText().toString().isEmpty()){
            setDetails();
        }
        return v;
    }

    public void onClick(View view) {
        if(view.getId()==R.id.btnNextIdeaBackground){
            if (validate()){
                ideaBundle.putString("BACKGROUND",background.getText().toString().trim());

                ProblemFragment fragment = new ProblemFragment();
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

    public  void setDetails(){
        background.setText(ideaBundle.getString("BACKGROUND"));
    }

    public boolean validate(){
        if (background.getText().toString().isEmpty()){
            background.requestFocus();
            return false;
        }
        return true;
    }

}


