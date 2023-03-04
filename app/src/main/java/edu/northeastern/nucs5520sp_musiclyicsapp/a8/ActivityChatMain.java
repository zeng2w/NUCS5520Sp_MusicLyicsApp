package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class ActivityChatMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        // Get child (i.e. one user) value (e.g., email, username)
        // Credit: https://www.youtube.com/watch?v=KRtLZF-xlAs
        // Credit: https://firebase.google.com/docs/database/android/read-and-write
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference currentUserRef = usersRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
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
}