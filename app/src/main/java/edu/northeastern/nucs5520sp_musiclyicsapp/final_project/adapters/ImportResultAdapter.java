package edu.northeastern.nucs5520sp_musiclyicsapp.final_project.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.GeniusSong;

public class ImportResultAdapter extends RecyclerView.Adapter<ImportResultAdapter.ImportResultViewHolder> {

    private final Context context;
    private final ArrayList<GeniusSong> outputList;

    public ImportResultAdapter(Context context, ArrayList<GeniusSong> outputList) {
        this.context = context;
        this.outputList = outputList;
    }

    @NonNull
    @Override
    public ImportResultAdapter.ImportResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.library_card_design, parent, false);
        return new ImportResultAdapter.ImportResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImportResultAdapter.ImportResultViewHolder holder, int position) {
        holder.tvSongName.setText(outputList.get(position).getSongName());
        holder.tvArtist.setText(outputList.get(position).getArtistsString());
        if (outputList.get(position).getLyrics().equals("")) {
            holder.tvLyricsEditor.setText("LYRICS NOT FOUND");
            holder.tvLyricsEditor.setTextColor(Color.RED);
        }
        else {
            holder.tvLyricsEditor.setText("Genius");
        }

    }

    @Override
    public int getItemCount() {
        return outputList.size();
    }

    public static class ImportResultViewHolder extends RecyclerView.ViewHolder {

        TextView tvSongName, tvArtist, tvLyricsEditor;

        public ImportResultViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSongName = itemView.findViewById(R.id.library_songTitle);
            tvArtist = itemView.findViewById(R.id.library_artist);
            tvLyricsEditor = itemView.findViewById(R.id.library_lyricEditor);


        }
    }
}
