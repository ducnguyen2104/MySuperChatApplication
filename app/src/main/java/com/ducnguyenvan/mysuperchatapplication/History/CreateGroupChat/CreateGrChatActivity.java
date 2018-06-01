package com.ducnguyenvan.mysuperchatapplication.History.CreateGroupChat;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.ducnguyenvan.mysuperchatapplication.Contacts.ContactsFragment;
import com.ducnguyenvan.mysuperchatapplication.Model.ContactItem;
import com.ducnguyenvan.mysuperchatapplication.Model.SelectableContactItem;
import com.ducnguyenvan.mysuperchatapplication.R;
import com.ducnguyenvan.mysuperchatapplication.databinding.ActivityCreateGroupChatBinding;

import java.util.ArrayList;

public class CreateGrChatActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    public static ArrayList<SelectableContactItem> contacts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCreateGroupChatBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_create_group_chat);
        CreateGroupChatViewModel viewModel = new CreateGroupChatViewModel(this);
        binding.setViewModel(viewModel);
        recyclerView = (RecyclerView)findViewById(R.id.groupchat_rv);
        contacts = new ArrayList<>();
        initRecyclerView();
    }

    public void initRecyclerView () {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        for(ContactItem item: ContactsFragment.contacts) {
            contacts.add(new SelectableContactItem(item.getImg(), item.getContactName(), false));
        }
        final CreateGroupChatRvAdapter adapter = new CreateGroupChatRvAdapter(contacts,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
