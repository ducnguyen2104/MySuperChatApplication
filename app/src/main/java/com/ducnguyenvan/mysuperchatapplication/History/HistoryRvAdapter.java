package com.ducnguyenvan.mysuperchatapplication.History;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ducnguyenvan.mysuperchatapplication.Model.Items.ConversationItem;
import com.ducnguyenvan.mysuperchatapplication.R;
import com.ducnguyenvan.mysuperchatapplication.databinding.ConversationItemRowBinding;

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
        conversationItemViewModel = new ConversationItemViewModel(context);
        conversationItemRowBinding.setViewModel(conversationItemViewModel);
        return new ConversationItemViewHolder(conversationItemRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ConversationItem item = conversations.get(position);
        ((ConversationItemViewHolder)holder).bind(item);
        conversationItemViewModel.cId = item.getcId();
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
}
