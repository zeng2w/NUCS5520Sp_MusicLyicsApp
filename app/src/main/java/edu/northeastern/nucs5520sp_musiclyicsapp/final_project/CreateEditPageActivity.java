package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

/*
this is Create/Edit page which user can create/edit song title, artist, the original lyric, and
the translation of the lyric by themselves
 */
public class CreateEditPageActivity extends AppCompatActivity {

    EditText editPage_songName;
    EditText editPage_artist;
    EditText editPage_editLyric;
    EditText editPage_editTranslate;
    CheckBox editPage_checkBox;
    Button editPage_buttonCancel;
    Button editPage_buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_page);

        editPage_songName = findViewById(R.id.editPage_songName);
        editPage_artist = findViewById(R.id.editPage_artist);
        editPage_editLyric = findViewById(R.id.editPage_editLyric);
        editPage_editTranslate = findViewById(R.id.editPage_editTranslate);
        editPage_checkBox = findViewById(R.id.editPage_checkBox);
        editPage_buttonCancel = findViewById(R.id.editPage_buttonCancel);
        editPage_buttonSave = findViewById(R.id.editPage_buttonSave);
    }
}