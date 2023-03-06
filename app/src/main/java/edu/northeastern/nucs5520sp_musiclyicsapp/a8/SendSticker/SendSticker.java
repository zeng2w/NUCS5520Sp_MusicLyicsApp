package edu.northeastern.nucs5520sp_musiclyicsapp.a8.SendSticker;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.utils.Data;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.utils.Utils;

public class SendSticker extends AppCompatActivity {

    DatabaseReference mDatabase;
    Button sendButton;
    EditText editTextReceiver;
    String receiver;
    String sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);

        sendButton = findViewById(R.id.sendButton);
        stickerPressed(R.id.anger);
        stickerPressed(R.id.awe);
        stickerPressed(R.id.facewithtearofjoy);
        stickerPressed(R.id.loudlycrying);
        stickerPressed(R.id.lovehearteyes);
        stickerPressed(R.id.smilingfacewithsunglasses);
        stickerPressed(R.id.thumbup);
        stickerPressed(R.id.unamused);
        stickerPressed(R.id.wink);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                sendSticker();
            }
        });
    }


    /**
     * Shows user the Sticker he/she picked as textview
     * @param buttonId ImageButton that user selected
     */
    public void stickerPressed(int buttonId) {
        TextView stickerChosen = findViewById(R.id.stickerChosen);
        ImageButton stickerButton = findViewById(buttonId);
        stickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerButton.setSelected(!stickerButton.isSelected());
                if (stickerButton.isSelected()) {
                    String imageID = getResources().getResourceEntryName(stickerButton.getId());
                    stickerChosen.setText(imageID);
                }
            }
        });
    }

    /**
     * Gets imageId as String
     * @return ImageID as String
     */
    private String getImageID() {
        TextView stickerChosen = findViewById(R.id.stickerChosen);
        String imageID = stickerChosen.getText().toString();
        return imageID;
    }

    /**
     * Sends sticker and add data of sender and receiver to RealtimeDatabase
     */
    public void sendSticker() {
        sender = Utils.getProperties(getApplicationContext(), "STICKY_USER_NAME");
        editTextReceiver = findViewById(R.id.editTextReceiver);
        receiver = editTextReceiver.getText().toString().trim();
        // If receiver field is empty, post a Snackbar message
        if (receiver.equals("")) {
            Snackbar.make(sendButton, "You must input a receiver", Snackbar.LENGTH_LONG).show();
            return;
        }
        String imageId = getImageID();
        // If user did not pick a sticker, post a Snackbar message
        if (imageId.equals("") || imageId.equals("Sticker Chosen")) {
            Snackbar.make(sendButton, "You must select a sticker!", Snackbar.LENGTH_LONG).show();
            return;
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mSender = mDatabase.child("users").child(sender);
        DatabaseReference mReceiver = mDatabase.child("users").child(receiver);
        mReceiver.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Data senderData = new Data(false, receiver, imageId);
                    Data receiverData = new Data(true, sender, imageId);

                    // Add data into RealtimeDatabase
                    String ID = String.valueOf(UUID.randomUUID());
                    mSender.child("data").child(ID).setValue(senderData);
                    mReceiver.child("data").child(ID).setValue(receiverData);

                    // Post a snackbar message that sticker has been send to receiver
                    Snackbar.make(sendButton, "Sticker " + imageId + " sent to " + receiver,
                            Snackbar.LENGTH_LONG).show();
                } else {
                    // Post a message to notify user that receiver does not exist.
                    Snackbar.make(sendButton, "Receiver " + receiver + " does not exist!",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /**
     * Hides the soft keyboard when button Send was clicked
     * @param v view
     */
    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}

