package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import static edu.northeastern.nucs5520sp_musiclyicsapp.final_project.App.CHANNEL_ID;

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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

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

    // Credit: https://towardsdatascience.com/using-the-spotify-api-with-your-android-application-the-essentials-1a3c1bc36b9e
    // Above credit for getting the Spotify songs in the playlist using spotify API.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String sharedPlaylistLink = intent.getStringExtra("Shared Playlist Link");

        spotifyService = new SpotifyService(getApplicationContext());
        lyricsService = new LyricsService(getApplicationContext());

        outputList = new ArrayList<>();

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        // If sharedPreferences is empty, user hasn't authorized this app to access their spotify account.
        // Go to ActivitySpotifyAuth.
        Log.d("Spotify token", sharedPreferences.getString("token",""));
//        if (sharedPreferences.getString("token","").equals("")) {
//            Log.d("Inside If", "Inside the if clause in ImportService when no Spotify token");
//            Toast.makeText(this, "Need authorization with your Spotify account to read your playlist information", Toast.LENGTH_SHORT).show();
//            Intent intentAuth = new Intent(ImportService.this, ActivitySpotifyAuth.class);
//            intentAuth.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intentAuth);
//        }

        Toast.makeText(this, "Need authorization with your Spotify account to read your playlist information", Toast.LENGTH_SHORT).show();
        Intent intentAuth = new Intent(ImportService.this, ActivitySpotifyAuth.class);
        intentAuth.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentAuth);

        getTracks(sharedPlaylistLink);

        return START_NOT_STICKY;
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

        spotifyService.getSongsFromPlaylist(playlistId, 100, 0, new VolleyCallback() {
            @Override
            public void onSuccess(boolean finished) {
                spotifySongsList = spotifyService.getSongs();

                // Post a notification indicating that our app has started to extract songs from Spotify Playlist using Spotify API.
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ImportService.this, CHANNEL_ID)
                        .setContentTitle("Reading Playlist Content")
                        .setContentText("Reading the songs in your shared Spotify playlist...")
                        .setSmallIcon(R.mipmap.ic_launcher_music);

                startForeground(1, notificationBuilder.build());

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
                                if (ActivityCompat.checkSelfPermission(ImportService.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                NotificationManagerCompat.from(ImportService.this).notify(1, notificationBuilder.build());
                                Log.d("outputList size", String.valueOf(outputList.size()));

                                // If the size of the outputList is the same as the size of the spotifySongList, we know lyrics extraction is complete.
                                if (outputList.size() == spotifySongsList.size()) {
                                    // Cancel the old notification displaying the import progress and create a new one that is clickable.
                                    NotificationManagerCompat.from(ImportService.this).cancel(1);
                                    // If user clicks on the notification, lead to the new Activity displaying import results.
                                    Intent intent = new Intent(ImportService.this, ActivityImportResult.class);
                                    Log.d("outputList size before putting in intent", String.valueOf(outputList.size()));

                                    // Put each GeniusSong in outputList into intent's bundle;
                                    intent.putExtra("outputList size", outputList.size());
//                                    for (int i = 0; i < outputList.size(); i++) {
//                                        intent.putExtra(String.format("outputList song %d", i + 1), outputList.get(i));
//                                    }

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
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onLyricsSuccess(GeniusSong geniusSong) {

            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}