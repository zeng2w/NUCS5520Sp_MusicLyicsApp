package edu.northeastern.nucs5520sp_musiclyicsapp.filebaseSetUp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    String username, email, password;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    // sign up with email and password
    private void signUp() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        firebaseUser.updateProfile(userProfileChangeRequest);
                        UserModel user =new UserModel(FirebaseAuth.getInstance().getUid(), username, email, password);
                        databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(user);
                        Log.d("------Sign up", "Successful");
                        //Toast.makeText(StickItToEm.this, "SignUp successful!", Toast.LENGTH_SHORT).show();

                        //startActivity(new Intent(StickItToEm.this, HomeActivity.class));
                        //finish();
                    }
                }).addOnFailureListener(er -> {
                    Log.d("------Sign up", "Error");
                    //Toast.makeText(StickItToEm.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();

                });
    }

    // log in with email and password
    private void login() {
        Log.d("email", email);
        Log.d("password", password);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.trim(), password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                firebaseUser.updateProfile(userProfileChangeRequest);
                Log.d("------login", "Successful");
//                Toast.makeText(StickItToEm.this, "Login successful!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(StickItToEm.this, HomeActivity.class));
//                finish();
            }
        }).addOnFailureListener(er -> {
            Log.d("------login", er.getMessage());
            //Toast.makeText(StickItToEm.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();

        });
    }
    // when activity start, check if user already login
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            // if user already login
            //reload();
        }
    }

    // after user login, when user click "follow" to another user
    // in firebase realtime db, will update instances: followingsList and followersList
    private void followUser(){
        DatabaseReference databaseReferenceFollowingsList;
        DatabaseReference databaseReferenceFollowersList;
        // current user follow another user
        // so current user as follower
        // another as following user
        String currentUserId = FirebaseAuth.getInstance().getUid();
        String followerUserId = currentUserId;
        // set following userid "1234567" by test
        String followingUserId = "1234567";

//        databaseReferenceSendImages.child(imageId).setValue(imageModel);
//        databaseReferenceReceiveImages.child(imageId).setValue(imageModel);

        databaseReferenceFollowingsList = FirebaseDatabase.getInstance().getReference("followingsList").child(followerUserId);
        databaseReferenceFollowersList = FirebaseDatabase.getInstance().getReference("followersList").child(followingUserId);
        databaseReferenceFollowingsList.child(followerUserId).child(followingUserId);
        databaseReferenceFollowersList.child(followingUserId).child(followerUserId);
        Log.d("------follow:", "Successful");

    }

    // in firebase realtime db, every user has their songLibrary
    private void addSongToLibrary(){
        String currentUserId = FirebaseAuth.getInstance().getUid();
        // create a new lyric, there should generate a new songID,
        // edit the lyric, the songId should same as before
        String songId = UUID.randomUUID().toString();
        String songName = "";
        String song_artist = "";
        String song_lyric = "";
        String lyric_creator = " external api"; //if song import api, creator set as external api, otherwise, set creator as username
        // above information, if import the song library from api, these song should store into db instance
        // call api to get song's name, artist, and lyric
        SongModel song = new SongModel(songId, songName, song_artist, song_lyric, lyric_creator);

        // TODO: IF song already exist with same name,artist and creator by api
        // if lyric is created by external api, then
        // the name, and artist are exist in db, do not store this song into realtime db,
        // else store it
        DatabaseReference databaseReferenceSongLocal = FirebaseDatabase.getInstance().getReference("songs");
        databaseReferenceSongLocal.child(songId).setValue(song);

        // sign these song to current user's song library
        DatabaseReference databaseReferenceSongLibrary = FirebaseDatabase.getInstance().getReference("songLibrary").child(currentUserId);
        databaseReferenceSongLibrary.child(songId).setValue(song);

    }

    // save translations to song
    private void songTranslation(){
        String translationId = UUID.randomUUID().toString();
        String songId = "";
        String language = "";
        String creator = "";
        TranslationModel translationModel = new TranslationModel(songId, translationId, language, creator);
        DatabaseReference databaseReferenceTranslation = FirebaseDatabase.getInstance().getReference("translation");
        databaseReferenceTranslation.child(songId).child(translationId).setValue(translationModel);


    }

    // write comments to song with specific lyric
    private void commentsList(){
        String commentId = UUID.randomUUID().toString();
        // the song which user wants to comment
        String songId = " ";
        String commentDate = " ";
        String userId = "";
        DateTimeFormatter dtf = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        }
        LocalDateTime now = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            commentDate = dtf.format(now);
        }
        CommentModel commentModel = new CommentModel(commentId, songId, commentDate, userId );
        DatabaseReference databaseReferenceComments = FirebaseDatabase.getInstance().getReference("comments");
        databaseReferenceComments.child(songId).child(commentId).setValue(commentModel);
    }

    // the replies of comments
    private void replyList(){
        String replyId = UUID.randomUUID().toString();
        // the comment which user wants to reply
        String commentID = "";
        String commentDate = " ";
        String userId = "";
        DateTimeFormatter dtf = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        }
        LocalDateTime now = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            commentDate = dtf.format(now);
        }
        DatabaseReference databaseReferenceReplies = FirebaseDatabase.getInstance().getReference("replies");
        CommentModel reply = new CommentModel(replyId, commentID, commentDate, userId);
        databaseReferenceReplies.child(commentID).child(replyId).setValue(reply);


    }

    // when user like a song
    private void songLike(){
        DatabaseReference databaseReferenceLikes = FirebaseDatabase.getInstance().getReference("likes");
        // the song which user like
        String songId = "";
        String currentUserId = FirebaseAuth.getInstance().getUid();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReferenceLikes.child(songId).child(currentUserId).setValue(currentUser);
    }

    // when user dislike a song
    private void songDislike(){
        DatabaseReference databaseReferenceLikes = FirebaseDatabase.getInstance().getReference("dislikes");
        // the song which user like
        String songId = "";
        String currentUserId = FirebaseAuth.getInstance().getUid();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReferenceLikes.child(songId).child(currentUserId).setValue(currentUser);
    }

}