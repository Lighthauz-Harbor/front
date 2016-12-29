package com.example.dvidr_000.lighthauzproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMoreIdeaFragment extends Fragment implements MyAdapter.ItemClickCallback{
    private RecyclerView recView;
    private MyAdapter adapter;
    private SessionManager sessionManager;
    private HashMap<String,String> user;
    private List<Idea> ideas;
    private String idStr;

    private TextView notice;
    private ProgressBar pb;

    public ViewMoreIdeaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_more_idea,container,false);

        getActivity().setTitle(R.string.Ideas);

        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();
        idStr = user.get(SessionManager.KEY_ID);

        ideas = getArguments().getParcelableArrayList("IDEAS");

        notice = (TextView) v.findViewById(R.id.viewMoreIdeaNotice);
        pb = (ProgressBar) v.findViewById(R.id.pBarViewMoreIdea);
        recView = (RecyclerView) v.findViewById(R.id.rec_list_view_more_idea);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MyAdapter(ideas, "IDEA", getActivity());
        adapter.setItemClickCallback(this);
        recView.setAdapter(adapter);

        if (ideas.isEmpty()){
            notice.setVisibility(View.VISIBLE);
        }
        return v;
    }

    @Override
    public void onItemClick(int p, View view) {
        Bundle args = new Bundle();
        args.putString("IDEA_ID",ideas.get(p).getId());

        IdeaDetailFragment fragment = new IdeaDetailFragment();
        fragment.setArguments(args);

        FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.fragment_container_detail,fragment);
        tr.addToBackStack(null);
        tr.commit();
    }
}
