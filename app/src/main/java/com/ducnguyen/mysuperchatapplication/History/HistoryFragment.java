package com.ducnguyen.mysuperchatapplication.History;

import android.content.Context;
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
import android.widget.Toast;

import com.ducnguyen.mysuperchatapplication.History.CreateGroupChat.CreateGrChatActivity;
import com.ducnguyen.mysuperchatapplication.Login.LoginActivity;
import com.ducnguyen.mysuperchatapplication.MainActivity;
import com.ducnguyen.mysuperchatapplication.Model.Conversation;
import com.ducnguyen.mysuperchatapplication.Model.Items.ConversationItem;
import com.ducnguyen.mysuperchatapplication.Model.LocalConversation;
import com.ducnguyen.mysuperchatapplication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HistoryFragment extends Fragment {

    EditText edt;
    Button btn;
    public static ArrayList<Conversation> conversations;
    public static ArrayList<ConversationItem> conversationsItem;
    public static RecyclerView recyclerView;
    public static HistoryRvAdapter adapter;
    public ArrayList<String> conversationIds;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    public HistoryFragment() { }

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
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateGrChatActivity.class);
            getContext().startActivity(intent);
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
                    public void onSuccess(List<LocalConversation> localConversations) { //data available in local
                        ArrayList<ConversationItem> newItemList = new ArrayList<>();
                        for (LocalConversation single : localConversations) {
                            newItemList.add(single.toConversation().makeConversationItem());
                        }
                        adapter.updateList(newItemList);
                        loadFirebaseAnđUpateLocal();
                    }

                    @Override
                    public void onError(Throwable e) { //data not available in local
                        loadFirebaseAnđUpateLocal();
                    }
                });
        loadFirebaseAnđUpateLocal();
    }

    public void loadFirebaseAnđUpateLocal() {
        DatabaseReference databaseReference = MainActivity.database.child("conversations");
        //load all conversation and update local DB
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Conversation> newConvList = new ArrayList<>();
                ArrayList<ConversationItem> newConvItemList = new ArrayList<>();
                for(DataSnapshot single : dataSnapshot.getChildren()) { //for each conversation
                    Conversation conversation = new Conversation();
                    Map<String, Object> map = (HashMap<String, Object>) single.getValue();
                    conversation.mapToObject(map);
                    for(String s : conversation.members) { //check if current user is a member of conversation
                        if(s.equals(MainActivity.currentUser.getUsername())) { //current logged in user is a member of conversation
                            //newConvItemList.add(conversation.makeConversationItem());
                            newConvList.add(conversation);
                            //update local db
                            Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Integer integer) {
                                    MainActivity.localDatabase.localDBDao().insertConversations(conversation.toLocalConversation(true));
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });
                        }
                    }
                }
                Log.i("newConv", "" + newConvList);
                //sort new conversation list by last msg time desc
                for(int i = 0; i < newConvList.size(); i ++) {
                    int max = 0;
                    for (int j = i + 1; j < newConvList.size(); j ++) {
                        if(newConvList.get(j).getLastMessage().getRealtimestamp() > newConvList.get(i).getLastMessage().getRealtimestamp()) {
                            Conversation temp = newConvList.get(i);
                            newConvList.set(i, newConvList.get(j));
                            newConvList.set(j, temp);
                        }
                    }
                }
                for(Conversation single : newConvList) {
                    newConvItemList.add(single.makeConversationItem());
                }
                HistoryFragment.conversationsItem = newConvItemList;
                HistoryFragment.adapter = new HistoryRvAdapter(newConvItemList,getContext());
                HistoryFragment.recyclerView.setAdapter(HistoryFragment.adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadFirebaseAnđUpdateLocal2() {
        DatabaseReference databaseReference = MainActivity.database.child("conversations");
        for(String s : MainActivity.currentUser.conversations) {

        }
    }
}
