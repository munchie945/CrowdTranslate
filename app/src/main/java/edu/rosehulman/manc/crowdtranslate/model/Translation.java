package edu.rosehulman.manc.crowdtranslate.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by manc on 1/17/2016.
 */
public class Translation implements Parcelable{

    public static final Creator<Translation> CREATOR = new TranslationCreator();

    private String text;
    private int numVotes;


    public Translation (String text){
        this.text = text;
        this.numVotes = 0;
    }

    // Constructor for Parcelable implementation
    protected Translation(Parcel in) {
        text = in.readString();
        numVotes = in.readInt();
    }

    public String getText(){
        return text;
    }

    public int getNumVotes(){
        return numVotes;
    }

    public void incrementNumVotes(){
        this.numVotes++;
    }

    // Parceleable implementation from this point on
    @Override
    public int describeContents() {
        // Note: not used
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeInt(numVotes);
    }

    private static class TranslationCreator implements Parcelable.Creator<Translation> {

        @Override
        public Translation createFromParcel(Parcel in) {
            return new Translation(in);
        }

        @Override
        public Translation[] newArray(int size) {
            return new Translation[size];
        }

    }
}


