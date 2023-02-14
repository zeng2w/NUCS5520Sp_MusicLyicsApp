package edu.northeastern.nucs5520sp_musiclyicsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.northeastern.nucs5520sp_musiclyicsapp.a6.AtYouService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void navigate(View view){
        int theId = view.getId();
        if(theId == R.id.hw6Button){
            startActivity(new Intent(MainActivity.this, AtYouService.class));
        }
    }
}