package com.ducnguyenvan.mysuperchatapplication.Conversation;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.Conversation;
import com.ducnguyenvan.mysuperchatapplication.Model.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConversationViewModel extends ViewModel {
    public ObservableField<String> message = new ObservableField<>();
    public DatabaseReference messageRef;
    public DatabaseReference conversationRef;
    public String key;
    public String membersString; //mem1*mem2*..., null if conversation is not a group chat
    public ObservableField<String> conversationName = new ObservableField<>();
    public String user1;
    public String user2;
    public boolean isSendSucceeded = false;
    public Context context;
    public int memNumber;
    public boolean isGroupChat;
    public ArrayList<String> members = new ArrayList<>();

    public ConversationViewModel(Context context) {
        this.context = context;
        key = ConversationActivity.currentCId;
        membersString = ConversationActivity.membersString;
        messageRef = MainActivity.database.child("messages").child(ConversationActivity.currentCId);
        conversationRef = MainActivity.database.child("conversations").child(ConversationActivity.currentCId);
        conversationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> map = (HashMap<String,Object>) dataSnapshot.getValue();
                Conversation conversation = new Conversation();
                if(map != null) { //conversation's already existed
                    conversation.mapToObject(map);
                    members = conversation.getMembers();
                    isGroupChat = members.size() > 2;
                    if (!isGroupChat) { //not a group chat
                        /*int dividerIndex = key.indexOf('*');
                        user1 = key.substring(0, dividerIndex);
                        user2 = key.substring(dividerIndex + 1, key.length());*/
                        user1 = members.get(0);
                        user2 = members.get(1);
                        memNumber = 2;
                        conversationName.set(user1.equals(MainActivity.currentUser.getUsername()) ? user2 : user1);
                    } else { //group chat
                        memNumber = members.size();
                        conversationName.set(conversation.getTitle().replace(",", ", "));
                        //conversationName.set(map.get("title")+ "");
                    }
                    if (membersString.length() == 0) {
                        membersString = map.get("title").toString().replace(",", "*");
                    }
                }
                else {
                    isGroupChat = membersString.length() > 0;
                    if(!isGroupChat) { //not a group chat
                        int dividerIndex = key.indexOf('*');
                        user1 = key.substring(0, dividerIndex);
                        user2 = key.substring(dividerIndex + 1, key.length());
                        memNumber = 2;
                        conversationName.set(user1.equals(MainActivity.currentUser.getUsername()) ? user2 : user1);
                    }
                    else  { //group chat
                        memNumber = membersString.length() - membersString.replace("*","").length() + 1;
                        conversationName.set(membersString.replace("*", ", "));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onSendBtnClicked() {
        if (message.get().length() != 0) {
            Date date = new Date(System.currentTimeMillis());
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String timestamp = sdf.format(date);
            final Message newMsg = new Message(message.get(), MainActivity.currentUser.getUsername(), timestamp);
            if (ConversationActivity.isConversationCreated) { //conversation's already created, update message and conversation node
                String key = messageRef.push().getKey();
                messageRef.child(key).setValue(newMsg)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                isSendSucceeded = false;
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                conversationRef.child("lastMessage").setValue(newMsg)
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                isSendSucceeded = false;
                                                Toast.makeText(context, "Gửi thất bại, vui lòng thử lại", Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                        })
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                isSendSucceeded = true;
                                                clearTextBox();
                                            }
                                        });
                            }
                        });

            } else { //create new conversation
                key = ConversationActivity.currentCId;
                if(!isGroupChat) { //conversation is not a group chat
                    String anotherUser = conversationName.get();
                    ArrayList<String> users = new ArrayList<>();
                    users.add(user1);
                    users.add(user2);
                    Conversation conversation = new Conversation(key, newMsg, user1 + " and " + user2, users);
                    Map<String, Object> conversationValues = conversation.toMap();
                    Map<String, Object> childUdpate = new HashMap<>();
                    childUdpate.put("conversations/" + key, conversationValues);
                    String subkey = MainActivity.database.child("messages").child(key).push().getKey();
                    childUdpate.put("messages/" + key + "/" + subkey, newMsg);
                    String subkey1 = MainActivity.database.child("users").child(MainActivity.currentUser.getUsername()).child("conversations").push().getKey();
                    childUdpate.put("users/" + MainActivity.currentUser.getUsername() + "/conversations/" + subkey1, key);
                    String subkey2 = MainActivity.database.child("users").child(anotherUser).child("conversations").push().getKey();
                    childUdpate.put("users/" + anotherUser + "/conversations/" + subkey2, key);
                    MainActivity.database.updateChildren(childUdpate)
                            .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Gửi thất bại, vui lòng thử lại", Toast.LENGTH_LONG).show();
                        }
                    })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    clearTextBox();
                                    ConversationActivity.initRecyclerView();
                                }
                            });
                }
                else { //group chat
                    ArrayList<Integer> indexes = new ArrayList<>();
                    indexes.add(membersString.indexOf('*'));
                    for (int i = 0; i < memNumber - 2; i++) { //number of indexes = number of members - 1
                        indexes.add(membersString.indexOf('*', (indexes.get(i) + 1)));
                    }
                    ArrayList<String> users = new ArrayList<>();
                    Log.i("indexes size", "" + indexes.size());
                    users.add(membersString.substring(0, indexes.get(0)));
                    for(int i = 0; i < indexes.size(); i++) {
                        if(i + 1 < indexes.size()) {
                            users.add(membersString.substring(indexes.get(i) + 1, indexes.get(i + 1)));

                        }
                        else {
                            users.add(membersString.substring(indexes.get(i) + 1, membersString.length()));
                        }
                    }
                    //now have users and indexes
                    Conversation conversation = new Conversation(key, newMsg, membersString.replace("*",", "), users);
                    Log.i("conv created",  conversation.getcId() + " " + conversation.getTitle());
                    Map<String, Object> conversationValues = conversation.toMap();
                    //update root/conversations and root/messages node
                    Map<String, Object> childUdpate = new HashMap<>();
                    childUdpate.put("conversations/" + key, conversationValues);
                    String subkey = MainActivity.database.child("messages").child(key).push().getKey();
                    childUdpate.put("messages/" + key + "/" + subkey, newMsg);
                    //update conversations node in every member of the conversation
                    for(int i = 0; i < users.size(); i++) {
                        String subkey1 = MainActivity.database.child("users").child(users.get(i)).child("conversations").push().getKey();
                        childUdpate.put("users/" + users.get(i) + "/conversations/" + subkey1, key);
                    }
                    MainActivity.database.updateChildren(childUdpate)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Gửi thất bại, vui lòng thử lại", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    clearTextBox();
                                    ConversationActivity.initRecyclerView();
                                }
                            });
                }
            }
        }
    }

    public void clearTextBox() {
        message.set("");
    }


}
