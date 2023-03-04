package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class ActivityLogIn extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private ProgressBar progressBar;
    private FirebaseUser firebaseUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        editTextEmail = findViewById(R.id.editTextEmail);
        progressBar = findViewById(R.id.progressBarLogIn);

        // credit to: Firebase Assistant in Android Studio
        mAuth = FirebaseAuth.getInstance();

        // If there is a current user, then automatically open the main chat ActivityChatMain.
        firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(ActivityLogIn.this, ActivityChatMain.class));
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.textViewSignUp) {
            Intent intentSignUp = new Intent(ActivityLogIn.this, ActivitySignUp.class);
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
                startActivity(new Intent(ActivityLogIn.this, ActivityChatMain.class));
            }
            else {
                Toast.makeText(ActivityLogIn.this,
                        "Failed. Please check your email address",
                        Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }


}