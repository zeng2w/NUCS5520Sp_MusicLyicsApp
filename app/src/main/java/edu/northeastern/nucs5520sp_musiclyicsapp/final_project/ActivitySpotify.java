package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import java.util.ArrayList;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class ActivitySpotify extends AppCompatActivity {

    private ArrayList<SpotifySong> songsList;
    private SongService songService;

    // Credit: https://towardsdatascience.com/using-the-spotify-api-with-your-android-application-the-essentials-1a3c1bc36b9e
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);

        songService = new SongService(getApplicationContext());

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);

        // If sharedPreferences doesn't have anything, then user authorization is needed.
        if (sharedPreferences.getAll().isEmpty()) {
            startActivity(new Intent(ActivitySpotify.this, ActivitySpotifyAuth.class));
        }

        getTracks();
    }

    // Whatever needs to be done with the songs obtained need to happen right below
    // songsList = songService.getSongs(); (because of asynchronous nature of the request)
    private void getTracks() {
        songService.getSongsFromPlaylist("3gKA8ik8CVKljJsbIrem0b", 100, 0, new VolleyCallback() {
            @Override
            public void onSuccess(boolean finished) {
                songsList = songService.getSongs();
                // Songs in songsList can be used as input to the Genius API.
                if (finished) {
                    // These commented-out lines are used to test if songsList has all elements we want.

//                    System.out.println("There are " + songsList.size() + " songs in the test playlist");
//                    System.out.println("songList size is: " + songsList.size());
//                    int count = 1;
//                    for (Song song: songsList) {
//                        System.out.println(count + " ==== " + "Song Title: " + song.getSongName());
//                        count += 1;
//                    }

                }
            }
        });
    }


}