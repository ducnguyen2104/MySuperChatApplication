package com.ducnguyenvan.mysuperchatapplication.Conversation;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.BaseMessageItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.MyImageMessageItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.MyMessageTextItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.YourImageMessageItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.YourMessageTextItem;
import com.ducnguyenvan.mysuperchatapplication.Model.LocalConversation;
import com.ducnguyenvan.mysuperchatapplication.Model.LocalMessage;
import com.ducnguyenvan.mysuperchatapplication.Model.Message;
import com.ducnguyenvan.mysuperchatapplication.R;
import com.ducnguyenvan.mysuperchatapplication.Utils.ImagePicker;
import com.ducnguyenvan.mysuperchatapplication.Utils.ImageSaver;
import com.ducnguyenvan.mysuperchatapplication.databinding.ActivityConversationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ConversationActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    static RecyclerView recyclerView;
    static MessageRvAdapter adapter;
    static ArrayList<BaseMessageItem> messageItems = new ArrayList<>();
    ActivityConversationBinding activityConversationBinding;
    static ConversationViewModel conversationViewModel;
    EditText editText;
    Button btn;
    Button sendImgButton;
    static String currentCId;
    static String membersString;
    static boolean isConversationCreated = false;
    static DatabaseReference messageRef;
    Button emoji;
    GridView gridView;
    static ChildEventListener childEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentCId = getIntent().getStringExtra("cId"); //username1|username2
        Log.i("convAct", "" + currentCId);
        membersString = getIntent().getStringExtra("membersString"); //!= null is conversation has 2 or more members
        if (membersString == null) {
            membersString = "";
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ConversationActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int scrWidth = displayMetrics.widthPixels;
        activityConversationBinding = DataBindingUtil.setContentView(this, R.layout.activity_conversation);
        editText = (EditText) findViewById(R.id.conversation_edt);
        btn = (Button) findViewById(R.id.conversation_btn_send);
        sendImgButton = findViewById(R.id.send_img_btn);
        sendImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(getApplicationContext());
                startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST);
            }
        });
        conversationViewModel = new ConversationViewModel(this);
        activityConversationBinding.setConversationViewModel(conversationViewModel);
        recyclerView = (RecyclerView) findViewById(R.id.conversation_rv);
        messageRef = MainActivity.database.child("messages").child(currentCId);
        DatabaseReference convRef = MainActivity.database.child("conversations").child(currentCId);
        MainActivity.localDatabase.localDBDao().findConversationById(currentCId).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<LocalConversation>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(LocalConversation localConversation) {
                isConversationCreated = true;
                initRecyclerView();
                Log.i("convAct", "init RV");
            }

            @Override
            public void onError(Throwable e) {
                initRecyclerView();
            }
        });
        emoji = findViewById(R.id.add_emoji_btn);
        gridView = findViewById(R.id.emoji_grid);
        gridView.setVisibility(View.GONE);
        ArrayList<String> emojiStringArr = new ArrayList<>();
        emojiStringArr.add(new String(Character.toChars(0x1F4A9)));
        emojiStringArr.add(new String(Character.toChars(0x1F44C)));
        emojiStringArr.add(new String(Character.toChars(0x1F601)));
        emojiStringArr.add(new String(Character.toChars(0x1F61B)));
        emojiStringArr.add(new String(Character.toChars(0x1F61C)));
        emojiStringArr.add(new String(Character.toChars(0x1F618)));
        emojiStringArr.add(new String(Character.toChars(0x1F604)));
        emojiStringArr.add(new String(Character.toChars(0x1F606)));
        emojiStringArr.add(new String(Character.toChars(0x1F923)));
        emojiStringArr.add(new String(Character.toChars(0x1F622)));
        emojiStringArr.add(new String(Character.toChars(0x1F62D)));
        emojiStringArr.add(new String(Character.toChars(0x1F610)));
        emojiStringArr.add(new String(Character.toChars(0x1F631)));
        emojiStringArr.add(new String(Character.toChars(0x1F635)));
        emojiStringArr.add(new String(Character.toChars(0x1F621)));
        emojiStringArr.add(new String(Character.toChars(0x1F615)));
        emojiStringArr.add(new String(Character.toChars(0x1F44D)));
        emojiStringArr.add(new String(Character.toChars(0x1F493)));
        emojiStringArr.add(new String(Character.toChars(0x1F427)));
        emojiStringArr.add(new String(Character.toChars(0x1F60A)));
        emojiStringArr.add(new String(Character.toChars(0x1F609)));
        emojiStringArr.add(new String(Character.toChars(0x1F641)));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emojiStringArr);
        gridView.setAdapter(arrayAdapter);
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gridView.getVisibility() == View.GONE) {
                    gridView.setVisibility(View.VISIBLE);
                    if (messageItems.size() > 1) {
                        recyclerView.scrollToPosition(messageItems.size() - 1);
                    }
                } else {
                    gridView.setVisibility(View.GONE);
                }
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = gridView.getItemAtPosition(position);
                String s = (String) o;
                editText.setText(editText.getText() + s);
            }
        });
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message newMessage = new Message();
                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                newMessage.mapToObject(map);
                //newMessage = dataSnapshot.getValue(Message.class);
                for (int i = 0; i < messageItems.size(); i++) {
                    if (newMessage.getName().equals(messageItems.get(i).getUsername()) && newMessage.getRealtimestamp() == messageItems.get(i).getRealtimestamp()) {
                        //if message is already present, return
                        return;
                    }
                }
                if (newMessage.getMessage().contains("/-img:")) { //img message
                    String imgName = currentCId + "/" + newMessage.getMessage().substring(6, newMessage.getMessage().length());
                    if (newMessage.getName().equals(MainActivity.currentUser.getUsername())) {
                        messageItems.add(new MyImageMessageItem(newMessage.getName(), imgName, newMessage.getTimestamp(), newMessage.getRealtimestamp(), false));
                    } else {
                        boolean isFirst = messageItems.size() == 0 || !newMessage.getName().equals(messageItems.get(messageItems.size() - 1).getUsername());
                        messageItems.add(new YourImageMessageItem(R.drawable.avt, newMessage.getName(), imgName, newMessage.getTimestamp(), newMessage.getRealtimestamp(), isFirst));
                    }
                } else {
                    if (newMessage.getName().equals(MainActivity.currentUser.getUsername())) {
                        messageItems.add(new MyMessageTextItem(newMessage.getName(), newMessage.getMessage(), newMessage.getTimestamp(), newMessage.getRealtimestamp()));
                    } else {
                        boolean isFirst = messageItems.size() == 0 || !newMessage.getName().equals(messageItems.get(messageItems.size() - 1).getUsername());
                        messageItems.add(new YourMessageTextItem(R.drawable.avt, newMessage.getName(), newMessage.getMessage(), newMessage.getTimestamp(), newMessage.getRealtimestamp(), isFirst));
                    }
                }
                adapter.updateList(messageItems);
                recyclerView.scrollToPosition(messageItems.size() - 1);
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
        };
    }

    public static void initRecyclerView() {
        YourMessageTextItem lastMessage = new YourMessageTextItem();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessageRvAdapter(messageItems, recyclerView.getContext());
        recyclerView.setAdapter(adapter);

        //load local
        Log.i("ConvAct", "load local msg");
        MainActivity.localDatabase.localDBDao().findMessagesByConvId(currentCId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<LocalMessage>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<LocalMessage> localMessages) {
                        Log.i("ConvAct", "load success local msg: " + localMessages.size());
                        for(LocalMessage single : localMessages) {
                            messageItems.add(single.toMessage().toMessageItem(messageItems.size() == 0 || !single.getName().equals(messageItems.get(messageItems.size() - 1).getUsername())));
                        }
                        recyclerView.scrollToPosition(messageItems.size() - 1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("ConvAct", "failed to load local msg ");
                    }
                });

        //load firebase
        Log.i("ConvAct", "load firebase msg");
        DatabaseReference databaseReference = MainActivity.database.child("messages").child(currentCId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Message message = new Message();
                    Map<String, Object> map = (HashMap<String, Object>) singleSnapshot.getValue();
                    //Log.i("map", map + "");
                    message.mapToObject(map);
                    if(message.getMessage() != null) {
                        isConversationCreated = true;
                    }
                     else {
                        return;
                    }
                    //newMessagesItems.add(message.toMessageItem(messageItems.size() == 0 || !message.getName().equals(messageItems.get(messageItems.size() - 1).getUsername())));
                    Single.just(message).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<Message>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Message message) {
                            //update in local db
                            MainActivity.localDatabase.localDBDao().insertMessages(message.toLocalMessage(true));
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
                    //adapter.notifyItemInserted(messageItems.size() - 1);
                }
                //Log.i("convAct", "load success firebase msg:" + newMessagesItems.size());
                //load local db again and update ui
                Log.i("ConvAct", "load local db again and update ui");
                ArrayList<BaseMessageItem> newMessagesItems = new ArrayList<>();
                MainActivity.localDatabase.localDBDao().findMessagesByConvId(currentCId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<LocalMessage>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<LocalMessage> localMessages) {
                                for(LocalMessage single : localMessages) {
                                    newMessagesItems.add(single.toMessage().toMessageItem(newMessagesItems.size() == 0 || !single.getName().equals(newMessagesItems.get(newMessagesItems.size() - 1).getUsername())));
                                }
                                if(adapter == null || messageItems == null || recyclerView == null) {
                                    return;
                                }
                                adapter.updateList(newMessagesItems);
                                messageItems = newMessagesItems;
                                recyclerView.scrollToPosition(messageItems.size() - 1);
                                Log.i("ConvAct", "scroll to " + messageItems.size());
                                updateRecyclerView();
                            }

                            @Override
                            public void onError(Throwable e) {
                            }
                        });


                //recyclerView.scrollToPosition(messageItems.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        messageItems.clear();
        activityConversationBinding.unbind();
        messageRef.removeEventListener(childEventListener);
        recyclerView = null;
        conversationViewModel = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public static void updateRecyclerView() {
        messageRef.addChildEventListener(childEventListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                if (bitmap == null) {
                    break;
                }
                //img.setImageBitmap(bitmap);
                final String key = MainActivity.database.child("messages").child(currentCId).push().getKey();
                //save to storage
                new ImageSaver(getApplicationContext()).
                        setFileName(key + ".jpg").
                        setDirectoryName("messages").
                        save(bitmap);
                //create message object
                long realTimestamp = System.currentTimeMillis();
                Date date = new Date(realTimestamp);
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String timestamp = sdf.format(date);
                final Message newMessage = new Message("/-img:" + key, MainActivity.currentUser.getUsername(),timestamp,realTimestamp, currentCId);
                //add message item
                final MyImageMessageItem newMsgItem = new MyImageMessageItem(newMessage.getName(), key, newMessage.getTimestamp(), newMessage.getRealtimestamp(), true);
                messageItems.add(newMsgItem);
                recyclerView.getAdapter().notifyItemInserted(messageItems.size() - 1);
                recyclerView.scrollToPosition(messageItems.size() - 1);
                //add to storage
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference msgStorageReference = storage.getReference().child("messages").child(currentCId).child(key + ".jpg");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataArr = baos.toByteArray();
                uploadToServer(msgStorageReference,dataArr,newMessage,key,newMsgItem);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
    public void uploadToServer(StorageReference msgStorageReference, byte[] dataArr, final Message newMessage, final String key, final MyImageMessageItem newMsgItem) {
        newMsgItem.setStatus("Đang gửi...");
        msgStorageReference.putBytes(dataArr)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        MainActivity.database.child("conversations").child(currentCId).child("lastMessage").setValue(newMessage);
                        MainActivity.database.child("messages").child(currentCId).child(key).setValue(newMessage);
                        //delete local img on success
                        new ImageSaver(getApplicationContext()).
                                setFileName(key + ".jpg").
                                setDirectoryName("messages").
                                deleteFile();
                        newMsgItem.setImgName(currentCId + "/" + key);
                        //hide text view "Đang gửi..."
                        newMsgItem.setStatusTextViewVisibility(View.GONE);
                        Log.i("visibility", "" + ((MyImageMessageItem)(messageItems.get(messageItems.size() - 1))).getStatusTextViewVisibility());
                        Toast.makeText(getApplicationContext(),"complete",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        newMsgItem.setRetryButtonVisibility(View.VISIBLE);
                        newMsgItem.setStatus("Gửi thất bại");
                    }
                });
    }
}
