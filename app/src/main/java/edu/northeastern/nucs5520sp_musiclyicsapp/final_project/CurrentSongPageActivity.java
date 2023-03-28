package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



import edu.northeastern.nucs5520sp_musiclyicsapp.R;

/*
Current Song Page: display the current song page which include album image, song title, artist,
lyric editor, text area where contains lyric of the song, button to like, button to comment, button
to report, button to translate, button to edit the lyric, button to add to user database, and lastly
the nav bar to transit to other page
 */

public class CurrentSongPageActivity extends AppCompatActivity {

    ImageView currentSong_albumImage;
    TextView currentSong_songTitle;
    TextView currentSong_artist;
    TextView currentSong_lyricEditor;
    TextView currentSong_textLyric;
    Button currentSong_buttonLike;
    Button currentSong_buttonComment;
    Button currentSong_buttonReport;
    Button currentSong_buttonTranslate;
    Button currentSong_buttonEdit;
    Button currentSong_buttonAddToFav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_song_page);

        currentSong_albumImage = findViewById(R.id.currentSong_albumImage);
        currentSong_songTitle = findViewById(R.id.currentSong_title);
        currentSong_artist = findViewById(R.id.currentSong_artist);
        currentSong_lyricEditor = findViewById(R.id.currentSong_lyricEditor);
        currentSong_textLyric = findViewById(R.id.currentSong_textLyric);
        currentSong_buttonLike = findViewById(R.id.currentSong_buttonLike);
        currentSong_buttonComment = findViewById(R.id.currentSong_buttonComment);
        currentSong_buttonReport = findViewById(R.id.currentSong_buttonReport);
        currentSong_buttonTranslate = findViewById(R.id.currentSong_buttonTranslate);
        currentSong_buttonEdit = findViewById(R.id.currentSong_buttonEdit);
        currentSong_buttonAddToFav = findViewById(R.id.currentSong_buttonAddToFav);

    }
}