package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import edu.northeastern.nucs5520sp_musiclyicsapp.MainActivity;
import edu.northeastern.nucs5520sp_musiclyicsapp.R;

/**
 * Class for the Message UI with a specific user.
 * Credit: https://www.youtube.com/watch?v=KB2BIm_m1Os
 */
public class ActivityMessage extends AppCompatActivity {

    FirebaseUser currentUser;
    // Points to all Users
    DatabaseReference ref;
    // Points to specific user currently chatting with
    DatabaseReference userRef;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        intent = getIntent();
        // Change the text displayed in the Action Bar.
        String usernameStr = intent.getStringExtra("username");
        String emailStr = intent.getStringExtra("email");
        String chatTitle = String.format("%s (%s)", usernameStr, emailStr);
        Objects.requireNonNull(getSupportActionBar()).setTitle(chatTitle);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");

        // Get the Uid by finding the matching email address
        // Credit: https://stackoverflow.com/questions/52036076/get-uid-in-list-by-searching-child-values-firebase-android
        ref.child("email").orderByChild("email").equalTo(emailStr).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userRef = ref.child(Objects.requireNonNull(snapshot.getKey()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Enable the back button in Action Bar.
        // Credit: https://stackoverflow.com/questions/15686555/display-back-button-on-action-bar
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Log the user out.
     * @param item  the MenuItem to be clicked
     * @return  true if logged out and false if not logged out
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        // Modify the back button in Action Bar to behave the same as back navigation button.
        // Credit: https://stackoverflow.com/questions/14437745/how-to-override-action-bar-back-button-in-android
        if (itemId == android.R.id.home) {
            // Changed
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}