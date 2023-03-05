package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.Adapter.StickersAdapter;

public class ActivityStickerSelector extends AppCompatActivity implements View.OnClickListener{

    private final int ROW_COUNT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector);

        // Change Action Bar text.
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Select Sticker");

        int[] imgArr = new int[]{R.drawable.sticker1,
                R.drawable.sticker2,
                R.drawable.sticker3,
                R.drawable.sticker4,
                R.drawable.sticker5,
                R.drawable.sticker6,
                R.drawable.sticker7,
                R.drawable.sticker8,
                R.drawable.sticker9,
                R.drawable.sticker10,
                R.drawable.sticker11,
                R.drawable.sticker12,
                R.drawable.sticker13,
                R.drawable.sticker14,
                R.drawable.sticker15,
                R.drawable.sticker16,
                R.drawable.sticker17,
                R.drawable.sticker18,
                R.drawable.sticker19,
                R.drawable.sticker20,
                R.drawable.sticker21,
                R.drawable.sticker22,
                R.drawable.sticker23};

        RecyclerView recyclerView = findViewById(R.id.recyclerView_images);
        // Not the LinearLayoutManager as before!
        recyclerView.setLayoutManager(new GridLayoutManager(this, ROW_COUNT));
        recyclerView.setAdapter(new StickersAdapter(imgArr));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.buttonCancelImageSelector) {
            onBackPressed();
        }
    }
}