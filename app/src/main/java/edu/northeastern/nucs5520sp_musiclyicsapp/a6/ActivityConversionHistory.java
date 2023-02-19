package edu.northeastern.nucs5520sp_musiclyicsapp.a6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class ActivityConversionHistory extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Conversion> conversionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion_history);

        // Retrieve the conversionList
        Intent intent = getIntent();
        conversionList = intent.getParcelableArrayListExtra("conversion list");
    }

    @Override
    public void onClick(View view) {

    }
}