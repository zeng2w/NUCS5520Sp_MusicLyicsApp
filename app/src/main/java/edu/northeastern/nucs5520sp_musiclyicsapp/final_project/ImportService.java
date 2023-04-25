package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import static edu.northeastern.nucs5520sp_musiclyicsapp.final_project.App.CHANNEL_ID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.GeniusSong;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SpotifyArtist;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SpotifySong;

public class ImportService extends Service {

    private ArrayList<SpotifySong> spotifySongsList;
    private SpotifyService spotifyService;
    private LyricsService lyricsService;
    private ArrayList<GeniusSong> outputList;
    private FirebaseUser currentUser;
    private DatabaseReference userTokenRef;

    // Credit: https://towardsdatascience.com/using-the-spotify-api-with-your-android-application-the-essentials-1a3c1bc36b9e
    // Above credit for getting the Spotify songs in the playlist using spotify API.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String sharedPlaylistLink = intent.getStringExtra("Shared Playlist Link");

        spotifyService = new SpotifyService(this);
        lyricsService = new LyricsService(this);

        outputList = new ArrayList<>();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userTokenRef = FirebaseDatabase.getInstance().getReference("user_tokens").child(currentUser.getUid()).child("spotify_token");

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        // If sharedPreferences is empty, user hasn't authorized this app to access their spotify account.
        // Go to ActivitySpotifyAuth.
        Log.d("Spotify token", sharedPreferences.getString("token",""));

//        getTracks(sharedPlaylistLink);

        ServiceRunnable serviceRunnable = new ServiceRunnable(sharedPlaylistLink);
        Thread t = new Thread(serviceRunnable);
        t.start();

        return START_NOT_STICKY;
    }

