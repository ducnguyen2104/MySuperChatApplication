package com.ducnguyenvan.mysuperchatapplication.Conversation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ducnguyenvan.mysuperchatapplication.Model.Message;
import com.ducnguyenvan.mysuperchatapplication.R;

import java.util.ArrayList;

public class MessageRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Message> messages;
    Context context;

    public MessageRvAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.message_item_row, parent, false);
        return new MessageItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MessageItemViewHolder)holder).bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    private static class MessageItemViewHolder extends RecyclerView.ViewHolder {

        TextView msg;
        public MessageItemViewHolder(View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.message_content);
        }

        public void bind(Message message) {
            msg.setText(message.getName() + ": " + message.getMessage());
        }
    }
}
