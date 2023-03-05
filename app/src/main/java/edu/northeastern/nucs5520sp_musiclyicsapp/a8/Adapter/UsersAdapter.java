package edu.northeastern.nucs5520sp_musiclyicsapp.a8.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.ActivityMessage;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.User;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private final Context context;
    private final List<User> usersList;

    // Credit: https://www.youtube.com/watch?v=KB2BIm_m1Os
    public UsersAdapter(Context context, List<User> usersList) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_users_row, parent, false);
        return new UsersAdapter.UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.username.setText(user.getUsername());
        holder.email.setText(user.getEmail());

        // If a user is clicked, go to the message page with that user.
        // Credit: https://www.youtube.com/watch?v=KB2BIm_m1Os
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ActivityMessage.class);
            intent.putExtra("username", user.getUsername());
            intent.putExtra("email", user.getEmail());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    static class UsersViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView email;

        public UsersViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.textView_username_row);
            email = itemView.findViewById(R.id.textView_email_row);
        }


    }

}
