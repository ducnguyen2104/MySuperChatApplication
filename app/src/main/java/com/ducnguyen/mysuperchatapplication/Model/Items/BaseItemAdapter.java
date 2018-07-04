package com.ducnguyen.mysuperchatapplication.Model.Items;

import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class BaseItemAdapter extends RecyclerView.Adapter<BaseItemViewHolder> {

    List<Item> items = null;

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public Item getItemAt(int position) {
        if(items == null) return null;
        if(position < 0 || position >= items.size()) return null;
        return items.get(position);
    }
}
