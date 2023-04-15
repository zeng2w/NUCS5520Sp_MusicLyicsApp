package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationBarItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        navBarView = findViewById(R.id.navBarView);

        //databaseReference = FirebaseDatabase.getInstance().getReference("Final_Project_Users");

        // show current username on User Page
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String current_username = user.getEmail();
            binding.userPageUsername.setText(current_username);
            //Log.d("----username:", current_username);
        }
        // show current username on User Page


        binding.userPageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserPageActivity.this, LogInActivity.class));
                finish();
            }
        });
    }
}