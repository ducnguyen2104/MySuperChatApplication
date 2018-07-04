package com.ducnguyen.mysuperchatapplication.Contacts;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;

import com.ducnguyen.mysuperchatapplication.Conversation.ConversationActivity;
import com.ducnguyen.mysuperchatapplication.MainActivity;

import java.util.ArrayList;
import java.util.Collections;

public class ContactItemViewModel extends ViewModel {

    Context context;
    public String username; //set on ContactRvAdapter
    //list contain users in the conversation to be found
    final ArrayList<String> users = new ArrayList<>();

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String cId;

    public ContactItemViewModel(Context context) {
        this.context = context;
    }

    public void onClick() {
        Intent intent = new Intent(context,ConversationActivity.class);
        findConversationByUsername();
        intent.putExtra("cId", cId);
        context.startActivity(intent);
    }

    public void findConversationByUsername() {
        if(users.size() == 0) {
            users.add(MainActivity.currentUser.username);
            users.add(username);
            Collections.sort(users);
        }
        cId = users.get(0) + "*" + users.get(1);
    }
}
