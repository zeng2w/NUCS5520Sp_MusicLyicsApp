package edu.northeastern.nucs5520sp_musiclyicsapp.filebaseSetUp;

public class SongModel {
    String songId;
    String song_name;
    String song_artist;
    String song_lyric;
    String lyric_creator;

    public SongModel() {
    }

    public SongModel(String songId, String song_name, String song_artist, String song_lyric, String lyric_creator) {
        this.songId = songId;
        this.song_name = song_name;
        this.song_artist = song_artist;
        this.song_lyric = song_lyric;
        this.lyric_creator = lyric_creator;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getSong_artist() {
        return song_artist;
    }

    public void setSong_artist(String song_artist) {
        this.song_artist = song_artist;
    }

    public String getSong_lyric() {
        return song_lyric;
    }

    public void setSong_lyric(String song_lyric) {
        this.song_lyric = song_lyric;
    }

    public String getLyric_creator() {
        return lyric_creator;
    }

    public void setLyric_creator(String lyric_creator) {
        this.lyric_creator = lyric_creator;
    }
}
