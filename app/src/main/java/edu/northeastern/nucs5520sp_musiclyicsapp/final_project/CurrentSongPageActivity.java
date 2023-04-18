package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityCurrentSongPageBinding;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SongModel;

/*
Current Song Page: display the current song page which include album image, song title, artist,
lyric editor, text area where contains lyric of the song, button to like, button to comment, button
to report, button to translate, button to edit the lyric, button to add to user database, and lastly
the nav bar to transit to other page
 */

public class CurrentSongPageActivity extends AppCompatActivity {

    ActivityCurrentSongPageBinding binding;
    String songName, songArtist, lyricCreator, lyric, translation, lyricCreatorId;
    DatabaseReference databaseReferenceUsersLyricsLibrary;
    StorageReference storageReference;
    // this string is assigned as the node key of each song in db library
    String songName_artist_node;
    // check if Add Button is checked
    Boolean isAdded;
    String imageUrl;

    DatabaseReference databaseReferenceSongLikes;
    DatabaseReference databaseReferenceSharedLyrics;
    DatabaseReference databaseReferenceUser;

    Button currentSong_buttonComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCurrentSongPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set the button click jump to comment page
        currentSong_buttonComment = findViewById(R.id.currentSong_buttonComment);

        // get the song's name/artist/creator infor from the library when user choose a song
        Intent intent = getIntent();
        songName = intent.getStringExtra("song_name");
        songArtist= intent.getStringExtra("song_artist");
        lyricCreator = intent.getStringExtra("lyric_creator");

        // this string is assigned as the node key of each song in db library
        songName_artist_node = songName.replaceAll("[^a-zA-Z0-9]", "")+songArtist.replaceAll("[^a-zA-Z0-9]", "");
        databaseReferenceUsersLyricsLibrary = FirebaseDatabase.getInstance().getReference("users_Lyrics_Library").child(FirebaseAuth.getInstance().getUid());
        databaseReferenceSharedLyrics = FirebaseDatabase.getInstance().getReference("shared_Lyrics");

        // edit Intent: when edit a song lyric, it will send all of information to edit page
        Intent editIntent = new Intent(CurrentSongPageActivity.this, CreateEditPageActivity.class);

        Log.d("------song name: ", songName);
        binding.currentSongTitle.setText(songName);
        // auto scrollable
        binding.currentSongTitle.setSelected(true);
        binding.currentSongArtist.setText("Artist: " + songArtist);
        binding.currentSongArtist.setSelected(true);
        binding.currentSongLyricEditor.setText("Lyric Creator" + lyricCreator);
        binding.currentSongLyricEditor.setSelected(true);
        // load image from firebase storage
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String fileName = songName.replaceAll("[^a-zA-Z0-9]", "") + songArtist.replaceAll("[^a-zA-Z0-9]", "") + currentUid;

        storageReference = FirebaseStorage.getInstance().getReference("images/" + currentUid).child(fileName);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageUrl = uri.toString();

