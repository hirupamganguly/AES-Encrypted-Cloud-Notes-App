package com.example.cloudnotes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText emailId, password;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth=FirebaseAuth.getInstance();

        emailId=findViewById(R.id.register_email_id);
        password=findViewById(R.id.register_password);

        registerButton=findViewById(R.id.r_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                boolean flagOfMerging=intent.getBooleanExtra("FlagOfAnonymousAccountMerge",false);
                if(flagOfMerging!=true) {
                    if(TextUtils.isEmpty(emailId.getText())||TextUtils.isEmpty(password.getText())){
                        Toast.makeText(RegisterActivity.this, "Fill up Properly", Toast.LENGTH_SHORT).show();
                    }
                    else if(password.getText().toString().length()<6){
                        Toast.makeText(RegisterActivity.this, "Password Should be greater than 5 charachters long..,", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        firebaseAuth.createUserWithEmailAndPassword(emailId.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(RegisterActivity.this, "REGISTRATION COMPLETED", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, ShowNotesActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "FAILED - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }else {
                    if(TextUtils.isEmpty(emailId.getText())||TextUtils.isEmpty(password.getText())){
                        Toast.makeText(RegisterActivity.this, "Fill up Properly", Toast.LENGTH_SHORT).show();
                    }
                    else if(password.getText().toString().length()<6){
                        Toast.makeText(RegisterActivity.this, "Password Should be greater than 5 charachters long..,", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        AuthCredential credential = EmailAuthProvider.getCredential(emailId.getText().toString(), password.getText().toString());
                        firebaseAuth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(RegisterActivity.this, "REGISTRATION COMPLETED", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, ShowNotesActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "FAILED - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}