package edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model;

import java.util.ArrayList;

/**
 * Model for a song object obtained from Genius.
 */
public class GeniusSong {

    private String songName;
    private ArrayList<String> artistsList;
    private String lyrics;

    public GeniusSong(String songName, ArrayList<String> artistsList, String lyrics) {
        this.songName = songName;
        this.artistsList = artistsList;
        this.lyrics = lyrics;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public ArrayList<String> getArtistsList() {
        return artistsList;
    }

    public void setArtistsList(ArrayList<String> artistsList) {
        this.artistsList = artistsList;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }
}
