package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImportSongDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ImportedSongs";
    public static final int DATABASE_VERSION = 1;

    public ImportSongDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE IF NOT EXISTS genius_songs (song_name TEXT, artists_string TEXT, song_lyrics TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS genius_songs");
        onCreate(sqLiteDatabase);
    }
}