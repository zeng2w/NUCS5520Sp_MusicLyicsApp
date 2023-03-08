package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.Adapter.StickersAdapter;

public class ActivityStickerSelector extends AppCompatActivity implements View.OnClickListener{

    private final int ROW_COUNT = 3;
    private String senderUid, senderUsername, senderEmail, receiverUid, receiverUsername, receiverEmail;
    private String[] imgArr;
    private int selectedPos;
    private StickersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector);

        // Change Action Bar text.
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Select Sticker");

        imgArr = new String[]{
                "sticker1",
                "sticker2",
                "sticker3",
                "sticker4",
                "sticker5",
                "sticker6",
                "sticker7",
                "sticker8",
                "sticker9",
                "sticker10",
                "sticker11",
                "sticker12",
                "sticker13",
                "sticker14",
                "sticker15",
                "sticker16",
                "sticker17",
                "sticker18",
                "sticker19",
                "sticker20",
                "sticker21",
                "sticker22",
                "sticker23"};

        RecyclerView recyclerView = findViewById(R.id.recyclerView_images);
        // Not the LinearLayoutManager as before!
        recyclerView.setLayoutManager(new GridLayoutManager(this, ROW_COUNT));
        adapter = new StickersAdapter(imgArr, this.getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        selectedPos = RecyclerView.NO_POSITION;

        // Recover the sender and receiver information.
        // Credit: https://stackoverflow.com/questions/5265913/how-to-use-putextra-and-getextra-for-string-data
        Intent intentSend = getIntent();
        senderUid = intentSend.getStringExtra("sender uid");
        senderUsername = intentSend.getStringExtra("sender username");
        senderEmail = intentSend.getStringExtra("sender email");
        receiverUid = intentSend.getStringExtra("receiver uid");
        receiverUsername = intentSend.getStringExtra("receiver username");
        receiverEmail = intentSend.getStringExtra("receiver email");
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.buttonCancelImageSelector) {
            onBackPressed();
        }
        else if (viewId == R.id.buttonSend) {
            selectedPos = adapter.getSelectedPos();
            if (selectedPos != RecyclerView.NO_POSITION) {
                sendMessage(senderUid, senderUsername, senderEmail, receiverUid, receiverUsername, receiverEmail, String.valueOf(imgArr[selectedPos]));
                // Reset selectedPos after send.
                selectedPos = RecyclerView.NO_POSITION;
                finish();
            }
            else {
                Toast.makeText(this, "Need to select a sticker first", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * Send sticker message from a sender to a receiver.
     * @param senderUsername         sender's username
     * @param senderEmail            sender's email
     * @param receiverUsername       receiver's username
     * @param receiverEmail          receiver's email
     * @param sticker                the sticker to be send
     */
    private void sendMessage(String senderUid, String senderUsername, String senderEmail, String receiverUid, String receiverUsername, String receiverEmail, String sticker) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender uid", senderUid);
        hashMap.put("sender username", senderUsername);
        hashMap.put("sender email", senderEmail);
        hashMap.put("receiver uid", receiverUid);
        hashMap.put("receiver username", receiverUsername);
        hashMap.put("receiver email", receiverEmail);
        hashMap.put("sticker", sticker);

        reference.child("Chats").push().setValue(hashMap);

        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList");

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatRef.push().setValue(new ChatList(senderUid, receiverUid));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Update the count for stickers sent for the currentUser.
        DatabaseReference stickersSentRef = FirebaseDatabase.getInstance().getReference("StickersSent").child(senderUid).child(sticker);
        stickersSentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // If sticker never sent before, count is 1
                    stickersSentRef.setValue("1");
                }
                else {
                    // Sticker sent before, increment the count by 1
                    int currentCount = Integer.parseInt(Objects.requireNonNull(snapshot.getValue(String.class)));
                    currentCount = currentCount + 1;
                    stickersSentRef.setValue(String.valueOf(currentCount));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference stickersReceivedRef = FirebaseDatabase.getInstance().getReference("StickersReceived").child(receiverUid).child(sticker);
        stickersReceivedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // If sticker never sent before, count is 1
                    stickersReceivedRef.setValue("1");
                }
                else {
                    // Sticker sent before, increment the count by 1
                    int currentCount = Integer.parseInt(Objects.requireNonNull(snapshot.getValue(String.class)));
                    currentCount = currentCount + 1;
                    stickersReceivedRef.setValue(String.valueOf(currentCount));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}