//    // Whatever needs to be done with the songs obtained need to happen right below
//    // songsList = songService.getSongs(); (because of asynchronous nature of the request)
//    private void getTracks(String sharedPlaylistLink) {
//        // Extract the playlist ID from sharedPlaylistLink.
//        Pattern pattern = Pattern.compile("/playlist/(.*?)\\?si=");
//        Matcher matcher = pattern.matcher(sharedPlaylistLink);
//        String playlistId = "";
//        if (matcher.find()) {
//            playlistId = matcher.group(1);
//        }
//
//        // First, read the spotify_token from firebase.
//        String finalPlaylistId = playlistId;
//        userTokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Case 1: token exists.
//                if (snapshot.exists()) {
//                    String spotifyToken = snapshot.getValue(String.class);
//                    spotifyService.getSongsFromPlaylist(spotifyToken, finalPlaylistId, 100, 0, new VolleyCallback() {
//                        @Override
//                        public void onSuccess(boolean finished) {
//                            spotifySongsList = spotifyService.getSongs();
//
//                            // Post a notification indicating that our app has started to extract songs from Spotify Playlist using Spotify API.
//                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ImportService.this, CHANNEL_ID)
//                                    .setContentTitle("Reading Playlist Content")
//                                    .setContentText("Reading the songs in your shared Spotify playlist...")
//                                    .setSmallIcon(R.mipmap.ic_launcher_music);
//
//                            startForeground(1, notificationBuilder.build());
//
//                            // Songs in songsList can be used as input to the Genius API.
//                            if (finished) {
//
//                                // Loop through the songs in songsList and extract lyrics for each song.
//                                for (SpotifySong spotifySong : spotifySongsList) {
//                                    // Song info setup.
//                                    String songName = spotifySong.getSongName();
//                                    ArrayList<String> artistsList = new ArrayList<>();
//                                    for (SpotifyArtist spotifyArtist : spotifySong.getArtists()) {
//                                        artistsList.add(spotifyArtist.getArtistName());
//                                    }
//                                    // Get the GeniusSong with lyrics inside.
//                                    lyricsService.getLyricsForSong(songName, artistsList, new VolleyCallback() {
//                                        @Override
//                                        public void onSuccess(boolean finished) {
//
//                                        }
//
//                                        @SuppressLint("DefaultLocale")
//                                        @Override
//                                        public void onLyricsSuccess(GeniusSong outputGeniusSong) {
//                                            Log.d("output received in ImportService's callback", outputGeniusSong.getLyrics());
//
//                                            outputList.add(outputGeniusSong);
//
//                                            // Change the notification to show progress in extracting song lyrics with Genius API and scraping Genius.com.
//                                            notificationBuilder.setContentTitle("Getting Lyrics")
//                                                    .setContentText(String.format("Lyrics extraction progress: %d / %d", outputList.size(), spotifySongsList.size()));
//                                            NotificationManagerCompat.from(ImportService.this).notify(1, notificationBuilder.build());
//                                            Log.d("outputList size", String.valueOf(outputList.size()));
//
//                                            // If the size of the outputList is the same as the size of the spotifySongList, we know lyrics extraction is complete.
//                                            if (outputList.size() == spotifySongsList.size()) {
//                                                // Cancel the old notification displaying the import progress and create a new one that is clickable.
//                                                NotificationManagerCompat.from(ImportService.this).cancel(1);
//                                                // If user clicks on the notification, lead to the new Activity displaying import results.
//                                                Intent intent = new Intent(ImportService.this, ActivityImportResult.class);
//                                                Log.d("outputList size before putting in intent", String.valueOf(outputList.size()));
//
//                                                // Put each GeniusSong in outputList in SQLite database.
//                                                ImportSongDatabaseHelper dbHelper = new ImportSongDatabaseHelper(ImportService.this);
//                                                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                                                db.delete("genius_songs", null, null);
//                                                for (GeniusSong song: outputList) {
//                                                    ContentValues values = new ContentValues();
//                                                    values.put("song_name", song.getSongName());
//                                                    values.put("artists_string", song.getArtistsString());
//                                                    values.put("song_lyrics", song.getLyrics());
//                                                    db.insert("genius_songs", null, values);
//                                                }
//
//                                                // Go to ActivityImportResult to check out results.
//                                                PendingIntent pendingIntent = PendingIntent.getActivity(ImportService.this, 0, intent, 0);
//                                                NotificationCompat.Builder notificationBuilderComplete = new NotificationCompat.Builder(ImportService.this, CHANNEL_ID)
//                                                        .setContentTitle("Import and Lyrics Extraction Complete")
//                                                        .setContentText("Click this to check out the results")
//                                                        .setSmallIcon(R.mipmap.ic_launcher_music)
//                                                        .setContentIntent(pendingIntent);
//                                                NotificationManagerCompat.from(ImportService.this).notify(2, notificationBuilderComplete.build());
//                                            }
//                                        }
//                                    });
//
//                                    // Cope with Genius API's rate limit for search, which is 1 per second.
//                                    try {
//                                        Thread.sleep(1000);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onLyricsSuccess(GeniusSong geniusSong) {
//
//                        }
//                    });
//                }
//                // Case 2: No token found for this currently logged in user. Post a toast reminding to authorize.
//                else {
//                    Toast.makeText(ImportService.this, "Spotify Authorization required. Go to User page", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(ImportService.this, "Database Error; please try to authorize later", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class ServiceRunnable implements Runnable {
        private String sharedPlaylistLink;

        public ServiceRunnable(String sharedPlaylistLink) {
            this.sharedPlaylistLink = sharedPlaylistLink;
        }

        public String getSharedPlaylistLink() {
            return sharedPlaylistLink;
        }

        @Override
        public void run() {
            getTracks(sharedPlaylistLink);
        }

        // Whatever needs to be done with the songs obtained need to happen right below
        // songsList = songService.getSongs(); (because of asynchronous nature of the request)
        private void getTracks(String sharedPlaylistLink) {
            // Extract the playlist ID from sharedPlaylistLink.
            Pattern pattern = Pattern.compile("/playlist/(.*?)\\?si=");
            Matcher matcher = pattern.matcher(sharedPlaylistLink);
            String playlistId = "";
            if (matcher.find()) {
                playlistId = matcher.group(1);
            }

            // First, read the spotify_token from firebase.
            String finalPlaylistId = playlistId;
            userTokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Case 1: token exists.
                    if (snapshot.exists()) {
                        String spotifyToken = snapshot.getValue(String.class);
                        spotifyService.getSongsFromPlaylist(spotifyToken, finalPlaylistId, 100, 0, new VolleyCallback() {
                            @Override
                            public void onSuccess(boolean finished) {
                                spotifySongsList = spotifyService.getSongs();

                                // Post a notification indicating that our app has started to extract songs from Spotify Playlist using Spotify API.
                                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ImportService.this, CHANNEL_ID)
                                        .setContentTitle("Reading Playlist Content")
                                        .setContentText("Reading the songs in your shared Spotify playlist...")
                                        .setSmallIcon(R.mipmap.ic_launcher_music);

                                NotificationManagerCompat.from(ImportService.this).notify(1, notificationBuilder.build());
//                                startForeground(1, notificationBuilder.build());

                                // Songs in songsList can be used as input to the Genius API.
                                if (finished) {

                                    // Loop through the songs in songsList and extract lyrics for each song.
                                    for (SpotifySong spotifySong : spotifySongsList) {
                                        // Song info setup.
                                        String songName = spotifySong.getSongName();
                                        ArrayList<String> artistsList = new ArrayList<>();
                                        for (SpotifyArtist spotifyArtist : spotifySong.getArtists()) {
                                            artistsList.add(spotifyArtist.getArtistName());
                                        }
                                        // Get the GeniusSong with lyrics inside.
                                        lyricsService.getLyricsForSong(songName, artistsList, new VolleyCallback() {
                                            @Override
                                            public void onSuccess(boolean finished) {

                                            }

                                            @SuppressLint("DefaultLocale")
                                            @Override
                                            public void onLyricsSuccess(GeniusSong outputGeniusSong) {
                                                Log.d("output received in ImportService's callback", outputGeniusSong.getLyrics());

                                                outputList.add(outputGeniusSong);

                                                // Change the notification to show progress in extracting song lyrics with Genius API and scraping Genius.com.
                                                notificationBuilder.setContentTitle("Getting Lyrics")
                                                        .setContentText(String.format("Lyrics extraction progress: %d / %d", outputList.size(), spotifySongsList.size()));
                                                NotificationManagerCompat.from(ImportService.this).notify(1, notificationBuilder.build());
                                                Log.d("outputList size", String.valueOf(outputList.size()));

                                                // If the size of the outputList is the same as the size of the spotifySongList, we know lyrics extraction is complete.
                                                if (outputList.size() == spotifySongsList.size()) {
                                                    // Cancel the old notification displaying the import progress and create a new one that is clickable.
                                                    NotificationManagerCompat.from(ImportService.this).cancel(1);
                                                    // If user clicks on the notification, lead to the new Activity displaying import results.
                                                    Intent intent = new Intent(ImportService.this, ActivityImportResult.class);
                                                    Log.d("outputList size before putting in intent", String.valueOf(outputList.size()));

                                                    // Put each GeniusSong in outputList in SQLite database.
                                                    ImportSongDatabaseHelper dbHelper = new ImportSongDatabaseHelper(ImportService.this);
                                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                                    db.delete("genius_songs", null, null);
                                                    for (GeniusSong song: outputList) {
                                                        ContentValues values = new ContentValues();
                                                        values.put("song_name", song.getSongName());
                                                        values.put("artists_string", song.getArtistsString());
                                                        values.put("song_lyrics", song.getLyrics());
                                                        db.insert("genius_songs", null, values);
                                                    }

                                                    // Go to ActivityImportResult to check out results.
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(ImportService.this, 0, intent, 0);
                                                    NotificationCompat.Builder notificationBuilderComplete = new NotificationCompat.Builder(ImportService.this, CHANNEL_ID)
                                                            .setContentTitle("Import and Lyrics Extraction Complete")
                                                            .setContentText("Click this to check out the results")
                                                            .setSmallIcon(R.mipmap.ic_launcher_music)
                                                            .setContentIntent(pendingIntent);
                                                    NotificationManagerCompat.from(ImportService.this).notify(2, notificationBuilderComplete.build());
                                                }
                                            }
                                        });

                                        // Cope with Genius API's rate limit for search, which is 1 per second.
                                        SystemClock.sleep(1000);
                                    }
                                }
                            }

                            @Override
                            public void onLyricsSuccess(GeniusSong geniusSong) {

                            }
                        });
                    }
                    // Case 2: No token found for this currently logged in user. Post a toast reminding to authorize.
                    else {
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ImportService.this, CHANNEL_ID)
                                .setContentTitle("Need Spotify Authorization")
                                .setContentText("Please go to User page to authorize our app with your Spotify account")
                                .setSmallIcon(R.mipmap.ic_launcher_music);

                        NotificationManagerCompat.from(ImportService.this).notify(1, notificationBuilder.build());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ImportService.this, CHANNEL_ID)
                            .setContentTitle("Database Error")
                            .setContentText("Firebase error, please try again later")
                            .setSmallIcon(R.mipmap.ic_launcher_music);

                    NotificationManagerCompat.from(ImportService.this).notify(1, notificationBuilder.build());
                }
            });


        }
    }
}