package com.example.kunalkrishna.breakout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aastha dixit and kunal krishna on 11/23/2015.
 */

public class RecordListAdapter extends ArrayAdapter<Record>{
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    /* Author:aastha dixit
       Description: Sets the value for the current record.
    */
    public RecordListAdapter(Context ctx, int resourceId, ArrayList<Record> listOfObjects)
    {
        super(ctx,resourceId,listOfObjects);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
    }

    /* Author:aastha dixit
       Description: Gets the value of first name, last name and score from a record.
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = (LinearLayout) inflater.inflate(resource,null);
        Record rec = getItem(position);
        TextView fname = (TextView) convertView.findViewById(R.id.first_name_list);
        fname.setText(rec.GetFirstName());
        TextView lname = (TextView) convertView.findViewById(R.id.last_name_list);
        lname.setText(rec.GetLastName());
        TextView lscore = (TextView) convertView.findViewById(R.id.list_score);
        lscore.setText(rec.GetScore());

        return convertView;
    }
}


