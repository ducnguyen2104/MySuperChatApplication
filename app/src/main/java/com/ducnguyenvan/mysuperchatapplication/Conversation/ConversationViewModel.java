package com.ducnguyenvan.mysuperchatapplication.Conversation;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.ducnguyenvan.mysuperchatapplication.History.HistoryFragment;
import com.ducnguyenvan.mysuperchatapplication.History.HistoryRvAdapter;
import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.Conversation;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.ConversationItem;
import com.ducnguyenvan.mysuperchatapplication.Model.LocalConversation;
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
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ConversationViewModel extends ViewModel {
    private static final int PICK_IMAGE_REQUEST = 1;
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
                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                Conversation conversation = new Conversation();
                if (map != null) { //conversation's already existed
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
                } else {
                    isGroupChat = membersString.length() > 0;
                    if (!isGroupChat) { //not a group chat
                        int dividerIndex = key.indexOf('*');
                        user1 = key.substring(0, dividerIndex);
                        user2 = key.substring(dividerIndex + 1, key.length());
                        memNumber = 2;
                        conversationName.set(user1.equals(MainActivity.currentUser.getUsername()) ? user2 : user1);
                    } else { //group chat
                        memNumber = membersString.length() - membersString.replace("*", "").length() + 1;
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
        MainActivity.reuploadAllFailedUploadMessage();
        MainActivity.reUploadAllFaileđUploadConversation();
        if (message.get().length() != 0) {
            String emojiMsg = replaceToEmoji(message.get());
            long realTimestamp = System.currentTimeMillis();
            Date date = new Date(realTimestamp);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String timestamp = sdf.format(date);
            final Message newMsg = new Message(emojiMsg, MainActivity.currentUser.getUsername(), timestamp, realTimestamp, key);
            //conversation's already created, update message and conversation node
            if (ConversationActivity.isConversationCreated) {
                //update local
                Single<Message> single = Single.just(newMsg);
                single.subscribeOn(Schedulers.io())
                        //.observeOn(Schedulers.io())
                        .subscribe(new SingleObserver<Message>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Message message) {
                                //update local conversation
                                MainActivity.localDatabase.localDBDao().updateLastMsg(ConversationActivity.currentCId, message.message, message.name, message.realtimestamp, message.timestamp, false);
                                //update UI in history tab
                                MainActivity.localDatabase.localDBDao().findConversationsByListIdsWhenLoggingIn(MainActivity.currentUser.conversations)
                                        //subscribeOn(Schedulers.io())
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
                                                HistoryFragment.adapter.updateList(newItemList);
                                                HistoryFragment.conversationsItem = newItemList;
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                            }
                                        });

                                //update local message
                                MainActivity.localDatabase.localDBDao().insertMessages(message.toLocalMessage(false));
                                //display on screen
                                ConversationActivity.messageItems.add(message.toMessageItem(ConversationActivity.messageItems.size() == 0 || !message.getName().equals(ConversationActivity.messageItems.get(ConversationActivity.messageItems.size() - 1).getUsername())));
                                Single.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Integer>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(Integer integer) {
                                        Log.i("ConvVM", "item added: " + message.getMessage());
                                        ConversationActivity.adapter.notifyItemInserted(ConversationActivity.messageItems.size() - 1);
                                        ConversationActivity.recyclerView.scrollToPosition(ConversationActivity.messageItems.size() - 1);
                                        clearTextBox();
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                                clearTextBox();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                //update on firebase
                String key = messageRef.push().getKey();
                messageRef.child(key).setValue(newMsg)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                isSendSucceeded = false;
                                Log.i("convAct", "send failed" + newMsg.getMessage());
                                return;
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
                                            }
                                        })
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                isSendSucceeded = true;
                                                //update uploaded status on local message
                                                Single.just(true).observeOn(Schedulers.io()).subscribe(new SingleObserver<Boolean>() {
                                                    @Override
                                                    public void onSubscribe(Disposable d) {

                                                    }

                                                    @Override
                                                    public void onSuccess(Boolean aBoolean) {
                                                        MainActivity.localDatabase.localDBDao().updateLocalMsgSendStatus(newMsg.getConvId(), newMsg.name, newMsg.realtimestamp, true);
                                                        MainActivity.localDatabase.localDBDao().updateLocalConversationSendStatus(newMsg.convId, true);
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {

                                                    }
                                                });
                                            }
                                        });
                            }
                        });
            } else { //create new conversation
                key = ConversationActivity.currentCId;
                if (!isGroupChat) { //conversation is not a group chat
                    String anotherUser = conversationName.get();
                    ArrayList<String> users = new ArrayList<>();
                    users.add(user1);
                    users.add(user2);
                    final Conversation conversation = new Conversation(key, newMsg, user1 + " and " + user2, users);
                    //update local
                    Single<Conversation> single = Single.just(conversation);
                    single.subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(new SingleObserver<Conversation>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Conversation conversation1) {
                                    //create new conversation in local DB
                                    MainActivity.localDatabase.localDBDao().insertConversations(conversation1.toLocalConversation(false));
                                    //update current user's conversation list
                                    MainActivity.currentUser.conversations.add(conversation1.getcId());
                                    MainActivity.localDatabase.localDBDao().updateUsers(MainActivity.currentUser.toLocalUser());
                                    //update UI
                                    HistoryFragment.conversationsItem.add(conversation.makeConversationItem());
                                    //HistoryFragment.adapter.updateList(HistoryFragment.conversationsItem);
                                    Single<ArrayList<ConversationItem>> newSingle = Single.just(HistoryFragment.conversationsItem);
                                    newSingle.observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ArrayList<ConversationItem>>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(ArrayList<ConversationItem> conversationItems) {
                                            HistoryFragment.adapter = new HistoryRvAdapter(conversationItems, context);
                                            HistoryFragment.recyclerView.setAdapter(HistoryFragment.adapter);
                                            ConversationActivity.isConversationCreated = true;
                                            Log.i("ConvVM", "new conv item added: " + newMsg.getMessage());
                                            ConversationActivity.messageItems.add(newMsg.toMessageItem(true));
                                            ConversationActivity.initRecyclerView();
                                            clearTextBox();
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

                    //Update on firebase
                    Map<String, Object> conversationValues = conversation.toMap();
                    Log.i("new conv VM", "" + conversationValues);
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
                                    Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(Integer integer) {
                                            MainActivity.localDatabase.localDBDao().updateLocalConversationSendStatus(conversation.cId, true);
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    });

                                }
                            });
                } else { //group chat
                    ArrayList<Integer> indexes = new ArrayList<>();
                    indexes.add(membersString.indexOf('*'));
                    for (int i = 0; i < memNumber - 2; i++) { //number of indexes = number of members - 1
                        indexes.add(membersString.indexOf('*', (indexes.get(i) + 1)));
                    }
                    ArrayList<String> users = new ArrayList<>();
                    users.add(membersString.substring(0, indexes.get(0)));
                    for (int i = 0; i < indexes.size(); i++) {
                        if (i + 1 < indexes.size()) {
                            users.add(membersString.substring(indexes.get(i) + 1, indexes.get(i + 1)));

                        } else {
                            users.add(membersString.substring(indexes.get(i) + 1, membersString.length()));
                        }
                    }
                    //now have users and indexes
                    Conversation conversation = new Conversation(key, newMsg, membersString.replace("*", ", "), users);

                    //update local
                    Single<Conversation> single = Single.just(conversation);
                    single.subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(new SingleObserver<Conversation>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Conversation conversation1) {
                                    //create new conversation in local DB
                                    MainActivity.localDatabase.localDBDao().insertConversations(conversation1.toLocalConversation(false));
                                    //update current user's conversation list
                                    MainActivity.currentUser.conversations.add(conversation1.getcId());
                                    MainActivity.localDatabase.localDBDao().updateUsers(MainActivity.currentUser.toLocalUser());
                                    //update UI
                                    HistoryFragment.conversationsItem.add(conversation.makeConversationItem());
                                    //HistoryFragment.adapter.updateList(HistoryFragment.conversationsItem);
                                    Single<ArrayList<ConversationItem>> newSingle = Single.just(HistoryFragment.conversationsItem);
                                    newSingle.observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ArrayList<ConversationItem>>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(ArrayList<ConversationItem> conversationItems) {
                                            HistoryFragment.adapter.updateList(conversationItems);
                                            ConversationActivity.initRecyclerView();
                                            ConversationActivity.isConversationCreated = true;
                                            clearTextBox();

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    });
                                    clearTextBox();
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });
                    //update firebase
                    Log.i("new gr chat", conversation.getcId() + ", " + conversation.getTitle() + " ");
                    Map<String, Object> conversationValues = conversation.toMap();
                    Log.i("new conv VM", "" + conversationValues);
                    //update root/conversations and root/messages node
                    Map<String, Object> childUdpate = new HashMap<>();
                    childUdpate.put("conversations/" + key, conversationValues);
                    String subkey = MainActivity.database.child("messages").child(key).push().getKey();
                    childUdpate.put("messages/" + key + "/" + subkey, newMsg);
                    //update conversations node in every member of the conversation
                    for (int i = 0; i < users.size(); i++) {
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
                                    ConversationActivity.initRecyclerView();
                                    ConversationActivity.isConversationCreated = true;
                                }
                            });
                }
            }
        }
        clearTextBox();
    }

    public void clearTextBox() {
        message.set("");
    }

    public String replaceToEmoji(String msg) {
        String s = msg.replace(":poop:", " " + new String(Character.toChars(0x1F4A9)) + " ")
                .replace(":ok:", " " + new String(Character.toChars(0x1F44C)) + " ")
                .replace(":D", " " + new String(Character.toChars(0x1F601)) + " ")
                .replace(":d", " " + new String(Character.toChars(0x1F601)) + " ")
                .replace(":P", " " + new String(Character.toChars(0x1F61B)) + " ")
                .replace(":p", " " + new String(Character.toChars(0x1F61B)) + " ")
                .replace(";P", " " + new String(Character.toChars(0x1F61C)) + " ")
                .replace(";p", " " + new String(Character.toChars(0x1F61C)) + " ")
                .replace(":*", " " + new String(Character.toChars(0x1F618)) + " ")
                .replace(":))", " " + new String(Character.toChars(0x1F604)) + " ")
                .replace("XD", " " + new String(Character.toChars(0x1F606)) + " ")
                .replace("xd", " " + new String(Character.toChars(0x1F606)) + " ")
                .replace("xD", " " + new String(Character.toChars(0x1F606)) + " ")
                .replace("Xd", " " + new String(Character.toChars(0x1F606)) + " ")
                .replace("=))", " " + new String(Character.toChars(0x1F923)) + " ")
                .replace(":'(", " " + new String(Character.toChars(0x1F622)) + " ")
                .replace(":((", " " + new String(Character.toChars(0x1F62D)) + " ")
                .replace(":|", " " + new String(Character.toChars(0x1F610)) + " ")
                .replace(":o", " " + new String(Character.toChars(0x1F631)) + " ")
                .replace(":O", " " + new String(Character.toChars(0x1F631)) + " ")
                .replace("@@", " " + new String(Character.toChars(0x1F635)) + " ")
                .replace(">:(", " " + new String(Character.toChars(0x1F621)) + " ")
                .replace(":/", " " + new String(Character.toChars(0x1F615)) + " ")
                .replace("(y)", " " + new String(Character.toChars(0x1F44D)) + " ")
                .replace("(y)", " " + new String(Character.toChars(0x1F44D)) + " ")
                .replace("<3", " " + new String(Character.toChars(0x1F493)) + " ")
                .replace("<(\")", " " + new String(Character.toChars(0x1F427)) + " ")
                .replace(":)", " " + new String(Character.toChars(0x1F60A)) + " ")
                .replace(";)", " " + new String(Character.toChars(0x1F609)) + " ")
                .replace(":(", " " + new String(Character.toChars(0x1F641)) + " ");
        return s;
    }

    public void onBackBtnClicked() {
        ((Activity) context).finish();
    }

}
