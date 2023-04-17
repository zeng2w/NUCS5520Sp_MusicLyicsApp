package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.GeniusSong;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SpotifyArtist;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SpotifySong;

public class GeniusService {

    private ArrayList<GeniusSong> SongsWithLyricsList;
    private final RequestQueue queue;
    private String GENIUS_ACCESS_TOKEN = "sd5_JeeFfCuttmEMQCQqcbPof3Q9kP6GCqzyhSX4Q8Eit9jClmicz77-FHOXKam2";

    public GeniusService(Context context) {
        this.SongsWithLyricsList = new ArrayList<>();
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<GeniusSong> getSongsWithLyricsList() {
        return SongsWithLyricsList;
    }

    public void getLyricsForSong(String songName, ArrayList<String> artists) {


            // Set up the search content
        String artistsLongString = String.join(" ", artists);

        // Build the API link. Remove white spaces at then ends of endpoint and replace white
        // spaces with %20.
        String endpoint = "api.genius.com/search?q=" + songName + artistsLongString;
        endpoint = endpoint.trim();
        endpoint = endpoint.replace(" ", "%20");

        // Create the request.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    JSONArray songsJsonArray = response.optJSONArray("hits");
                    // We choose the first song among all songs returned from the search, if
                    // the search result is not empty.
                    if (!songsJsonArray.isNull(0)) {
                        JSONObject topSong = songsJsonArray.optJSONObject(0);
                        String lyricsUrl = topSong.optJSONObject("result").optString("url");

                    }




        }, error -> {

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = GENIUS_ACCESS_TOKEN;
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    /**
     * Scrape the lyrics from the genius.com link to the lyrics page of a song.
     * Credit: https://jsoup.org/
     */
    public String getLyricsFromUrl(String lyricsUrl) throws IOException {
        // Use Jsoup to get the
        Document doc = Jsoup.connect(lyricsUrl).get();
        Element


    }



}
