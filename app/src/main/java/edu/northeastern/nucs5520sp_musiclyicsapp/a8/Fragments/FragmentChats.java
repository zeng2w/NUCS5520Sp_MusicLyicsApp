package edu.northeastern.nucs5520sp_musiclyicsapp.a8.Fragments;

import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.Adapter.UsersAdapter;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.Chat;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentChats} factory method to
 * create an instance of this fragment.
 */
public class FragmentChats extends Fragment {

    private RecyclerView recyclerView;

    private UsersAdapter usersAdapter;
    private List<User> usersList;

    FirebaseUser currentUser;
    DatabaseReference reference;

    private List<String> usersStrList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        usersStrList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersStrList.clear();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    String senderUid = dataSnapshot.child("sender uid").getValue(String.class);
                    String receiverUid = dataSnapshot.child("receiver uid").getValue(String.class);
                    String sticker = dataSnapshot.child("sticker").getValue(String.class);
                    Chat chat = new Chat(senderUid, receiverUid, sticker);

                    // For chat's sent by the current user, add the receiver's Uid to usersStrList.
                    if (chat.getSenderUid().equals(currentUser.getUid())) {
                        usersStrList.add(chat.getReceiverUid());
                    }

                    // For chats' received by the current user, add the sender's Uid to usersStrList.
                    if (chat.getReceiverUid().equals(currentUser.getUid())) {
                        usersStrList.add(chat.getSenderUid());
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    /**
     * Read all chats related to the currently logged in user.
     */
    private void readChats() {

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    User user = dataSnapshot.getValue(User.class);
                    String userUid = dataSnapshot.getKey();

                    // Add all users whose Uid is in usersStrList into usersList, but no duplicates.
                    for (String id: usersStrList) {

                        assert userUid != null;
                        // If the user (User object) is among the usersStrList, we want to add it
                        // to usersList.
                        if (userUid.equals(id)) {
                            // Make sure no duplicated users are added to usersList
                            if (usersList.size() != 0) {
                                for (User existingUser: usersList) {
                                    assert user != null;
                                    if (!user.getEmail().equals(existingUser.getEmail())) {
                                        usersList.add(user);
                                    }
                                }
                            }
                            else {
                                usersList.add(user);
                            }
                        }
                    }
                }
                usersAdapter = new UsersAdapter(getContext(), usersList);
                recyclerView.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}