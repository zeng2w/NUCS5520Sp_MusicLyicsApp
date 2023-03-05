package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivitySendStickerBinding;

public class SendStickerActivity extends AppCompatActivity {
    ActivitySendStickerBinding binding;
    private String recieverId;
    private String recieverName;
    private String recieverEmail;
    private String currentUserId;
    private String currentUserName;
    private String chosenImage;

    DatabaseReference databaseReferenceSendImages;
    DatabaseReference databaseReferenceReceiveImages;
    DatabaseReference databaseReference;


    //String senderRoom, recieverRoom;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    //int MY_RESULT_LOAD_IMAGE = 10;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySendStickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get the Firebase storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //select sticker button
        binding.selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        // when click send sticker button upload image to firebase storage
        binding.SendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendImage();
            }
        });

        Intent intent = getIntent();
        recieverId = intent.getStringExtra("userId");
        recieverName = intent.getStringExtra("name");
        recieverEmail = intent.getStringExtra("email");
        chosenImage = intent.getStringExtra("imageSrc");

        currentUserId = FirebaseAuth.getInstance().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String uid = dataSnapshot.getKey();
                    if(uid.equals(FirebaseAuth.getInstance().getUid())){
//                        String userId = dataSnapshot.child("userId").getValue().toString();
                        String username = dataSnapshot.child("username").getValue().toString();
//                        String email = dataSnapshot.child("email").getValue().toString();
//                        String password = dataSnapshot.child("password").getValue().toString();
                        currentUserName = username;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.sendToText.setText("Send Sticker To: " + recieverName);
        if(chosenImage != null) {
            binding.selectedImageView.setImageResource(Integer.parseInt(chosenImage));
        }

//        senderRoom = FirebaseAuth.getInstance().getUid() + recieverId;
//        recieverRoom = recieverId + FirebaseAuth.getInstance().getUid();

        databaseReferenceSendImages = FirebaseDatabase.getInstance().getReference("sendImages").child(currentUserId);
        databaseReferenceReceiveImages = FirebaseDatabase.getInstance().getReference("ReceiveImages").child(recieverId);

//        databaseReferenceSendImages.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    ImageModel imageModel = dataSnapshot.getValue(ImageModel.class);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//        databaseReferenceReceiveImages.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    ImageModel imageModel = dataSnapshot.getValue(ImageModel.class);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });




        Intent goHomeIntent = new Intent(SendStickerActivity.this, HomeActivity.class);



        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goHomeIntent);
            }
        });

    }

    private void selectImage() {
        Intent goSelectImageActivity = new Intent(SendStickerActivity.this, StickersListActivity.class);
        goSelectImageActivity.putExtra("receiverId", recieverId);
        goSelectImageActivity.putExtra("receiverName", recieverName);
        goSelectImageActivity.putExtra("receiverEmail", recieverEmail);

        startActivity(goSelectImageActivity);
        finish();

    }

    private void sendImage() {
        String imageId = UUID.randomUUID().toString();
        if(chosenImage != null){
            DateTimeFormatter dtf = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            }
            LocalDateTime now = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                now = LocalDateTime.now();
            }
            ImageModel imageModel = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                imageModel = new ImageModel(imageId, chosenImage, currentUserId,currentUserName,recieverId,recieverName,dtf.format(now));
                databaseReferenceSendImages.child(imageId).setValue(imageModel);
                databaseReferenceReceiveImages.child(imageId).setValue(imageModel);

                Toast.makeText(SendStickerActivity.this, "Send Image Successful!", Toast.LENGTH_SHORT).show();

            }

        } else{
            Toast.makeText(SendStickerActivity.this, "not choose a sticker yet", Toast.LENGTH_SHORT).show();

        }
    }


//    private void sendImage(String imageSrc) {
//        String imageId = UUID.randomUUID().toString();
//        ImageModel imageModel = new ImageModel(imageId, FirebaseAuth.getInstance().getUid(), imageSrc, recieverId);
//        databaseReferenceSender.child(imageId).setValue(imageModel);
//        databaseReferenceReciever.child(imageId).setValue(imageModel);
//    }
}