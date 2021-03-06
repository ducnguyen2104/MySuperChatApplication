package com.ducnguyen.mysuperchatapplication.History.CreateGroupChat;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;

import com.ducnguyen.mysuperchatapplication.Conversation.ConversationActivity;
import com.ducnguyen.mysuperchatapplication.MainActivity;
import com.ducnguyen.mysuperchatapplication.Model.Items.SelectableContactItem;

import java.util.ArrayList;
import java.util.Collections;

public class CreateGroupChatViewModel extends ViewModel {

    Context context;

    public CreateGroupChatViewModel(Context context) {
        this.context = context;
    }

    public void onClick() {
        String membersString = ""; //format: mem1*mem2*...
        ArrayList<String> members = new ArrayList<>();
        members.add(MainActivity.currentUser.getUsername());
        for(SelectableContactItem item : CreateGrChatActivity.contacts) {
            if(item.getIsSelected() == true) {
                members.add(item.getContactName());
            }
        }
        Collections.sort(members);
        for(String s : members) {
            if(membersString.length() == 0) {
                membersString += s;
            }
            else {
                membersString += ("*" + s);
            }
        }

        //put string into intent
        Intent intent = new Intent(context,ConversationActivity.class);
        intent.putExtra("membersString", membersString);
        String cId = MainActivity.database.child("conversations").push().getKey();
        intent.putExtra("cId", cId);
        (context).startActivity(intent);
        ((Activity)context).finish();

    }

    public void onBackBtnClicked() {
        ((Activity)context).finish();
    }
}
