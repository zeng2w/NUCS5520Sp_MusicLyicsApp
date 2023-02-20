package edu.northeastern.nucs5520sp_musiclyicsapp.a6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class ActivityConversionHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion_history);

        // Retrieve the conversionList
        Intent intent = getIntent();
        ArrayList<Conversion> conversionList = intent.getParcelableArrayListExtra("conversion list");

        // Create the RecyclerView object and connect with the UI object in "Conversion
        //      History" Activity
        RecyclerView recyclerView = findViewById(R.id.recyclerView_conversion_history);

        // Set up adapter and layout manager for the recyclerView
        ConversionHistoryAdapter adapter = new ConversionHistoryAdapter(this, conversionList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}