package com.ducnguyenvan.mysuperchatapplication.History.CreateGroupChat;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ducnguyenvan.mysuperchatapplication.Conversation.ConversationActivity;
import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.SelectableContactItem;

import java.util.ArrayList;
import java.util.Collections;

public class CreateGroupChatViewModel extends ViewModel {

    Context context;

    public CreateGroupChatViewModel(Context context) {
        this.context = context;
    }

    public void onClick() {
        String cId = "";
        ArrayList<String> members = new ArrayList<>();
        members.add(MainActivity.currentUser.getUsername());
        for(SelectableContactItem item : CreateGrChatActivity.contacts) {
            if(item.getIsSelected() == true) {
                members.add(item.getContactName());
            }
        }
        Collections.sort(members);
        for(String s : members) {
            if(cId.length() == 0) {
                cId += s;
            }
            else {
                cId += ("*" + s);
            }
        }
        Log.i("cId", "" + cId);
        Intent intent = new Intent(context,ConversationActivity.class);
        intent.putExtra("cId", cId);
        (context).startActivity(intent);
        ((Activity)context).finish();

    }
}
