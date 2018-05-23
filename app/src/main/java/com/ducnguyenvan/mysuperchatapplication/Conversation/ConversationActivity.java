package com.ducnguyenvan.mysuperchatapplication.Conversation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.Message;
import com.ducnguyenvan.mysuperchatapplication.R;
import com.ducnguyenvan.mysuperchatapplication.databinding.ActivityConversationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConversationActivity extends AppCompatActivity {

    static RecyclerView recyclerView;
    static ArrayList<Message> messages = new ArrayList<>();
    static String cId = "one";
    ActivityConversationBinding activityConversationBinding;
    ConversationViewModel conversationViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityConversationBinding = DataBindingUtil.setContentView(this,R.layout.activity_conversation);
        conversationViewModel = new ConversationViewModel();
        activityConversationBinding.setConversationViewModel(conversationViewModel);
        recyclerView = (RecyclerView)findViewById(R.id.conversation_rv);
        if(checkConversationNotNull()) {
            initRecyclerView();
        }
    }

    public boolean checkConversationNotNull() {
        DatabaseReference databaseReference = MainActivity.database.child("messages").child(cId);
        return databaseReference != null;
    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        DatabaseReference databaseReference = MainActivity.database.child("messages").child(cId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Message message = new Message();
                    message = singleSnapshot.getValue(Message.class);
                    messages.add(message);
                    final MessageRvAdapter adapter = new MessageRvAdapter(messages,getApplicationContext());
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        messages.clear();
        super.onDestroy();
    }
}
