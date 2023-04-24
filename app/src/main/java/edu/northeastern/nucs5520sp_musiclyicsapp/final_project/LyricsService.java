package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model.GeniusSong;

public class LyricsService {

    private final RequestQueue queue;
    private String GENIUS_ACCESS_TOKEN = "dmSrz_BbuyZziOaxUKrx56UQdH3dwWuPm4h4YC0RLFtT20ZjnB0GiOwtq4Wo3TYs";
    private GeniusSong outputSong;

    public LyricsService(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public GeniusSong getOutputSong() {
        return this.outputSong;
    }

    public void getLyricsForSong(String songName, ArrayList<String> artistsList, VolleyCallback callback) {

        String cleanedSongName = songName.replaceAll("[^a-zA-Z\\s]", "");
        String artistsLongString = String.join(" ", artistsList);
        artistsLongString = artistsLongString.replaceAll("[^a-zA-Z\\s]", "");

        // Build the API link. Remove white spaces at then ends of endpoint and replace white
        // spaces with %20.
        String endpoint = "https://api.genius.com/search?q=" + cleanedSongName + artistsLongString;
        endpoint = endpoint.trim();
        endpoint = endpoint.replace(" ", "%20");

        Log.d("LYRICS URL", endpoint);

        // Create the request.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
//                    JSONArray songsJsonArray = response.optJSONObject("response").optJSONArray("hits");
                    JSONArray songsJsonArray = response.optJSONObject("response").optJSONArray("hits");
                    // Logging
                    for(int i = 0; i < songsJsonArray.length(); i++) {
                        try {
                            Log.d("GENIUS JSONARRAY HITS", songsJsonArray.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // We choose the first song among all songs returned from the search, if
                    // the search result is not empty.
                    String lyrics = "";
                    Log.d("INSIDE GENIUS REQUEST", "inside genius request");
                    if (songsJsonArray != null) {
                        Log.d("INSIDE songJsonArray", "inside songJsonArray");
                        // Url is buried inside the first JSONObject (i.e., first search result),
                        // inside its "result" key, whose value is also a JSONObject. Inside this
                        // JSONObject there is a "url" key.
                        JSONObject topSong = songsJsonArray.optJSONObject(0);
                        JSONObject topSongDetails = topSong.optJSONObject("result");
                        assert topSongDetails != null;
                        String lyricsUrl = topSongDetails.optString("url");
                        Log.d("TOPSONGDETAILS JSONOBJECT", topSongDetails.toString());
                        Log.d("LYRICSURL FROM JSON", lyricsUrl);
                        String artistsString = String.join(", ", artistsList);
                        GetLyricsRunnable getLyricsRunnable = new GetLyricsRunnable(songName, artistsString, lyricsUrl);
                        Thread t = new Thread(getLyricsRunnable);
                        t.start();
                        // Wait until the new thread has run the GetLyricsRunnable.
                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // After the GetLyricsRunnable finished running, we can finally get the final
                        // output with lyrics (if result found on Genius.com).
                        GeniusSong outputGeniusSong = getLyricsRunnable.getGeniusSong();
                        // Send the output out with callback function.
                        callback.onLyricsSuccess(outputGeniusSong);
                        Log.d("genius song after thread run", outputGeniusSong.getSongName());
                        Log.d("genius song lyrics after thread run", outputGeniusSong.getLyrics());


                        // This will lead us into the new Runnable thread to scrape lyrics from Genius.
                    }
                }, error -> {
                    Log.e("ERROR", "Error when trying to get lyrics from Genius.");
                    if (error instanceof ServerError && error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        if (statusCode == 401) {
                            Log.e("ERROR", "Genius Unauthorized");
                        }
                        else if (statusCode == 403) {
                            Log.e("ERROR", "Genius request has been refused");
                        }
                        else if (statusCode == 404) {
                            Log.e("ERROR", "Genius request not found");
                        }
                        else if (statusCode == 429) {
                            Log.e("ERROR", "Genius request rate limit reached");
                        }
                        else if (statusCode == 500) {
                            Log.e("ERROR", "Genius API side error");
                        }
                    }
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

    public void getLyricsForSearch(String searchTerm, VolleyCallback callback) {

        String cleanedSearchTerm = searchTerm.replaceAll("[^a-zA-Z\\s]", "");

        // Build the API link. Remove white spaces at then ends of endpoint and replace white
        // spaces with %20.
        String endpoint = "https://api.genius.com/search?q=" + cleanedSearchTerm;
        endpoint = endpoint.trim();
        endpoint = endpoint.replace(" ", "%20");

        Log.d("LYRICS URL", endpoint);

        // Create the request.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
//                    JSONArray songsJsonArray = response.optJSONObject("response").optJSONArray("hits");
                    JSONArray songsJsonArray = response.optJSONObject("response").optJSONArray("hits");
                    // Logging
                    for(int i = 0; i < songsJsonArray.length(); i++) {
                        try {
                            Log.d("GENIUS JSONARRAY HITS", songsJsonArray.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // We choose the first song among all songs returned from the search, if
                    // the search result is not empty.
                    String lyrics = "";
                    Log.d("INSIDE GENIUS REQUEST", "inside genius request");
                    if (songsJsonArray != null) {
                        Log.d("INSIDE songJsonArray", "inside songJsonArray");
                        // Url is buried inside the first JSONObject (i.e., first search result),
                        // inside its "result" key, whose value is also a JSONObject. Inside this
                        // JSONObject there is a "url" key.
                        JSONObject topSong = songsJsonArray.optJSONObject(0);
                        JSONObject topSongDetails = topSong.optJSONObject("result");
                        assert topSongDetails != null;
                        String lyricsUrl = topSongDetails.optString("url");
                        String songName = topSongDetails.optString("full_title").trim();
                        String artistNames = topSongDetails.optString("artist_names").trim();
                        // Clean up song name to remove "by xxx" at the end, i.e. artist_names.
                        if (songName.length() - artistNames.length() - 3 >= 0) {
                            songName = songName.substring(0, songName.length() - artistNames.length() - 3);
                        }
                        Log.d("song name from search", songName);
                        Log.d("TOPSONGDETAILS JSONOBJECT", topSongDetails.toString());
                        Log.d("LYRICSURL FROM JSON", lyricsUrl);
                        GetLyricsRunnable getLyricsRunnable = new GetLyricsRunnable(songName, artistNames, lyricsUrl);
                        Thread t = new Thread(getLyricsRunnable);
                        t.start();
                        // Wait until the new thread has run the GetLyricsRunnable.
                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // After the GetLyricsRunnable finished running, we can finally get the final
                        // output with lyrics (if result found on Genius.com).
                        GeniusSong outputGeniusSong = getLyricsRunnable.getGeniusSong();
                        // Send the output out with callback function.
                        callback.onLyricsSuccess(outputGeniusSong);
                        Log.d("genius song after thread run", outputGeniusSong.getSongName());
                        Log.d("genius song lyrics after thread run", outputGeniusSong.getLyrics());


                        // This will lead us into the new Runnable thread to scrape lyrics from Genius.
                    }
                }, error -> {
                    Log.e("ERROR", "Error when trying to get lyrics from Genius.");
                    if (error instanceof ServerError && error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        if (statusCode == 401) {
                            Log.e("ERROR", "Genius Unauthorized");
                        }
                        else if (statusCode == 403) {
                            Log.e("ERROR", "Genius request has been refused");
                        }
                        else if (statusCode == 404) {
                            Log.e("ERROR", "Genius request not found");
                        }
                        else if (statusCode == 429) {
                            Log.e("ERROR", "Genius request rate limit reached");
                        }
                        else if (statusCode == 500) {
                            Log.e("ERROR", "Genius API side error");
                        }
                    }
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
     * (Below helped me with defining user agent.
     * Credit: https://itecnote.com/tecnote/jsoup-error-403-when-trying-to-read-the-contents-of-a-directory-on-the-website
     */
    public String getLyricsFromUrl(String lyricsUrl) throws IOException {

        // Credit: https://itecnote.com/tecnote/jsoup-error-403-when-trying-to-read-the-contents-of-a-directory-on-the-website/
        // !!!!! Thanks to this I am not getting 403 forbidden.
        String userAgent = "Mozilla/5.0";

        Connection connection = Jsoup.connect(lyricsUrl).userAgent(userAgent);

        Document doc = connection.get();

        Log.d("DOC CONTENT", doc.toString());

        Element lyricsContainer = doc.selectFirst("div[data-lyrics-container='true']");
        Log.d("lyricsContainer content", lyricsContainer.toString());

        String lyrics = "";

        Log.d("lyricsContainer children size", String.valueOf(lyricsContainer.childrenSize()));

        Log.d("INSIDE IF", "inside lyricsContainer != null if ");
        Elements aElements = lyricsContainer.select("a");
        Log.d("aElements content", aElements.toString());
        Log.d("aElements is empty", String.valueOf(aElements.isEmpty()));
        // For some songs on Genius.com, the lyrics are the text of the container div element.
        // This happens when lyrics doesn't have a "story" with it. (See any lyrics page on
        // genius.com for details)
        if (aElements.isEmpty()) {
            lyrics = lyricsContainer.html().replaceAll("(?i)<br[^>]*>", "\n").replaceAll("<.*?>", "");
        }
        else {
            StringBuilder lyricsBuilder = new StringBuilder();
            Log.d("INSIDE IF", "INSIDE aElements != null");
            for (Element aElement: aElements) {
                Element spanElement = aElement.selectFirst("span");
                if (spanElement != null) {
                    // Replace all the <br> tags in html with line breaks, and remove all other tags like <i>.
                    String lyricsHtml = spanElement.html().replaceAll("(?i)<br[^>]*>", "\n").replaceAll("<.*?>", "");
                    Log.d("single line lyrics", lyricsHtml);
                    lyricsBuilder.append(lyricsHtml).append("\n");
                }
            }
            lyrics = lyricsBuilder.toString();
        }

        // Some lines have two line breaks. Replace them with one line break.
        Log.d("LYRICS FROM GETLYRICSFROMURL()", lyrics);
        return lyrics.replaceAll("\n\n", "\n");
    }

    class GetLyricsRunnable implements Runnable {
        private String songName;
        private String artistsString;
        private String lyricsUrl;
        private GeniusSong geniusSong;

        public GetLyricsRunnable(String songName, String artistsString, String lyricsUrl) {
            this.songName = songName;
            this.artistsString = artistsString;
            this.lyricsUrl = lyricsUrl;
        }

        public GeniusSong getGeniusSong() {
            return geniusSong;
        }

        public void setGeniusSong(GeniusSong song) {
            this.geniusSong = song;
        }

        @Override
        public void run() {
            try {
                GeniusSong songWithLyrics = new GeniusSong(this.songName, this.artistsString, getLyricsFromUrl(this.lyricsUrl));
                setGeniusSong(songWithLyrics);
                Log.d("LYRICS IN THREAD'S RUN METHOD", geniusSong.getLyrics());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
