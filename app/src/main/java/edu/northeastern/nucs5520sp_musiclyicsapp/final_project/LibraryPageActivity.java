package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityLibraryPageBinding;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.adapters.LibraryAdapter;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SongModel;

/*
Library page that include recycler view of song title, artist, and lyric editor. At the bottom is
nav bar to transit other page.
 */
public class LibraryPageActivity extends AppCompatActivity {

    RecyclerView library_recyclerView;

    ActivityLibraryPageBinding binding;
    DatabaseReference databaseReferenceUsersLyricsLibrary;
    LibraryAdapter libraryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLibraryPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set adapter to recycler view
        libraryAdapter = new LibraryAdapter(this);
        binding.libraryRecyclerView.setAdapter(libraryAdapter);
        /*
        set layout manager for the recycler view
         */
        binding.libraryRecyclerView.setLayoutManager(new LinearLayoutManager(LibraryPageActivity.this));

        databaseReferenceUsersLyricsLibrary = FirebaseDatabase.getInstance().getReference("users_Lyrics_Library").child(FirebaseAuth.getInstance().getUid());

        databaseReferenceUsersLyricsLibrary.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                libraryAdapter.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String song_name = dataSnapshot.child("song_name").getValue().toString();
                    String song_artist = dataSnapshot.child("song_artist").getValue().toString();
                    String lyric_creator = dataSnapshot.child("lyric_creator").getValue().toString();
                    String song_lyric = dataSnapshot.child("song_lyric").getValue().toString();
                    String song_translation = dataSnapshot.child("song_translation").getValue().toString();
                    SongModel song = new SongModel(song_name,song_artist,song_lyric,song_translation,lyric_creator);
                    libraryAdapter.add(song);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LibraryPageActivity.this, CreateEditPageActivity.class));
                finish();
            }
        });

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to search page
            }
        });

    }
}