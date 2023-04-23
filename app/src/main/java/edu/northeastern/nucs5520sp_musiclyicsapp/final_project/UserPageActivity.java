package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import edu.northeastern.nucs5520sp_musiclyicsapp.MainActivity;
import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityUserPageBinding;

/*
User Page: showing the profile of user.
user Image: user could choose their profile image
user Name: user name
user Following: other user that they are following
user Follower: other user that follow them
Account:
Setting
Bug report:

Log out button: to log out the account
Nav Bar: to move to different UI such as current User Page, Current Song Page, Library Page
 */
public class UserPageActivity extends AppCompatActivity {

//    NavigationBarItemView navBarView;

    ActivityUserPageBinding binding;
    DatabaseReference databaseReference;
    StorageReference storageReferenceAvatar;
    DatabaseReference databaseReferenceCommentReplies;
    DatabaseReference databaseReferenceComments;
    String currentUid;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        navBarView = findViewById(R.id.navBarView);

        // Set up the click to link to spotify account.
        binding.userPageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserPageActivity.this, ActivitySpotifyAuth.class));
            }
        });

        //databaseReference = FirebaseDatabase.getInstance().getReference("Final_Project_Users");
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.navBarView.setSelectedItemId(R.id.navBar_user);
        // show current username on User Page
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String current_username = user.getEmail();
            binding.userPageUsername.setText(current_username);
            //Log.d("----username:", current_username);
        }

        // load user avatar
        storageReferenceAvatar = FirebaseStorage.getInstance().getReference("avatar/" + currentUid).child(currentUid);
        storageReferenceAvatar.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String image = uri.toString();
                Picasso.get().load(image).into(binding.userPageUserProfile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.userPageUserProfile.setImageResource(R.drawable.person_image);
            }
        });

        // change avatar button
        binding.floatingActionButtonChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CropImage.activity().setGuidelines(CropImageView.Guidelines.On).start
                ImagePicker.with(UserPageActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        binding.userPageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserPageActivity.this, LogInActivity.class));
                finish();
            }
        });

        // navigation bar on click action
        binding.navBarView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
//                case R.id.navBar_currentSong:
//                    startActivity(new Intent(UserPageActivity.this, CurrentSongPageActivity.class));
//                    break;
                case R.id.navBar_library:
                    startActivity(new Intent(UserPageActivity.this, LibraryPageActivity.class));
                    break;
                case R.id.navBar_currentSong:
                    startActivity(new Intent(UserPageActivity.this, CurrentSongPageActivity.class));
                    break;
            }
            return true;
        });


        // check replies
        // listening the replies of the comments which current user had posted
        databaseReferenceComments = FirebaseDatabase.getInstance().getReference("comments");
        databaseReferenceCommentReplies = FirebaseDatabase.getInstance().getReference("replies");
        databaseReferenceComments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    for(DataSnapshot commentDataSnapshot: dataSnapshot.getChildren()) {
                        Log.d("----------listen comments database", commentDataSnapshot.child("userId").getValue().toString());
                        if (commentDataSnapshot.child("userId").getValue().toString().equals(currentUid)) {
                            Log.d("-----------comment find", "");
                            Log.d("--------commentId", commentDataSnapshot.getKey());
                            databaseReferenceCommentReplies.child(Objects.requireNonNull(commentDataSnapshot.getKey())).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot repliesDataSnapshot: snapshot.getChildren()) {
                                        Log.d("------username", snapshot.getValue().toString());
                                        String username = repliesDataSnapshot.child("username").getValue().toString();
                                        String receiveDateTime = repliesDataSnapshot.child("currentDate").getValue().toString();
                                        DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                        LocalDateTime now = LocalDateTime.now();
                                        LocalDateTime lower = now.minusSeconds(5);
                                        LocalDateTime dateTime = LocalDateTime.parse(receiveDateTime,dft);
                                        if(dateTime.isAfter(lower)) {
                                            sendNotification(username, receiveDateTime);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.navBarView.setSelectedItemId(R.id.navBar_user);
    }

    private void sendNotification(String username, String receiveDate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

//            Bitmap myBitmap = BitmapFactory.decodeResource(getResources(),
//                    Integer.parseInt(imageName));

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n")
                    .setSmallIcon(R.drawable.notification_image).setAutoCancel(true)
                    .setContentText(username + " replied you on " + receiveDate);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(999, builder.build());

//            Notification notification = builder.build();
//            managerCompat.notify(999, builder.build());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("--------request code", String.valueOf(requestCode));
        Log.d("--------result code", String.valueOf(resultCode));
        Log.d("-------Ok code", String.valueOf(Activity.RESULT_OK));

        if(resultCode == Activity.RESULT_OK){
            if(data != null){
                imageUri = data.getData();
                Toast.makeText(this, "Avatar changed", Toast.LENGTH_SHORT).show();
                // save avatar to db
                Picasso.get().load(imageUri).into(binding.userPageUserProfile);
                uploadAvatar();
            }
        } else {
            Toast.makeText(this, "Avatar not Changed", Toast.LENGTH_SHORT).show();
            //binding.userPageUserProfile.setImageResource(R.drawable.person_image);
            storageReferenceAvatar.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String image = uri.toString();
                    Picasso.get().load(image).into(binding.userPageUserProfile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    binding.userPageUserProfile.setImageResource(R.drawable.person_image);
                }
            });

        }

    }

    private void uploadAvatar() {
        if(imageUri != null) {
            storageReferenceAvatar.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //binding.imageButton.setImageURI(null);
                    //Toast.makeText()
                    Log.d("------upload Avatar", "successful");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("------upload Avatar", "fail");

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}