package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

/*
Login page: for user to login
editTextUsername: for user to input email
editTextPassword: for user to input password
buttonSubmit: for user to click to verify their account( if user doesn't have an account yet, send a
warning that user need to sign up for account.
buttonSignup: for user to click to signup an account(if account already signup, send a warning that
user need to log in instead

 */
public class LogInActivity extends AppCompatActivity {

    Button buttonSubmit;
    Button buttonSignup;
    EditText emailAddress;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSignup = findViewById(R.id.buttonSignup);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);


    }
}