package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.UUID;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityCommentBinding;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityLibraryPageBinding;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.adapters.CommentAdapter;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.CommentModel;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding binding;
    CommentAdapter commentAdapter;

    String songName;
    String songArtist;
    String lyricCreator;
    String lyricCreatorId;
    String lyric, translation,imageUrl;

    DatabaseReference databaseReferenceUser;
    DatabaseReference databaseReferenceSongComment;
    String commentId, username, context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // this intent for receive the edit action from the lyric detail page
        Intent intent = getIntent();
        songName = intent.getStringExtra("song_name");
        songArtist= intent.getStringExtra("song_artist");
        lyricCreator = intent.getStringExtra("lyricCreator");
        lyric = intent.getStringExtra("song_lyric");
        translation = intent.getStringExtra("song_translation");
        imageUrl = intent.getStringExtra("image_url");

        databaseReferenceSongComment = FirebaseDatabase.getInstance().getReference("comments");
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("Final_Project_Users");

        // find lyric Creator id
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (dataSnapshot.child("username").getValue().toString().equals(lyricCreator)){
                        //lyricCreatorId = dataSnapshot.getKey().toString();

                        binding.textviewTrashTemp.setText(dataSnapshot.getKey().toString());
                        showSongComments(dataSnapshot.getKey().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // set Comments recycler view
        commentAdapter = new CommentAdapter(this);
        binding.commentList.setAdapter(commentAdapter);
        binding.commentList.setLayoutManager(new LinearLayoutManager(CommentActivity.this));


        binding.buttonPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentId = UUID.randomUUID().toString();
                username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                context = binding.commentContext.getText().toString();
                CommentModel new_comment = new CommentModel(commentId, username,context);
                // if user comment a lyric which created by genius
                lyricCreatorId = binding.textviewTrashTemp.getText().toString();
                String s;
                if(lyricCreator.toLowerCase().equals("genius")){
                    s = songName.replaceAll("[^a-zA-Z0-9]", "") + songArtist.replaceAll("[^a-zA-Z0-9]", "")+ "genius";
                } else {
                    s = songName.replaceAll("[^a-zA-Z0-9]", "") + songArtist.replaceAll("[^a-zA-Z0-9]", "")+ lyricCreatorId;
                }
                if(!Objects.equals(context, "")){
                    databaseReferenceSongComment.child(s).child(commentId).setValue(new_comment);
                    Toast.makeText(CommentActivity.this, "Post Comment Successful", Toast.LENGTH_SHORT).show();
                    binding.commentContext.setText("");
                } else {
                    Toast.makeText(CommentActivity.this, "Empty comment", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.buttonCommentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backCurrentSongIntent = new Intent(CommentActivity.this, CurrentSongPageActivity.class);
                backCurrentSongIntent.putExtra("song_name",songName);
                backCurrentSongIntent.putExtra("song_artist", songArtist);
                backCurrentSongIntent.putExtra("lyric_creator", lyricCreator);

                startActivity(backCurrentSongIntent);
            }
        });


    }

    private void showSongComments(String creatorId) {
        String s;
        if(lyricCreator.toLowerCase().equals("genius")){
            s = songName.replaceAll("[^a-zA-Z0-9]", "") + songArtist.replaceAll("[^a-zA-Z0-9]", "")+ "genius";
        } else {
            s = songName.replaceAll("[^a-zA-Z0-9]", "") + songArtist.replaceAll("[^a-zA-Z0-9]", "")+ creatorId;
        }
        databaseReferenceSongComment.child(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("---------show", s);
                commentAdapter.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String username = dataSnapshot.child("username").getValue().toString();
                    String commentContext = dataSnapshot.child("context").getValue().toString();
                    String currentDateTime = dataSnapshot.child("currentDate").getValue().toString();
                    String numLike = dataSnapshot.child("num_like").getValue().toString();
                    String numDislike = dataSnapshot.child("num_dislike").getValue().toString();
                    CommentModel comment = new CommentModel(username,commentContext, Integer.parseInt(numDislike),Integer.parseInt(numLike),currentDateTime);
                    Log.d("-----in show adapter", username);
                    commentAdapter.add(comment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}