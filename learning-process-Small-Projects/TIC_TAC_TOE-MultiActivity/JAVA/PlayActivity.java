package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity {
    int[] gameState={2,2,2,2,2,2,2,2,2}; // null-> 2  X->0  O->1
    int[][] winPositions={
            {0,1,2},{3,4,5},{6,7,8}, // horizontally
            {0,3,6},{1,4,7},{2,5,8}, // vertically
            {0,4,8},{2,4,6} // diagonally
    };
    int[] countTheWinning={0,0};
    String player_1;
    String player_2;
    boolean isGameActive=true;
    int activePlayer=0; //X->0
    int countSteps=0; // atmost steps will be 8 as 9 cell present in this grid
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // recieved INTENT's data
        Intent intent= getIntent();
        String[] recievedIntent=intent.getStringArrayExtra(MainActivity.key);
        player_1=recievedIntent[0];
        player_2=recievedIntent[1];

        // Set Actions text
        TextView textView=findViewById(R.id.action_text_view);
        textView.setText(player_1+"'s Turn ");
    }

    public void tap_method(View view) { //OnCLick of each ImaveView
        ImageView imageView= (ImageView) view;
        int tappedCell=Integer.parseInt(imageView.getTag().toString());
        // Reset Activty Handel
        if(!isGameActive || countSteps>8){
            countSteps=0;
            gameReset();
        }
        // Tapped activity Handel
        if(gameState[tappedCell]==2){ // if cell is vacant cell
            gameState[tappedCell]=activePlayer;
            if(countSteps==8){
                TextView textView1=findViewById(R.id.bottom_text_view);
                textView1.setText("Please tap to play next board " +player_1+""+". Board will RESET itself");
            }
            if(activePlayer==0){
                imageView.setImageResource(R.drawable.x);
                activePlayer=1;
                TextView textView=findViewById(R.id.action_text_view);
                textView.setText(player_2+"'s Turn ");
                countSteps++;
            }
            else{
                imageView.setImageResource(R.drawable.o);
                activePlayer=0;
                TextView textView=findViewById(R.id.action_text_view);
                textView.setText(player_1+"'s Turn ");
                countSteps++;
            }
        }
        if(countSteps>4) {
            for (int[] winPos : winPositions) {
                if (gameState[winPos[0]] == gameState[winPos[1]] && gameState[winPos[1]] == gameState[winPos[2]] && gameState[winPos[0]] != 2) {
                    isGameActive = false;
                    String winningAnnouncement = "";
                    if (gameState[winPos[0]] == 0) {
                        countTheWinning[0] = countTheWinning[0] + 1;
                        winningAnnouncement = player_1 + "WON \n  POINTS: " + player_1 + "->" + countTheWinning[0] + " & " + player_2 + "->" + countTheWinning[1];
                    } else {
                        countTheWinning[1] = countTheWinning[1] + 1;
                        winningAnnouncement = player_1 + "WON \n POINTS: " + player_1 + "->" + countTheWinning[0] + " & " + player_2 + "->" + countTheWinning[1];
                    }
                    TextView textView = findViewById(R.id.action_text_view);
                    textView.setText(winningAnnouncement);
                    TextView textView1 = findViewById(R.id.bottom_text_view);
                    textView1.setText("Please tap to play next board " + player_1 + "" + ". Board will RESET itself");
                    break;
                }
            }
        }
    }

    private void gameReset() {
        isGameActive=true;
        activePlayer=0;
        int[] resetGameState={2,2,2,2,2,2,2,2,2};
        gameState=resetGameState;
        ((ImageView)findViewById(R.id.imageView1)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView2)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView3)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView4)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView5)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView6)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView7)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView8)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView9)).setImageResource(0);
        TextView textView=findViewById(R.id.action_text_view);
        textView.setText(player_1+"'s Turn ");
        TextView textView1=findViewById(R.id.bottom_text_view);
        textView1.setText("");
    }
}