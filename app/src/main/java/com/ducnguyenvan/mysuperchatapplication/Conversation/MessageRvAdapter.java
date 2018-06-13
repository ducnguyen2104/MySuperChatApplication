package com.ducnguyenvan.mysuperchatapplication.Conversation;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ducnguyenvan.mysuperchatapplication.Model.Items.BaseItemAdapter;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.BaseItemViewHolder;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.BaseMessageItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.YourImageMessageItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.YourMessageTextItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.MyImageMessageItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.MyMessageTextItem;
import com.ducnguyenvan.mysuperchatapplication.R;
import com.ducnguyenvan.mysuperchatapplication.databinding.MyImgMessageItemRowBinding;
import com.ducnguyenvan.mysuperchatapplication.databinding.MyMessageItemRowBinding;
import com.ducnguyenvan.mysuperchatapplication.databinding.YourImgMessageItemRowBinding;
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
    YourImgMessageItemRowBinding yourImgBinding;

    public MessageRvAdapter(ArrayList<BaseMessageItem> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position) instanceof MyMessageTextItem) {
            return MSG_TXT_ME;
        } else if (messages.get(position) instanceof YourMessageTextItem) {
            return MSG_TXT_YOU;
        } else if (messages.get(position) instanceof MyImageMessageItem) {
            return MSG_IMG_ME;
        } else if (messages.get(position) instanceof YourImageMessageItem) {
            return MSG_IMG_YOU;
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
                return new MyTxtMessageItemViewHolder(myBinding);
            case MSG_TXT_YOU:
                yourbinding = DataBindingUtil.inflate(layoutInflater, R.layout.your_message_item_row, parent, false);
                return new YourTxtMessageItemViewHolder(yourbinding);
            case MSG_IMG_ME:
                myImgBinding = DataBindingUtil.inflate(layoutInflater, R.layout.my_img_message_item_row, parent, false);
                return new MyImgMessageItemViewHolder(myImgBinding);
            case MSG_IMG_YOU:
                yourImgBinding = DataBindingUtil.inflate(layoutInflater, R.layout.your_img_message_item_row, parent, false);
                return new YourImgMessageItemViewHolder(yourImgBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseItemViewHolder holder, int position) {
        if (holder instanceof MyTxtMessageItemViewHolder) {
            ((MyTxtMessageItemViewHolder) holder).bind((MyMessageTextItem) messages.get(position));
        } else if(holder instanceof YourTxtMessageItemViewHolder) {
            ((YourTxtMessageItemViewHolder) holder).bind((YourMessageTextItem) messages.get(position));
        } else if(holder instanceof MyImgMessageItemViewHolder){
            ((MyImgMessageItemViewHolder) holder).bind((MyImageMessageItem) messages.get(position));
        } else {
            ((YourImgMessageItemViewHolder) holder).bind((YourImageMessageItem) messages.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    private static class MyTxtMessageItemViewHolder extends BaseItemViewHolder {
        private MyMessageItemRowBinding myBinding;

        public MyTxtMessageItemViewHolder(MyMessageItemRowBinding myBinding) {
            super(myBinding.getRoot());
            this.myBinding = myBinding;
        }

        public void bind(MyMessageTextItem message) {
            this.myBinding.setMessage(message);
            this.myBinding.executePendingBindings();
        }
    }

    private static class MyImgMessageItemViewHolder extends BaseItemViewHolder {
        private MyImgMessageItemRowBinding myBinding;

        public MyImgMessageItemViewHolder(MyImgMessageItemRowBinding myBinding) {
            super(myBinding.getRoot());
            this.myBinding = myBinding;
        }

        public void bind(MyImageMessageItem message) {
            this.myBinding.setMessage(message);
            this.myBinding.executePendingBindings();
        }
    }

    private static class YourTxtMessageItemViewHolder extends BaseItemViewHolder {
        private YourMessageItemRowBinding yourBinding;

        public YourTxtMessageItemViewHolder(YourMessageItemRowBinding yourBinding) {
            super(yourBinding.getRoot());
            this.yourBinding = yourBinding;
        }

        public void bind(YourMessageTextItem message) {
            this.yourBinding.setMessage(message);
            this.yourBinding.executePendingBindings();
        }
    }

    private static class YourImgMessageItemViewHolder extends BaseItemViewHolder {
        private YourImgMessageItemRowBinding yourBinding;

        public YourImgMessageItemViewHolder(YourImgMessageItemRowBinding yourBinding) {
            super(yourBinding.getRoot());
            this.yourBinding = yourBinding;
        }

        public void bind(YourImageMessageItem message) {
            this.yourBinding.setMessage(message);
            this.yourBinding.executePendingBindings();
        }
    }
}
