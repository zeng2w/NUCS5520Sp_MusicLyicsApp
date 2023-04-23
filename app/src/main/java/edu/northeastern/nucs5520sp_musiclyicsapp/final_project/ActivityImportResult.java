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

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.GeniusSong;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.adapters.ImportResultAdapter;

public class ActivityImportResult extends AppCompatActivity {

    private ArrayList<GeniusSong> outputList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_result);

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

    }

    // Credit: https://youtu.be/FbpD5RZtbCc
    public void stopService() {
        Intent serviceIntent = new Intent(this, ImportService.class);
        stopService(serviceIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ActivityImportResult.this, LibraryPageActivity.class));
        finish();
    }
}