package edu.northeastern.nucs5520sp_musiclyicsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.northeastern.nucs5520sp_musiclyicsapp.a6.ActivityAtYourService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView teamName;
    Button buttonAtYourService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.buttonAtYourService) {
            Intent intentAtYourService = new Intent(MainActivity.this, ActivityAtYourService.class);
            startActivity(intentAtYourService);
        }
    }
}