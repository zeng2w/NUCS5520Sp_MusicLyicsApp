package edu.northeastern.nucs5520sp_musiclyicsapp.a8.ShowHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.utils.Data;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    public Context context;
    public ArrayList<Data> messageItemList;

    public MessageAdapter(Context context, ArrayList<Data> messageItemList) {
        this.context = context;
        this.messageItemList = messageItemList;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_message, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Data messageItem = messageItemList.get(position);
        holder.setHistorySticker(messageItem.getUri().toString());
        holder.setHistoryTime(messageItem.getPostTime());
        if (messageItem.isReceive()) {
            holder.setHistorySender(messageItem.getOtherUser());
            holder.setHistoryIsReceived(true);
        } else {
            holder.setHistoryRecipient(messageItem.getOtherUser());
            holder.setHistoryIsReceived(false);
        }
    }

    @Override
    public int getItemCount() {
        return messageItemList.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        public ImageView historySticker;
        public TextView historyTime, historyOtherUser, historyIsReceived;
        public View view;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public void setHistorySticker(String url) {
            historySticker = view.findViewById(R.id.historysticker);
            Glide.with(context).load(url).into(historySticker);
        }

        public void setHistoryTime(String time) {
            historyTime = view.findViewById(R.id.historytime);
            historyTime.setText("Time: " + time);
        }

        public void setHistorySender(String sender) {
            historyOtherUser = view.findViewById(R.id.historyotheruser);
            historyOtherUser.setText("Sender: " + sender);
        }

        public void setHistoryRecipient(String recipient) {
            historyOtherUser = view.findViewById(R.id.historyotheruser);
            historyOtherUser.setText("Recipient: " + recipient);
        }

        public void setHistoryIsReceived(boolean isReceived) {
            historyIsReceived = view.findViewById(R.id.historyisreceived);
            if (isReceived) {
                historyIsReceived.setText("Message received on:");
                return;
            }
            historyIsReceived.setText("Message sent on:");
        }
    }
}
