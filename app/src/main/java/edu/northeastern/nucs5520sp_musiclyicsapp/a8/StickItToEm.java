package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.LoginPage.LoginPage;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.SendSticker.SendSticker;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.ShowHistory.StickerHistory;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.utils.Data;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.utils.User;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.utils.Utils;

public class StickItToEm extends AppCompatActivity {

    public static int REQUEST_CODE_LOGIN = 1;
    public static int REQUEST_CODE_SEND = 2;
    public static int REQUEST_CODE_SHOW_HISTORY = 3;
    public static String STICKY_DEVICE_ID;
    public static User u;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);
        if (Utils.getProperties(getApplicationContext(), "STICKY_DEVICE_ID").equals("")) {
            STICKY_DEVICE_ID = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
            Utils.setProperties(getApplicationContext(), "STICKY_DEVICE_ID", STICKY_DEVICE_ID);
        } else {
            STICKY_DEVICE_ID = Utils.getProperties(getApplicationContext(), "STICKY_DEVICE_ID");
        }
        createNotificationChannel();
        startActivityIfNeeded(new Intent(StickItToEm.this, LoginPage.class), REQUEST_CODE_LOGIN);

    }
    public void openSendStickerActivity(View view) {
        startActivityIfNeeded(new Intent(StickItToEm.this, SendSticker.class), REQUEST_CODE_SEND);
    }

    public void startShowHistoryActivity(View view) {
        startActivityIfNeeded(new Intent(new Intent(getApplicationContext(), StickerHistory.class)), REQUEST_CODE_SHOW_HISTORY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN) {
            // wait for login page finished to get the user name
            String username = Utils.getProperties(getApplicationContext(), "STICKY_USER_NAME");
            TextView displayUserIdField = findViewById(R.id.display_username);
            displayUserIdField.setText("Good to see you: " + username);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            // update the user device id in case the user sign-in on a different device
            mDatabase.child("users").child(username).child("userDeviceID").
                    setValue(STICKY_DEVICE_ID, "STICKY_DEVICE_ID");
            ArrayList<Data> dataList = new ArrayList<>();
            mDatabase.child("users").child(username).child("data").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!dataList.isEmpty()) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            Data d = postSnapshot.getValue(Data.class);
                            if(!dataList.contains(d)) {
                                pushNotification(d);
                                dataList.add(d);
                            }
                        }
                    } else {
                        dataList.clear();
                        // loop through the data list to show the chat history for this user
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Data d = postSnapshot.getValue(Data.class);
                            dataList.add(d);
                        }
                        u = new User(STICKY_DEVICE_ID, dataList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("The read failed: " + error.getMessage());
                }
            });
        }
    }

    private void pushNotification(Data d) {
        String channelId = "STICKER_APP";
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(d.imageId, "drawable", getPackageName()));
        NotificationCompat.Builder notifyBuild = new NotificationCompat.Builder(this, channelId)
                //"Notification icons must be entirely white."
                .setSmallIcon(R.drawable.ic_launcher_icon_foreground)
                .setLargeIcon(largeIcon)
                .setContentTitle("New message from " + d.otherUser)
                .setContentText(d.otherUser + " send you a new sticker!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // hide the notification after its selected
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, notifyBuild.build());
    }

    private void createNotificationChannel() {
        // This must be called early because it must be called before a notification is sent.
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Name";
            String description = "Notification channel for sticker app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("STICKER_APP", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
}