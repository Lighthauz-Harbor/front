package com.example.dvidr_000.lighthauzproject;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


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

            Bundle args = new Bundle();
            View v = getView();

            EditText title = (EditText) v.findViewById(R.id.etIdeaInfoBusinessNameFill);
            EditText category = (EditText) v.findViewById(R.id.etIdeaInforBusinessCategoryFill);
            EditText description = (EditText) v.findViewById(R.id.etIdeaInfoDescriptionFill);

            args.putString("TITLE",title.getText().toString());
            args.putString("CATEGORY",category.getText().toString());
            args.putString("DESCRIPTION",description.getText().toString());

            BackgroundFragment fragment = new BackgroundFragment();
            fragment.setArguments(args);

            FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
            tr.replace(R.id.fragment_container_detail,fragment);
            tr.addToBackStack(null);
            tr.commit();
        }
    }
}
