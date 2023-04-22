package edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Model a song created from the response back from Genius API's call.
 */
public class GeniusSong implements Parcelable {

    private String songName;
    private ArrayList<String> artistsList;
    private String lyrics;

    public GeniusSong(String songName, ArrayList<String> artistsList,  String lyrics) {
        this.songName = songName;
        this.artistsList = artistsList;
        this.lyrics = lyrics;
    }

    protected GeniusSong(Parcel in) {
        songName = in.readString();
        artistsList = in.createStringArrayList();
        lyrics = in.readString();
    }

    public static final Creator<GeniusSong> CREATOR = new Creator<GeniusSong>() {
        @Override
        public GeniusSong createFromParcel(Parcel in) {
            return new GeniusSong(in);
        }

        @Override
        public GeniusSong[] newArray(int size) {
            return new GeniusSong[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(songName);
        dest.writeStringList(artistsList);
        dest.writeString(lyrics);
    }
}
