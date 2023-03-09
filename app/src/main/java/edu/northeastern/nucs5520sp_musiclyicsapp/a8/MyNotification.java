package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

// Credit: https://developer.android.com/develop/ui/views/notifications/build-notification
public class MyNotification {

    Context context;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

    public MyNotification(Context context) {
        this.context = context;
    }

    public void checkForNotification() {

        reference.orderByChild("notified").equalTo("False").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    String senderUsername = dataSnapshot.child("sender username").getValue(String.class);
                    String senderEmail = dataSnapshot.child("sender email").getValue(String.class);
                    String receiverUid = dataSnapshot.child("receiver uid").getValue(String.class);
                    String sticker = dataSnapshot.child("sticker").getValue(String.class);

                    assert receiverUid != null;
                    if (receiverUid.equals(currentUser.getUid())) {
                        sendNotification(senderUsername, senderEmail, sticker);
                        dataSnapshot.child("notified").getRef().setValue("True");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel("test", "test_channel", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void sendNotification(String senderUsername, String senderEmail, String sticker) {

        createNotificationChannel();

        // Credit: https://developer.android.com/develop/ui/views/notifications/build-notification#click
        Intent intent = new Intent(context, ActivityMessage.class);
        intent.putExtra("username", senderUsername);
        intent.putExtra("email", senderEmail);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        @SuppressLint("DiscouragedApi") NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "test")
                .setSmallIcon(R.mipmap.ic_launcher_music)
                .setContentTitle(String.format("New sticker from %s (%s)", senderUsername, senderEmail))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier(sticker, "drawable", context.getPackageName()))))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}
