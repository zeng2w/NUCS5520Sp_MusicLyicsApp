package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.Inflater;

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
    DatabaseReference databaseReferenceReceiveImagesNotify;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySendStickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid = dataSnapshot.getKey();
                    if (uid.equals(FirebaseAuth.getInstance().getUid())) {
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
        if (chosenImage != null) {
            binding.selectedImageView.setImageResource(Integer.parseInt(chosenImage));
        }


        databaseReferenceSendImages = FirebaseDatabase.getInstance().getReference("sendImages").child(currentUserId);
        databaseReferenceReceiveImages = FirebaseDatabase.getInstance().getReference("ReceiveImages").child(recieverId);

        Intent goHomeIntent = new Intent(SendStickerActivity.this, HomeActivity.class);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goHomeIntent);
            }
        });

    }

    private void notification(String username, String receiveDate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n")
                .setContentText("send sticker").setSmallIcon(R.drawable.notification_image).setAutoCancel(true)
                .setContentText(username + " send a sticker at " + receiveDate);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        managerCompat.notify(999, builder.build());

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

}