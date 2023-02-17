package edu.northeastern.nucs5520sp_musiclyicsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView teamName;
    Button buttonAtYourService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        teamName = findViewById(R.id.textView);
        buttonAtYourService = findViewById(R.id.buttonAtYourService);
        buttonAtYourService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, WebServiceActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(MainActivity.this, example.class);
                startActivity(intent);
            }
        });
    }

}