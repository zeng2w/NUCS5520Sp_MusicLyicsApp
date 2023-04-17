package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityCameraCaptureTextBinding;

public class CameraCaptureTextActivity extends AppCompatActivity {

    ActivityCameraCaptureTextBinding binding;
    private static final int REQUEST_CAMERA_CODE = 100;
    Uri imageUri;
    TextRecognizer textRecognizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraCaptureTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        //Reference: https://www.youtube.com/watch?v=sjkDbxyoNW0

        if(ContextCompat.checkSelfPermission(CameraCaptureTextActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CameraCaptureTextActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        binding.buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CropImage.activity().setGuidelines(CropImageView.Guidelines.On).start
                ImagePicker.with(CameraCaptureTextActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        binding.buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = binding.textViewData.getText().toString();
                if(text.isEmpty()){
                    Toast.makeText(CameraCaptureTextActivity.this, "there is no text to copy", Toast.LENGTH_SHORT).show();
                } else {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CameraCaptureTextActivity.this.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Data", binding.textViewData.getText().toString());
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(CameraCaptureTextActivity.this, "Text copy to Clioboard", Toast.LENGTH_SHORT).show();
                    Intent editIntent = new Intent(CameraCaptureTextActivity.this, CreateEditPageActivity.class);
                    editIntent.putExtra("song_lyric", binding.textViewData.getText().toString());
                    startActivity(editIntent);
                    finish();
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("--------request code", String.valueOf(requestCode));
        Log.d("--------result code", String.valueOf(resultCode));
        Log.d("-------Ok code", String.valueOf(Activity.RESULT_OK));

        if(resultCode == Activity.RESULT_OK){
            if(data != null){
                imageUri = data.getData();
                Toast.makeText(this, "image selected", Toast.LENGTH_SHORT).show();
                recognizeText();
            }
        } else {
            Toast.makeText(this, "image not selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void recognizeText() {

        if(imageUri != null){
            Log.d("-------uri image", String.valueOf(imageUri));
            try {
                InputImage inputImage = InputImage.fromFilePath(CameraCaptureTextActivity.this, imageUri);

                Task<Text> result = textRecognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text text) {
                        String recognizeText = text.getText();
                        binding.textViewData.setText(recognizeText);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CameraCaptureTextActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("--------recog error", e.getMessage());
                    }
                });

            } catch (IOException exception){
                exception.printStackTrace();
            }
        }


    }
}