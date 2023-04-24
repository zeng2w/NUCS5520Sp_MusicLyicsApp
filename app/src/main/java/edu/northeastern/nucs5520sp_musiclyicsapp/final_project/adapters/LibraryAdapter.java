package edu.northeastern.nucs5520sp_musiclyicsapp.final_project.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.CurrentSongPageActivity;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SongModel;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.MyViewHolder> {
    private Context context;
    private List<SongModel> songList;

    public LibraryAdapter(Context context){
        this.context = context;
        songList = new ArrayList<>();
    }

    public void add(SongModel song){
        songList.add(song);
        notifyDataSetChanged();
    }

    public void clear(){
        songList.clear();
        notifyDataSetChanged();
    }

    public int getSize(){
        return songList.size();
    }

    public List<SongModel> getSongList(){
        return songList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_card_design, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SongModel song = songList.get(position);
        holder.song_name.setText(song.getSong_name());
        holder.song_artist.setText("Artist: " + song.getSong_artist());
        holder.lyric_creator.setText("Lyric Creator: " + song.getLyric_creator());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CurrentSongPageActivity.class);
                SharedPreferences currentSongPreference = LibraryAdapter.this.context.getSharedPreferences("CURRENT_SONG", 0);
                SharedPreferences.Editor editor = currentSongPreference.edit();
                editor.putString("song_name", song.getSong_name());
                editor.putString("song_artist", song.getSong_artist());
                editor.putString("lyric_creator", song.getLyric_creator());
                editor.putString("song_translation", song.getSong_translation());
                editor.putString("lyric", song.getSong_lyric());
                editor.apply();
                context.startActivities(new Intent[]{intent});
            }
        });



    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView song_name;
        private TextView song_artist;
        private TextView lyric_creator;

        //private Button sendStickerButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            song_name = itemView.findViewById(R.id.library_songTitle);
            song_artist = itemView.findViewById(R.id.library_artist);
            lyric_creator = itemView.findViewById(R.id.library_lyricEditor);
        }

    }
}
