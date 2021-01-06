package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    static final String key="com.example.testapplication.sendingKeyToPlayActivity";
    Button button;
    EditText editText_player1;
    EditText editText_player2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.play_button);
        editText_player1=findViewById(R.id.edit_text_player1);
        editText_player2=findViewById(R.id.edit_text_player2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] sendDataArray= {editText_player1.getText().toString(),editText_player2.getText().toString()};
                if(sendDataArray[0].length()==0) sendDataArray[0]="Player_1";
                if(sendDataArray[1].length()==0) sendDataArray[1]="Player_2";
                Intent intent=new Intent(MainActivity.this,PlayActivity.class);
                intent.putExtra(key,sendDataArray);
                startActivity(intent);
            }
        });
    }
}