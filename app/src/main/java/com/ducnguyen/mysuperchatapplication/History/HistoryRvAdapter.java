package com.ducnguyen.mysuperchatapplication.History;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ducnguyen.mysuperchatapplication.Model.Items.ConversationItem;
import com.ducnguyen.mysuperchatapplication.R;
import com.ducnguyen.mysuperchatapplication.Utils.ConversationItemListDiffCallback;
import com.ducnguyen.mysuperchatapplication.databinding.ConversationItemRowBinding;

import java.util.ArrayList;

public class HistoryRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ConversationItem> conversations;
    Context context;
    ConversationItemRowBinding conversationItemRowBinding;
    ConversationItemViewModel conversationItemViewModel;

    public HistoryRvAdapter(ArrayList<ConversationItem> conversations, Context context) {
        this.conversations = conversations;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        conversationItemRowBinding = DataBindingUtil.inflate(layoutInflater,R.layout.conversation_item_row,parent,false);
        return new ConversationItemViewHolder(conversationItemRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ConversationItem item = conversations.get(position);
        conversationItemViewModel = new ConversationItemViewModel(context, item.getcId());
        conversationItemRowBinding.setViewModel(conversationItemViewModel);
        ((ConversationItemViewHolder)holder).bind(item);
        Log.i("convItemAdt", "cid: " + conversationItemViewModel.cId);
    }

    @Override
    public int getItemCount() {
        return conversations != null ? conversations.size() : 0;
    }

    private static class ConversationItemViewHolder extends RecyclerView.ViewHolder {

        private ConversationItemRowBinding conversationItemRowBinding;

        public ConversationItemViewHolder(ConversationItemRowBinding conversationItemRowBinding) {
            super(conversationItemRowBinding.getRoot());
            this.conversationItemRowBinding = conversationItemRowBinding;
        }

        public void bind(ConversationItem item) {
            this.conversationItemRowBinding.setConversation(item);
            this.conversationItemRowBinding.executePendingBindings();
        }

    }

    public void updateList(ArrayList<ConversationItem> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ConversationItemListDiffCallback(this.conversations, newList));
        diffResult.dispatchUpdatesTo(this);
        this.conversations = newList;
    }
}
