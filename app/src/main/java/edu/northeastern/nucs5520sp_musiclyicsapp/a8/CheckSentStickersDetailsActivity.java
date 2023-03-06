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
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityCheckSentStickersDetailsBinding;

public class CheckSentStickersDetailsActivity extends AppCompatActivity {

    ActivityCheckSentStickersDetailsBinding binding;
    DatabaseReference databaseReferenceSendImages;
    SendDetailsAdapter sendDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckSentStickersDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sendDetailsAdapter = new SendDetailsAdapter(this);
        binding.sentStickersRecycler.setAdapter(sendDetailsAdapter);
        binding.sentStickersRecycler.setLayoutManager(new LinearLayoutManager(this));

        databaseReferenceSendImages = FirebaseDatabase.getInstance().getReference("sendImages");
        databaseReferenceSendImages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sendDetailsAdapter.clear();
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
                            sendDetailsAdapter.notifyDataSetChanged();
                            sendDetailsAdapter.add(imageModel);
                            sendDetailsAdapter.notifyDataSetChanged();
                        }

//
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckSentStickersDetailsActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}