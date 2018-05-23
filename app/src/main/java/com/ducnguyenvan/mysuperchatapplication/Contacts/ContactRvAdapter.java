package com.ducnguyenvan.mysuperchatapplication.Contacts;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ducnguyenvan.mysuperchatapplication.Model.ContactItem;
import com.ducnguyenvan.mysuperchatapplication.R;
import com.ducnguyenvan.mysuperchatapplication.databinding.ContactItemRowBinding;

import java.util.ArrayList;

public class ContactRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ContactItem> contacts;
    Context context;
    ContactItemRowBinding contactItemRowBinding;
    ContactItemViewModel viewModel;

    public ContactRvAdapter(ArrayList<ContactItem> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        contactItemRowBinding = DataBindingUtil.inflate(layoutInflater,R.layout.contact_item_row,parent,false);
        viewModel = new ContactItemViewModel(context);
        contactItemRowBinding.setViewModel(viewModel);
        return new ContactItemViewHolder(contactItemRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactItem contactItem = contacts.get(position);
        ((ContactItemViewHolder)holder).bind(contactItem);
    }

    @Override
    public int getItemCount() {
        return contacts != null ? contacts.size() : 0;
    }

    private static class ContactItemViewHolder extends RecyclerView.ViewHolder{
        private ContactItemRowBinding contactItemRowBinding;

        public ContactItemViewHolder(ContactItemRowBinding contactItemRowBinding) {
            super(contactItemRowBinding.getRoot());
            this.contactItemRowBinding = contactItemRowBinding;

        }
         public void bind(ContactItem contactItem) {
            this.contactItemRowBinding.setContact(contactItem);
            this.contactItemRowBinding.executePendingBindings();
         }

    }
}
