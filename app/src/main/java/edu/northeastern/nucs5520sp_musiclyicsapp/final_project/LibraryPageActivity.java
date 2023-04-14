package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityLibraryPageBinding;

/*
Library page that include recycler view of song title, artist, and lyric editor. At the bottom is
nav bar to transit other page.
 */
public class LibraryPageActivity extends AppCompatActivity {

    RecyclerView library_recyclerView;

    ActivityLibraryPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLibraryPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        library_recyclerView = findViewById(R.id.library_recyclerView);

        /*
        set layout manager for the recycler view
         */
        library_recyclerView.setLayoutManager(new LinearLayoutManager(LibraryPageActivity.this));

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LibraryPageActivity.this, CreateEditPageActivity.class));
                finish();
            }
        });

    }
}