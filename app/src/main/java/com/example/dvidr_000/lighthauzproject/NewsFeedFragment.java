package com.example.dvidr_000.lighthauzproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment implements MyAdapter.ItemClickCallback{

    private RecyclerView recView;
    private MyAdapter adapter;
    private int loginIndex;


    public NewsFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_news_feed, container, false);

        loginIndex = getActivity().getIntent().getIntExtra("LOGIN_INDEX",0);
        List<News> news = new ArrayList();

        News newNews = new News(1,1,"NEW_IDEA");
        news.add(newNews);

        newNews = new News(2,2,"NEW_IDEA");
        news.add(newNews);

        newNews = new News(3,3,"NEW_IDEA");
        news.add(newNews);

        recView = (RecyclerView) v.findViewById(R.id.rec_list_news);
        //Check out GridLayoutManager and StaggeredGridLayoutManager for more options
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MyAdapter(getActivity(),"NEW_IDEA",news);
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onItemClick(int p,View view) {
        switch (view.getId()){
            case R.id.tv_title_list_news:

            case R.id.ic_list_news:



                Intent intent1 = new Intent(getActivity(),DetailActivity.class);
                intent1.putExtra("EXTRA_CONTENT","VIEW_PROFILE");
                intent1.putExtra("USER_ID",p+1);
                intent1.putExtra("LOGIN_INDEX",loginIndex);

                startActivity(intent1);

                break;

            case R.id.wrapper_content_news:

                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("EXTRA_CONTENT","IDEA_DETAIL");
                intent.putExtra("IDEA_ID",p+1);
                intent.putExtra("LOGIN_INDEX",loginIndex);

                startActivity(intent);
                break;
        }

    }

    public class News {
        private int userId;
        private int ideaId;
        private String content;

        public News(int userId, int ideaId, String content){
            this.userId=userId;
            this.ideaId=ideaId;
            this.content=content;
        }

        public int getUserId() {
            return userId;
        }

        public int getIdeaId() {
            return ideaId;
        }

        public String getContent() {
            return content;
        }
    }
}
