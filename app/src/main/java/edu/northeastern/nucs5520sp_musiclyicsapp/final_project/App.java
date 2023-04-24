package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.google.firebase.database.FirebaseDatabase;

// Credit: https://youtu.be/FbpD5RZtbCc
// For notification channel for the foreground service to import songs from a Spotify playlist and
// obtain lyrics for these songs from Genius.com.
public class App extends Application {

    public static final String CHANNEL_ID = "importServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

    @SuppressLint("ObsoleteSdkInt")
    private void createNotificationChannel() {
        // Check if SDK greater than Android Oreo.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel importChannel = new NotificationChannel(CHANNEL_ID, "Import Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(importChannel);
        }
    }
}