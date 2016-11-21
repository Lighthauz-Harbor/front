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
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class IdeaListFragment extends Fragment implements MyAdapter.ItemClickCallback{

    private RecyclerView recView;
    private MyAdapter adapter;
    int loginIndex=0;

    public IdeaListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_idea_list, container, false);

        loginIndex = getActivity().getIntent().getIntExtra("LOGIN_INDEX",0);

        populate(v);

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
        intent.putExtra("IDEA_ID",p);
        intent.putExtra("LOGIN_INDEX",loginIndex);


        startActivity(intent);
    }





    public void populate(View view){

        ArrayList<Idea> ideas = (ArrayList) Idea.getIdeas();
        ArrayList<Idea> selected = new ArrayList<>();

        //sementara pake admin (array 0)
        ArrayList<Integer> userIdea = (ArrayList) User.getUsers().get(loginIndex).getIdea();

        for(int i=0;i<userIdea.size();i++){
            selected.add(ideas.get(userIdea.get(i)));
        }

        recView = (RecyclerView) view.findViewById(R.id.rec_list_idea);
        //Check out GridLayoutManager and StaggeredGridLayoutManager for more options
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MyAdapter(selected, "IDEA", getActivity());
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

    }
}
