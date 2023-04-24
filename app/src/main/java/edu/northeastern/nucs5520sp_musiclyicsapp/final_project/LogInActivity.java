package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.nucs5520sp_musiclyicsapp.MainActivity;
import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityLogInBinding;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.UserModel;

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
    ActivityLogInBinding binding;
    String username, emailString, passwordString;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("Final_Project_Users");

        // log in
       binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               SharedPreferences sharedPreferencesCurrentSong = LogInActivity.this.getSharedPreferences("CURRENT_SONG",0);
               sharedPreferencesCurrentSong.edit().clear().commit();
               emailString = binding.emailAddress.getText().toString();
               passwordString = binding.password.getText().toString();
               if(TextUtils.isEmpty(emailString) || TextUtils.isEmpty(passwordString)){
                   Toast.makeText(LogInActivity.this, "Email is empty", Toast.LENGTH_SHORT).show();
               } else {
                   login();
               }
           }
       });

       // signUp
       binding.buttonSignup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               emailString = binding.emailAddress.getText().toString();
               username = emailString;
               passwordString = binding.password.getText().toString();
               if(TextUtils.isEmpty(emailString) || TextUtils.isEmpty(passwordString)){
                   Toast.makeText(LogInActivity.this, "Empty input", Toast.LENGTH_SHORT).show();
               } else {
                   signUp();
               }
           }
       });

    }

    private void login() {
        Log.d("------email: ", emailString);
        Log.d("------password: ", passwordString);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailString.trim(), passwordString).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                System.out.println("we are in login()'s onSuccess()");
                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                firebaseUser.updateProfile(userProfileChangeRequest);
                Toast.makeText(LogInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LogInActivity.this, LibraryPageActivity.class));
                finish();
            }
        }).addOnFailureListener(er -> {
            Toast.makeText(LogInActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("login error", er.getMessage());
        });
    }

    // Check if there is a user who already log in when project start
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(LogInActivity.this, LibraryPageActivity.class));
            finish();
        }
    }

    private void signUp() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailString.trim(), passwordString)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        firebaseUser.updateProfile(userProfileChangeRequest);
                        UserModel user = new UserModel(FirebaseAuth.getInstance().getUid(), username, emailString, passwordString);
                        databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(user);
                        Toast.makeText(LogInActivity.this, "SignUp successful!", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(LogInActivity.this, LibraryPageActivity.class));
                        finish();
                    }
                }).addOnFailureListener(er -> {
                    Toast.makeText(LogInActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();

                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}