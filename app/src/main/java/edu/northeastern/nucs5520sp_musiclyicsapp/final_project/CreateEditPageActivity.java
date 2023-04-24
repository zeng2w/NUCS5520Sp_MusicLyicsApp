package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityCreateEditPageBinding;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SongModel;

/*
this is Create/Edit page which user can create/edit song title, artist, the original lyric, and
the translation of the lyric by themselves
 */
public class CreateEditPageActivity extends AppCompatActivity {

    ActivityCreateEditPageBinding binding;
    Uri imageUploadUri;
    StorageReference storageReference;
    DatabaseReference databaseReferenceUsersLyricsLibrary;
    DatabaseReference databaseReferenceSharedLyrics;
    String currentUserEmail;
    String currentUid;
    String songName, songArtist, lyricCreator, lyric, translation;
    String imageFromIntent;

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


        // this intent for receive the edit action from the lyric detail page
        Intent intent = getIntent();
        songName = intent.getStringExtra("song_name");
        songArtist= intent.getStringExtra("song_artist");
        lyricCreator = intent.getStringExtra("lyricCreator");
        lyric = intent.getStringExtra("song_lyric");
        translation = intent.getStringExtra("song_translation");
        imageFromIntent = intent.getStringExtra("image_url");


        // if intent is not null, that means user need to edit the lyric
        if(songName != null){
            Log.d("----edit action", "true");
            binding.editPageSongName.setText(songName);
            binding.editPageArtist.setText(songArtist);
            binding.editPageEditLyric.setText(lyric);
            binding.editPageEditTranslate.setText(translation);
            Picasso.get().load(imageFromIntent).into(binding.imageButton);
        } else {
            Log.d("----edit action", "false");

        }

        // button camera
        binding.editPageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateEditPageActivity.this, CameraCaptureTextActivity.class));
            }
        });


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
                        // if there is nothing to change, then shared lyrics will not save anything
                        // this avoid different user shared the same lyrics and translation to the same song
                        if(newLyric_Name.equals(songName) && newLyric_Artist.equals(songArtist) && newLyric.equals(lyric)
                            && newLyric_translation.equals(translation)){
                            databaseReferenceUsersLyricsLibrary.child(currentUid)
                                    .child(s).setValue(new_song);
                            Toast.makeText(CreateEditPageActivity.this, "Lyric already shared, and you haven't change anything", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReferenceUsersLyricsLibrary.child(currentUid)
                                    .child(s).setValue(new_song);
                            databaseReferenceSharedLyrics.child(s + currentUid).setValue(new_song);
                            // when save successful, go to library page, then the new added song will show on library page
//                            startActivity(new Intent(CreateEditPageActivity.this, LibraryPageActivity.class));
//                            finish();
                            // back to last page
                            onBackPressed();
                        }

                    } else {
                        databaseReferenceUsersLyricsLibrary.child(currentUid)
                                .child(s).setValue(new_song);
                        // when save successful, go to library page, then the new added song will show on library page
//                        startActivity(new Intent(CreateEditPageActivity.this, LibraryPageActivity.class));
//                        finish();
                        // back to last page
                        onBackPressed();
                    }

                    // upload image to firebase store
                    uploadImage();
                }

            }
        });

        // click image view then edit the image of the music
        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               selectImage();
            }
        });

        // when click 'Cancel' Button, lead user go back to library Page
        binding.editPageButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back to library page
                //startActivity(new Intent(CreateEditPageActivity.this, LibraryPageActivity.class));
                // back to last page
                onBackPressed();
            }
        });
    }

    private void uploadImage() {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
//        Date now = new Date();
        String name = binding.editPageSongName.getText().toString();
        String artist = binding.editPageArtist.getText().toString();
        String fileName = name.replaceAll("[^a-zA-Z0-9]", "") + artist.replaceAll("[^a-zA-Z0-9]", "") + currentUid;
        storageReference = FirebaseStorage.getInstance().getReference("images/" +currentUid).child(fileName);

        if(imageUploadUri != null) {
            storageReference.putFile(imageUploadUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //binding.imageButton.setImageURI(null);
                    //Toast.makeText()
                    Log.d("------upload image", "successful");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("------upload image", "fail");

                }
            });
        }
    }

    private void selectImage() {
        Intent imageIntent = new Intent();
        imageIntent.setType("image/");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imageIntent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && data != null && data.getData() != null){
            imageUploadUri = data.getData();
            binding.imageButton.setImageURI(imageUploadUri);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CreateEditPageActivity.this, LibraryPageActivity.class));
        finish();
    }
}