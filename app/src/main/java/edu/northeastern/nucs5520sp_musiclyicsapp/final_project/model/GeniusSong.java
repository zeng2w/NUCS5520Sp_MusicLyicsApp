package edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Model a song created from the response back from Genius API's call.
 */
public class GeniusSong {

    private String songName;
    private String artistsString;
    private String lyrics;

    public GeniusSong(String songName, String artistsString, String lyrics) {
        this.songName = songName;
        this.artistsString = artistsString;
        this.lyrics = lyrics;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistsString() {
        return artistsString;
    }

    public void setArtistsString(String artistsString) {
        this.artistsString = artistsString;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }
}