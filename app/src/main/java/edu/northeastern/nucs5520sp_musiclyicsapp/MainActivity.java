package edu.northeastern.nucs5520sp_musiclyicsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.northeastern.nucs5520sp_musiclyicsapp.a6.AtYouService;

public class MainActivity extends AppCompatActivity {

    TextView teamName;
    Button hw6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teamName = findViewById(R.id.textView);
        hw6 = findViewById(R.id.hw6Button);

        hw6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AtYouService.class);
                startActivity(intent);
            }
        });
    }

}