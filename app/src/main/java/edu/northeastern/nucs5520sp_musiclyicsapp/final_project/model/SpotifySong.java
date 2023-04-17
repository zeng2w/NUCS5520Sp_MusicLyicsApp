package edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model;

import java.util.List;

import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SpotifyArtist;

/**
 * Models a single song object.
 * Credit: https://towardsdatascience.com/using-the-spotify-api-with-your-android-application-the-essentials-1a3c1bc36b9e
 */
public class SpotifySong {

    private String songId;
    private String songName;
    private String albumId;
    private String albumName;
    private String albumCoverUrl;
    private List<SpotifyArtist> spotifyArtists;


    public SpotifySong(String songId, String songName, String albumId, String albumName, String albumCoverUrl, List<SpotifyArtist> spotifyArtists) {
        this.songId = songId;
        this.songName = songName;
        this.albumId = albumId;
        this.albumName = albumName;
        this.albumCoverUrl = albumCoverUrl;
        this.spotifyArtists = spotifyArtists;
        this.spotifyArtists = spotifyArtists;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }

    public void setAlbumCoverUrl(String albumCoverUrl) {
        this.albumCoverUrl = albumCoverUrl;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public List<SpotifyArtist> getArtists() {
        return this.spotifyArtists;
    }

    public void setArtists(List<SpotifyArtist> spotifyArtists) {
        this.spotifyArtists = spotifyArtists;
    }
}