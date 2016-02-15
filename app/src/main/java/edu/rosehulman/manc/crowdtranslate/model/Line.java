package edu.rosehulman.manc.crowdtranslate.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

/**
 * Created by manc on 1/17/2016.
 */
public class Line implements Parcelable{

    public static final Creator<Line> CREATOR = new LineCreator();

    @JsonIgnore
    private String key;

    // directly saved fields in database
    private int position;
    private String projectKey;
    private String text;

    // things you'll have to get from a query
    private ArrayList<Translation> translations = new ArrayList<>();

    // empty constructor for Jackson
    public Line(){
    }

    public Line(String lineOfText, String projectKey){
        this.text = lineOfText;
        this.projectKey = projectKey;
//        this.position = position;
        this.translations = new ArrayList<>();
    }
    public Line(String lineOfText){
        this.text = lineOfText;
        this.translations = new ArrayList<>();
    }
    protected Line(Parcel in) {
        key = in.readString();
        position = in.readInt();
        projectKey = in.readString();
        text = in.readString();
        in.readTypedList(translations, Translation.CREATOR);
    }

    // getters
    public String getKey() {
        return key;
    }

    public int getPosition() {
        return position;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public String getText() {
        return text;
    }

    public ArrayList<Translation> getTranslations() {
        return translations;
    }


    // setters
    public void setKey(String key) {
        this.key = key;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTranslations(ArrayList<Translation> translations) {
        this.translations = translations;
    }


    public void addTranslation(Translation t){
        this.translations.add(t);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeInt(position);
        dest.writeString(projectKey);
        dest.writeString(text);
        dest.writeTypedList(translations);
    }

    private static class LineCreator implements Creator<Line> {
        @Override
        public Line createFromParcel(Parcel source) {
            return new Line(source);
        }

        @Override
        public Line[] newArray(int size) {
            return new Line[size];
        }
    }
}
