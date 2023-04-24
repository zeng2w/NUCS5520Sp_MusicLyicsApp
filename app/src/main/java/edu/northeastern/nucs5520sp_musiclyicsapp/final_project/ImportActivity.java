package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import static com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityImportBinding;

public class ImportActivity extends AppCompatActivity {

    ActivityImportBinding binding;

    private EditText editTextLink;

    private Button buttonImport;

    private static final int PERMISSION_REQUEST_CODE = 1;
    private boolean shouldShowRequestPermissionRationale = false;

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

        // Check if app can push notification. if permission not granted
        if (ActivityCompat.checkSelfPermission(ImportActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Show user why we need the notification permission to run Import Service.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                // Show explanation.
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("App must send notification for import to work, post progress and notify when user can add imported songs to library.")
                        .setTitle("Permission Required");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Request the permission again.
                        shouldShowRequestPermissionRationale = true;
                        ActivityCompat.requestPermissions(ImportActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS},
                                PERMISSION_REQUEST_CODE);
                    }
                });
                builder.create().show();
            }
            else {
                // Request the permission directly
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        }
        // Permission already granted.
        else {
            buttonImport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    startService();
                    ImportActivityRunnable runnable = new ImportActivityRunnable();
                    Thread t = new Thread(runnable);
                    t.start();
                }
            });
        }
    }

    // Credit: https://youtu.be/FbpD5RZtbCc
    public void startService() {
        String sharedPlaylistLink = editTextLink.getText().toString().trim();

        Intent serviceIntent = new Intent(this, ImportService.class);
        serviceIntent.putExtra("Shared Playlist Link", sharedPlaylistLink);

        startService(serviceIntent);
//        startActivity(new Intent(this, LibraryPageActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with using the feature that requires the permission
                buttonImport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        startService();
                        ImportActivityRunnable runnable = new ImportActivityRunnable();
                        Thread t = new Thread(runnable);
                        t.start();
                    }

                });
            } else {
                // Permission denied, notify the user and/or disable the feature that requires the permission
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    class ImportActivityRunnable implements Runnable {

        @Override
        public void run() {
            startMyService();
        }

        // Credit: https://youtu.be/FbpD5RZtbCc
        public void startMyService() {
            String sharedPlaylistLink = editTextLink.getText().toString().trim();

            Intent serviceIntent = new Intent(ImportActivity.this, ImportService.class);
            serviceIntent.putExtra("Shared Playlist Link", sharedPlaylistLink);

            startService(serviceIntent);
//        startActivity(new Intent(this, LibraryPageActivity.class));
        }
    }
}