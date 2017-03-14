package com.example.kunalkrishna.breakout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by aastha dixit and kunal krishna on 11/27/2015.
 */
public class MainPage extends Activity {
    public Button button1;
    public Button button2;

    /* Author:kunal krishna
       Description: The main page is the starting point for the game where two buttons for starting the game and viewing the high scores are present.
       These buttons take the player to respective activities of playing a game or viewing the high scores.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        button1 = (Button) findViewById(R.id.start_game);
        button2 = (Button) findViewById(R.id.high_score);

        button1.setOnClickListener(new View.OnClickListener() {
            //starts the game on clicking this button of start game
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(MainPage.this, Breakout.class);
                startActivity(mainActivity);
            }

        });
        //calls the high score activity on clicking the button for high score.
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent scorelist = new Intent(MainPage.this, Score_List_Activity.class);
                startActivity(scorelist);
            }

        });


    }
}
