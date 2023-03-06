package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {


    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    ActivityHomeBinding binding;
    DatabaseReference databaseReference;

    UserAdapter userAdapter;

    private TextView currentUserText;

    DatabaseReference databaseReferenceReceiveImages;

    private static final String LAST_NOTIFICATION = "LAST_NOTIFICATION";
    private static final String NUMBER_OF_NOTIFICATION = "NUMBER_OF_NOTIFICATION";
    private ArrayList<String> notificationList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUserText = findViewById(R.id.userLogin);

        userAdapter = new UserAdapter(this);
        binding.recycler.setAdapter(userAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Log.d("reference", databaseReference.child("BqNb4k4sl0f80C8Ju2WYBqsGTw83").child("username").toString());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAdapter.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String uid = dataSnapshot.getKey();
                    Log.d("child value", dataSnapshot.child("username").getValue().toString());
                    Log.d("current login id: ", FirebaseAuth.getInstance().getUid());
                    if(!uid.equals(FirebaseAuth.getInstance().getUid())){
                        String userId = dataSnapshot.child("userId").getValue().toString();
                        String username = dataSnapshot.child("username").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String password = dataSnapshot.child("password").getValue().toString();
                        User user = new User(userId, username, email, password);
                        userAdapter.notifyDataSetChanged();

                        userAdapter.add(user);
                        userAdapter.notifyDataSetChanged();
                    }
                    else{
                        currentUserText.setText("Current User: " + dataSnapshot.child("username").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceReceiveImages = FirebaseDatabase.getInstance().getReference("ReceiveImages").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        databaseReferenceReceiveImages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(previousChildName != null){
                    Log.d("previousChildName", previousChildName);
                }
                //if(onOptionsItemSelected())
                DateTimeFormatter dtf = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                }
                LocalDateTime now = null;
                LocalDateTime lower = null;
                //LocalDateTime higher;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    now = LocalDateTime.now();
                    lower = now.minusSeconds(5);

                }
                ImageModel imageModel = null;
                LocalDateTime date = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    date = LocalDateTime.parse(snapshot.child("receiveDate").getValue().toString(),dtf);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if(date.isAfter(lower)) {
                        notification(snapshot.child("senderName").getValue().toString(), snapshot.child("receiveDate").getValue().toString()
                                , snapshot.child("imageName").getValue().toString());
                    }
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CheckStickerHistoryActivity.class));
                finish();
            }
        });

        binding.checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CheckSentStickersDetailsActivity.class));
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, StickItToEm.class));
            finish();
            return true;
        }
        return false;
    }

    private void notification(String username, String receiveDate, String imageName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

            Bitmap myBitmap = BitmapFactory.decodeResource(getResources(),
                    Integer.parseInt(imageName));

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n")
                    .setSmallIcon(R.drawable.notification_image).setAutoCancel(true)
                    .setContentText(username + " send a sticker at " + receiveDate)
                    .setLargeIcon(myBitmap).setStyle(new NotificationCompat.BigPictureStyle().bigPicture(myBitmap).bigLargeIcon(null));

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

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(999, builder.build());

//            Notification notification = builder.build();
//            managerCompat.notify(999, builder.build());
        }



    }


}