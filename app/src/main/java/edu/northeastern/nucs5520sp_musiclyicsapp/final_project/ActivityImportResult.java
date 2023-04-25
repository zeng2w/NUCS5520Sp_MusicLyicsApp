package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.GeniusSong;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.adapters.ImportResultAdapter;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SongModel;

public class ActivityImportResult extends AppCompatActivity {

    private ArrayList<GeniusSong> outputList;

    private Button importToLibraryButton;
    private Button importCancelButton;
    DatabaseReference databaseReferenceUserLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_result);

        importToLibraryButton = findViewById(R.id.buttonImportResultImport);
        importCancelButton = findViewById(R.id.buttonImportResultCancel);
        databaseReferenceUserLibrary = FirebaseDatabase.getInstance().getReference("users_Lyrics_Library");

        outputList = new ArrayList<>();

        // Read GeniusSong objects from SQLite database and add to outputList.
        ImportSongDatabaseHelper dbHelper = new ImportSongDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("genius_songs", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String songName = cursor.getString(cursor.getColumnIndexOrThrow("song_name"));
            String artistsString = cursor.getString(cursor.getColumnIndexOrThrow("artists_string"));
            String songLyrics = cursor.getString(cursor.getColumnIndexOrThrow("song_lyrics"));
            GeniusSong song = new GeniusSong(songName, artistsString, songLyrics);
            outputList.add(song);
        }

        Log.d("outputList second song name received", outputList.get(1).getSongName());
        Log.d("outputList size received", String.valueOf(outputList.size()));
        stopService();

        // Create the RecyclerView object and connect with the UI object.
        RecyclerView recyclerView = findViewById(R.id.libraryRecyclerView);

        ImportResultAdapter adapter = new ImportResultAdapter(this, outputList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // import the result to library
        importToLibraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < outputList.size(); i ++){
                    GeniusSong gSong = outputList.get(i);
                    String songName = gSong.getSongName();
                    String songArtist = gSong.getArtistsString();
                    String songLyric = gSong.getLyrics();
                    String songCreator = "Genius";
                    String songTranslation = "";
                    SongModel song = new SongModel(songName, songArtist,songLyric,songTranslation,songCreator);
                    String path = songName.replaceAll("[^a-zA-Z0-9]", "") + songArtist.replaceAll("[^a-zA-Z0-9]", "");
                    databaseReferenceUserLibrary.child(FirebaseAuth.getInstance().getUid()).child(path).setValue(song);
                }
                Toast.makeText(ActivityImportResult.this, "Import to Library Successful", Toast.LENGTH_SHORT);
                startActivity(new Intent(ActivityImportResult.this, LibraryPageActivity.class));
            }
        });

        importCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityImportResult.this, LibraryPageActivity.class));
            }
        });

    }

    // Credit: https://youtu.be/FbpD5RZtbCc
    public void stopService() {
        Intent serviceIntent = new Intent(this, ImportService.class);
        stopService(serviceIntent);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityImportResult.this, LibraryPageActivity.class));
    }
}