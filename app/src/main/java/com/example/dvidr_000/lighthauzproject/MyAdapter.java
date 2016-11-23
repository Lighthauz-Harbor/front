package com.example.dvidr_000.lighthauzproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by richentra on 12-Nov-16.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private List listData;
    private LayoutInflater inflater;
    private String content;

    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback{
        void onItemClick(int p,View view);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback){
        this.itemClickCallback = itemClickCallback;
    }

    public MyAdapter(List<Idea> listData, String content, Context c){
        inflater = LayoutInflater.from(c);
        this.listData = listData;
        this.content=content;
    }

    public MyAdapter(List<User> listData, Context c, String content){
        inflater = LayoutInflater.from(c);
        this.listData = listData;
        this.content=content;
    }

    public MyAdapter(Context c, String content, List<NewsFeedFragment.News> listData){
        inflater = LayoutInflater.from(c);
        this.listData = listData;
        this.content=content;
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView name;
        private ImageView icon;
        private View container;
        private TextView title;
        private ImageView contentPic;
        private TextView description;
        private TextView category;
        private TextView event;
        private TextView datetime;

        public MyHolder(View itemView) {
            super(itemView);

            if(content.equals("NEW_IDEA")){
                name = (TextView)itemView.findViewById(R.id.tv_title_list_news);
                icon = (ImageView)itemView.findViewById(R.id.ic_list_news);
                datetime = (TextView)itemView.findViewById(R.id.tv_time_list_news);

                contentPic = (ImageView)itemView.findViewById(R.id.ic_content_idea);
                title = (TextView)itemView.findViewById(R.id.tv_title_content_idea);
                description = (TextView)itemView.findViewById(R.id.tv_desc_content_idea);
                category = (TextView)itemView.findViewById(R.id.tv_category_fill_content_idea);
                event = (TextView)itemView.findViewById(R.id.tv_event_list_news);
                container = itemView.findViewById(R.id.wrapper_content_news);

                name.setOnClickListener(this);
                icon.setOnClickListener(this);
                container.setOnClickListener(this);
            }
            else {
                name = (TextView)itemView.findViewById(R.id.tv_title_list_simple);
                icon = (ImageView)itemView.findViewById(R.id.ic_list_simple);
                container = itemView.findViewById(R.id.card_item_simple);
                container.setOnClickListener(this);
            }


        }

        @Override
        public void onClick(View view) {
            itemClickCallback.onItemClick(getLayoutPosition(),view);
        }
    }

    @Override
    public MyAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(content.equals("NEW_IDEA")){
            view = inflater.inflate(R.layout.list_news, parent, false);

            FrameLayout inside = (FrameLayout) view.findViewById(R.id.wrapper_content_news);
            View view1 = inflater.inflate(R.layout.content_news_idea,null,false);
            inside.addView(view1);

        }
        else {
            view = inflater.inflate(R.layout.list_simple, parent, false);
        }

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyHolder holder, int position) {
        switch(content){
            case "IDEA":

                Idea idea =  (Idea) listData.get(position);
                holder.name.setText(idea.getTitle());
                break;

            case "USER":

                User user =  (User) listData.get(position);
                holder.name.setText(user.getName());
                holder.icon.setImageResource(user.getProfilePic());
                break;

            case "NEW_IDEA":

                NewsFeedFragment.News news = (NewsFeedFragment.News) listData.get(position);
                User user1 = User.getUsers().get(news.getUserId());
                Idea idea1 = Idea.getIdeas().get(news.getIdeaId());






                holder.name.setText(user1.getName());
                holder.icon.setImageResource(user1.getProfilePic());

                holder.title.setText(idea1.getTitle());
                holder.description.setText(idea1.getDescription());
                holder.datetime.setText(setDate(idea1));
                holder.category.setText(idea1.getCategory());
                holder.event.setText(R.string.eventNewIdea);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public String setDate(Idea idea){

        String dateString;
        Date date = idea.getCreateDate();
        android.text.format.DateFormat df = new android.text.format.DateFormat();

        Date now = new Date();
        if(date.getYear()-now.getYear()!=0){
            dateString = df.format("MMM dd, yyyy HH:mm", date).toString();
        }
        else if(now.getDate()-date.getDate()==1){
            dateString = df.format("Yesterday at HH:mm", date).toString();
        }
        else if (date.getDate()-now.getDate()==0){
            if (date.getHours()-now.getHours()==0){
                if (date.getMinutes()-now.getMinutes()==0){
                    dateString = "Just now";
                }
                else if (now.getMinutes()-date.getMinutes()==1){
                    dateString = "One minute ago";
                }
                else {
                    dateString = Integer.toString(now.getMinutes()-date.getMinutes()) + " minutes ago";
                }
            }
            else if (now.getHours()-date.getHours()==1){
                dateString = "an hour ago";
            }
            else {
                dateString = Integer.toString(now.getHours()-date.getHours()) + " hours ago";
            }
        }
        else {
            dateString = df.format("MMM dd HH:mm", date).toString();
        }

        return dateString;
    }


}