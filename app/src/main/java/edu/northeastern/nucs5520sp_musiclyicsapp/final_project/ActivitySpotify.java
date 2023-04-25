package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.GeniusSong;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SpotifyArtist;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SpotifySong;

public class ActivitySpotify extends AppCompatActivity {

    private ArrayList<SpotifySong> spotifySongsList;
    private SpotifyService spotifyService;
    private LyricsService lyricsService;
    private ArrayList<GeniusSong> outputList;

    // Credit: https://towardsdatascience.com/using-the-spotify-api-with-your-android-application-the-essentials-1a3c1bc36b9e
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);

        spotifyService = new SpotifyService(getApplicationContext());
        lyricsService = new LyricsService(getApplicationContext());

        outputList = new ArrayList<>();

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        // If sharedPreferences is empty, user hasn't authorized this app to access their spotify account.
        // Go to ActivitySpotifyAuth.
        if (sharedPreferences.getAll().isEmpty()) {
            Toast.makeText(this, "Need authorization with your Spotify account to read your playlist information", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ActivitySpotify.this, ActivitySpotifyAuth.class));
        }

        getTracks();
    }

    // Whatever needs to be done with the songs obtained need to happen right below
    // songsList = songService.getSongs(); (because of asynchronous nature of the request)
    private void getTracks() {
        spotifyService.getSongsFromPlaylist("token placeholder","3IKJxSf21hxk8C3MbHIlQC", 100, 0, new VolleyCallback() {
            @Override
            public void onSuccess(boolean finished) {
                spotifySongsList = spotifyService.getSongs();
                // Songs in songsList can be used as input to the Genius API.
                if (finished) {
                    // Loop through the songs in songsList and extract lyrics for each song.
                    for (SpotifySong spotifySong: spotifySongsList) {
                        // Song info setup.
                        String songName = spotifySong.getSongName();
                        ArrayList<String> artistsList = new ArrayList<>();
                        for (SpotifyArtist spotifyArtist: spotifySong.getArtists()) {
                            artistsList.add(spotifyArtist.getArtistName());
                        }
                        // Get the GeniusSong with lyrics inside.
                        lyricsService.getLyricsForSong(songName, artistsList, new VolleyCallback() {
                            @Override
                            public void onSuccess(boolean finished) {

                            }

                            @Override
                            public void onLyricsSuccess(GeniusSong outputGeniusSong) {
                                Log.d("output received in ActivitySpotify's callback", outputGeniusSong.getLyrics());
                                outputList.add(outputGeniusSong);
                                Log.d("outputList size", String.valueOf(outputList.size()));

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
}