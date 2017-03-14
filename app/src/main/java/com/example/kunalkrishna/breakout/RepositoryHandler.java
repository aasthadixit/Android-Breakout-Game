package com.example.kunalkrishna.breakout;
/**
 * Created by aastha dixit and kunal krishna on 11/23/2015.
 */
import android.content.Context;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * It is a Singleton class
 */
public class RepositoryHandler {
    private static RepositoryHandler repoObj = null;
    private ArrayList<Record> repository= null;
    private final String repofilePath = "repo.txt";

    /* Author:kunal krishna
     * Description: A CTOR to initialize List of Records
     */
    private RepositoryHandler(Context context){
        repository = new ArrayList<Record>();
        PopulateRepository(context);
    }

    /* Author:kunal krishna
     * Description: Gets Record at Position
     */
    public Record GetRecordAt(int pos) {
        if(pos > repository.size()-1) {
            throw new RuntimeException("Position is greater than size of the list.");
        }

        return repository.get(pos);
    }

    /* Author:kunal krishna
     * Description: Get All record
     */
    public ArrayList<Record> GetAllRecords() {
        return repository;
    }

    /* Author:kunal krishna
     * Description: Adds a new Record. returns false if we already have similar record.
     */
    public boolean AddRecord(Context context, Record recordTobeAdded) {
        boolean retVal = true;

        if(retVal) {
            repository.add(recordTobeAdded);
            Collections.sort(repository,new Comparator<Record>() {
                @Override
                public int compare(Record  rec1, Record  rec2)
                {
                    return  rec1.GetScore().compareTo(rec2.GetScore());
                }
            });
        }

        return retVal;
    }

    /* Author:kunal krishna
     * Description: Delete Record from repository
     */
    public void DeleteRecord(int index) {
        repository.remove(index);
    }



    /* Author:kunal krishna
     * Description: Reads file and populate repository
     */
    private void PopulateRepository(Context context) {

        try{
            FileInputStream fin = context.openFileInput(repofilePath);
            if(fin != null) {
                InputStreamReader tmp = new InputStreamReader(fin);
                String line = "";
                String[] strarr;
                BufferedReader buffReader = new BufferedReader(tmp);
                while((line = buffReader.readLine()) != null) {
                    strarr = line.split("\t", -1);
                    AddRecord(context,new Record(strarr[0], strarr[1], strarr[2]));
                }
                fin.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Author:aastha dixit
     * Description: Creates and populates Repo File.
     */
    public void CreateAndPopulateRepoFile(Context context) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(repofilePath, Context.MODE_PRIVATE);

            for (Record curr: repository) {
                outputStream.write(curr.toString().getBytes());
            }

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Author:aastha dixit
     * Description: Creates or GetRepository
     */
    public static RepositoryHandler GetOrCreateRepository(Context context) {
        if(repoObj == null) {
            repoObj = new RepositoryHandler(context);
        }
        return repoObj;
    }
}

