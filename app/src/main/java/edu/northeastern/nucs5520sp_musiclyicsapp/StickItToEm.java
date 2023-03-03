package edu.northeastern.nucs5520sp_musiclyicsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class StickItToEm extends AppCompatActivity {

    private static final String TAG = StickItToEm.class.getSimpleName();

    private DatabaseReference usersRef;
    private List<Sticker> mStickerList;
    private StickerAdapter mAdapter;
    String username = "user123";

    String sender = "username1"; // replace with the actual sender username
    String receiver = "username2"; // replace with the actual receiver username
    long timestamp = System.currentTimeMillis(); // get the current timestamp
    String stickerId = "sticker1"; // replace with the actual sticker identifier


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // to create a Firebase Authentication user account for each username when the user logs in
        // for the first time:
        firebaseAuth.createUserWithEmailAndPassword(username + "@gmail.com", "password")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User account created successfully
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        // Save additional user data to Firebase Realtime Database, if needed
                        // For example, you could create a "users" child node and add a sub-node for
                        // each user's data

                    } else {
                        // User account creation failed
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(StickItToEm.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // Create a "users" child node in the Firebase Realtime Database
        // This is where we'll store the sticker count data for each user
        usersRef = firebaseDatabase.getReference("users");

        // Initialize the sticker list and adapter
        mStickerList = new ArrayList<Sticker>();
        mAdapter = new StickerAdapter(this, mStickerList);

        // Set the adapter for the sticker list view
        ListView stickerListView = findViewById(R.id.sticker_list_view);
        stickerListView.setAdapter(mAdapter);

        // Load the sticker data from Firebase Realtime Database
        loadStickerData();

        // Get the current user's Firebase Authentication UID
        String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        // Create a child node under the "users" node for the current user's sticker counts
        DatabaseReference userStickersRef = usersRef.child(uid).child("stickers");

        // Set the initial count for each sticker type to 0
        Map<String, Integer> stickerCounts = new HashMap<>();
        stickerCounts.put("sticker1", 0);
        stickerCounts.put("sticker2", 0);
        stickerCounts.put("sticker3", 0);
        stickerCounts.put("sticker4", 0);

        //This code gets the current count for a specific sticker type for the current user,
        // increments the count by 1, and updates the count in Firebase Realtime Database. You can
        // replace "sticker1" with the actual sticker type, and replace "currentCount" with the
        // actual current count for that sticker type.

        // To update the sticker counts when a user sends a sticker, you can use the following code:
        String stickerType = "sticker1"; // replace with the actual sticker type
        int currentCount = 0; // replace with the current count for this sticker type
        int newCount = currentCount + 1; // increment the count by 1

        // Update the sticker count for the current user and sticker type in Firebase Realtime
        // Database
        userStickersRef.child(stickerType).setValue(newCount);




        // It first gets a reference to the current user's sticker history node in the database,
        // and then creates a new child node for the sticker that was sent. It sets the values for
        // the new sticker node, including the sender username, receiver username, timestamp, and
        // sticker identifier. The push() method generates a unique key for the new child node.

        // Create a new child node for the sticker that was sent
        String newStickerKey = userStickersRef.push().getKey();
        assert newStickerKey != null;
        DatabaseReference newStickerRef = userStickersRef.child(newStickerKey);

        // Set the values for the new sticker node
        newStickerRef.child("sender").setValue(sender);
        newStickerRef.child("receiver").setValue(receiver);
        newStickerRef.child("timestamp").setValue(timestamp);
        newStickerRef.child("sticker_id").setValue(stickerId);

        //Implement the sticker sending feature in your app. Allow users to select a sticker from
        // a predefined set of images and choose a recipient from their list of friends who also
        // have the app. When the user sends a sticker, update the sticker count and sticker
        // history data for both the sender and recipient in Firebase Realtime Database.


        // Create a DatabaseReference to the sticker count data for the sender and recipient
        DatabaseReference senderStickerCountRef = FirebaseDatabase.getInstance().getReference().child("stickerCounts").child(sender);
        DatabaseReference recipientStickerCountRef = FirebaseDatabase.getInstance().getReference().child("stickerCounts").child(receiver);

        // Create a DatabaseReference to the sticker history data for the sender and recipient
        DatabaseReference senderStickerHistoryRef = FirebaseDatabase.getInstance().getReference().child("stickerHistory").child(sender);
        DatabaseReference recipientStickerHistoryRef = FirebaseDatabase.getInstance().getReference().child("stickerHistory").child(receiver);


        // Create a new sticker history object
        StickerHistory stickerHistory = new StickerHistory(sender, receiver, timestamp, stickerIdentifier);

        // Increment the sticker count for the sender and recipient
        senderStickerCountRef.child(stickerIdentifier).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer count = mutableData.getValue(Integer.class);
                if (count == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(count + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                // Handle transaction completion
            }
        });

        recipientStickerCountRef.child(stickerIdentifier).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer count = mutableData.getValue(Integer.class);
                if (count == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(count + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                // Handle transaction completion
            }
        });

        // Add the sticker history object to the sender and recipient sticker history nodes
        senderStickerHistoryRef.push().setValue(stickerHistory);
        recipientStickerHistoryRef.push().setValue(stickerHistory);

        // Assume stickerId is the received sticker identifier
        // Assume stickerMap is a map that maps sticker identifiers to their corresponding images

        if (stickerCounts.containsKey(stickerId)) {
            // Display the corresponding sticker image
            imageView.setImageResource(stickerMap.get(stickerId));
        } else {
            // Display a default sticker image or notify the user that the sticker could not be displayed
            imageView.setImageResource(R.drawable.default_sticker);
            Toast.makeText(this, "Unknown sticker received", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current sticker list data in Firebase Realtime Database
        usersRef.child("users").child(username).child("stickers").setValue(mStickerList);
    }

    private void loadStickerData() {
        // Load the sticker data from Firebase Realtime Database
        usersRef.child("stickers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot stickerSnapshot : snapshot.getChildren()) {
                    Sticker sticker = stickerSnapshot.getValue(Sticker.class);
                    mStickerList.add(sticker);
                }

                // Notify the adapter that the data set has changed
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error loading sticker data", error.toException());
            }
        });
    }

}