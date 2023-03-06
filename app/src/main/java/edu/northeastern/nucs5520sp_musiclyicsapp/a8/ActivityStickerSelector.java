package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.Adapter.StickersAdapter;

public class ActivityStickerSelector extends AppCompatActivity implements View.OnClickListener{

    private final int ROW_COUNT = 3;
    private String senderUid, senderUsername, senderEmail, receiverUid, receiverUsername, receiverEmail;
    private int[] imgArr;
    private int selectedPos;
    private StickersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector);

        // Change Action Bar text.
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Select Sticker");

        imgArr = new int[]{R.drawable.sticker1,
                R.drawable.sticker2,
                R.drawable.sticker3,
                R.drawable.sticker4,
                R.drawable.sticker5,
                R.drawable.sticker6,
                R.drawable.sticker7,
                R.drawable.sticker8,
                R.drawable.sticker9,
                R.drawable.sticker10,
                R.drawable.sticker11,
                R.drawable.sticker12,
                R.drawable.sticker13,
                R.drawable.sticker14,
                R.drawable.sticker15,
                R.drawable.sticker16,
                R.drawable.sticker17,
                R.drawable.sticker18,
                R.drawable.sticker19,
                R.drawable.sticker20,
                R.drawable.sticker21,
                R.drawable.sticker22,
                R.drawable.sticker23};

        RecyclerView recyclerView = findViewById(R.id.recyclerView_images);
        // Not the LinearLayoutManager as before!
        recyclerView.setLayoutManager(new GridLayoutManager(this, ROW_COUNT));
        adapter = new StickersAdapter(imgArr);
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
    }
}