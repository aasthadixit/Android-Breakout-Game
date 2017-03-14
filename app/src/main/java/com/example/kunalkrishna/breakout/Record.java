package com.example.kunalkrishna.breakout;

/**
 * Created by aastha dixit and kunal krishna on 11/23/2015.
 */
import java.io.Serializable;

public class Record implements Serializable,Comparable{
    private String mFirstName = "";
    private String mLastName = "";
    private String mPlayerScore = "";


    /* Author:kunal krishna
     * Description: Default CTOR
     */
    public Record(){}

    /* Author:kunal krishna
     * Description: A public parameterized CTOR
     */
    public Record(String fname, String lname, String mScore) {
        mFirstName = fname;
        mLastName = lname;
        mPlayerScore = mScore;
    }

    /* Author:kunal krishna
     * Description: Getter for FirstName
     */
    public String GetFirstName() {
        return mFirstName;
    }

    /* Author:kunal krishna
     * Description: Setter for First Name
     */
    public void SetFirstName(String firstName) { mFirstName = firstName;}

    /* Author:kunal krishna
     * Description: Getter for LastName
     */
    public String GetLastName() {
        return mLastName;
    }

    /* Author:kunal krishna
     * Description: Setter for Last Name
     */
    public void SetLastName(String lastName) {mLastName = lastName;}

    /* Author:kunal krishna
     * Description: Getter for Full Name includes First and Last Name.
     * Will be Used to display Main Page.
     */
    public String GetFullName() {
        return mFirstName + " " + mLastName + " ";
    }

    /* Author:kunal krishna
     * Description: Getter email Id
     */
    public String GetScore() {
        return mPlayerScore;
    }

    /* Author:kunal krishna
     * Description: Setter for Email Id
     */
    public void SetScore (String Score) {mPlayerScore=Score;}

    /* Author:kunal krishna
     * Description: Compares Two records
     */
    public boolean equals(Record recordTobeCompared) {
        boolean retVal = true;
        if(!this.GetFirstName().equals(recordTobeCompared.GetFirstName())) {
            return false;
        }

        if(!this.GetLastName().equals(recordTobeCompared.GetLastName())) {
            return false;
        }

        if(!this.GetScore().equals(recordTobeCompared.GetScore())) {
            return false;
        }

        return retVal;
    }

    /* Author:kunal krishna
     * Description: Converts Record to a String
     */
    public String toString() {
        return (this.GetFirstName()+"\t"+this.GetLastName()+"\t"+this.GetScore()+"\n");
    }

    /* Author:kunal krishna
     * Description: A method that returns true if the record is valid.
     * Record is Valid only if all fields are non empty and of correct format
     */
    public boolean isValidRecord() {

        return (this.mFirstName.isEmpty())?false:true;
    }

    /*Author:kunal krishna
      Description: To make the Record comparable
     * @param another
     * @return
     */
    @Override
    public int compareTo(Object another) {
        Record recPassed = (Record) another;
        int retVal = 0;
        if(this.GetFirstName().compareTo(recPassed.GetFirstName()) == 0){
            return this.GetFirstName().compareTo(recPassed.GetFirstName());
        }

        return retVal;
    }
}

