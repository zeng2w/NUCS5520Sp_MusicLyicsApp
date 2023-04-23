package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import static edu.northeastern.nucs5520sp_musiclyicsapp.R.id.local_search_toggle;
import static edu.northeastern.nucs5520sp_musiclyicsapp.R.id.online_search_toggle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivitySearchBinding;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.adapters.LibraryAdapter;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.GeniusSong;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SongModel;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    LibraryAdapter libraryAdapter;
    DatabaseReference databaseReferenceUserLibrary;
    DatabaseReference databaseReferenceSharedLibrary;
    LyricsService lyricsService;



    // set the local and online togglebutton
    private ToggleButton localSearchToggle;
    private ToggleButton onlineSearchToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lyricsService = new LyricsService(getApplicationContext());

        // set adapter for showing the search result in recycler view
        libraryAdapter = new LibraryAdapter(this);
        binding.searchHistoryRecyclerview.setAdapter(libraryAdapter);
        binding.searchHistoryRecyclerview.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        //database reference
        databaseReferenceUserLibrary = FirebaseDatabase.getInstance().getReference("users_Lyrics_Library").child(FirebaseAuth.getInstance().getUid());
        databaseReferenceSharedLibrary = FirebaseDatabase.getInstance().getReference("shared_Lyrics");

        // change the name of myToggleButton into localSearchToggle
//        ToggleButton myToggleButton = findViewById(local_search_toggle);

//        Log.d("-----if local button checked", String.valueOf(binding.localSearchToggle.isChecked()));
        localSearchToggle = findViewById(local_search_toggle);
        onlineSearchToggle = findViewById(online_search_toggle);

        localSearchToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the toggle state change here
                if (isChecked) {
                    // The toggle button is on
                    onlineSearchToggle.setChecked(false);
                } else {
                    // The toggle button is off
                    localSearchToggle.setChecked(false);
                    onlineSearchToggle.setChecked(true);
                }
            }
        });

        onlineSearchToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    localSearchToggle.setChecked(false);
                } else {
                    localSearchToggle.setChecked(true);
                    onlineSearchToggle.setChecked(false);
                }
            }
        });

        // when local search button clicked, then click Search button, it will search users' library song
        // else, when online search button clicked, then click Search button, it will search
        // users' shared song in firebase realtime db and
        // lyrics from API
        binding.floatingActionButtonSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_inputText = binding.searchEdittext.getText().toString().trim();
                if(search_inputText.isEmpty()){
                    Toast.makeText(SearchActivity.this, "Search Empty", Toast.LENGTH_SHORT).show();
                } else{
                    if(binding.localSearchToggle.isChecked()) {
                        Log.d("-------local search", String.valueOf(binding.localSearchToggle.isChecked()));
                        Log.d("-----input search", search_inputText);
                        databaseReferenceUserLibrary.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                libraryAdapter.clear();
                                Log.d("------snapshot.getChildren", String.valueOf(snapshot.getChildrenCount()));
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot.child("song_name").getValue().toString().toLowerCase().contains(search_inputText.toLowerCase())) {
                                        String song_name = dataSnapshot.child("song_name").getValue().toString();
                                        String song_artist = dataSnapshot.child("song_artist").getValue().toString();
                                        String lyric_creator = dataSnapshot.child("lyric_creator").getValue().toString();
                                        String song_lyric = dataSnapshot.child("song_lyric").getValue().toString();
                                        String song_translation = dataSnapshot.child("song_translation").getValue().toString();
                                        SongModel song = new SongModel(song_name, song_artist, song_lyric, song_translation, lyric_creator);
                                        libraryAdapter.add(song);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        if (libraryAdapter.getSize() == 0){
                            Toast.makeText(SearchActivity.this, "No result found in your library", Toast.LENGTH_LONG).show();
                        }
                    } else if(binding.onlineSearchToggle.isChecked()){
                        Log.d("--------online search", String.valueOf(binding.onlineSearchToggle.isChecked()));
                        // search from shared lyric in firebase realtime db
                        databaseReferenceSharedLibrary.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                libraryAdapter.clear();
                                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                    if(dataSnapshot.child("song_name").getValue().toString().toLowerCase().contains(search_inputText.toLowerCase())){
                                        String song_name = dataSnapshot.child("song_name").getValue().toString();
                                        String song_artist = dataSnapshot.child("song_artist").getValue().toString();
                                        String lyric_creator = dataSnapshot.child("lyric_creator").getValue().toString();
                                        String song_lyric = dataSnapshot.child("song_lyric").getValue().toString();
                                        String song_translation = dataSnapshot.child("song_translation").getValue().toString();
                                        SongModel song = new SongModel(song_name,song_artist,song_lyric,song_translation,lyric_creator);
                                        libraryAdapter.add(song);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        // search from API
                        lyricsService.getLyricsForSearch(search_inputText, new VolleyCallback() {
                            @Override
                            public void onSuccess(boolean finished) {

                            }

                            @Override
                            public void onLyricsSuccess(GeniusSong outputGeniusSong) {
                                    SongModel song = new SongModel(outputGeniusSong.getSongName(), outputGeniusSong.getArtistsString(), outputGeniusSong.getLyrics(),"", "Genius");
                                    if (!song.getSong_lyric().equals("")) {
                                        libraryAdapter.add(song);
                                    }


                            }
                        });

                        if(libraryAdapter.getSize() == 0){
                            Toast.makeText(SearchActivity.this, "No result found in shared lyrics or outside resource", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }
        });


        // navBar click action
        binding.navBarView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navBar_library:
                    startActivity(new Intent(SearchActivity.this, LibraryPageActivity.class));
                    break;
                case R.id.navBar_user:
                    startActivity(new Intent(SearchActivity.this, UserPageActivity.class));
                    break;
            }
            return true;
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SearchActivity.this,LibraryPageActivity.class));
        finish();
    }
}