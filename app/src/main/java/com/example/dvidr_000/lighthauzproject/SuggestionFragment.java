package com.example.dvidr_000.lighthauzproject;


import android.content.ClipData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestionFragment extends Fragment implements MyAdapter.ItemClickCallback{

    private RecyclerView recView;
    private MyAdapter adapter;
    ArrayList<User> selected;

    public SuggestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_suggestion, container, false);

        getActivity().setTitle("Suggestions");

        ArrayList users = (ArrayList) User.getUsers();

        //dummy
        selected = new ArrayList<>();
        selected.add((User )users.get(1));
        selected.add((User )users.get(2));
        selected.add((User )users.get(3));

        populate(v,selected);

        // Inflate the layout for this fragment
        return v;
    }

    public void populate(View view,ArrayList<User> selected){

        recView = (RecyclerView) view.findViewById(R.id.rec_list_suggestion);
        //Check out GridLayoutManager and StaggeredGridLayoutManager for more options
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MyAdapter(selected, getActivity(),"USER");
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

    }

    @Override
    public void onItemClick(int p, View view) {
        Bundle args = new Bundle();

        args.putInt("USER_ID",selected.get(p).getId());

        ViewProfileFragment fragment = new ViewProfileFragment();
        fragment.setArguments(args);

        FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.fragment_container_detail,fragment);
        tr.addToBackStack(null);
        tr.commit();
    }
}
