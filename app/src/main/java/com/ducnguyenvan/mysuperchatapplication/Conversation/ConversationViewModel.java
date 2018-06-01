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
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConversationViewModel extends ViewModel {
    public ObservableField<String> message = new ObservableField<>();
    public DatabaseReference messageRef;
    public DatabaseReference conversationRef;
    public String key;
    public String converrsationName;
    public String user1;
    public String user2;
    public boolean isSendSucceeded = false;
    public Context context;
    public int memNumber;
    public boolean isGroupChat;

    public ConversationViewModel(Context context) {
        this.context = context;
        messageRef = MainActivity.database.child("messages").child(ConversationActivity.currentCId);
        conversationRef = MainActivity.database.child("conversations").child(ConversationActivity.currentCId);
        key = ConversationActivity.currentCId;
        int dividerIndex = key.indexOf('*');
        user1 = key.substring(0, dividerIndex);
        user2 = key.substring(dividerIndex + 1, key.length());
        int dividernumber = key.length() - key.replace("*", "").length(); //count number of '*' in string
        memNumber = dividernumber + 1;
        Log.i("mem number", "" + memNumber);
        isGroupChat = memNumber > 1;
        converrsationName = isGroupChat ? "Group chat" : user1.equals(MainActivity.currentUser.getUsername()) ? user2 : user1;
    }

    public void onSendBtnClicked() {
        if (message.get().length() != 0) {
            final Message newMsg = new Message(message.get(), MainActivity.currentUser.getUsername(), 0);
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
                    String anotherUser = converrsationName;
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
                    indexes.add(key.indexOf('*'));
                    for (int i = 0; i < memNumber - 2; i++) { //number of indexes = number of members - 1
                        indexes.add(key.indexOf('*', (indexes.get(i) + 1)));
                    }
                    ArrayList<String> users = new ArrayList<>();
                    Log.i("indexes size", "" + indexes.size());
                    users.add(key.substring(0, indexes.get(0)));
                    for(int i = 0; i < indexes.size(); i++) {
                        if(i + 1 < indexes.size()) {
                            users.add(key.substring(indexes.get(i) + 1, indexes.get(i + 1)));

                        }
                        else {
                            users.add(key.substring(indexes.get(i) + 1, key.length()));
                        }
                    }
                    //now have users and indexes
                    Conversation conversation = new Conversation(key, newMsg, key.replace('*',','), users);
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
