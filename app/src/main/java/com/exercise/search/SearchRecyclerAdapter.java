package com.exercise.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_LOAD_NEXT = 100;
    private static final int VIEW_TYPE_ITEM = 101;

    private List<Item> items;
    private boolean hasNext;
    private Callback callback;
    private boolean isLoading;


    public interface Callback {
        void onLoadNext();
        void onItemSelected(Item item);
    }

    public SearchRecyclerAdapter(Callback callback) {
        this.callback = callback;
        items = new ArrayList<>();
    }

    public void setItems(List<Item> items, boolean hasNext) {
        this.items = items;
        this.hasNext = hasNext;
        isLoading = false;
        notifyDataSetChanged();
    }

    public void addItems(List<Item> items, boolean hasNext) {
        this.items.addAll(items);
        this.hasNext = hasNext;
        isLoading = false;
        notifyItemRangeInserted(this.items.size(), items.size());
    }

    public void updateItem(Item item, int position) {
        items.remove(position);
        items.add(position, item);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return items.size() + (hasNext ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (hasNext && position == items.size()) {
            return VIEW_TYPE_LOAD_NEXT;
        }
        return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_LOAD_NEXT:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_next_item, parent, false);
                return new LoadNextViewHolder(v);

            case VIEW_TYPE_ITEM:
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                return new ItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        callback.onItemSelected(items.get(itemViewHolder.getAdapterPosition()));
                    }
                }
            });
            itemViewHolder.title.setText(items.get(position).title);
            Glide.with(itemViewHolder.image.getContext().getApplicationContext())
                    .load(items.get(position).link)
                    .dontAnimate()
                    .into(itemViewHolder.image);

        } else if (holder instanceof LoadNextViewHolder && !isLoading) {
            isLoading = true;
            if (callback != null) {
                callback.onLoadNext();
            }
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        ItemViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    private class LoadNextViewHolder extends RecyclerView.ViewHolder {
        LoadNextViewHolder(View view) {
            super(view);
        }
    }
}
