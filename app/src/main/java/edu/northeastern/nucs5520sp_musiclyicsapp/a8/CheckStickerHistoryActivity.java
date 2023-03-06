package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityCheckStickerHistoryBinding;

public class CheckStickerHistoryActivity extends AppCompatActivity {
    ActivityCheckStickerHistoryBinding binding;
    DatabaseReference databaseReferenceReceiveImages;
    ReceivedStickerAdapter receivedStickerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckStickerHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        receivedStickerAdapter = new ReceivedStickerAdapter(this);
        binding.receivedHistoryRecycler.setAdapter(receivedStickerAdapter);
        binding.receivedHistoryRecycler.setLayoutManager(new LinearLayoutManager(this));

        databaseReferenceReceiveImages = FirebaseDatabase.getInstance().getReference("ReceiveImages");
        databaseReferenceReceiveImages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receivedStickerAdapter.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String sid = dataSnapshot.getKey();
                    if(sid.equals(FirebaseAuth.getInstance().getUid())){
                        Log.d("test",dataSnapshot.getValue().toString());
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            Log.d("get key", dataSnapshot1.child("imageId").getValue().toString());
                            String imageId = dataSnapshot1.child("imageId").getValue().toString();
                            String imageName = dataSnapshot1.child("imageName").getValue().toString();
                            String senderId =dataSnapshot1.child("senderId").getValue().toString();
                            String senderName=dataSnapshot1.child("senderName").getValue().toString();
                            String receiverId=dataSnapshot1.child("receiverId").getValue().toString();
                            String receiverName=dataSnapshot1.child("receiverName").getValue().toString();
                            String receiveDate=dataSnapshot1.child("receiveDate").getValue().toString();
                            ImageModel imageModel = new ImageModel(imageId,imageName,senderId,senderName,receiverId,receiverName,receiveDate);
                            Log.d("imageName", imageName);
                            Log.d("imageModel", imageModel.getImageName().toString());
                            receivedStickerAdapter.notifyDataSetChanged();
                            receivedStickerAdapter.add(imageModel);
                            receivedStickerAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.backToHomeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckStickerHistoryActivity.this, HomeActivity.class));
                finish();
            }
        });


    }
}