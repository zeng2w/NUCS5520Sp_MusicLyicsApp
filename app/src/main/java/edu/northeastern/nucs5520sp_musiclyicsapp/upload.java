package edu.northeastern.nucs5520sp_musiclyicsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class upload extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button choose;
    private Button upload;
    private ImageView image;
    private ProgressBar bar;
    private TextView history;
    private Uri imageUri;
    private int count = 0;
    private StorageReference myStoRef;
    private DatabaseReference mydbRef;
    private StorageTask myTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        choose = findViewById(R.id.buttonChoose);
        upload = findViewById(R.id.buttonUpload);
        image = findViewById(R.id.imageUpload);
        bar = findViewById(R.id.progressBar);
        history = findViewById(R.id.textHistory);

        FirebaseApp.initializeApp(this);
        myStoRef = FirebaseStorage.getInstance().getReference("Uploads");
        mydbRef = FirebaseDatabase.getInstance().getReference("Uploads");
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myTask != null && myTask.isInProgress())
                {
                    Toast.makeText(upload.this, "Uploading", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadimages();
                }
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHistory();
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData() != null)
        {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(image);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void uploadimages() {
        if(imageUri != null)
        {
            count ++;
            StorageReference fileRef = myStoRef.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

            myTask = fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bar.setProgress(0);
                        }
                    },5000);

                    Toast.makeText(upload.this,"Upload Sucessful",Toast.LENGTH_SHORT).show();
                    Images image = new Images(taskSnapshot.getUploadSessionUri().toString(),count);
                    String uploadId = mydbRef.push().getKey();
                    mydbRef.child(uploadId).setValue(image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(upload.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                    bar.setProgress((int)progress);
                }
            });
        }
        else{
            Toast.makeText(this, "No image uploaded", Toast.LENGTH_SHORT).show();
        }
    }

    private void openHistory() {
        Intent intent = new Intent(this,History.class);
        startActivity(intent);
    }
}