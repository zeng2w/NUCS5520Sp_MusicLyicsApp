package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import edu.northeastern.nucs5520sp_musiclyicsapp.MainActivity;
import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.Fragments.FragmentChats;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.Fragments.FragmentUsers;

public class ActivityChatMain extends AppCompatActivity {

    FirebaseUser currentUser;
    DatabaseReference currentUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        // Credit: https://www.youtube.com/watch?v=KB2BIm_m1Os
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get child (i.e. one user) value (e.g., email, username)
        // Credit: https://www.youtube.com/watch?v=KRtLZF-xlAs
        // Credit: https://firebase.google.com/docs/database/android/read-and-write
        currentUserRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentUsernameStr = Objects.requireNonNull(snapshot.child("username").getValue()).toString();
                // Change Action Bar text.
                // Credit to: https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwjoxYb-9ML9AhVEFVkFHRZuAycQFnoECA4QAw&url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DlM-QxcE_mSU&usg=AOvVaw2IOfsOtCDDwDERAjLqZhEQ
                Objects.requireNonNull(getSupportActionBar()).setTitle(currentUsernameStr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Enable the back button in Action Bar.
        // Credit: https://stackoverflow.com/questions/15686555/display-back-button-on-action-bar
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize the Tab Layout and View Pager objects on ActivityChatMain's UI.
        // Credit: https://www.youtube.com/watch?v=KB2BIm_m1Os
        TabLayout tabLayout = findViewById(R.id.tabLayout_chat_main);
        ViewPager viewPager = findViewById(R.id.viewPager_chat_main);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add the fragments to the viewPagerAdapter.
        viewPagerAdapter.addFragment(new FragmentChats(), "Chats");
        viewPagerAdapter.addFragment(new FragmentUsers(), "Users");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        MyNotification notification = new MyNotification(this);
        notification.buildNotification();

    }

    /**
     * Add the menu to the chat UI for logging out.
     * Credit: https://www.youtube.com/watch?v=KB2BIm_m1Os
     * @param menu  the menu for logging out
     * @return  true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_main, menu);
        return true;
    }

    /**
     * Log the user out.
     * @param item  the MenuItem to be clicked
     * @return  true if logged out and false if not logged out
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ActivityChatMain.this, ActivityLogIn.class));
            finish();
            return true;
        }
        // Back arrow button (upper left)
        // Credit: https://stackoverflow.com/questions/15686555/display-back-button-on-action-bar
        else if (itemId == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        else if (itemId == R.id.menu_sticker_history) {
            startActivity(new Intent(ActivityChatMain.this, ActivityStickersHistory.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityChatMain.this, MainActivity.class));
        finish();
    }

    // Credit: https://www.youtube.com/watch?v=KB2BIm_m1Os
    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> fragments;
        private final ArrayList<String> titles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position  fragment's position
         */
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        //

        /**
         * This method may be called by the ViewPager to obtain a title string
         * to describe the specified page. This method may return null
         * indicating no title for this page. The default implementation returns
         * null.
         *
         * @param position The position of the title requested
         * @return A title for the requested page
         */
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}