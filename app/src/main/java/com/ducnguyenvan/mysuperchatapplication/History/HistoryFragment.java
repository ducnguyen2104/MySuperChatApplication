package com.ducnguyenvan.mysuperchatapplication.History;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ducnguyenvan.mysuperchatapplication.History.CreateGroupChat.CreateGrChatActivity;
import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.Conversation;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.ConversationItem;
import com.ducnguyenvan.mysuperchatapplication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryFragment extends Fragment {

    EditText edt;
    Button btn;
    public static ArrayList<Conversation> conversations; //= new ArrayList<>();
    public static ArrayList<ConversationItem> conversationsItem; //= new ArrayList<>();
    public static RecyclerView recyclerView;
    public static HistoryRvAdapter adapter;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    public HistoryFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        conversations = new ArrayList<>();
        conversationsItem = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_history,container,false);
        edt = (EditText)rootView.findViewById(R.id.find_conversation_edt);
        edt.setWidth((int)(MainActivity.scrWidth*0.8));
        btn = (Button)rootView.findViewById(R.id.create_grchat_btn);
        btn.setWidth((int)(MainActivity.scrWidth*0.2));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateGrChatActivity.class);
                getContext().startActivity(intent);
            }
        });
        initRecyclerView(rootView);
        return rootView;
    }
    public void initRecyclerView(View rootView) {
        recyclerView = rootView.findViewById(R.id.history_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        ArrayList<String> conversationIds = MainActivity.currentUser.getConversations();
        adapter = new HistoryRvAdapter(conversationsItem,getContext());
        recyclerView.setAdapter(adapter);
        DatabaseReference databaseReference = MainActivity.database.child("conversations");
        if(conversationIds.size() == 0) { //new user, no history
            updateRecyclerView();
            return;
        }
        for(int i = 0; i < conversationIds.size(); i++) {
            Query conversationQuery = databaseReference.orderByChild("cId").equalTo(conversationIds.get(i));
            conversationQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Conversation conversation = new Conversation();
                        Map<String,Object> map = (HashMap<String,Object>) singleSnapshot.getValue();
                        Log.i("map", "" + map);
                        conversation.mapToObject(map);
                        //conversation = singleSnapshot.getValue(Conversation.class);
                        if(conversation.cId != null) {
                            conversations.add(conversation);
                            StringBuilder membersToStringBuilder = new StringBuilder();
                            for(String s : conversation.getMembers()) {
                                if(membersToStringBuilder.length() == 0) {
                                    membersToStringBuilder.append(s);
                                }
                                else {
                                    membersToStringBuilder.append(", " + s);
                                }
                            }
                            String name = conversation.getMembers().size() == 2 ?
                                    (MainActivity.currentUser.getUsername().equals(conversation.members.get(0)) ? conversation.members.get(1) : conversation.members.get(0)) :
                                    membersToStringBuilder.toString();
                            String lastMsg = conversation.getLastMessage().getMessage().contains("/-img:") ? conversation.getLastMessage().getName() + " đã gửi một ảnh"
                                    : (conversation.getLastMessage().getName()+ ": "+ conversation.getLastMessage().getMessage());
                            conversationsItem.add(new ConversationItem(R.drawable.avt, conversation.getcId(), name,
                                    lastMsg
                                    ,conversation.getLastMessage().getTimestamp()+""));
                            Log.i("Add", ""+conversation.getcId());
                            adapter.notifyItemInserted(conversationsItem.size() -1);
                        }
                    }
                    updateRecyclerView();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }


    public static void updateRecyclerView() { //called when close conversation activity
        DatabaseReference databaseReference = MainActivity.database.child("conversations");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("history fragment", "child added");
                Conversation conversation = new Conversation();
                Map<String,Object> map = (HashMap<String,Object>) dataSnapshot.getValue();
                conversation.mapToObject(map);
                if(conversation.getcId() == null) {
                    return;

                }
                for(int i = 0; i < conversationsItem.size(); i++) {
                    if(conversation.getcId().equals(conversationsItem.get(i).getcId())) {
                        //if conversation is already in the list, return
                        return;
                    }
                }
                //create new conversation item
                for(int i = 0; i < conversation.getMembers().size(); i++) { //check if current user is member of the conversation
                    if(MainActivity.currentUser.getUsername().equals(conversation.getMembers().get(i))) {
                        String name = conversation.getMembers().size() == 2 ?
                                MainActivity.currentUser.getUsername().equals(conversation.members.get(0)) ? conversation.members.get(1) : conversation.members.get(0) :
                                "Group chat: " + conversation.getTitle();
                        String lastMsg = conversation.getLastMessage().getMessage().contains("/-img:") ? conversation.getLastMessage().getName() + " đã gửi một ảnh"
                                : (conversation.getLastMessage().getName()+ ": "+ conversation.getLastMessage().getMessage());
                        conversationsItem.add(new ConversationItem(R.drawable.avt, conversation.getcId(), name,
                                lastMsg
                                ,conversation.getLastMessage().getTimestamp()+""));
                        adapter.notifyItemInserted(conversationsItem.size() - 1);
                    }
                }

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Conversation conversation = new Conversation();
                Map<String,Object> map = (HashMap<String,Object>) dataSnapshot.getValue();
                conversation.mapToObject(map);
                for(int i = 0; i < conversations.size(); i ++) {
                    if (conversations.get(i).getcId().equals(conversation.getcId())) {
                        String lastMsg = conversation.getLastMessage().getMessage().contains("/-img:") ? conversation.getLastMessage().getName() + " đã gửi một ảnh"
                                : (conversation.getLastMessage().getName()+ ": "+ conversation.getLastMessage().getMessage());
                        conversationsItem.get(i).setLastMsg(lastMsg);
                        conversationsItem.get(i).setLastMsgTime(conversation.getLastMessage().getTimestamp() + "");
                        adapter.notifyItemChanged(i);
                    }
                }
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

    @Override
    public void onDestroyView() {
        conversations = null;
        super.onDestroyView();
    }
}
