package com.example.dvidr_000.lighthauzproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class IdeaListFragment extends Fragment implements IdeaAdapter.ItemClickCallback{

    private RecyclerView recView;
    private IdeaAdapter adapter;
    private ArrayList listData;


    public IdeaListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_idea_list, container, false);


        recView = (RecyclerView)v.findViewById(R.id.rec_list_idea);
        //Check out GridLayoutManager and StaggeredGridLayoutManager for more options
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new IdeaAdapter(Idea.getIdeas(), getActivity());
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("EXTRA_CONTENT","CREATE_IDEA");
                startActivity(intent);
            }
        });


        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onItemClick(int p) {
        //buka detail idea
        Intent intent = new Intent(getActivity(),DetailActivity.class);
        intent.putExtra("EXTRA_CONTENT","IDEA_DETAIL");
        intent.putExtra("EXTRA_ID",p);

        startActivity(intent);
    }
}
