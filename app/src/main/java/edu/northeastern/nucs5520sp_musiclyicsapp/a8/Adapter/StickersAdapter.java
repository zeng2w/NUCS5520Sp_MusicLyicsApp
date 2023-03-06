package edu.northeastern.nucs5520sp_musiclyicsapp.a8.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class StickersAdapter extends RecyclerView.Adapter<StickersAdapter.StickersViewHolder> {

    private final int[] imgArr;
    int selectedPos = -1;

    public StickersAdapter(int[] imgArr) {
        this.imgArr = imgArr;
    }


    @NonNull
    @Override
    // Credit: https://www.youtube.com/watch?v=PDz-fXL7q9A
    public StickersAdapter.StickersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_image_selector_row, parent, false);
        return new StickersAdapter.StickersViewHolder(view);
    }

    // Change color of the selected item.
    // Credit: https://youtu.be/G3L5M14S3Yc
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull StickersAdapter.StickersViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imageView.setImageResource(imgArr[position]);

        holder.imageView.setOnClickListener(view -> {
            selectedPos = position;
            notifyDataSetChanged();
        });

        // Change the colors of the selected and unselected items in RecyclerView.
        if (selectedPos == position) {
            holder.imageView.setBackgroundColor(ContextCompat.getColor(holder.imageView.getContext(), R.color.purple_200));
        }
        else {
            holder.imageView.setBackgroundColor(Color.WHITE);
        }
    }

    public int getSelectedPos() {
        return this.selectedPos;
    }

    @Override
    public int getItemCount() {
        return imgArr.length;
    }

    // Credit: https://www.youtube.com/watch?v=7GPUpvcU1FE
    static class StickersViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public StickersViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_row_1);

        }
    }
}
