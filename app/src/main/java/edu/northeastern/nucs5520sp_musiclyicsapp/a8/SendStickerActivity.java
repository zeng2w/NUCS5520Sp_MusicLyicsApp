package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivitySendStickerBinding;

public class SendStickerActivity extends AppCompatActivity {
    ActivitySendStickerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySendStickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");

        Intent goHomeIntent = new Intent(SendStickerActivity.this, HomeActivity.class);

        binding.sendToText.setText("Send Sticker To: " + name);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goHomeIntent);
            }
        });

    }
}