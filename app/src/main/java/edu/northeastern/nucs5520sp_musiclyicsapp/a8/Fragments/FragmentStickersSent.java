package edu.northeastern.nucs5520sp_musiclyicsapp.a8.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.Adapter.StickerCountAdapter;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.StickerCount;

/**
 * A simple Fragment subclass.
 * Use the FragmentStickersSent factory method to
 * create an instance of this fragment.
 */
public class FragmentStickersSent extends Fragment {

    private RecyclerView recyclerView;
    private StickerCountAdapter stickerCountAdapter;
    private List<StickerCount> stickerCountList;

    public FragmentStickersSent() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stickers_sent, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        stickerCountList = new ArrayList<>();

        readStickersCount();

        return view;

    }

    private void readStickersCount() {

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        DatabaseReference stickersReceivedRef = FirebaseDatabase.getInstance().getReference("StickersSent").child(currentUser.getUid());
        stickersReceivedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stickerCountList.clear();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    String sticker = dataSnapshot.getKey();
                    String count = dataSnapshot.getValue(String.class);
                    StickerCount stickerCount = new StickerCount(sticker, count);

                    stickerCountList.add(stickerCount);
                }

                stickerCountList.sort((stickerCount, t1) -> Integer.parseInt(t1.getCount()) - Integer.parseInt(stickerCount.getCount()));

                stickerCountAdapter = new StickerCountAdapter(getContext(), stickerCountList);
                recyclerView.setAdapter(stickerCountAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}