package com.example.cloudnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNoteActivity extends AppCompatActivity {
    EditText edittitle, editContent;
    ProgressBar progressBar;
    FloatingActionButton floatingActionButton;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        progressBar=findViewById(R.id.progressBar_edit_note);
        edittitle=findViewById(R.id.title_edit_note);
        editContent=findViewById(R.id.content_edit_note);
        floatingActionButton=findViewById(R.id.save_edit_note_floating_button);
        Intent intent=getIntent();
        edittitle.setText(intent.getStringExtra("title"));
        editContent.setText(intent.getStringExtra("content"));
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore= FirebaseFirestore.getInstance();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                AddNoteActivity addNoteActivity=new AddNoteActivity();
                Map<String,Object> updatednote=new HashMap<>();
                String title=addNoteActivity.AES_ENCRYPTION(edittitle.getText().toString());
                String content=addNoteActivity.AES_ENCRYPTION(editContent.getText().toString());
                updatednote.put("Title",title);
                updatednote.put("Content",content);
                String note_id=intent.getStringExtra("noteId");
                DocumentReference documentReference=firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("UserNotes").document(note_id);
                documentReference.update(updatednote).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(EditNoteActivity.this,ShowNotesActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditNoteActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}