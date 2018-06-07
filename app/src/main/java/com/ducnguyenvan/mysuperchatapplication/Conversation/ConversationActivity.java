package com.ducnguyenvan.mysuperchatapplication.Conversation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.BaseMessageItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.MessageTextItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.MyImageMessageItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.MyMessageTextItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Message;
import com.ducnguyenvan.mysuperchatapplication.R;
import com.ducnguyenvan.mysuperchatapplication.databinding.ActivityConversationBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConversationActivity extends AppCompatActivity {

    static RecyclerView recyclerView;
    static ArrayList<BaseMessageItem> messageItems = new ArrayList<>();
    ActivityConversationBinding activityConversationBinding;
    static ConversationViewModel conversationViewModel;
    EditText editText;
    Button btn;
    static String currentCId;
    static String membersString;
    static boolean isConversationCreated = false;
    static DatabaseReference messageRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentCId = getIntent().getStringExtra("cId"); //username1|username2
        membersString = getIntent().getStringExtra("membersString"); //!= null is conversation has 2 or more members
        if(membersString == null) {
            membersString = "";
        }
        Log.i("current cid", currentCId + "");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ConversationActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int scrWidth = displayMetrics.widthPixels;
        activityConversationBinding = DataBindingUtil.setContentView(this,R.layout.activity_conversation);
        editText = (EditText)findViewById(R.id.conversation_edt);
        btn = (Button)findViewById(R.id.conversation_btn_send);
        editText.setWidth(scrWidth/10*8);;
        btn.setWidth(scrWidth/10*2);
        conversationViewModel = new ConversationViewModel(getApplicationContext());
        activityConversationBinding.setConversationViewModel(conversationViewModel);
        recyclerView = (RecyclerView)findViewById(R.id.conversation_rv);
        messageRef = MainActivity.database.child("messages").child(currentCId);
        DatabaseReference convRef = MainActivity.database.child("conversations").child(currentCId);
        convRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    isConversationCreated = true;
                    initRecyclerView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void initRecyclerView() {
        MessageTextItem lastMessage = new MessageTextItem();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        final MessageRvAdapter adapter = new MessageRvAdapter(messageItems,recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        DatabaseReference databaseReference = MainActivity.database.child("messages").child(currentCId);
        boolean lastMsgMyMsg = false;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Message message = new Message();
                    Map<String, Object> map = (HashMap<String,Object>) singleSnapshot.getValue();
                    message.mapToObject(map);
                    String msg = message.getMessage() ;
                    if(!msg.equals(":o")) {
                        if (message.getName().equals(MainActivity.currentUser.getUsername())) {
                            messageItems.add(new MyMessageTextItem(message.getName(), msg, message.getTimestamp()));
                        } else {
                            messageItems.add(new MessageTextItem(R.drawable.avt, message.getName(), message.getMessage(), message.getTimestamp()));
                        }
                    } else {
                        messageItems.add(new MyImageMessageItem(message.getName(), R.drawable.whatt, message.getTimestamp()));
                    }
                    adapter.notifyItemInserted(messageItems.size() - 1);
                    updateRecyclerView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        messageItems.clear();
        activityConversationBinding.unbind();
        recyclerView = null;
        conversationViewModel = null;
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public static void updateRecyclerView() {
        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message newMessage = new Message();
                Map<String, Object> map = (HashMap<String,Object>) dataSnapshot.getValue();
                newMessage.mapToObject(map);
                //newMessage = dataSnapshot.getValue(Message.class);
                for(int i = 0; i < messageItems.size(); i++) {
                    if(newMessage.getName().equals(messageItems.get(i).getUsername())
                            && newMessage.getTimestamp().equals(messageItems.get(i).getTimestamp())) {
                        //if message is already present, return
                        return;
                    }
                }
                if(!newMessage.getMessage().equals(":o")) {
                    if (newMessage.getName().equals(MainActivity.currentUser.getUsername())) {
                        String msg = newMessage.getMessage();
                        messageItems.add(new MyMessageTextItem(newMessage.getName(), msg, newMessage.getTimestamp()));
                    } else {
                        messageItems.add(new MessageTextItem(R.drawable.avt, newMessage.getName(), newMessage.getMessage(), newMessage.getTimestamp()));
                    }
                }
                else {
                    messageItems.add(new MyImageMessageItem(newMessage.getName(),R.drawable.whatt,newMessage.getTimestamp()));
                }
                recyclerView.getAdapter().notifyItemInserted(messageItems.size() -1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
