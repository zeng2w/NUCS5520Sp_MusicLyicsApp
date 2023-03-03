package edu.northeastern.nucs5520sp_musiclyicsapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import edu.northeastern.nucs5520sp_musiclyicsapp.a6.AtYourService;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.StickItToEm;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("CREATION", "Hi");
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.buttonAtYourService) {
            Intent intentAtYourService = new Intent(MainActivity.this, AtYourService.class);
            startActivity(intentAtYourService);
        } else if (viewId == R.id.buttonAbout){
            Intent intentAbout = new Intent(MainActivity.this, GroupInformationActivity.class);
            startActivity(intentAbout);
        } else if (viewId == R.id.buttonStickIt){
            Intent intentStickIt = new Intent(MainActivity.this, StickItToEm.class);
            startActivity(intentStickIt);
        }
    }
}