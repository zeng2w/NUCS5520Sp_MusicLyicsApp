package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

/*
Library page that include recycler view of song title, artist, and lyric editor. At the bottom is
nav bar to transit other page.
 */
public class LibraryPageActivity extends AppCompatActivity {

    RecyclerView library_recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_page);

        library_recyclerView = findViewById(R.id.library_recyclerView);



        /*
        set layout manager for the recycler view
         */
        library_recyclerView.setLayoutManager(new LinearLayoutManager(LibraryPageActivity.this));

    }
}