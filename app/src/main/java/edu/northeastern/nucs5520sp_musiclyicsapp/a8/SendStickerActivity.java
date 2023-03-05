package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

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
import java.util.UUID;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivitySendStickerBinding;

public class SendStickerActivity extends AppCompatActivity {
    ActivitySendStickerBinding binding;
    private String recieverId;
    private String recieverName;
    private String recieverEmail;
    private String chosenImage;

    DatabaseReference databaseReferenceSender;
    DatabaseReference databaseReferenceReciever;


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
                uploadImage();
            }
        });

        Intent intent = getIntent();
        recieverId = intent.getStringExtra("userId");
        recieverName = intent.getStringExtra("name");
        recieverEmail = intent.getStringExtra("email");
        chosenImage = intent.getStringExtra("imageSrc");

        binding.sendToText.setText("Send Sticker To: " + recieverName);
        if(chosenImage != null) {
            binding.selectedImageView.setImageResource(Integer.parseInt(chosenImage));
        }

//        senderRoom = FirebaseAuth.getInstance().getUid() + recieverId;
//        recieverRoom = recieverId + FirebaseAuth.getInstance().getUid();

//        databaseReferenceSender = FirebaseDatabase.getInstance().getReference("sendImage").child(senderRoom);
//        databaseReferenceReciever = FirebaseDatabase.getInstance().getReference("sendImage").child(recieverRoom);


//        databaseReferenceSender.addValueEventListener(new ValueEventListener() {
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

        // defining implicit intent to mobile gallery
//        Intent selectImageIntent = new Intent();
//        selectImageIntent.setType("image/*");
//        selectImageIntent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(selectImageIntent, "Select Sticker from here..."),
//                PICK_IMAGE_REQUEST);
    }

    // override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        //if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            // get the uri of data
            filePath = data.getData();
            try {
                // setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                binding.selectedImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void uploadImage() {
    }


    private void sendImage(String imageSrc) {
        String imageId = UUID.randomUUID().toString();
        ImageModel imageModel = new ImageModel(imageId, FirebaseAuth.getInstance().getUid(), imageSrc, recieverId);
        databaseReferenceSender.child(imageId).setValue(imageModel);
        databaseReferenceReciever.child(imageId).setValue(imageModel);
    }
}