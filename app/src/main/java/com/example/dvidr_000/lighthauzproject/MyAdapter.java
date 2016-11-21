package com.example.dvidr_000.lighthauzproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.net.URI;
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
        void onItemClick(int p);
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

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title;
        private ImageView icon;
        private View container;

        public MyHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.tv_title_list_idea);
            icon = (ImageView)itemView.findViewById(R.id.ic_list_idea);
            //We'll need the container later on, when we add an View.OnClickListener
            container = itemView.findViewById(R.id.cont_item_root);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickCallback.onItemClick(getLayoutPosition());
        }
    }

    @Override
    public MyAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_idea, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyHolder holder, int position) {
        switch(content){
            case "IDEA":

                Idea idea =  (Idea) listData.get(position);
                holder.title.setText(idea.getTitle());
                break;

            case "USER":

                User user =  (User) listData.get(position);
                holder.title.setText(user.getName());
                holder.icon.setImageResource(user.getProfilePic());
                break;

            case "NEWS":

                break;
        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}