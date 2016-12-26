package com.example.dvidr_000.lighthauzproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by richentra on 12-Nov-16.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private List listData;
    private LayoutInflater inflater;
    private String content;
    private static Context context;

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
        this.context=c;
        inflater = LayoutInflater.from(c);
        this.listData = listData;
        this.content=content;
    }

    public MyAdapter(Context c, List<CategoryPickFragment.Category> listData, String content){
        inflater = LayoutInflater.from(c);
        this.content=content;
        this.listData=listData;
    }

    public MyAdapter(String content, Context c, List<CommentFragment.Comment> listData){
        inflater = LayoutInflater.from(c);
        this.content=content;
        this.listData=listData;
    }

    public void swap(List<NewsFeedFragment.News> data){
        this.listData=data;
        notifyDataSetChanged();
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
        private CheckBox check;

        public MyHolder(View itemView) {
            super(itemView);

            if(content.equals("NEWS")){
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
            }
            else if(content.equals("IDEA_DETAIL")){
                contentPic = (ImageView)itemView.findViewById(R.id.ic_content_idea);
                title = (TextView)itemView.findViewById(R.id.tv_title_content_idea);
                description = (TextView)itemView.findViewById(R.id.tv_desc_content_idea);
                category = (TextView)itemView.findViewById(R.id.tv_category_fill_content_idea);
                container = itemView.findViewById(R.id.card_item_plain);
            }
            else if (content.equals("CATEGORY_LIST")) {
                title = (TextView) itemView.findViewById(R.id.tv_title_list_category);
                check = (CheckBox) itemView.findViewById(R.id.checkBox_list_category);
                container = itemView.findViewById(R.id.list_category_root);
                check.setOnClickListener(this);
            }
            else if (content.equals("USER_HORIZONTAL")||content.equals("IDEA_HORIZONTAL")){
                name = (TextView)itemView.findViewById(R.id.tv_title_list_simple_horizontal);
                icon = (ImageView)itemView.findViewById(R.id.ic_list_simple_horizontal);
                container = itemView.findViewById(R.id.card_item_simple_horizontal);

                String tag;

                if (content.equals("IDEA_HORIZONTAL")){
                    tag = "IDEA_HORIZONTAL";

                }
                else {
                    tag = "USER_HORIZONTAL";
                }
                container.setTag(tag);

            }
            else if (content.equals("COMMENT_LIST")){
                name = (TextView)itemView.findViewById(R.id.tv_title_comment);
                icon = (ImageView)itemView.findViewById(R.id.ic_list_comment);
                datetime = (TextView)itemView.findViewById(R.id.tv_time_comment);
                description = (TextView)itemView.findViewById(R.id.tv_text_comment);
                container = itemView.findViewById(R.id.card_item_comment);
                name.setOnClickListener(this);
                icon.setOnClickListener(this);
            }
            else {
                name = (TextView)itemView.findViewById(R.id.tv_title_list_simple);
                icon = (ImageView)itemView.findViewById(R.id.ic_list_simple);
                container = itemView.findViewById(R.id.card_item_simple);
            }

            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickCallback.onItemClick(getLayoutPosition(),view);
        }
    }

    @Override
    public MyAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if(content.equals("NEWS")){
            view = inflater.inflate(R.layout.list_news, parent, false);

            FrameLayout inside = (FrameLayout) view.findViewById(R.id.wrapper_content_news);
            View view1 = inflater.inflate(R.layout.content_idea,null,false);
            inside.addView(view1);

        }
        else if (content.equals("IDEA_DETAIL")){
            view = inflater.inflate(R.layout.list_plain, parent, false);

            FrameLayout inside = (FrameLayout) view.findViewById(R.id.wrapper_content_plain);
            View view1 = inflater.inflate(R.layout.content_idea,null,false);
            inside.addView(view1);
        }
        else if (content.equals("CATEGORY_LIST")){
            view = inflater.inflate(R.layout.list_title_checkbox, parent, false);
        }
        else if (content.equals("USER_HORIZONTAL")||content.equals("IDEA_HORIZONTAL")){
            view = inflater.inflate(R.layout.list_simple_horizontal, parent, false);
        }
        else if (content.equals("COMMENT_LIST")){
            view = inflater.inflate(R.layout.list_comment, parent, false);
        }
        else {
            view = inflater.inflate(R.layout.list_simple, parent, false);
        }

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyHolder holder, int position) {
        switch(content){
            case "IDEA_HORIZONTAL":
            case "IDEA":

                Idea idea =  (Idea) listData.get(position);
                holder.name.setText(idea.getTitle());
                imageLoader(idea.getPic(),holder.icon);
                break;

            case "IDEA_DETAIL":

                Idea idea1 =  (Idea) listData.get(position);
                holder.title.setText(idea1.getTitle());
                holder.description.setText(idea1.getDescription());
                holder.category.setText(idea1.getCategory());
                imageLoader(idea1.getPic(),holder.contentPic);
                break;

            case "USER_HORIZONTAL":
            case "USER":
                User user1 =  (User) listData.get(position);
                holder.name.setText(user1.getName());
                imageLoader(user1.getProfPic(),holder.icon);
                break;

            case "NEWS":

                String url;
                String type;

                NewsFeedFragment.News news = (NewsFeedFragment.News) listData.get(position);

                holder.name.setText(news.getName());
                holder.title.setText(news.getTitle());
                holder.description.setText(news.getDescription());
                holder.datetime.setText(setDate(news.getTime()));
                holder.category.setText(news.getCategory());

                url = news.getPic();
                imageLoader(url,holder.contentPic);

                url = news.getProfPic();
                imageLoader(url,holder.icon);

                type = news.getType();

                if(type.equals("create")){
                    holder.event.setText(R.string.newsNewIdea);
                }
                else if(type.equals("update")) {
                    holder.event.setText(R.string.newsUpdateIdea);
                }

                break;
            case "CATEGORY_LIST":
                CategoryPickFragment.Category category = (CategoryPickFragment.Category) listData.get(position);
                holder.title.setText(category.getName());
                holder.check.setChecked(category.isSelected());

                break;
            case "COMMENT_LIST":
                CommentFragment.Comment comment = (CommentFragment.Comment) listData.get(position);
                holder.name.setText(comment.getName());
                holder.description.setText(comment.getText());
                holder.datetime.setText(setDate(comment.getTimestamp()));
                imageLoader(comment.getProfilePic(),holder.icon);
                break;
        }

    }

    public List getListData() {
        return listData;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static String setDate(Long dateparam){

        String dateString;
        Date date = new Date(dateparam);
        android.text.format.DateFormat df = new android.text.format.DateFormat();

        Date now = new Date();
        if(date.getYear()-now.getYear()!=0){
            dateString = df.format("MMM dd, yyyy HH:mm", date).toString();
        }
        else if(now.getDate()-date.getDate()==1){
            dateString = "Yesterday at " + df.format("HH:mm", date).toString();
        }
        else if (date.getDate()-now.getDate()==0){
            if (now.getHours()-date.getHours()==0){
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

    public static void imageLoader(String url,final ImageView img){

        ImageLoader imageLoader = MySingleton.getInstance(context).getImageLoader();

// If you are using normal ImageView
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    img.setImageBitmap(response.getBitmap());
                }
            }
        });

    }


}