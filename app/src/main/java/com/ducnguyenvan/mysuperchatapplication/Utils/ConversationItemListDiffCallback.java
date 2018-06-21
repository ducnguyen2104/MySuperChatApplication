package com.ducnguyenvan.mysuperchatapplication.Utils;

import android.support.v7.util.DiffUtil;

import com.ducnguyenvan.mysuperchatapplication.Model.Items.ConversationItem;

import java.util.List;

public class ConversationItemListDiffCallback extends DiffUtil.Callback {

    private List<ConversationItem> oldList;
    private List<ConversationItem> newList;

    public ConversationItemListDiffCallback(List<ConversationItem> oldList, List<ConversationItem> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList == null ? 0 : oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList == null ? 0 : newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equalsContent(newList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
        //return oldList.get(oldItemPosition).equalsContent(newList.get(newItemPosition));
    }
}
