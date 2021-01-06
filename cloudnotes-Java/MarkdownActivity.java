package com.example.cloudnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import us.feras.mdv.MarkdownView;

public class MarkdownActivity extends AppCompatActivity {
    private MarkdownView markdownView;
    TextView titleUniNote,contentUniNote;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown);

        floatingActionButton = findViewById(R.id.edit_floating_button);
        titleUniNote = findViewById(R.id.title_uninote);
        markdownView = findViewById(R.id.markdownView);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String noteID = intent.getStringExtra("noteId");
        titleUniNote.setText(title);
        markdownView.loadMarkdown(content);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MarkdownActivity.this, EditNoteActivity.class);
                intent1.putExtra("title", title);
                intent1.putExtra("content", content);
                intent1.putExtra("noteId", noteID);
                startActivity(intent1);
                finish();
            }
        });
    }
}