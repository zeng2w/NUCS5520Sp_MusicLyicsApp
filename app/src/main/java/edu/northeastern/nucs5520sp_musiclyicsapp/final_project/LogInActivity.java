package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.HomeActivity;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.StickItToEm;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.User;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityLogInBinding;

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

    ActivityLogInBinding binding;
    String username, emailString, passwordString;
    //password = "12345671111" by user1, user2,user3,user4,user6,user9
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSignup = findViewById(R.id.buttonSignup);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);

       binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               emailString = binding.emailAddress.getText().toString();
               if(TextUtils.isEmpty(emailString)){
                   Toast.makeText(LogInActivity.this, "Email is empty", Toast.LENGTH_SHORT).show();
               } else {
                   login();
               }
           }
       });

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
        Log.d("email", emailString);
        Log.d("password", passwordString);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailString.trim(), passwordString).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                firebaseUser.updateProfile(userProfileChangeRequest);
                Toast.makeText(LogInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LogInActivity.this, UserPageActivity.class));
                finish();
            }
        }).addOnFailureListener(er -> {
            Log.d("login error", er.getMessage());
            Toast.makeText(LogInActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(LogInActivity.this, UserPageActivity.class));
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
                        User user =new User(FirebaseAuth.getInstance().getUid(), username, emailString, passwordString);
                        databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(user);
                        Toast.makeText(LogInActivity.this, "SignUp successful!", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(LogInActivity.this, HomeActivity.class));
                        finish();
                    }
                }).addOnFailureListener(er -> {
                    Toast.makeText(LogInActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();

                });
    }
}