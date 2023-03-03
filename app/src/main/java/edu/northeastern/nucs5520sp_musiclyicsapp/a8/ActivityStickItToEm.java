package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class ActivityStickItToEm extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.textViewSignUp) {
            Intent intentSignUp = new Intent(ActivityStickItToEm.this, ActivitySignUp.class);
            startActivity(intentSignUp);
        }
    }
}