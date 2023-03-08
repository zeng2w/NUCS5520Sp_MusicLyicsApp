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
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.Fragments.FragmentStickersSent;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.Fragments.FragmentStickersReceived;

public class ActivityStickersHistory extends AppCompatActivity {

    FirebaseUser currentUser;
    DatabaseReference stickersSentRef;
    DatabaseReference stickersReceivedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickers_history);

        // Change text in Action Bar.
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Sticker History");
        // Enable back button in Action Bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        stickersSentRef = FirebaseDatabase.getInstance().getReference("StickersSent").child(currentUser.getUid());
        stickersReceivedRef = FirebaseDatabase.getInstance().getReference("StickersReceived").child(currentUser.getUid());

        // Initialize the Tab Layout and View Pager objects on ActivityChatMain's UI.
        // Credit: https://www.youtube.com/watch?v=KB2BIm_m1Os
        TabLayout tabLayout = findViewById(R.id.tabLayout_stickers_history);
        ViewPager viewPager = findViewById(R.id.viewPager_stickers_history);
        ActivityStickersHistory.ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add the fragments to the viewPagerAdapter.
        viewPagerAdapter.addFragment(new FragmentStickersSent(), "Stickers Sent");
        viewPagerAdapter.addFragment(new FragmentStickersReceived(), "Stickers Received");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Return to the tab before entering the specific chat page.
     * @param item  the MenuItem to be clicked
     * @return  true if the item is the back button in action bar; otherwise call the super method
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        // Modify the back button in Action Bar to behave the same as back navigation button.
        // Credit: https://stackoverflow.com/questions/14437745/how-to-override-action-bar-back-button-in-android
        if (itemId == android.R.id.home) {
            // Changed
            startActivity(new Intent(ActivityStickersHistory.this, ActivityChatMain.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityStickersHistory.this, ActivityChatMain.class));
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