package com.ducnguyenvan.mysuperchatapplication.History;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ducnguyenvan.mysuperchatapplication.Model.ConversationItem;
import com.ducnguyenvan.mysuperchatapplication.R;

import java.util.ArrayList;

public class HistoryRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ConversationItem> conversations;
    Context context;

    public HistoryRvAdapter(ArrayList<ConversationItem> conversations, Context context) {
        this.conversations = conversations;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.conversation_item_row, parent, false);
        return new HistoryRvAdapter.ConversationItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ConversationItemViewHolder)holder).bind(conversations.get(position));
    }

    @Override
    public int getItemCount() {
        return conversations != null ? conversations.size() : 0;
    }

    private static class ConversationItemViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;
        TextView lastMsg;
        TextView lastMsgTime;

        public ConversationItemViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.history_img);
            name = itemView.findViewById(R.id.history_name);
            lastMsg = itemView.findViewById(R.id.history_last_msg);
            lastMsgTime = itemView.findViewById(R.id.history_last_msg_time);
        }

        public void bind(ConversationItem item) {
            img.setImageResource(item.getImg());
            name.setText(item.getConversationName());
            lastMsg.setText(item.getLastMsg());
            lastMsgTime.setText(item.getLastMsgTime());
        }
    }
}
