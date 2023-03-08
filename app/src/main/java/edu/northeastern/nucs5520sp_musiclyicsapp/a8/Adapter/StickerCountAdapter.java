package edu.northeastern.nucs5520sp_musiclyicsapp.a8.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.a8.StickerCount;

public class StickerCountAdapter extends RecyclerView.Adapter<StickerCountAdapter.StickerCountViewHolder>{

    private final List<StickerCount> stickerCountList;
    private final Context context;

    public StickerCountAdapter(Context context, List<StickerCount> stickerCountList) {
        this.context = context;
        this.stickerCountList = stickerCountList;
    }


    @NonNull
    @Override
    public StickerCountAdapter.StickerCountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_stickers_row, parent, false);
        return new StickerCountAdapter.StickerCountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerCountAdapter.StickerCountViewHolder holder, int position) {
        @SuppressLint("DiscouragedApi") int resId = context.getResources().getIdentifier(stickerCountList.get(position).getSticker(), "drawable", this.context.getPackageName());
        holder.imageView.setImageResource(resId);
        holder.textView.setText(stickerCountList.get(position).getCount());
    }

    @Override
    public int getItemCount() {
        return stickerCountList.size();
    }

    static class StickerCountViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public StickerCountViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_sticker);
            textView = itemView.findViewById(R.id.textViewCount);
        }
    }
}
