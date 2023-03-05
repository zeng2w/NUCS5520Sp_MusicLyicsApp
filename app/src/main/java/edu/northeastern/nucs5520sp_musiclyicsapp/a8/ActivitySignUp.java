package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class ActivitySignUp extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText editTextUsername, editTextEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        editTextUsername = findViewById(R.id.editTextTextUsernameSignUp);
        editTextUsername.setFilters(new InputFilter[] {usernameFilter});
        editTextEmail = findViewById(R.id.editTextEmailSignUp);
        progressBar = findViewById(R.id.progressBarSignUp);

        editTextEmail.requestFocus();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.textViewLogIn) {
            // Same as pressing the back button; go back to log in page, but won't automatically
            // log in if there's a current user logged in.
            finish();
        }
        else if (viewId == R.id.buttonSignUp) {
            signUp();
        }
    }

    /**
     * Method to sign the user up with given username and optionally a recovery email.
     * Credit to: https://www.youtube.com/watch?v=Z-RE1QuUWPg
     */
    private void signUp() {
        String usernameStr = editTextUsername.getText().toString().trim();
        String emailStr = editTextEmail.getText().toString().trim();

        // Email cannot be empty.
        if (emailStr.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        // Check for a valid email address
        if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            editTextEmail.setError("Please provide a valid recovery email");
            editTextEmail.requestFocus();
            return;
        }

        // If username is empty, then default to email
        if (usernameStr.isEmpty()) {
            usernameStr = emailStr;
        }

        // set Progress Bar to visible
        progressBar.setVisibility(View.VISIBLE);

        // Create new user and add the new user to Firebase Realtime Database
        // Credit to: https://www.youtube.com/watch?v=Z-RE1QuUWPg
        String finalUsernameStr = usernameStr;
        mAuth.createUserWithEmailAndPassword(emailStr, "password")
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        // Create User object.
                        User user = new User(finalUsernameStr, emailStr);
                        String successMsg = String.format("%s has been registered successfully", finalUsernameStr);
                        // Add user to database
                        FirebaseDatabase.getInstance()
                                .getReference("Users")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    // Display a Toast if user is successfully added to database
                                    // Lead to main chat page.
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(ActivitySignUp.this,
                                                successMsg,
                                                Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ActivitySignUp.this, ActivityChatMain.class));
                                    }
                                    else {
                                        Toast.makeText(ActivitySignUp.this,
                                                "Failed to add new user to database",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                });
                    }
                    // If the sign up is not successful, make a toast.
                    else {
                        Toast.makeText(ActivitySignUp.this,
                                "Failed to register. User already exists",
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * Input filter to block spaces in username entry
     * Credit to: https://stackoverflow.com/questions/21828323/how-can-restrict-my-edittext-input-to-some-special-character-like-backslash-t
     */
    private final InputFilter usernameFilter = (charSequence, i, i1, spanned, i2, i3) -> {
        String blockedCharSet = " ";
        if (charSequence!= null && blockedCharSet.contains("" + charSequence)) {
            return "";
        }
        return null;
    };


}