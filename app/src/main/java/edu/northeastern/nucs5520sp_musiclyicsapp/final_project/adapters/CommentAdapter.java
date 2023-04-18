package edu.northeastern.nucs5520sp_musiclyicsapp.final_project.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    // Sorting method
    public void sortItemsByLikes() {
        // Sort the itemList based on your desired criteria
        Collections.sort(commentModelList, new Comparator<CommentModel>() {
            @Override
            public int compare(CommentModel comment1, CommentModel comment2) {
                // sorting by descending of likes number
                return comment2.getNum_like()-comment1.getNum_like();
            }
        });
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }

    public void sortItemsByNewest() {
        // Sort the itemList based on your desired criteria
        Collections.sort(commentModelList, new Comparator<CommentModel>() {
            @Override
            public int compare(CommentModel comment1, CommentModel comment2) {
                // sorting by now -> before
                return comment2.getCurrentDate().compareToIgnoreCase(comment1.getCurrentDate());
            }
        });
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }

    public void sortItemsByOldest() {
        // Sort the itemList based on your desired criteria
        Collections.sort(commentModelList, new Comparator<CommentModel>() {
            @Override
            public int compare(CommentModel comment1, CommentModel comment2) {
                // sorting before -> now
                return comment1.getCurrentDate().compareToIgnoreCase(comment2.getCurrentDate());
            }
        });
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }

    public void sortItemsByDislikes() {
        // Sort the itemList based on your desired criteria
        Collections.sort(commentModelList, new Comparator<CommentModel>() {
            @Override
            public int compare(CommentModel comment1, CommentModel comment2) {
                // sorting by descending of dislikes number
                return comment2.getNum_dislike()-comment1.getNum_dislike();
            }
        });
        notifyDataSetChanged(); // Notify the adapter that data has changed
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
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.likeButton.setImageResource(R.drawable.baseline_thumb_up_24_red);
                int likes = commet.getNum_like()+1;
                commet.setNum_like(likes);
                String songId = commet.getSongId();
                String commentId = commet.getCommentId();
                Log.d("-----comment id", commentId);
                //CommentModel(String songId, String username, String context, int num_dislike, int num_like, String currentDate)
                CommentModel updateComment = new CommentModel(songId, commentId, commet.getUsername(),commet.getUserId(), commet.getContext(),commet.getNum_dislike(),commet.getNum_like(),commet.getCurrentDate());
                DatabaseReference databaseReferenceComment = FirebaseDatabase.getInstance().getReference("comments");
                databaseReferenceComment.child(songId).child(commentId).setValue(updateComment);
                notifyDataSetChanged();
            }
        });

        holder.dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.dislikeButton.setImageResource(R.drawable.baseline_thumb_down_24_black);
                int dislike = commet.getNum_dislike()+1;
                commet.setNum_dislike(dislike);
                String songId = commet.getSongId();
                String commentId = commet.getCommentId();
                Log.d("-----comment id", commentId);
                //CommentModel(String songId, String username, String context, int num_dislike, int num_like, String currentDate)
                CommentModel updateComment = new CommentModel(songId, commentId, commet.getUsername(), commet.getUserId(), commet.getContext(),commet.getNum_dislike(),commet.getNum_like(),commet.getCurrentDate());
                DatabaseReference databaseReferenceComment = FirebaseDatabase.getInstance().getReference("comments");
                databaseReferenceComment.child(songId).child(commentId).setValue(updateComment);
                notifyDataSetChanged();
            }
        });

        // load avatar for each user of comments
        StorageReference storageReferenceAvatar = FirebaseStorage.getInstance().getReference("avatar/" + commet.getUserId()).child(commet.getUserId());
        storageReferenceAvatar.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String image = uri.toString();
                Picasso.get().load(image).into(holder.avatar_image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.avatar_image.setImageResource(R.drawable.person_image);
            }
        });


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
        private ImageButton likeButton;
        private ImageButton dislikeButton;
        private ImageView avatar_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.user_name_text_view);
            commentContext = itemView.findViewById(R.id.comment_text_view);
            date = itemView.findViewById(R.id.comment_date_text_view);
            num_dislike = itemView.findViewById(R.id.textView_num_dislike);
            num_like = itemView.findViewById(R.id.textView_num_like);
            likeButton = itemView.findViewById(R.id.thumb_up_button);
            dislikeButton = itemView.findViewById(R.id.thumb_down_button);
            avatar_image = itemView.findViewById(R.id.profile_image_view);
        }
    }
}
