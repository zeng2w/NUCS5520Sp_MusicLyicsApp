package edu.northeastern.nucs5520sp_musiclyicsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.northeastern.nucs5520sp_musiclyicsapp.a6.AtYourService;

public class MainActivity extends AppCompatActivity {

    TextView teamName;
    Button buttonAtYourService;
    Button buttonStickItToEm;
    Button buttonAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.d("CREATION", "Hi");

        buttonAtYourService = findViewById(R.id.buttonAtYourService);
        buttonStickItToEm = findViewById(R.id.buttonStickItToEm);
        buttonAbout = findViewById(R.id.buttonAbout);

        buttonAtYourService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAtYourService = new Intent(MainActivity.this, AtYourService.class);
                startActivity(intentAtYourService);
            }
        });

        buttonStickItToEm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStickItToEm = new Intent(MainActivity.this, StickItToEm.class);
                startActivity(intentStickItToEm);
            }
        });

        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAbout = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intentAbout);
            }
        });
    }



}