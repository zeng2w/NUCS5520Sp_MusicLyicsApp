package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.GeniusSong;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SpotifyArtist;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.SpotifySong;

public class GeniusService {

    private ArrayList<GeniusSong> SongsWithLyricsList;
//    private final RequestQueue queue;
    private String GENIUS_ACCESS_TOKEN = "sd5_JeeFfCuttmEMQCQqcbPof3Q9kP6GCqzyhSX4Q8Eit9jClmicz77-FHOXKam2";

//    public GeniusService(Context context) {
//        this.SongsWithLyricsList = new ArrayList<>();
//        queue = Volley.newRequestQueue(context);
//    }

    public GeniusService() {
        this.SongsWithLyricsList = new ArrayList<>();
    }

    public ArrayList<GeniusSong> getSongsWithLyricsList() {
        return SongsWithLyricsList;
    }

//    public void getLyricsForSong(String songName, ArrayList<String> artists) {
//
//
//            // Set up the search content
//        String artistsLongString = String.join(" ", artists);
//
//        // Build the API link. Remove white spaces at then ends of endpoint and replace white
//        // spaces with %20.
//        String endpoint = "api.genius.com/search?q=" + songName + artistsLongString;
//        endpoint = endpoint.trim();
//        endpoint = endpoint.replace(" ", "%20");
//
//        // Create the request.
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, endpoint, null, response -> {
//                    JSONArray songsJsonArray = response.optJSONArray("hits");
//                    // We choose the first song among all songs returned from the search, if
//                    // the search result is not empty.
//                    if (!songsJsonArray.isNull(0)) {
//                        JSONObject topSong = songsJsonArray.optJSONObject(0);
//                        String lyricsUrl = topSong.optJSONObject("result").optString("url");
//
//                    }
//
//
//
//
//        }, error -> {
//
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                String token = GENIUS_ACCESS_TOKEN;
//                String auth = "Bearer " + token;
//                headers.put("Authorization", auth);
//                return headers;
//            }
//        };
//
//        queue.add(jsonObjectRequest);
//    }

    /**
     * Scrape the lyrics from the genius.com link to the lyrics page of a song.
     * Credit: https://jsoup.org/
     * (Below helped me with defining user agent.
     * Credit: https://itecnote.com/tecnote/jsoup-error-403-when-trying-to-read-the-contents-of-a-directory-on-the-website
     */
    public String getLyricsFromUrl(String lyricsUrl) throws IOException {

        // Credit: https://itecnote.com/tecnote/jsoup-error-403-when-trying-to-read-the-contents-of-a-directory-on-the-website/
        // !!!!! Thanks to this I am not getting 403 forbidden.
        String userAgent = "Mozilla/5.0";

        Connection connection = Jsoup.connect(lyricsUrl).userAgent(userAgent);

        Document doc = connection.get();

        Element lyricsContainer = doc.selectFirst("div[data-lyrics-container='true']");
        assert lyricsContainer != null;
        Elements aElements = lyricsContainer.select("a");
        StringBuilder lyricsBuilder = new StringBuilder();
        for (Element aElement: aElements) {
            Element spanElement = aElement.selectFirst("span");
            assert spanElement != null;
            lyricsBuilder.append(spanElement.text().trim()).append("\n");
        }
        String lyrics = lyricsBuilder.toString();
        lyrics = lyrics.replaceAll("\\s(?=[A-Z])", "\n");
        return lyrics;
    }

    public static void main(String[] args) throws IOException {
        GeniusService service = new GeniusService();

        String testLyrics = service.getLyricsFromUrl("https://genius.com/Coldplay-yellow-lyrics");
        System.out.println("Yellow lyrics: \n" + testLyrics);
    }




}
