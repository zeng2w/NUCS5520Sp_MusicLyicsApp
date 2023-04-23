package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import edu.northeastern.nucs5520sp_musiclyicsapp.MainActivity;
import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityLibraryPageBinding;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.adapters.LibraryAdapter;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SongModel;

/*
Library page that include recycler view of song title, artist, and lyric editor. At the bottom is
nav bar to transit other page.
 */
public class LibraryPageActivity extends AppCompatActivity {
    ActivityLibraryPageBinding binding;
    DatabaseReference databaseReferenceUsersLyricsLibrary;
    LibraryAdapter libraryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLibraryPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.navBarView.setSelectedItemId(R.id.navBar_library);
        // set adapter to recycler view
        libraryAdapter = new LibraryAdapter(this);
        binding.libraryRecyclerView.setAdapter(libraryAdapter);

        /*
        set layout manager for the recycler view
         */
        binding.libraryRecyclerView.setLayoutManager(new LinearLayoutManager(LibraryPageActivity.this));

        databaseReferenceUsersLyricsLibrary = FirebaseDatabase.getInstance().getReference("users_Lyrics_Library").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        databaseReferenceUsersLyricsLibrary.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("-----num of song in library", String.valueOf(snapshot.getChildrenCount()));
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
            }
        });

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to search page
                Context context = LibraryPageActivity.this;
                Intent intent = new Intent(context, SearchActivity.class);
                startActivity(intent);
            }
        });

        binding.importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LibraryPageActivity.this, ImportActivity.class));
            }
        });

        // navigation bar on click action
        binding.navBarView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
//                case R.id.navBar_currentSong:
//                    startActivity(new Intent(LibraryPageActivity.this, CurrentSongPageActivity.class));
//                    break;
                case R.id.navBar_user:
                    startActivity(new Intent(LibraryPageActivity.this, UserPageActivity.class));
                    break;
                case R.id.navBar_currentSong:
                    startActivity(new Intent(LibraryPageActivity.this, CurrentSongPageActivity.class));
                    break;
            }
            return true;
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        Toast.makeText(LibraryPageActivity.this, "Remove a Lyric from Library", Toast.LENGTH_SHORT).show();
                        int position = viewHolder.getLayoutPosition();
                        //itemList.remove(position);
                        //delete item in Db
                        String deletedSong_name = libraryAdapter.getSongList().get(position).getSong_name();
                        String deletedSong_artist = libraryAdapter.getSongList().get(position).getSong_artist();
                        String s = deletedSong_name.replaceAll("[^a-zA-Z0-9]", "") + deletedSong_artist.replaceAll("[^a-zA-Z0-9]", "");
                        Log.d("----Deleted Song name", libraryAdapter.getSongList().get(position).getSong_name());
                        databaseReferenceUsersLyricsLibrary.child(s).removeValue();
                        libraryAdapter.notifyItemRemoved(position);

                    }
                });
        itemTouchHelper.attachToRecyclerView(binding.libraryRecyclerView);


    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.navBarView.setSelectedItemId(R.id.navBar_library);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}