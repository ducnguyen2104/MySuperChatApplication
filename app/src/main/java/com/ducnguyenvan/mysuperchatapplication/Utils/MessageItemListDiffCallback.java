package com.ducnguyenvan.mysuperchatapplication.Utils;

import android.support.v7.util.DiffUtil;

import com.ducnguyenvan.mysuperchatapplication.Model.Items.BaseMessageItem;

import java.util.List;

public class MessageItemListDiffCallback extends DiffUtil.Callback {

    private List<BaseMessageItem> oldList;
    private List<BaseMessageItem> newList;

    public MessageItemListDiffCallback(List<BaseMessageItem> oldList, List<BaseMessageItem> newList) {
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
        BaseMessageItem newItem = newList.get(newItemPosition);
        BaseMessageItem oldItem = oldList.get(oldItemPosition);
        return oldItem.equalsContent(newItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }
}
