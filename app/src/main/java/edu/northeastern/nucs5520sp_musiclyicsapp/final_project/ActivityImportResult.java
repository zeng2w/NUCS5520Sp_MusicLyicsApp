package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

        // Obtain the outputList that contains GeniusSongs object
        outputList = getIntent().getParcelableArrayListExtra("outputList");

        Log.d("outputList second song lyrics received", outputList.get(1).getLyrics());
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




}