package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityImportBinding;

public class ActivityImport extends AppCompatActivity {

    ActivityImportBinding binding;

    private EditText editTextLink;

    private Button buttonImport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.navBarView.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()){
//                case R.id.navBar_library:
//                    startActivity(new Intent(ActivityImport.this, LibraryPageActivity.class));
//                    break;
//                case R.id.navBar_user:
//                    startActivity(new Intent(ActivityImport.this, UserPageActivity.class));
//                    break;
//            }
//            return true;
//        });
        editTextLink = findViewById(R.id.spotify_list_edittext);

        buttonImport = findViewById(R.id.import_button);

        buttonImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService();
            }
        });
    }

    // Credit: https://youtu.be/FbpD5RZtbCc
    public void startService() {
        String sharedPlaylistLink = editTextLink.getText().toString().trim();

        Intent serviceIntent = new Intent(this, ImportService.class);
        serviceIntent.putExtra("Shared Playlist Link", sharedPlaylistLink);

        startService(serviceIntent);
    }
}