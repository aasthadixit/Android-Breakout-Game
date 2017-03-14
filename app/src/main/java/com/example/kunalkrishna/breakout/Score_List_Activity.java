package com.example.kunalkrishna.breakout;
/**
 * Created by aastha dixit and kunal krishna on 11/24/2015.
 */
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;

public class Score_List_Activity extends AppCompatActivity
{
        private RepositoryHandler repositoryHandler = null;

        /* Author:kunal krishna
           Description: Populates the view for all the scores.
         */
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.score_list);
            PopulateView();
            final AppCompatActivity thisActivity = this;
        }

        /* Author:kunal krishna
           Description: Resumes the activity when game is paused.
        */
        @Override
        protected void onResume()
        {
            super.onResume();
            PopulateView();
        }

        /* Author:aastha dixit
           Description: Pauses the game when this activity is disrupted.
        */
        @Override
        protected void onPause()
        {
            super.onPause();
            repositoryHandler.CreateAndPopulateRepoFile(this);
        }

        /* Author:aastha dixit
           Description: Called when the game is stopped.
        */
        @Override
        protected void onStop() {
            super.onStop();
            repositoryHandler.CreateAndPopulateRepoFile(this);
        }


        /* Author:aastha dixit
           Description: The entire list of 10 scores is called.
        */
        private void PopulateView()
        {
            repositoryHandler = RepositoryHandler.GetOrCreateRepository(this);
            ArrayList<Record> records = repositoryHandler.GetAllRecords();

            ListView list = (ListView)findViewById(R.id.Player);
            RecordListAdapter adapter = new RecordListAdapter(this,R.layout.player_list,records);
            list.setAdapter(adapter);
        }

    /* Author:aastha dixit
       Description: Calls the MainPage activity when the back button is called.
    */
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);

    }
    }


