package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import edu.northeastern.nucs5520sp_musiclyicsapp.MainActivity;
import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class ActivityChatMain extends AppCompatActivity {

    FirebaseUser currentUser;
    DatabaseReference currentUserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        // Credit: https://www.youtube.com/watch?v=KB2BIm_m1Os
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get child (i.e. one user) value (e.g., email, username)
        // Credit: https://www.youtube.com/watch?v=KRtLZF-xlAs
        // Credit: https://firebase.google.com/docs/database/android/read-and-write
        currentUserRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentUsernameStr = Objects.requireNonNull(snapshot.child("username").getValue()).toString();
                // Change Action Bar text.
                // Credit to: https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwjoxYb-9ML9AhVEFVkFHRZuAycQFnoECA4QAw&url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DlM-QxcE_mSU&usg=AOvVaw2IOfsOtCDDwDERAjLqZhEQ
                Objects.requireNonNull(getSupportActionBar()).setTitle(currentUsernameStr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Add the menu to the chat UI for logging out.
     * Credit: https://www.youtube.com/watch?v=KB2BIm_m1Os
     * @param menu  the menu for logging out
     * @return  true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_main, menu);
        return true;
    }

    /**
     * Log the user out.
     * @param item  the MenuItem to be clicked
     * @return  true if logged out and false if not logged out
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ActivityChatMain.this, ActivityLogIn.class));
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityChatMain.this, MainActivity.class));
        finish();
    }
}