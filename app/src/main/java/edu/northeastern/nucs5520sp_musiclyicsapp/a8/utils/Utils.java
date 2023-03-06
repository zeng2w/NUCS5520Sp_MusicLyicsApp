package edu.northeastern.nucs5520sp_musiclyicsapp.a8.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Utils {
    public static String getProperties(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static void setProperties(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void postToastMessage(final String message, final Context context){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