                Picasso.get().load(imageUrl).into(binding.currentSongAlbumImage);


            }
        });

        // set navbar
        binding.navBarView.setSelectedItemId(R.id.navBar_currentSong);

        // check if song already in user's library
        // if already in, then the "add" icon will change to "check" icon
        // else, show "add" icon
        // if user enter current page is from library, then need to get song lyrics
        // if user enter this from search page, then lyrics will get from api
        databaseReferenceUsersLyricsLibrary.child(songName_artist_node).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // if exists
                if(snapshot.exists()){
                    Log.d("----- exists in database", "true");
                    changeIconIfAlreadyExists();
                    // get lyric of this song and update UI
                    lyric = snapshot.child("song_lyric").getValue().toString();
                    translation = snapshot.child("song_translation").getValue().toString();
                    //Log.d("-----------lyric:", lyric);
                    binding.currentSongTextLyric.setText(lyric);
                } else {
                    Log.d("----- exists in database", "false");
                    changeToAddIcon();
                    // if not exist in user's library, then will show the lyrics from shared_Lyrics
                    showSharedLyric();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // find lyric Creator id
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("Final_Project_Users");
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (dataSnapshot.child("username").getValue().toString().equals(lyricCreator)){
                        lyricCreatorId = dataSnapshot.getKey().toString();
                        Log.d("-------lyric creator id", lyricCreatorId);
                        saveSongLikeOnDB(dataSnapshot.getKey().toString());
                        //showSharedLyric(lyricCreatorId);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Intent commentIntent = new Intent(CurrentSongPageActivity.this, CommentActivity.class);
        currentSong_buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentIntent.putExtra("song_name",songName);
                commentIntent.putExtra("song_artist", songArtist);
                commentIntent.putExtra("lyricCreator", lyricCreator);
                commentIntent.putExtra("song_lyric", lyric);
                commentIntent.putExtra("song_translation", translation);
                commentIntent.putExtra("image_url", imageUrl);
                commentIntent.putExtra("lyricCreatorId", lyricCreatorId);
                startActivity(commentIntent);
            }
        });

        // edit button in lyric details page will open the Create/Edit page to edit the lyric
        binding.currentSongButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editIntent.putExtra("song_name", songName);
                editIntent.putExtra("song_artist", songArtist);
                editIntent.putExtra("lyricCreator", lyricCreator);
                editIntent.putExtra("song_lyric", lyric);
                editIntent.putExtra("song_translation", translation);
                editIntent.putExtra("image_url", imageUrl);
                startActivity(new Intent(editIntent));
                finish();
            }
        });

        // translation Button
        binding.currentSongButtonTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.currentSongTextLyric.getText().toString().equals(lyric)){
                    binding.currentSongTextLyric.setText(translation);
                    Drawable drawable = ResourcesCompat.getDrawable(
                            getResources(),
                            R.drawable.baseline_translate_24_green,
                            getTheme()
                    );
                    binding.currentSongButtonTranslate.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                } else if (binding.currentSongTextLyric.getText().toString().equals(translation)){
                    binding.currentSongTextLyric.setText(lyric);
                    Drawable drawable = ResourcesCompat.getDrawable(
                            getResources(),
                            R.drawable.translate,
                            getTheme()
                    );
                    binding.currentSongButtonTranslate.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                }

            }
        });

        // add button action
        binding.currentSongButtonAddToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAdded){
                    databaseReferenceUsersLyricsLibrary.child(songName_artist_node).removeValue();
                    Log.d("----button action", "removed from library");
                    changeToAddIcon();
                } else {
                    SongModel song = new SongModel(songName, songArtist,lyric,translation,lyricCreator);
                    //Log.d("-----want added song", songName + " " + songArtist + " " + lyric + " " + translation + " " + lyricCreator);
                    databaseReferenceUsersLyricsLibrary.child(songName_artist_node).setValue(song);
                    changeIconIfAlreadyExists();
                    Log.d("-----button action", "added to library");
                }
            }
        });

        // navigation bar on click action
        binding.navBarView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navBar_library:
                    startActivity(new Intent(CurrentSongPageActivity.this, LibraryPageActivity.class));
                    break;
                case R.id.navBar_user:
                    startActivity(new Intent(CurrentSongPageActivity.this, UserPageActivity.class));
                   break;
            }
            return true;
        });

        // make lyric scrolling
        binding.currentSongTextLyric.setMovementMethod(new ScrollingMovementMethod());
    }

    private void showSharedLyric() {
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (dataSnapshot.child("username").getValue().toString().equals(lyricCreator)){
                        lyricCreatorId = dataSnapshot.getKey().toString();
                        Log.d("-------lyric creator id", lyricCreatorId);
                        //saveSongLikeOnDB(dataSnapshot.getKey().toString());
                        //showSharedLyric(lyricCreatorId);
                        String path = songName.replaceAll("[^a-zA-Z0-9]","")+ songArtist.replaceAll("[^a-zA-Z0-9]","")+dataSnapshot.getKey().toString();
                        databaseReferenceSharedLyrics.child(path).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Log.d("------exist in shared lyric", "Object exists: " + snapshot.getValue());
                                    lyric = snapshot.child("song_lyric").getValue().toString();
                                    translation = snapshot.child("song_translation").getValue().toString();
                                    binding.currentSongTextLyric.setText(lyric);
                                } else {
                                    Log.d("------not exist in shared lyric", "Object not exists");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showUserLibraryLyric() {

    }

    private void saveSongLikeOnDB(String lyricCreatorId) {
        // db like path for song
        String path = songName.replaceAll("[^a-zA-Z0-9]","")+ songArtist.replaceAll("[^a-zA-Z0-9]","")+lyricCreatorId;
        //check if current user liked this song lyric
        databaseReferenceSongLikes = FirebaseDatabase.getInstance().getReference("likes").child(path);
        String currentUserId = FirebaseAuth.getInstance().getUid();

        // check if user already liked this song
        final Boolean[] isLiked = {false};
        databaseReferenceSongLikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(currentUserId)){
                         isLiked[0] = true;
                    }
                }
                if(isLiked[0]){
                    Drawable drawable = ResourcesCompat.getDrawable(
                            getResources(),
                            R.drawable.baseline_thumb_up_24_red,
                            getTheme()

                    );
                    binding.editPageFullScreenButton1.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                } else {
                    Drawable drawable = ResourcesCompat.getDrawable(
                            getResources(),
                            R.drawable.like,
                            getTheme()

                    );
                    binding.editPageFullScreenButton1.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                }

                // when click the like button
                binding.editPageFullScreenButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isLiked[0]){
                            Drawable drawable = ResourcesCompat.getDrawable(
                                    getResources(),
                                    R.drawable.like,
                                    getTheme()

                            );
                            binding.editPageFullScreenButton1.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                            databaseReferenceSongLikes.child(currentUserId).removeValue();
                            isLiked[0] = false;
                        } else {
                            Drawable drawable = ResourcesCompat.getDrawable(
                                    getResources(),
                                    R.drawable.baseline_thumb_up_24_red,
                                    getTheme()

                            );
                            binding.editPageFullScreenButton1.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                            databaseReferenceSongLikes.child(currentUserId).setValue(currentUserId);
                            isLiked[0] = true;
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void changeToAddIcon() {
        // android:drawableEnd="@drawable/addtofav"
        //Button search = (Button) findViewById(R.id.yoursearchbutton);
        //search.setCompoundDrawablesWithIntrinsicBounds('your_drawable',null,null,null);
        Drawable drawable = ResourcesCompat.getDrawable(
                getResources(),
                R.drawable.addtofav,
                getTheme()
        );
        binding.currentSongButtonAddToFav.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
        isAdded = false;
    }

    private void changeIconIfAlreadyExists() {
        // android:drawableEnd="@drawable/baseline_playlist_add_check_24"
        //Button search = (Button) findViewById(R.id.yoursearchbutton);
        //search.setCompoundDrawablesWithIntrinsicBounds('your_drawable',null,null,null);
        Drawable drawable = ResourcesCompat.getDrawable(
                getResources(),
                R.drawable.baseline_check_circle_outline_24,
                getTheme()
        );
        binding.currentSongButtonAddToFav.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
        isAdded = true;
    }
}