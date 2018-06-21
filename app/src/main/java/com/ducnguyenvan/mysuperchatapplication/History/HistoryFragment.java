package com.ducnguyenvan.mysuperchatapplication.History;

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

import io.reactivex.Scheduler;
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
        //linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new HistoryRvAdapter(conversationsItem, getContext());
        recyclerView.setAdapter(adapter);
        if (conversationIds.size() == 0) { //new user, no history
            //updateRecyclerView(getContext());
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
                            Log.i("history frag", "local conv " + single.cId);
                        }
                        adapter.updateList(newItemList);
                        //loadFirebaseAnđUpateLocal();
                        //updateRecyclerView(getContext());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("history frag", "no local conv ");
                        //loadFirebaseAnđUpateLocal();
                        //updateRecyclerView(getContext());
                    }
                });
        loadFirebaseAnđUpateLocal();
    }

    public void loadFirebaseAnđUpateLocal() {
        Log.i("history frag", "load fb and update local");
        DatabaseReference databaseReference = MainActivity.database.child("conversations");
        //load all conversation and update local DB
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Conversation> newConversations = new ArrayList<>();
                List<LocalConversation> newLocalConversations = new ArrayList<>();
                ArrayList<ConversationItem> newConversationItems = new ArrayList<>();
                for(DataSnapshot single : dataSnapshot.getChildren()) {
                    Conversation conversation = new Conversation();
                    Map<String, Object> map = (HashMap<String, Object>) single.getValue();
                    conversation.mapToObject(map);
                    Single.just(conversation).observeOn(Schedulers.io()).subscribe(new SingleObserver<Conversation>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Conversation conversation) {
                            Log.i("history frag", "update local db " + conversation.cId);
                            MainActivity.localDatabase.localDBDao().insertConversations(conversation.toLocalConversation(true));
                            MainActivity.localDatabase.localDBDao().findConversationsByListIdsWhenLoggingIn(MainActivity.currentUser.conversations)
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new SingleObserver<List<LocalConversation>>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(List<LocalConversation> localConversations) {
                                            Log.i("history frag", "local conv: " + localConversations);
                                            ArrayList<ConversationItem> newList = new ArrayList<>();
                                            for(LocalConversation localConversation : localConversations) {
                                                for(String s : MainActivity.currentUser.conversations) {
                                                    Log.i("history frag", "s: " + s);
                                                    Log.i("history frag", "local conv cid: " + localConversation.cId);
                                                    if(s.equals(localConversation.cId)) {
                                                        newList.add(localConversation.toConversation().makeConversationItem());
                                                        Log.i("history frag", "updated conv " + localConversation.cId);
                                                    }
                                                }
                                            }
                                            Log.i("history frag", "conversation list: " + newList.size());
                                            adapter = new HistoryRvAdapter(newList, getContext());
                                            recyclerView.setAdapter(adapter);
                                            //adapter.updateList(newList);
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    });
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
                }
                Log.i("history frag", "conversations" + MainActivity.currentUser.conversations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onDestroyView() {
        conversations = null;
        super.onDestroyView();
    }
}
