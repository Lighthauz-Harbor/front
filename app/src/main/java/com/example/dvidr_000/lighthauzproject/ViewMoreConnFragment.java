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
public class ViewMoreConnFragment extends Fragment implements DataAdapter.ItemClickCallback{
    private RecyclerView recView;
    private DataAdapter adapter;
    private SessionManager sessionManager;
    private HashMap<String,String> user;
    private List<User> users;
    private String idStr;

    private TextView notice;
    private ProgressBar pb;

    public ViewMoreConnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_more_conn,container,false);

        getActivity().setTitle(R.string.Connections);

        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();
        idStr = user.get(SessionManager.KEY_ID);

        users = getArguments().getParcelableArrayList("USERS");

        notice = (TextView) v.findViewById(R.id.viewMoreConnNotice);
        pb = (ProgressBar) v.findViewById(R.id.pBarViewMoreConn);
        recView = (RecyclerView) v.findViewById(R.id.rec_list_view_more_conn);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new DataAdapter(users, getActivity(), "USER");
        adapter.setItemClickCallback(this);
        recView.setAdapter(adapter);

        if (users.isEmpty()){
            notice.setVisibility(View.VISIBLE);
        }
        return v;
    }

    @Override
    public void onItemClick(int p, View view) {
        Bundle args = new Bundle();
        args.putString("USER_ID",users.get(p).getId());

        ViewProfileFragment fragment = new ViewProfileFragment();
        fragment.setArguments(args);

        FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.fragment_container_detail,fragment);
        tr.addToBackStack(null);
        tr.commit();
    }
}
