package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    DatabaseReference databaseReferenceSongComment;
    String commentId, username, userId, context;

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
        lyricCreatorId = intent.getStringExtra("lyricCreatorId");

        databaseReferenceSongComment = FirebaseDatabase.getInstance().getReference("comments");

        showSongComments(lyricCreatorId);
        setLyricsLikeNumIntoUI(lyricCreatorId);

        // set Comments recycler view
        commentAdapter = new CommentAdapter(this);
        binding.commentList.setAdapter(commentAdapter);
        binding.commentList.setLayoutManager(new LinearLayoutManager(CommentActivity.this));

        binding.buttonPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentId = UUID.randomUUID().toString();
                username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                context = binding.commentContext.getText().toString();
                // if user comment a lyric which created by genius
                String s;
                if(lyricCreator.toLowerCase().equals("Genius")){
                    s = songName.replaceAll("[^a-zA-Z0-9]", "") + songArtist.replaceAll("[^a-zA-Z0-9]", "")+ "genius";
                } else {
                    s = songName.replaceAll("[^a-zA-Z0-9]", "") + songArtist.replaceAll("[^a-zA-Z0-9]", "")+ lyricCreatorId;
                }
                CommentModel new_comment = new CommentModel(s, commentId, username, userId,context);
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
                backCurrentSongIntent.putExtra("lyric", lyric);
                backCurrentSongIntent.putExtra("song_translation",translation);

                startActivity(backCurrentSongIntent);
            }
        });

        // sort spinner select action
        binding.sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item from the Spinner
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Sort by Newest")){
                    Log.d("-------nothing selected", "Newest");
                    commentAdapter.sortItemsByNewest();
                } else if(selectedItem.equals("Sort By Earliest")){
                    commentAdapter.sortItemsByOldest();
                }
//                else if(selectedItem.equals("Sort by Popularity")){
//                    Log.d("-------selected", "Popularity");
//                    commentAdapter.sortItemsByLikes();
//                }
//                else if(selectedItem.equals("Sort By Dislike")){
//                    commentAdapter.sortItemsByDislikes();
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("-------nothing selected", "nothing selected of spinner");

            }
        });

    }

    private void setLyricsLikeNumIntoUI(String creatorId) {
        if(creatorId == null){
            creatorId = "genius";
        }
        String path = songName.replaceAll("[^a-zA-Z0-9]", "") + songArtist.replaceAll("[^a-zA-Z0-9]", "")+ creatorId;
        DatabaseReference databaseReferenceSongLike = FirebaseDatabase.getInstance().getReference("likes").child(path);
        databaseReferenceSongLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("-----------children count", String.valueOf(snapshot.getChildrenCount()));
                binding.likesCount.setText("( " + snapshot.getChildrenCount() + " )" );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showSongComments(String creatorId) {
        String s;
        if(lyricCreator.toLowerCase().equals("Genius")){
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
                    String commentId = dataSnapshot.child("commentId").getValue().toString();
                    String username = dataSnapshot.child("username").getValue().toString();
                    String commentUserId = dataSnapshot.child("userId").getValue().toString();
                    String commentContext = dataSnapshot.child("context").getValue().toString();
                    String currentDateTime = dataSnapshot.child("currentDate").getValue().toString();
                    String numLike = dataSnapshot.child("num_like").getValue().toString();
                    String numDislike = dataSnapshot.child("num_dislike").getValue().toString();
                    CommentModel comment = new CommentModel(s, commentId,username, commentUserId,commentContext, Integer.parseInt(numDislike),Integer.parseInt(numLike),currentDateTime);
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