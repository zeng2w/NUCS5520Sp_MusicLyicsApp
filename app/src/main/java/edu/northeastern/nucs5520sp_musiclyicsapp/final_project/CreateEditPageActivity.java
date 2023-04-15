package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityCreateEditPageBinding;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SongModel;

/*
this is Create/Edit page which user can create/edit song title, artist, the original lyric, and
the translation of the lyric by themselves
 */
public class CreateEditPageActivity extends AppCompatActivity {

    ActivityCreateEditPageBinding binding;
    DatabaseReference databaseReferenceUsersLyricsLibrary;
    DatabaseReference databaseReferenceSharedLyrics;
    String currentUserEmail;
    String currentUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateEditPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUserEmail = user.getEmail();
            currentUid = user.getUid();
        }


        databaseReferenceSharedLyrics = FirebaseDatabase.getInstance().getReference("shared_Lyrics");
        databaseReferenceUsersLyricsLibrary = FirebaseDatabase.getInstance().getReference("users_Lyrics_Library");

        // when click 'Save' button, if user select "Accessible to other users",
        // the created new lyric will save to firebase realtime db --> both shared_Lyrics and users_Lyrics_Library
        // otherwise, just save into db --> users_Lyrics_library
        binding.editPageButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a new lyric
                String newLyric_Name = binding.editPageSongName.getText().toString();
                String newLyric_Artist = binding.editPageArtist.getText().toString();
                String newLyric = binding.editPageEditLyric.getText().toString();
                String newLyric_translation = binding.editPageEditTranslate.getText().toString();
                Log.d("-----new lyrics", newLyric);

                // Log.d("-----song name:", newLyric_Name);
                SongModel new_song = new SongModel(newLyric_Name, newLyric_Artist, newLyric, newLyric_translation, currentUserEmail);
                Log.d(" ----- new lyrics in the new_song object", new_song.getSong_lyric());
                if(TextUtils.isEmpty(newLyric_Name) || TextUtils.isEmpty(newLyric_Artist) || TextUtils.isEmpty(newLyric)){
                    Toast.makeText(CreateEditPageActivity.this, "Input Empty, pleas fill out song Name/Artist/Lyrics", Toast.LENGTH_SHORT).show();
                } else{
                    // else will save the lyrics to db
                    // if user select 'Accessible to other user', it will save into db --> both shared_Lyrics and users_Lyrics_Library
                    String s = newLyric_Name.replaceAll("[^a-zA-Z0-9]", "") + newLyric_Artist.replaceAll("[^a-zA-Z0-9]", "");
                    if (binding.editPageCheckBox.isChecked()){
                        databaseReferenceUsersLyricsLibrary.child(currentUid)
                                .child(s).setValue(new_song);
                        databaseReferenceSharedLyrics.child(newLyric_Name+ newLyric_Artist + currentUid).setValue(new_song);
                    } else {
                        databaseReferenceUsersLyricsLibrary.child(currentUid)
                                .child(s).setValue(new_song);
                    }
                    // when save successful, go to library page, then the new added song will show on library page
                    startActivity(new Intent(CreateEditPageActivity.this, LibraryPageActivity.class));
                    finish();
                    //Toast.makeText(CreateEditPageActivity.this, "Saved Successful!", Toast.LENGTH_SHORT).show();

                }

            }
        });

        // when click 'Cancel' Button, lead user go back to library Page
        binding.editPageButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back to library page
                startActivity(new Intent(CreateEditPageActivity.this, LibraryPageActivity.class));
            }
        });
    }
}