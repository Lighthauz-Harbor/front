package com.example.dvidr_000.lighthauzproject;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment implements MyAdapter.ItemClickCallback{

    private RecyclerView recView;
    private MyAdapter adapter;
    private List<News> news;
    private ProgressBar pb;
    private int previousTotal=0;
    private int retrieve=5;
    private boolean loading = true;

    SessionManager sessionManager;
    HashMap<String,String> user;

    public NewsFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_news_feed, container, false);

        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();

        news = new ArrayList<>();

        pb = (ProgressBar) v.findViewById(R.id.pBarNewsFeed);
        recView = (RecyclerView) v.findViewById(R.id.rec_list_news);
        //Check out GridLayoutManager and StaggeredGridLayoutManager for more options
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) v.findViewById(R.id.news_feed_swipe_refresh_layout);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                previousTotal=0;
                refreshItems();
            }
            void refreshItems() {
                // Load items
                requestNews(previousTotal,retrieve);

                // Load complete
                onItemsLoadComplete();
            }

            void onItemsLoadComplete() {
                // Update the adapter and notify data set changed
                adapter.notifyDataSetChanged();

                // Stop refresh animation
                srl.setRefreshing(false);
            }


        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recView.setLayoutManager(layoutManager);

        recView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int firstVisibleItem, visibleItemCount, totalItemCount;
                visibleItemCount = recView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + retrieve)) {
                    // End has been reached

                    Log.i("Yaeye!", "end of list");

                    // Do something
                    requestNews(previousTotal,retrieve);

                    loading = true;
                }
            }

        });


        requestNews(previousTotal,retrieve);

        adapter = new MyAdapter(getActivity(),"NEWS",news);
        adapter.setItemClickCallback(this);


        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onItemClick(int p,View view) {
        switch (view.getId()){
            case R.id.tv_title_list_news:

            case R.id.ic_list_news:



                /*Intent intent1 = new Intent(getActivity(),DetailActivity.class);
                intent1.putExtra("EXTRA_CONTENT","VIEW_PROFILE");
                intent1.putExtra("USER_ID",p+1);
                intent1.putExtra("LOGIN_INDEX",loginIndex);

                startActivity(intent1);*/

                break;

            case R.id.wrapper_content_news:

                /*Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("EXTRA_CONTENT","IDEA_DETAIL");
                intent.putExtra("IDEA_ID",p+1);
                intent.putExtra("LOGIN_INDEX",loginIndex);

                startActivity(intent);*/
                break;
        }

    }

    public class News {
        private String userId;
        private String ideaId;
        private String type;
        private String name;
        private String title;
        private String category;
        private String description;
        private String pic;
        private String profPic;
        private Long time;

        public News(String type, String userId, String ideaId, String name, String profPic, String title, String category, String description, String pic, Long time ){
            this.userId=userId;
            this.ideaId=ideaId;
            this.name=name;
            this.title=title;
            this.category=category;
            this.description=description;
            this.pic=pic;
            this.profPic=profPic;
            this.time=time;
            this.type=type;
        }

        public String getUserId() {
            return userId;
        }

        public String getIdeaId() {
            return ideaId;
        }

        public String getType() {
            return type;
        }

        public String getName() {return name;}

        public String getTitle() {return title;}

        public String getCategory() {return category;}

        public String getDescription() {return description;}

        public String getPic() {return pic;}

        public String getProfPic() {return profPic;}

        public Long getTime() {return time;}
    }

    public void requestNews(int skip, int num){

        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/news/";

        JsonObjectRequest req = new JsonObjectRequest(url + user.get(SessionManager.KEY_ID) + "/" + Integer.toString(skip) + "/" + Integer.toString(num),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray myArray;
                        try{
                            if (previousTotal==0){
                                news.clear();
                            }

                            myArray = response.getJSONArray("news");
                            for(int i=0;i<myArray.length();i++){

                                JSONObject author = myArray.getJSONObject(i).getJSONObject("author");
                                JSONObject idea = myArray.getJSONObject(i).getJSONObject("idea");
                                News newNews = new News(myArray.getJSONObject(i).getString("type"),author.getString("id"),idea.getString("id"),author.getString("name"),author.getString("pic"),idea.getString("title"),idea.getString("category"),idea.getString("description"),idea.getString("pic"),myArray.getJSONObject(i).getLong("timestamp"));
                                news.add(newNews);

                            }

                            if(previousTotal==0){
                                recView.setAdapter(adapter);
                            } else {
                                adapter.swap(news);
                            }

                            pb.setVisibility(View.GONE);
                            recView.setVisibility(View.VISIBLE);

                        }
                        catch (JSONException e){
                            Log.e("MYAPP", "unexpected JSON exception", e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

// Adding request to request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(req, tag_json);
    }


}
