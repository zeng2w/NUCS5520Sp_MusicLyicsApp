package edu.northeastern.nucs5520sp_musiclyicsapp.final_project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.CommentModel;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.MyViewHolder>{
    private Context context;
    private List<CommentModel> replyList;

    public ReplyAdapter(Context context){
        this.context = context;
        replyList = new ArrayList<>();
    }

    public void add (CommentModel reply){
        replyList.add(reply);
        notifyDataSetChanged();
    }

    public void clear(){
        replyList.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_card, parent, false);
        return new ReplyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CommentModel reply = replyList.get(position);
        if(reply.getUsername() != null) {
            holder.username.setText(reply.getUsername());
            holder.context.setText(reply.getContext());
            holder.currentDate.setText(reply.getCurrentDate());
        }

    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView username;
        private TextView context;
        private TextView currentDate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.reply_username);
            context = itemView.findViewById(R.id.reply_context);
            currentDate = itemView.findViewById(R.id.reply_currentDateTime);

        }

    }
}
