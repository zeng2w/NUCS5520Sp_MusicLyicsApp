package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class SendDetailsAdapter extends RecyclerView.Adapter<SendDetailsAdapter.MyViewHolder> {
    private Context context;
    private List<ImageModel> imageModelList;
    private int sendTimes = 0;
    private Map<String, Integer> image_sendTimes_map = new HashMap<>();

    public SendDetailsAdapter(Context context){
        this.context = context;
        imageModelList = new ArrayList<>();
    }
    public void add(ImageModel imageModel){
        if(image_sendTimes_map.containsKey(imageModel.getImageName())){
            image_sendTimes_map.put(imageModel.getImageName(), image_sendTimes_map.get(imageModel.getImageName())+1);
        } else{
            image_sendTimes_map.put(imageModel.getImageName(), 1);
            imageModelList.add(imageModel);
            notifyDataSetChanged();
        }


    }

    public void clear(){
        imageModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_stickers_details_row,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ImageModel imageModel = imageModelList.get(position);
        holder.imageView.setImageResource(Integer.parseInt(imageModel.getImageName()));
        holder.times.setText(String.valueOf(image_sendTimes_map.get(imageModel.getImageName())));

    }

    @Override
    public int getItemCount() {
        return imageModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView times;
        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            times = itemView.findViewById(R.id.sendTimesTextView);
            imageView = itemView.findViewById(R.id.sentImageView);
        }

    }
}
