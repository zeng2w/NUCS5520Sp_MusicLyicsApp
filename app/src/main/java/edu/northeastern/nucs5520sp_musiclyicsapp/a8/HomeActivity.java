package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    private ArrayList<User> userList = new ArrayList<>();

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    ActivityHomeBinding binding;
    DatabaseReference databaseReference;

    UserAdapter userAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userAdapter = new UserAdapter(this);
        binding.recycler.setAdapter(userAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Log.d("reference", databaseReference.child("BqNb4k4sl0f80C8Ju2WYBqsGTw83").child("username").toString());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAdapter.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String uid = dataSnapshot.getKey();
                    Log.d("child value", dataSnapshot.child("username").getValue().toString());
                    Log.d("current login id: ", FirebaseAuth.getInstance().getUid());
                    if(!uid.equals(FirebaseAuth.getInstance().getUid())){
                        String userId = dataSnapshot.child("userId").getValue().toString();
                        String username = dataSnapshot.child("username").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String password = dataSnapshot.child("password").getValue().toString();
                        User user = new User(userId, username, email, password);
                        userAdapter.notifyDataSetChanged();

                        userAdapter.add(user);
                        userAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, StickItToEm.class));
            finish();
            return true;
        }
        return false;
    }
}