package edu.northeastern.nucs5520sp_musiclyicsapp.a8.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.Chat;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private final Context context;
    private final List<Chat> chatsList;

    FirebaseUser currentUser;

    // Credit: https://www.youtube.com/watch?v=KB2BIm_m1Os
    public MessageAdapter(Context context, List<Chat> usersList) {
        this.chatsList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.MessageViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.MessageViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        Chat chat = chatsList.get(position);

        holder.show_message.setImageResource(Integer.parseInt(chat.getSticker()));
    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        ImageView show_message;

        public MessageViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.imageView_show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatsList.get(position).getSenderUid().equals(currentUser.getUid())) {
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
