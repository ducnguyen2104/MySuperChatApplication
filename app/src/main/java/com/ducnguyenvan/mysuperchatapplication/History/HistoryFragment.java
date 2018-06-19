package com.ducnguyenvan.mysuperchatapplication.History;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.ducnguyenvan.mysuperchatapplication.Model.LocalConversation;
import com.ducnguyenvan.mysuperchatapplication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HistoryFragment extends Fragment {

    EditText edt;
    Button btn;
    public static ArrayList<Conversation> conversations; //= new ArrayList<>();
    public static ArrayList<ConversationItem> conversationsItem; //= new ArrayList<>();
    public static RecyclerView recyclerView;
    public static HistoryRvAdapter adapter;
    public ArrayList<String> conversationIds;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    public HistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        conversations = new ArrayList<>();
        conversationsItem = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        edt = (EditText) rootView.findViewById(R.id.find_conversation_edt);
        edt.setWidth((int) (MainActivity.scrWidth * 0.8));
        btn = (Button) rootView.findViewById(R.id.create_grchat_btn);
        btn.setWidth((int) (MainActivity.scrWidth * 0.2));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateGrChatActivity.class);
                getContext().startActivity(intent);
            }
        });
        conversationIds = MainActivity.currentUser.getConversations();
        initRecyclerView(rootView);
        return rootView;
    }

    public void initRecyclerView(View rootView) { //from local database
        recyclerView = rootView.findViewById(R.id.history_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new HistoryRvAdapter(conversationsItem, getContext());
        recyclerView.setAdapter(adapter);
        if (conversationIds.size() == 0) { //new user, no history
            updateRecyclerView();
            return;
        }

        //load local
        MainActivity.localDatabase.localDBDao().findConversationsByListIdsWhenLoggingIn(conversationIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<LocalConversation>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<LocalConversation> localConversations) {
                        ArrayList<ConversationItem> newItemList = new ArrayList<>();
                        for (LocalConversation single : localConversations) {
                            newItemList.add(single.toConversation().makeConversationItem());
                        }
                        adapter.updateList(newItemList);
                        loadFirebaseAnđUpateLocal();
                        updateRecyclerView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadFirebaseAnđUpateLocal();
                        updateRecyclerView();
                    }
                });
    }

    public void loadFirebaseAnđUpateLocal() {
        DatabaseReference databaseReference = MainActivity.database.child("conversations");
        //get all conversations with ids in list
        /*for (int i = 0; i < conversationIds.size(); i++) {
            databaseReference.child(conversationIds.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Conversation conversation = new Conversation();
                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                    conversation.mapToObject(map);
                    ConversationItem newItem = conversation.makeConversationItem();
                    newConversations.add(conversation);
                    newConversationItems.add(newItem);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }*/
        //load all conversation
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Conversation> newConversations = new ArrayList<>();
                List<LocalConversation> newLocalConversations = new ArrayList<>();
                ArrayList<ConversationItem> newConversationItems = new ArrayList<>();
                for(DataSnapshot single : dataSnapshot.getChildren()) {
                    Conversation conversation = new Conversation();
                    Map<String, Object> map = (HashMap<String, Object>) single.getValue();
                    conversation.mapToObject(map);
                    Log.i("firebase:", map + "");
                    Log.i("firebase:", conversation.toString() + "");
                    ConversationItem newItem = conversation.makeConversationItem();
                    for(String s : conversationIds) {
                        if(conversation.getcId().equals(s)) {
                            newConversations.add(conversation);
                            newConversationItems.add(newItem);
                            break;
                        }
                    }

                }
                Log.i("firebase", "" + newConversations + ", " + newConversationItems.size());
                adapter.updateList(newConversationItems);
                conversations = newConversations;
                conversationsItem = newConversationItems;
                Log.i("firebase update", "" + conversations + ", " + conversationsItem.size());
                for (Conversation single : conversations) {
                    newLocalConversations.add(single.toLocalConversation());
                }
                Single<List<LocalConversation>> single = Single.just(newLocalConversations);
                single.observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new SingleObserver<List<LocalConversation>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<LocalConversation> localConversations) {
                                MainActivity.localDatabase.localDBDao().insertConversations(localConversations.toArray(new LocalConversation[localConversations.size()]));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public static void updateRecyclerView() { //called when close conversation activity
        DatabaseReference databaseReference = MainActivity.database.child("conversations");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Conversation conversation = new Conversation();
                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                Log.i("history fragment", "child added" + map);
                conversation.mapToObject(map);
                if (conversation.getcId() == null) {
                    return;
                }
                for (int i = 0; i < conversationsItem.size(); i++) {
                    if (conversation.getcId().equals(conversationsItem.get(i).getcId())) {
                        //if conversation is already in the list, return
                        return;
                    }
                }
                //create new conversation item
                for (int i = 0; i < conversation.getMembers().size(); i++) { //check if current user is member of the conversation
                    if (MainActivity.currentUser.getUsername().equals(conversation.getMembers().get(i))) {
                        conversationsItem.add(conversation.makeConversationItem());
                        //adapter.notifyItemInserted(conversationsItem.size() - 1);
                        adapter.updateList(conversationsItem);
                        break;
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Conversation conversation = new Conversation();
                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                conversation.mapToObject(map);
                Log.i("history fragment", "child changed" + map);
                for (int i = 0; i < conversations.size(); i++) {
                    if (conversations.get(i).getcId().equals(conversation.getcId())) {
                        String lastMsg = conversation.getLastMessage().getMessage().contains("/-img:") ? conversation.getLastMessage().getName() + " đã gửi một ảnh"
                                : (conversation.getLastMessage().getName() + ": " + conversation.getLastMessage().getMessage());
                        conversationsItem.get(i).setLastMsg(lastMsg);
                        conversationsItem.get(i).setLastMsgTime(conversation.getLastMessage().getTimestamp() + "");
                        adapter.updateList(conversationsItem);
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
