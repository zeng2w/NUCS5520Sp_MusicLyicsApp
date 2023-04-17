package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import static edu.northeastern.nucs5520sp_musiclyicsapp.R.id.local_search_toggle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ToggleButton myToggleButton = findViewById(local_search_toggle);

        myToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the toggle state change here
                if (isChecked) {
                    // The toggle button is on
                } else {
                    // The toggle button is off
                }
            }
        });

        binding.navBarView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navBar_library:
                    startActivity(new Intent(SearchActivity.this, LibraryPageActivity.class));
                    break;
                case R.id.navBar_user:
                    startActivity(new Intent(SearchActivity.this, UserPageActivity.class));
                    break;
            }
            return true;
        });

    }
}