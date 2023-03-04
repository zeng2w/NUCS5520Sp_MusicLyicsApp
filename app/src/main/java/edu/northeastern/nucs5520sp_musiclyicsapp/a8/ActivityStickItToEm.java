package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class ActivityStickItToEm extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText username;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

        username = findViewById(R.id.editTextUsername);
        progressBar = findViewById(R.id.progressBarLogIn);

        // credit to: Firebase Assistant in Android Studio
        mAuth = FirebaseAuth.getInstance();
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