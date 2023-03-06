package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityStickersListBinding;

public class StickersListActivity extends AppCompatActivity {
    ActivityStickersListBinding binding;
    private String recieverId;
    private String recieverName;
    private String recieverEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStickersListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        recieverId = intent.getStringExtra("receiverId");
        recieverName = intent.getStringExtra("receiverName");
        recieverEmail = intent.getStringExtra("receiverEmail");

        binding.sticker1.setImageResource(R.drawable.sticker1);
        binding.sticker2.setImageResource(R.drawable.sticker2);
        binding.sticker3.setImageResource(R.drawable.sticker3);
        binding.sticker4.setImageResource(R.drawable.sticker4);
        binding.sticker5.setImageResource(R.drawable.sticker5);
        binding.sticker6.setImageResource(R.drawable.sticker6);
        binding.sticker7.setImageResource(R.drawable.sticker7);
        binding.sticker8.setImageResource(R.drawable.sticker8);
        binding.sticker9.setImageResource(R.drawable.sticker9);
        binding.sticker10.setImageResource(R.drawable.sticker10);
        binding.sticker11.setImageResource(R.drawable.sticker11);
        binding.sticker12.setImageResource(R.drawable.sticker12);

    }


    public void chosen(View view){
        int viewId = view.getId();
        Intent getChosenIntent = new Intent(StickersListActivity.this, SendStickerActivity.class);
        switch (viewId){
            case R.id.sticker1:
                String s = String.valueOf(R.drawable.sticker1);
                getChosenIntent.putExtra("imageSrc", s);
                break;
            case R.id.sticker2:
                getChosenIntent.putExtra("imageSrc", String.valueOf(R.drawable.sticker2));
                break;
            case R.id.sticker3:
                getChosenIntent.putExtra("imageSrc", String.valueOf(R.drawable.sticker3));
                break;
            case R.id.sticker4:
                getChosenIntent.putExtra("imageSrc", String.valueOf(R.drawable.sticker4));
                break;
            case R.id.sticker5:
                getChosenIntent.putExtra("imageSrc", String.valueOf(R.drawable.sticker5));
                break;
            case R.id.sticker6:
                getChosenIntent.putExtra("imageSrc", String.valueOf(R.drawable.sticker6));
                break;
            case R.id.sticker7:
                getChosenIntent.putExtra("imageSrc", String.valueOf(R.drawable.sticker7));
                break;
            case R.id.sticker8:
                getChosenIntent.putExtra("imageSrc", String.valueOf(R.drawable.sticker8));
                break;
            case R.id.sticker9:
                getChosenIntent.putExtra("imageSrc", String.valueOf(R.drawable.sticker9));
                break;
            case R.id.sticker10:
                getChosenIntent.putExtra("imageSrc", String.valueOf(R.drawable.sticker10));
                break;
            case R.id.sticker11:
                getChosenIntent.putExtra("imageSrc", String.valueOf(R.drawable.sticker11));
                break;
            case R.id.sticker12:
                getChosenIntent.putExtra("imageSrc", String.valueOf(R.drawable.sticker12));
                break;

        }
        getChosenIntent.putExtra("userId", recieverId);
        getChosenIntent.putExtra("name", recieverName);
        getChosenIntent.putExtra("email", recieverEmail);

        startActivity(getChosenIntent);
        finish();



    }
}