package com.ducnguyenvan.mysuperchatapplication.Conversation;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ducnguyenvan.mysuperchatapplication.Model.Items.BaseItemAdapter;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.BaseItemViewHolder;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.BaseMessageItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.MessageTextItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.MyImageMessageItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.MyMessageTextItem;
import com.ducnguyenvan.mysuperchatapplication.R;
import com.ducnguyenvan.mysuperchatapplication.databinding.MyImgMessageItemRowBinding;
import com.ducnguyenvan.mysuperchatapplication.databinding.MyMessageItemRowBinding;
import com.ducnguyenvan.mysuperchatapplication.databinding.YourMessageItemRowBinding;

import java.util.ArrayList;

public class MessageRvAdapter extends BaseItemAdapter {

    private static final int MSG_TXT_ME = 0;
    private static final int MSG_TXT_YOU = 1;
    private static final int MSG_IMG_ME = 2;
    private static final int MSG_IMG_YOU = 3;
    ArrayList<BaseMessageItem> messages;
    Context context;
    MyMessageItemRowBinding myBinding;
    YourMessageItemRowBinding yourbinding;
    MyImgMessageItemRowBinding myImgBinding;

    public MessageRvAdapter(ArrayList<BaseMessageItem> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position) instanceof MyMessageTextItem) {
            return MSG_TXT_ME;
        } else if (messages.get(position) instanceof MessageTextItem) {
            return MSG_TXT_YOU;
        } else if (messages.get(position) instanceof MyImageMessageItem) {
            return MSG_IMG_ME;
        } else {
            return -1;
        }
    }

    @NonNull
    @Override
    public BaseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case MSG_TXT_ME:
                myBinding = DataBindingUtil.inflate(layoutInflater, R.layout.my_message_item_row, parent, false);
                return new MyMessageItemViewHolder(myBinding);
            case MSG_TXT_YOU:
                yourbinding = DataBindingUtil.inflate(layoutInflater, R.layout.your_message_item_row, parent, false);
                return new YourMessageItemViewHolder(yourbinding);
            case MSG_IMG_ME:
                myImgBinding = DataBindingUtil.inflate(layoutInflater, R.layout.my_img_message_item_row, parent, false);
                return new MyMessageImgItemViewHolder(myImgBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseItemViewHolder holder, int position) {
        if (holder instanceof MyMessageItemViewHolder) {
            ((MyMessageItemViewHolder) holder).bind((MyMessageTextItem) messages.get(position));
        } else if(holder instanceof YourMessageItemViewHolder) {
            ((YourMessageItemViewHolder) holder).bind((MessageTextItem) messages.get(position));
        } else {
            ((MyMessageImgItemViewHolder) holder).bind((MyImageMessageItem) messages.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    private static class MyMessageItemViewHolder extends BaseItemViewHolder {
        private MyMessageItemRowBinding myBinding;

        public MyMessageItemViewHolder(MyMessageItemRowBinding myBinding) {
            super(myBinding.getRoot());
            this.myBinding = myBinding;
        }

        public void bind(MyMessageTextItem message) {
            this.myBinding.setMessage(message);
            this.myBinding.executePendingBindings();
        }
    }

    private static class MyMessageImgItemViewHolder extends BaseItemViewHolder {
        private MyImgMessageItemRowBinding myBinding;

        public MyMessageImgItemViewHolder(MyImgMessageItemRowBinding myBinding) {
            super(myBinding.getRoot());
            this.myBinding = myBinding;
        }

        public void bind(MyImageMessageItem message) {
            this.myBinding.setMessage(message);
            this.myBinding.executePendingBindings();
        }
    }

    private static class YourMessageItemViewHolder extends BaseItemViewHolder {
        private YourMessageItemRowBinding yourBinding;

        public YourMessageItemViewHolder(YourMessageItemRowBinding yourBinding) {
            super(yourBinding.getRoot());
            this.yourBinding = yourBinding;
        }

        public void bind(MessageTextItem message) {
            this.yourBinding.setMessage(message);
            this.yourBinding.executePendingBindings();
        }
    }
}
