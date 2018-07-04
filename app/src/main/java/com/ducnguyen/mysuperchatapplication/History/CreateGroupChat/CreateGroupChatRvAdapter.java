package com.ducnguyen.mysuperchatapplication.History.CreateGroupChat;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ducnguyen.mysuperchatapplication.Model.Items.SelectableContactItem;
import com.ducnguyen.mysuperchatapplication.R;
import com.ducnguyen.mysuperchatapplication.databinding.GroupChatContactItemRowBinding;

import java.util.ArrayList;

public class CreateGroupChatRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<SelectableContactItem> contacts;
    Context context;
    GroupChatContactItemRowBinding binding;

    public CreateGroupChatRvAdapter(ArrayList<SelectableContactItem> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.group_chat_contact_item_row,parent,false);
        return new GrChatContactItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SelectableContactItem contactItem = contacts.get(position);
        ((CreateGroupChatRvAdapter.GrChatContactItemViewHolder)holder).bind(contactItem);
    }

    @Override
    public int getItemCount() {
        return contacts != null ? contacts.size() : 0;
    }

    private static class GrChatContactItemViewHolder extends RecyclerView.ViewHolder {

        private GroupChatContactItemRowBinding binding;


        public GrChatContactItemViewHolder(GroupChatContactItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SelectableContactItem contactItem) {
            this.binding.setContact(contactItem);
            this.binding.executePendingBindings();
        }
    }

}