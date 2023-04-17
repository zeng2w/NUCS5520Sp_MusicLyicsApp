package edu.northeastern.nucs5520sp_musiclyicsapp.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityCommentBinding;
import edu.northeastern.nucs5520sp_musiclyicsapp.databinding.ActivityLibraryPageBinding;
import edu.northeastern.nucs5520sp_musiclyicsapp.final_project.adapters.CommentAdapter;

public class CommentActivity extends AppCompatActivity {

    RecyclerView comment_recycleView;
    ActivityCommentBinding binding;
    CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set adapter to recycler view
        commentAdapter = new CommentAdapter(this);
//        binding.commentRecycleView.setAdapter(commentAdapter);
    }
}