package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class ActivityStickItToEm extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

        editTextEmail = findViewById(R.id.editTextEmail);
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
        else if (viewId == R.id.buttonLogIn) {
            logIn();
        }
    }

    /**
     * Log the user in.
     * Credit to: https://www.youtube.com/watch?v=KB2BIm_m1Os
     */
    private void logIn() {
        String emailStr = editTextEmail.getText().toString().trim();
        String password = "password";

        // Detect if user didn't provide an email
        if (emailStr.isEmpty()) {
            editTextEmail.setError("Email is required for log in");
            editTextEmail.requestFocus();
            return;
        }

        // Detect if the email address is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Sign in
        mAuth.signInWithEmailAndPassword(emailStr, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {

            }
            else {
                Toast.makeText(ActivityStickItToEm.this,
                        "Failed. Please check your email address",
                        Toast.LENGTH_LONG).show();
            }
        });
    }


}