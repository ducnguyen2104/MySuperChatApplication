package com.ducnguyenvan.mysuperchatapplication.Conversation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.Message;
import com.ducnguyenvan.mysuperchatapplication.R;

import java.util.ArrayList;

public class MessageRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int MSG_ME = 0;
    private static final int MSG_YOU = 1;
    ArrayList<Message> messages;
    Context context;

    public MessageRvAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getName().equals(MainActivity.currentUser.getUsername())) {
            return MSG_ME;
        }
        else {
            return MSG_YOU;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == MSG_ME) {
            View view = layoutInflater.inflate(R.layout.my_message_item_row, parent, false);
            return new MyMessageItemViewHolder(view);
        }
        else {
            View view = layoutInflater.inflate(R.layout.your_message_item_row, parent, false);
            return new MyMessageItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyMessageItemViewHolder) {
            ((MyMessageItemViewHolder) holder).bind(messages.get(position));
        }
        else {
            ((YourMessageItemViewHolder) holder).bind(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    private static class MyMessageItemViewHolder extends RecyclerView.ViewHolder {

        TextView msg;
        public MyMessageItemViewHolder(View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.message_content);
        }

        public void bind(Message message) {
            msg.setText(message.getName() + ": " + message.getMessage());
        }
    }

    private static class YourMessageItemViewHolder extends RecyclerView.ViewHolder {

        TextView msg;
        public YourMessageItemViewHolder(View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.message_content);
        }

        public void bind(Message message) {
        }
    }
}
