package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

/*
Edit Album fragment, when user click on the album image in Create/Edit page, this fragment will
display and let user edit the album name
 */
public class EditAlbumFragment extends Fragment {

    ImageButton editAlbum_albumImage;
    EditText editAlbum_albumName;
    Button editAlbum_buttonCancel;
    Button editAlbum_buttonSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_album_fragment, container, false);
    }
}
