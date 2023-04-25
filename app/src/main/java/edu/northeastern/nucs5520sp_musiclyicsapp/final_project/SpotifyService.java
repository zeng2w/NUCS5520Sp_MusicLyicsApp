package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import static edu.northeastern.nucs5520sp_musiclyicsapp.final_project.App.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SpotifyArtist;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SpotifySong;

// Credit: https://towardsdatascience.com/using-the-spotify-api-with-your-android-application-the-essentials-1a3c1bc36b9e
public class SpotifyService {

    private final ArrayList<SpotifySong> songs;
    // See it as a persistent storage of info related to Spotify, e.g., the access token when user
    // logged into Spotify and granted our app permissions.
    // For making requests to the Spotify API.
    private final RequestQueue queue;
    private Context context;

    public SpotifyService(Context context) {
        songs = new ArrayList<>();
        queue = Volley.newRequestQueue(context);
        this.context = context;

    }

    public ArrayList<SpotifySong> getSongs() {
        return songs;
    }

    /**
     * Extract songs from a playlist link from Spotify using Spotify web API.
     * @param playlistId  the id of the playlist
     * @param callback  the VolleyCallback object.
     */
    public void getSongsFromPlaylist(String spotifyToken, String playlistId, int limit, int offset, final VolleyCallback callback) {

        @SuppressLint("DefaultLocale")String endpoint = String.format("https://api.spotify.com/v1/playlists/%s/tracks?offset=%d&limit=%d", playlistId, offset, limit);



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    JSONArray tracksJsonArray = response.optJSONArray("items");
                    assert tracksJsonArray != null;
                    // Number of songs in the playlist. Used for stopping the recursion later in
                    // the method.
                    for (int i = 0; i < tracksJsonArray.length(); i++) {
                        // Extract relevant information about the track.
                        // Followed this to understand the JSON returned from API call: https://developer.spotify.com/documentation/web-api/reference/get-playlists-tracks
                        JSONObject track = tracksJsonArray.optJSONObject(i).optJSONObject("track");

                        assert track != null;
                        JSONObject album = track.optJSONObject("album");
                        JSONArray artistsJSONArray = track.optJSONArray("artists");
                        String songId = track.optString("id");
                        String songName = track.optString("name");
                        assert album != null;
                        String albumId = album.optString("id");
                        String albumName = album.optString("name");
                        // There are three objects inside the "images" JSONArray for an album, but they have the same url, just different sizes.
                        String albumCoverUrl = null;
                        // There may not be an album cover.
                        JSONObject albumCover = album.optJSONArray("images").optJSONObject(0);
                        if (albumCover != null) {
                            albumCoverUrl = albumCover.optString("url");
                        }
                        // Add all artists to an ArrayList.
                        List<SpotifyArtist> artistsList = new ArrayList<>();
                        assert artistsJSONArray != null;
                        for (int j = 0; j < artistsJSONArray.length(); j++) {
                            JSONObject artistsJSONObject = artistsJSONArray.optJSONObject(j);
                            String artistId = artistsJSONObject.optString("id");
                            String artistName = artistsJSONObject.optString("name");
                            SpotifyArtist spotifyArtist = new SpotifyArtist(artistId, artistName);
                            artistsList.add(spotifyArtist);
                        }
                        // Create the Song object with song info, album info, and artists info. Add to list.
                        SpotifySong song = new SpotifySong(songId, songName, albumId, albumName, albumCoverUrl, artistsList);
                        songs.add(song);
                    }

                    String next = response.optString("next");
                    // NOTICE: that if next key in the response is null, it will equal to "null",
                    // instead of "" by calling optString.
//                    System.out.println("Next string is: " + next);
//                    System.out.println("Next string true false is: " + !next.equals("null"));
                    // The second if condition is to stop the recursion; if missing, we will always
                    // be stuck at the last recursive call.
                    if (!next.equals("null")) {
                        // Create a handler and post a delayed callback after 1 second
                        new Handler().postDelayed(() -> {
                            getSongsFromPlaylist(spotifyToken, playlistId, limit, offset + limit, callback);
                            callback.onSuccess(false);
                        }, 1000);
                    }
                    else {
                        callback.onSuccess(true);
                    }

                }, error -> {
                    Log.e("ERROR", "Error when parsing song info from Spotify API");
                    if (error instanceof ServerError && error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        if (statusCode == 429) {
                            Log.e("ERROR", "Too many requests! status code 429");
                            // Read the header for response 429
                            Map<String, String> headers = error.networkResponse.headers;
                            String retryAfterHeader = headers.get("Retry-After");
                            if (retryAfterHeader != null) {
                                int retryAfterSeconds = Integer.parseInt(retryAfterHeader);
                                Log.e("ERROR", "Need to retry after seconds: " + retryAfterSeconds);
                                System.out.println("Need to retry after seconds: " + retryAfterSeconds);
                            }
                        }
                        else if (statusCode == 401) {
                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                                    .setContentTitle("Need Spotify Authorization")
                                    .setContentText("Please go to Users page to authorize your Spotify account with our app")
                                    .setSmallIcon(R.mipmap.ic_launcher_music);

                            NotificationManagerCompat.from(context).notify(1, notificationBuilder.build());
                        }
                        else if (statusCode == 404) {
                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                                    .setContentTitle("Invalid playlist link")
                                    .setContentText("Please double check your shared playlist link")
                                    .setSmallIcon(R.mipmap.ic_launcher_music);

                            NotificationManagerCompat.from(context).notify(1, notificationBuilder.build());
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + spotifyToken;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
}
