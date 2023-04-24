package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.GeniusSong;

public interface VolleyCallback {

    void onSuccess(boolean finished);
    void onLyricsSuccess(GeniusSong outputGeniusSong);
}
