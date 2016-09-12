package com.brantyu.demo.adapterdelegates;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brantyu.byrecyclerview.adapter.DisplayableItem;
import com.brantyu.byrecyclerview.delegate.AdapterDelegate;
import com.brantyu.demo.R;
import com.brantyu.demo.model.Snake;

import java.util.List;


/**
 * @author Hannes Dorfmann
 */
public class SnakeListItemAdapterDelegate implements AdapterDelegate<List<DisplayableItem>> {

    private LayoutInflater inflater;

    public SnakeListItemAdapterDelegate(Activity activity) {
        inflater = activity.getLayoutInflater();
    }

    @Override
    public boolean isForViewType(@NonNull List<DisplayableItem> items, int position) {
        return items.get(position) instanceof Snake;
    }

    @NonNull
    @Override
    public SnakeListItemAdapterDelegate.SnakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new SnakeListItemAdapterDelegate.SnakeViewHolder(
                inflater.inflate(R.layout.item_snake, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull List<DisplayableItem> items, int position, @NonNull RecyclerView.ViewHolder holder) {
        SnakeViewHolder snakeViewHolder = (SnakeViewHolder) holder;
        Snake item = (Snake) items.get(position);
        snakeViewHolder.name.setText(item.getName());
        snakeViewHolder.race.setText(item.getRace());
    }

    static class SnakeViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView race;

        public SnakeViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            race = (TextView) itemView.findViewById(R.id.race);
        }
    }
}
