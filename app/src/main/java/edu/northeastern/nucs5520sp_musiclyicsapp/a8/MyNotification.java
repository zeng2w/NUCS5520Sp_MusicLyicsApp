package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class MyNotification {

    Context context;

    public MyNotification(Context context) {
        this.context = context;
    }

    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel("test", "test_channel", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void buildNotification() {

        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "test")
                .setSmallIcon(R.drawable.sticker1)
                .setContentTitle("Test Notification")
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier("sticker1", "drawable", context.getPackageName()))))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}
