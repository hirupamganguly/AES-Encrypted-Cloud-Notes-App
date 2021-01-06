package com.example.cloudnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UniNoteActivity extends AppCompatActivity {
    TextView titleUniNote,contentUniNote;
    FloatingActionButton floatingActionButton, floatingActionButtonMarkdown;
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uni_note);
        floatingActionButton = findViewById(R.id.edit_floating_button);
        floatingActionButtonMarkdown=findViewById(R.id.floatingActionMarkdown);
        titleUniNote = findViewById(R.id.title_uninote);
        contentUniNote = findViewById(R.id.content_uninote);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String noteID = intent.getStringExtra("noteID");
        titleUniNote.setText(title);
        contentUniNote.setText(content);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UniNoteActivity.this, EditNoteActivity.class);
                intent1.putExtra("title", title);
                intent1.putExtra("content", content);
                intent1.putExtra("noteId", noteID);
                startActivity(intent1);
            }
        });
        floatingActionButtonMarkdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent11 = new Intent(UniNoteActivity.this, MarkdownActivity.class);
                intent11.putExtra("title", title);
                intent11.putExtra("content", content);
                intent11.putExtra("noteId", noteID);
                startActivity(intent11);
                finish();
            }
        });
        SeekBar seekBar=findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                size=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(size<8) contentUniNote.setTextSize(size*3);
                else contentUniNote.setTextSize(size*4);
            }
        });
    }
}