package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityStickItToEmBinding;

public class StickItToEm extends AppCompatActivity {
    ActivityStickItToEmBinding binding;

    private EditText usernameText;
    private EditText emailText;
    private Button loginButton;
    private Button signUpButton;

    String username, email, password;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStickItToEmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        usernameText = findViewById(R.id.usernameInput);
        emailText = findViewById(R.id.emailInput);
        loginButton = findViewById(R.id.login);
        signUpButton = findViewById(R.id.signUp);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //username = usernameText.getText().toString();
                email = binding.emailInput.getText().toString();
                password = "1234567";
                login();


                Toast.makeText(StickItToEm.this, "Login successful!", Toast.LENGTH_SHORT).show();

                //User user = new User(username);
            }

        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = binding.usernameInput.getText().toString();
                email = binding.emailInput.getText().toString();

                //email = emailText.getText().toString();
                password = "1234567111";
                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(email)){
                    Toast.makeText(StickItToEm.this, "Empty input", Toast.LENGTH_SHORT).show();
                } else{
                    signUp();
                }


            }
        });
    }

    private void signUp() {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password.trim())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        firebaseUser.updateProfile(userProfileChangeRequest);
                        User user =new User(FirebaseAuth.getInstance().getUid(), username, email, password);
                        databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(user);
                        Toast.makeText(StickItToEm.this, "SignUp successful!", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(StickItToEm.this, HomeActivity.class));
                        finish();
                    }
                }).addOnFailureListener(er -> {
                    Toast.makeText(StickItToEm.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();

                });
    }

    private void login() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                firebaseUser.updateProfile(userProfileChangeRequest);
                startActivity(new Intent(StickItToEm.this, HomeActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(StickItToEm.this, HomeActivity.class));
            finish();
        }
    }
}