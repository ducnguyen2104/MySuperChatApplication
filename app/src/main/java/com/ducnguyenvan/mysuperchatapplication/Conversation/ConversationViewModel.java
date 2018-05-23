package com.ducnguyenvan.mysuperchatapplication.Conversation;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

public class ConversationViewModel extends ViewModel {
    public String message;

        public void onSendBtnClicked() {
            Log.i("mess",ConversationActivity.messages.size() + "");
            //ConversationActivity.messages.add(new Message(message,MainActivity.currentUser.getUsername(),000));
    }


}
