package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import java.util.Objects;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class ActivitySpotifyAuth extends AppCompatActivity {

    /*
    Credit:
    Official tutorial for Android SDK: https://developer.spotify.com/documentation/android/tutorials/getting-started#authorize-your-application
    Add .aar App Remote SDK to project: https://stackoverflow.com/questions/67724242/how-to-import-aar-module-on-android-studio-4-2
     */

    /*
    There are two packages: one for authorization, one for music control in your app.
    Authorization is needed before making Web API calls. There are 2 separate .aar files for remote-control and
    auth. Add them to libs folder under app folder in the Project view in Android Studio (upper left corner, where
    it should say "Android" by default).
    Credit:
    For authorization: https://developer.spotify.com/documentation/android/tutorials/authorization
    The info for keystore is stored in my Apple Notes.
     */

    // Credit: https://towardsdatascience.com/using-the-spotify-api-with-your-android-application-the-essentials-1a3c1bc36b9e
    private SharedPreferences.Editor editor;
    private DatabaseReference databaseReferenceUserToken;
    private FirebaseUser currentUser;

    // Set up Spotify credentials.
    // Client ID is found in your dashboard at developer.spotify.com.
    private static final String CLIENT_ID = "c233e9cd9d6f47b48066547309fbc6b0";
    private static final String REDIRECT_URI = "edu.northeastern.nucs5520spmusiclyicsapp://callback";
    private static final int REQUEST_CODE = 1337;
    // Credit: https://developer.spotify.com/documentation/web-api/concepts/scopes
    private static final String SCOPES = "playlist-read-private, playlist-read-collaborative";

    public ActivitySpotifyAuth() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_spotify_auth);

        authenticateSpotify();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReferenceUserToken = FirebaseDatabase.getInstance().getReference("user_tokens");

    }

    /**
     * Prompt the user to give permission to our app to connect to their Spotify account and grab
     * relevant information.
     * Credit: https://developer.spotify.com/documentation/android/tutorials/authorization
     * https://towardsdatascience.com/using-the-spotify-api-with-your-android-application-the-essentials-1a3c1bc36b9e
     * (Section: Logging In)
     */
    private void authenticateSpotify() {
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{SCOPES});
        AuthorizationRequest request = builder.build();

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

            switch (response.getType()) {
                //Response was successful and contains auth token.
                case TOKEN:
                    // Write the auth token for this specific user to firebase.
                    databaseReferenceUserToken.child(currentUser.getUid()).child("spotify_token").setValue(response.getAccessToken());

                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("STARTING", "GOT AUTH TOKEN");
                    editor.apply();
                    break;

                // Auth flow returned an error.
                case ERROR:
                    Toast.makeText(this, "Unable to obtain auth token", Toast.LENGTH_SHORT).show();
                    break;

                // Most likely auth flow was canceled
                default:
                    Toast.makeText(this, "User authentication canceled", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}