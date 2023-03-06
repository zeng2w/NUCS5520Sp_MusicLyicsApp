package edu.northeastern.nucs5520sp_musiclyicsapp.a8.ShowHistory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.StickItToEm;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.utils.Data;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.utils.Utils;

public class StickerHistory extends AppCompatActivity {
    private RecyclerView showHistoryRecyclerView;
    private ArrayList<Data> messageItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_history);

        showHistoryRecyclerView = findViewById(R.id.showhistoryrecyclerview);
        showHistoryRecyclerView.setHasFixedSize(true);
        showHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageItemList = new ArrayList<>();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        Utils.postToastMessage("Loading data...", getApplicationContext());

        if (StickItToEm.u != null) {
            ArrayList<Data> dataList = new ArrayList<>(StickItToEm.u.data);
            for (Data userData : dataList) {
                String imageName = userData.imageId + ".png";
                storageRef.child(imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        userData.uri = uri;
                        messageItemList.add(userData);
                        MessageAdapter messageAdapter = new MessageAdapter(StickerHistory.this, messageItemList);
                        Collections.sort(messageItemList);
                        showHistoryRecyclerView.setAdapter(messageAdapter);
                        messageAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Utils.postToastMessage("Failure to get the images.", getApplicationContext());
                    }
                });
            }
        }
    }
}