package com.ducnguyenvan.mysuperchatapplication.History;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;

import com.ducnguyenvan.mysuperchatapplication.Conversation.ConversationActivity;

public class ConversationItemViewModel extends ViewModel {

    Context context;
    public String cId;

    public ConversationItemViewModel(Context context) {
        this.context = context;
    }

    public void onClick() {
        Intent intent = new Intent(context,ConversationActivity.class);
        intent.putExtra("cId", cId);
        context.startActivity(intent);
    }
}
