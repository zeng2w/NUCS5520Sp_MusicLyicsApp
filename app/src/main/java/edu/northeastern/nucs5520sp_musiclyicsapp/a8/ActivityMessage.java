package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

/**
 * Class for the Message UI with a specific user.
 * Credit: https://www.youtube.com/watch?v=KB2BIm_m1Os
 */
public class ActivityMessage extends AppCompatActivity {

    FirebaseUser currentUser;
    // Points to all Users
    DatabaseReference usersRef;
    // Points to specific user currently chatting with
    DatabaseReference senderRef;
    String senderId, senderEmail, senderUsername, receiverId, receiverEmail, receiverUsername;
    // Collection of sender and receiver info
    HashMap<String, String> usersInfo;
    Button buttonSendSticker;

    Intent intentChatMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        intentChatMain = getIntent();

        buttonSendSticker = findViewById(R.id.buttonSendSticker);

        // Change the text displayed in the Action Bar.
        String receiverUsernameStr = intentChatMain.getStringExtra("username");
        String receiverEmailStr = intentChatMain.getStringExtra("email");
        String chatTitle = String.format("%s (%s)", receiverUsernameStr, receiverEmailStr);
        Objects.requireNonNull(getSupportActionBar()).setTitle(chatTitle);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Enable the back button in Action Bar.
        // Credit: https://stackoverflow.com/questions/15686555/display-back-button-on-action-bar
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // NEED TO USE CALLBACK BECAUSE FIREBASE IS ASYNCHRONOUS! (asynchronous callback)
        // CREDIT TO: https://www.youtube.com/watch?v=OvDZVV5CbQg
        collectSenderReceiverInfo();
    }

    /**
     * Write sender and receiver info into usersInfo.
     */
    public void collectSenderReceiverInfo() {

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        senderRef = usersRef.child(currentUser.getUid());
        usersInfo = new HashMap<>();

        // Put sender uid into the HashMap.
        senderId = currentUser.getUid();
        usersInfo.put("sender uid", senderId);

        // Read receiver's username and email from intent's bundle.
        receiverUsername = intentChatMain.getStringExtra("username");
        receiverEmail = intentChatMain.getStringExtra("email");
        // Put receiver username and email into the HashMap.
        usersInfo.put("receiver username", receiverUsername);
        usersInfo.put("receiver email", receiverEmail);

        // Extract receiver's Uid
        // Credit: https://stackoverflow.com/questions/66264594/firebase-getting-data-outside-of-ondatachange-addlistenerforsinglevalueevent
        // Credit: https://www.youtube.com/watch?v=OvDZVV5CbQg
        usersRef.orderByChild("email").equalTo(receiverEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    receiverId = dataSnapshot.getKey();
                }
                // Put receiver id into usersInfo; receiver information collection finished.
                usersInfo.put("receiver uid", receiverId);

                // Add a nested ValueEventListener to add the sender's username and email to usersInfo.
                senderRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User sender = snapshot.getValue(User.class);
                        assert sender != null;
                        senderUsername = sender.getUsername();
                        senderEmail = sender.getEmail();
                        usersInfo.put("sender username", senderUsername);
                        usersInfo.put("sender email", senderEmail);

                        // Sender info also complete; now we add OnClickListener for "Send Sticker" Button.
                        buttonSendSticker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentSend = new Intent(ActivityMessage.this, ActivityStickerSelector.class);
                                intentSend.putExtra("sender uid", usersInfo.get("sender uid"));
                                intentSend.putExtra("sender username", usersInfo.get("sender username"));
                                intentSend.putExtra("sender email", usersInfo.get("sender email"));
                                intentSend.putExtra("receiver uid", usersInfo.get("receiver uid"));
                                intentSend.putExtra("receiver username", usersInfo.get("receiver username"));
                                intentSend.putExtra("receiver email", usersInfo.get("receiver email"));
                                startActivity(intentSend);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Return to the tab before entering the specific chat page.
     * @param item  the MenuItem to be clicked
     * @return  true if the item is the back button in action bar; otherwise call the super method
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

//    @Override
//    public void onClick(View view) {
//        int viewId = view.getId();
//        if (viewId == R.id.buttonSendSticker) {
//
////            Intent intentSend = new Intent(ActivityMessage.this, ActivityStickerSelector.class);
//
//            // Save the sender's uid, username, and email.
////            intentSend.putExtra("sender uid", currentUser.getUid());
//
//            DatabaseReference senderRef = usersRef.child(currentUser.getUid());
//
//            senderRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    User currentUserObj = snapshot.getValue(User.class);
//                    Intent intentSend = new Intent(ActivityMessage.this, ActivityStickerSelector.class);
////                    intentSend.putExtra("sender uid", currentUser.getUid());
//                    assert currentUserObj != null;
//                    intentSend.putExtra("sender username", currentUserObj.getUsername());
//                    intentSend.putExtra("sender email", currentUserObj.getEmail());
//                    startActivity(intentSend);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//    }
}