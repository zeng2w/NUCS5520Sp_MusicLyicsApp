package edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model;

/**
 * Model for an Artist in Spotify.
 */
public class SpotifyArtist {

    private String artistId;
    private String artistName;

    public SpotifyArtist(String artistId, String artistName) {
        this.artistId = artistId;
        this.artistName = artistName;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
