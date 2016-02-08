package edu.rosehulman.manc.crowdtranslate.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by manc on 1/17/2016.
 */
public class Translation implements Parcelable{

    public static final Creator<Translation> CREATOR = new TranslationCreator();

    @JsonIgnore
    private String key;

    private String lineKey;
    private int numVotes;
    private String text;

    // default empty constructor for Jackson
    public Translation (){}

    public Translation (String text){
        this.text = text;
        this.numVotes = 0;
    }

    // Constructor for Parcelable implementation
    protected Translation(Parcel in) {
        key = in.readString();
        lineKey = in.readString();
        numVotes = in.readInt();
        text = in.readString();
    }

    public String getKey() {
        return key;
    }

    public String getLineKey() {
        return lineKey;
    }

    public int getNumVotes(){
        return numVotes;
    }

    // Getters and setters
    public String getText(){
        return text;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLineKey(String lineKey) {
        this.lineKey = lineKey;
    }

    public void setNumVotes(int numVotes) {
        this.numVotes = numVotes;
    }

    public void setText(String text) {
        this.text = text;
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
        dest.writeString(key);
        dest.writeString(lineKey);
        dest.writeInt(numVotes);
        dest.writeString(text);
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


