package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityCurrentSongPageBinding;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SongModel;

/*
Current Song Page: display the current song page which include album image, song title, artist,
lyric editor, text area where contains lyric of the song, button to like, button to comment, button
to report, button to translate, button to edit the lyric, button to add to user database, and lastly
the nav bar to transit to other page
 */

public class CurrentSongPageActivity extends AppCompatActivity {

    ActivityCurrentSongPageBinding binding;
    String songName, songArtist, lyricCreator;
    DatabaseReference databaseReferenceUsersLyricsLibrary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCurrentSongPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        songName = intent.getStringExtra("song_name");
        songArtist= intent.getStringExtra("song_artist");
        lyricCreator = intent.getStringExtra("lyric_creator");

        Log.d("------song name: ", songName);
        binding.currentSongTitle.setText(songName);
        binding.currentSongTitle.setSelected(true);
        binding.currentSongArtist.setText("Artist: " + songArtist);
        binding.currentSongArtist.setSelected(true);
        binding.currentSongLyricEditor.setText("Lyric Creator" + lyricCreator);
        binding.currentSongLyricEditor.setSelected(true);
        binding.currentSongAlbumImage.setImageResource(R.drawable.sticker6);

        // get song lyrics
        databaseReferenceUsersLyricsLibrary = FirebaseDatabase.getInstance().getReference("users_Lyrics_Library").child(FirebaseAuth.getInstance().getUid()).child(songName.replaceAll("[^a-zA-Z0-9]", "")+songArtist.replaceAll("[^a-zA-Z0-9]", ""));;
        //databaseReferenceUsersLyricsLibrary.child(songName+songArtist);
        Log.d("----library reference", databaseReferenceUsersLyricsLibrary.toString());
        databaseReferenceUsersLyricsLibrary.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get lyric of this song and update UI
                String lyric = snapshot.child("song_lyric").getValue().toString();
                Log.d("-----------lyric:", lyric);
                binding.currentSongTextLyric.setText(lyric);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }
}