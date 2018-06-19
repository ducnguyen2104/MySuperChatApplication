package com.ducnguyenvan.mysuperchatapplication.History;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ducnguyenvan.mysuperchatapplication.Conversation.ConversationActivity;

public class ConversationItemViewModel extends ViewModel {

    Context context;
    public String cId;

    public ConversationItemViewModel(Context context, String cId) {
        this.context = context;
        this.cId = cId;
    }

    public void onClick() {
        Intent intent = new Intent(context,ConversationActivity.class);
        intent.putExtra("cId", cId);
        Log.i("convItemVM", "cId: " + cId);
        context.startActivity(intent);
    }
}
