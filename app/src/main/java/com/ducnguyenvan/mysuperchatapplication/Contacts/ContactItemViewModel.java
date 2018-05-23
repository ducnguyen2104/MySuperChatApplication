package com.ducnguyenvan.mysuperchatapplication.Contacts;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;

import com.ducnguyenvan.mysuperchatapplication.Conversation.ConversationActivity;

public class ContactItemViewModel extends ViewModel {
    Context context;

    public ContactItemViewModel(Context context) {
        this.context = context;
    }

    public void onClick() {
        Intent intent = new Intent(context,ConversationActivity.class);
        context.startActivity(intent);
    }
}
