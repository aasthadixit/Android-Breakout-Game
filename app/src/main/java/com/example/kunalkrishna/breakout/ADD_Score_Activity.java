package com.example.kunalkrishna.breakout;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by aastha dixit and kunal krishna on 11/20/2015.
 */
public class ADD_Score_Activity extends Activity {


    private Record mCurrRecord = null;
    private boolean mAddMode = false;
    private boolean mViewMode = false;
    private boolean mEditMode = false;
    static  String score1;

    public Button button;
    /* Author: kunal krishna
    *Description: class to handle activity for adding user score
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_score_activity);


        //Retrieving Message Passed from Parent Activity which is ContactList
        //PopulateView();

        //Retrieving Message Passed from Parent Activity which is ContactList

        score1 = getIntent().getExtras().getString("Score");

        //Set Size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //Retrieving width for setting label and TextFields
        int lWidth = size.x*4/10;
        int eWidth = size.x*6/10;

        TextView lFName = (TextView)findViewById(R.id.First_Name);
        lFName.setWidth(lWidth);
        TextView lLName = (TextView)findViewById(R.id.Last_Name);
        lLName.setWidth(lWidth);
        TextView lscore = (TextView)findViewById(R.id.Current_Score);
        lscore.setWidth(lWidth);
        EditText fName = (EditText)findViewById(R.id.firstname);
        fName.setWidth(eWidth);
        EditText lName = (EditText)findViewById(R.id.lastname);
        lName.setWidth(eWidth);
        EditText score = (EditText)findViewById(R.id.score);
        score.setWidth(eWidth);
        score.setFocusable(false);

        button = (Button) findViewById(R.id.button1);

        	/* Author:kunal krishna
          * Description: adding listener for button to open next activity to view high score
        */
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                mCurrRecord = new Record();
                PrepareRecord();
                RepositoryHandler.GetOrCreateRepository(getApplicationContext()).AddRecord(getApplicationContext(), mCurrRecord);
                finish();
                Intent add_or_view_intent = new Intent(ADD_Score_Activity.this,Score_List_Activity.class);
                startActivity(add_or_view_intent);
            }
        });
        button.setVisibility(View.VISIBLE);
        PopulateEditText();
    }


    // This is the end of our BreakoutView inner class
    // This method executes when the player quits the game

    /* Author:kunal krishna
      *Description: method for onpause
        */
    @Override
    protected void onPause() {
        super.onPause();
      //  repositoryHandler.CreateAndPopulateRepoFile(this);
    }


    /* Author:kunal krishna
      *Description: method for on stop
        */
    @Override
    protected void onStop() {
        super.onStop();
       // repositoryHandler.CreateAndPopulateRepoFile(this);
    }

    /* Author:kunal krishna
      *Description: method for populate menu option
        */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_add_score, menu);
        return true;
    }

    /* Author: kunal krishna
      *Description: method for populate text field in the layout
        */
    private void PopulateEditText() {
        EditText fName = (EditText)findViewById(R.id.firstname);
        EditText lName = (EditText)findViewById(R.id.lastname);
        EditText score = (EditText)findViewById(R.id.score);

        score.setText(""+score1);
        fName.setText("");
        lName.setText("");
    }

    /* Author: kunal krishna
      *Description: prepare record before adding score so that repository can add this record
        */
    private void PrepareRecord() {

        EditText fName = (EditText)findViewById(R.id.firstname);
        EditText lName = (EditText)findViewById(R.id.lastname);
        EditText Score = (EditText)findViewById(R.id.score);

        mCurrRecord.SetFirstName(fName.getText().toString());
        mCurrRecord.SetLastName(lName.getText().toString());
        mCurrRecord.SetScore(Score.getText().toString());
    }




    /**
     ** Author:kunal krishna
     *Description:
     * Overriding the onPrepareOptionMenu so that we could change Menu on runtime
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
            getMenuInflater().inflate(R.menu.menu_add_score, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    /**kunal krishna
     * Adding functionality on each click event from menu buttons
     * @param item
     * @return boolean (Default
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.button) {
            PrepareRecord();
            finish();
//        }
        return super.onOptionsItemSelected(item);
    }

    /* Author:kunal krishna
          *Description: handled condition if user press back buttion.
            */
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);

    }

}



