package com.example.dvidr_000.lighthauzproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by richentra on 12-Nov-16.
 */

public class IdeaAdapter extends RecyclerView.Adapter<IdeaAdapter.IdeaHolder> {

    private List<Idea> listData;
    private LayoutInflater inflater;

    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback{
        void onItemClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback){
        this.itemClickCallback = itemClickCallback;
    }

    public IdeaAdapter(List<Idea> listData, Context c){
        inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    class IdeaHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title;
        private ImageView icon;
        private View container;

        public IdeaHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.tv_title_list_idea);
            icon = (ImageView)itemView.findViewById(R.id.ic_list_idea);
            //We'll need the container later on, when we add an View.OnClickListener
            container = itemView.findViewById(R.id.cont_item_root);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickCallback.onItemClick(getAdapterPosition());
        }
    }

    @Override
    public IdeaAdapter.IdeaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_idea, parent, false);
        return new IdeaHolder(view);
    }

    @Override
    public void onBindViewHolder(IdeaAdapter.IdeaHolder holder, int position) {
        Idea item = listData.get(position);
        holder.title.setText(item.getTitle());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}