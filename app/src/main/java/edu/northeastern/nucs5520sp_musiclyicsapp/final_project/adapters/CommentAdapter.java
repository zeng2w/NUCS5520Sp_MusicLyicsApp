package edu.northeastern.nucs5520sp_musiclyicsapp.final_project.adapters;

import android.content.Context;
import android.util.Log;
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

public class CommentAdapter extends RecyclerView.Adapter<edu.northeastern.nucs5520sp_musiclyicsapp.final_project.adapters.CommentAdapter.MyViewHolder>{
    private Context context;
    private List<CommentModel> commentModelList;

    public CommentAdapter(Context context) {
        this.context = context;
        commentModelList = new ArrayList<>();
    }
    public void add(CommentModel comment) {
        commentModelList.add(comment);
        notifyDataSetChanged();
    }

    public void clear(){
        commentModelList.clear();
        notifyDataSetChanged();
    }

    public List<CommentModel> getCommentModelList(){
        return commentModelList;
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {
        CommentModel commet = commentModelList.get(position);
        holder.username.setText(commet.getUsername());
        holder.commentContext.setText(commet.getContext());
        holder.date.setText(commet.getCurrentDate());
        Log.d("--------num like", String.valueOf(commet.getNum_like()));
        holder.num_dislike.setText(String.valueOf(commet.getNum_dislike()));
        holder.num_like.setText(String.valueOf(commet.getNum_like()));
//        holder.num_like.setText(commet.getNum_like());
//        holder.num_dislike.setText(commet.getNum_dislike());
//        holder.thumbUpNum.setText(commet.getNum_thumb_up());
//        holder.thumbDownNum.setText(commet.getGetNum_thumb_down());

//        Intent intent = new Intent(context, CommentActivity.class);
//        intent.putExtra("username", commet.getUsername());
//        intent.putExtra("commentContext", commet.getContext());
//        intent.putExtra("date", commet.getCurrentDate());
//        intent.putExtra("thumbUpNum", commet.getNum_thumb_up());
//        intent.putExtra("thumbDownNum", commet.getGetNum_thumb_down());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                context.startActivities(new Intent[]{intent});
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return commentModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView commentContext;
        private TextView date;
        private TextView num_dislike;
        private TextView num_like;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.user_name_text_view);
            commentContext = itemView.findViewById(R.id.comment_text_view);
            date = itemView.findViewById(R.id.comment_date_text_view);
            num_dislike = itemView.findViewById(R.id.textView_num_dislike);
            num_like = itemView.findViewById(R.id.textView_num_like);
        }
    }
}
