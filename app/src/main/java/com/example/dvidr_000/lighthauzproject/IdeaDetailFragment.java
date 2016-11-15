package com.example.dvidr_000.lighthauzproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class IdeaDetailFragment extends Fragment {


    public IdeaDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_idea_detail, container, false);

        getActivity().setTitle("Idea Details");

        TextView title = (TextView) v.findViewById(R.id.tv_idea_detail_title);
        TextView desc = (TextView) v.findViewById(R.id.tv_idea_detail_desc);
        int ideaId=0;
        getActivity().getIntent().getIntExtra("EXTRA_ID",ideaId);
        title.setText(Idea.getIdeas().get(ideaId).getTitle());
        desc.setText(Idea.getIdeas().get(ideaId).getDescription());
        return v;
    }

}
