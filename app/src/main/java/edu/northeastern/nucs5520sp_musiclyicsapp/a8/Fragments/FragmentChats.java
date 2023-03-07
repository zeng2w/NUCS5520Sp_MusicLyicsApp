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
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.ChatList;
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

        reference = FirebaseDatabase.getInstance().getReference("ChatList");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersStrList.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    ChatList chat = dataSnapshot.getValue(ChatList.class);

                    assert chat != null;

                    String senderUid = chat.getSenderUid();
                    String receiverUid = chat.getReceiverUid();

                    // If the current user is the sender, and the receiver is not in usersStrList,
                    // add the receiver to usersStrList.
                    if ((senderUid.equals(currentUser.getUid()))
                            && (!usersStrList.contains(receiverUid))) {
                        usersStrList.add(receiverUid);
                    }

                    // If the current user is the receiver, and the sender is not in usersStrList,
                    // add the sender to usersStrList.
                    else if ((receiverUid.equals(currentUser.getUid()))
                            && (!usersStrList.contains(senderUid))) {
                        usersStrList.add(senderUid);
                    }
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    private void chatList() {

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersList.clear();

                // Go through each User object, and add to usersList if Uid is in usersStrList.
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    User user = dataSnapshot.getValue(User.class);
                    String userUid = dataSnapshot.getKey();

                    for (String relatedUserUid: usersStrList) {
                        assert userUid != null;
                        if (userUid.equals(relatedUserUid)) {
                            usersList.add(user);
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