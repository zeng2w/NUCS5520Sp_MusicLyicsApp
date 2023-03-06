package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class ReceivedStickerAdapter extends RecyclerView.Adapter<ReceivedStickerAdapter.MyViewHolder> {
    private Context context;
    private List<ImageModel> imageModelList;

    public ReceivedStickerAdapter(Context context){
        this.context = context;
        imageModelList = new ArrayList<>();
    }
    public void add(ImageModel imageModel){
        imageModelList.add(imageModel);
        notifyDataSetChanged();
    }

    public void clear(){
        imageModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReceivedStickerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recieved_stickers_history_row,
                parent, false);
        return new ReceivedStickerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ImageModel imageModel = imageModelList.get(position);

        holder.imageView.setImageResource(Integer.parseInt(imageModel.getImageName()));
        holder.sender.setText("Sender: " + imageModel.getSenderName());
        holder.receiveTime.setText("Receive time: " + imageModel.getReceiveDate());

    }

    @Override
    public int getItemCount() {
        return imageModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView sender;
        private TextView receiveTime;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recievedSticker);
            sender = itemView.findViewById(R.id.senderTextView);
            receiveTime = itemView.findViewById(R.id.sendDateTextView);
        }

    }
}
