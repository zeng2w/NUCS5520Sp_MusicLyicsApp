package edu.northeastern.nucs5520sp_musiclyicsapp.a8.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class StickersAdapter extends RecyclerView.Adapter<StickersAdapter.StickersViewHolder> {

    private final int[] imgArr;

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

    @Override
    public void onBindViewHolder(@NonNull StickersAdapter.StickersViewHolder holder, int position) {
        holder.imageView.setImageResource(imgArr[position]);
    }

    @Override
    public int getItemCount() {
        return imgArr.length;
    }

    static class StickersViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public StickersViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_row_1);
        }
    }
}
