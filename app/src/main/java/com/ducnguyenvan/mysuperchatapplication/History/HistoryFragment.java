package com.ducnguyenvan.mysuperchatapplication.History;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.Conversation;
import com.ducnguyenvan.mysuperchatapplication.Model.ConversationItem;
import com.ducnguyenvan.mysuperchatapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    EditText edt;
    Button btn;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    public HistoryFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history,container,false);
        edt = (EditText)rootView.findViewById(R.id.find_conversation_edt);
        edt.setWidth((int)(MainActivity.scrWidth*0.8));
        btn = (Button)rootView.findViewById(R.id.create_grchat_btn);
        btn.setWidth((int)(MainActivity.scrWidth*0.2));
        initRecyclerView(rootView);
        return rootView;
    }
    public void initRecyclerView(View rootView) {
        final RecyclerView recyclerView = rootView.findViewById(R.id.history_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        final ArrayList<ConversationItem> conversationsItem = new ArrayList<>();
        ArrayList<String> conversationIds = MainActivity.currentUser.getConversations();
        if(conversationIds == null)
        {
            return;
        }
        for(int i = 0; i < conversationIds.size(); i++) {
            DatabaseReference databaseReference = MainActivity.database.child("conversations");
            Query conversationQuery = databaseReference.orderByChild("cId").equalTo(conversationIds.get(i));
            conversationQuery.addValueEventListener(new ValueEventListener() {
                Conversation conversation = new Conversation();
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        conversation = singleSnapshot.getValue(Conversation.class);
                        if(conversation != null) {
                            conversationsItem.add(new ConversationItem(R.drawable.avt, conversation.getTitle(),
                                    (conversation.getLastMessage().getName()+ ": "+ conversation.getLastMessage().getMessage())
                                    ,conversation.getLastMessage().getTimestamp()+""));
                        }
                        final HistoryRvAdapter adapter = new HistoryRvAdapter(conversationsItem,getContext());
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
