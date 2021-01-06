package com.example.cloudnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AddNoteActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FloatingActionButton saveButton;
    EditText title_addNote,content_addNote;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        progressBar=findViewById(R.id.progressBar_aadNote);
        title_addNote=findViewById(R.id.note_title_addNote);
        content_addNote=findViewById(R.id.note_content_addNote);
        saveButton=findViewById(R.id.save_button_addNote);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String noteTitle=title_addNote.getText().toString();
                String noteContent= content_addNote.getText().toString();
                if(noteTitle.length()==0||noteContent.length()==0){
                    Toast.makeText(AddNoteActivity.this, "Can not save with empty fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    DocumentReference documentReference=firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("UserNotes").document();
                    Map<String,Object> newnote=new HashMap<>();
                    newnote.put("Title",AES_ENCRYPTION(noteTitle));
                    newnote.put("Content",AES_ENCRYPTION(noteContent));
                    documentReference.set(newnote).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddNoteActivity.this, "SAVED", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddNoteActivity.this,ShowNotesActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddNoteActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }


    //AES Methods:
    private byte[] secrectKey = {9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53};
    private SecretKeySpec secretKeySpec = new SecretKeySpec(secrectKey, "AES");

    public String AES_ENCRYPTION(String text) {

        try {
            byte[] inputedByteArray = text.getBytes();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] outputedByteArray = cipher.doFinal(inputedByteArray);
            String encryptedText = null;
            encryptedText = new String(outputedByteArray, "ISO-8859-1");
            return encryptedText;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "FAILED TO ENCRYPT";
    }

    public String AES_DECRYPTION(String text){
        try{
            byte[] inputedByteArray = text.getBytes("ISO-8859-1");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] outputedByteArray = cipher.doFinal(inputedByteArray);
            String decryptedText = null;
            decryptedText = new String(outputedByteArray);
            return decryptedText;
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "FAILED TO DECRYPT";
    }
}