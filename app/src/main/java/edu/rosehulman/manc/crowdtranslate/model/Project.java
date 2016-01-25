package edu.rosehulman.manc.crowdtranslate.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by manc on 1/17/2016.
 */
public class Project implements Parcelable{

    private String name;

    // TODO: better implementation of Lang to bind it better
    private String sourceLang; // langugae of text
    private String destLang; // language to be translated into

    // TODO: temp implementation of tags; replace with real one
    private ArrayList<String> tags;
    private ArrayList<Line> lines;

    public Project(String name, String sourceLang, String destLang){
        this.name = name;
        this.sourceLang = sourceLang;
        this.destLang = destLang;
        this.tags = new ArrayList();
        this.lines = new ArrayList();
    }

    public Project(String name, String sourceLang, String destLang, String text){
        this.name = name;
        this.sourceLang = sourceLang;
        this.destLang = destLang;
        this.tags = new ArrayList();

        this.lines = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(text,"\n");
        while(stringTokenizer.hasMoreElements()){
            lines.add(new Line(stringTokenizer.nextToken()));
        }
    }
    protected Project(Parcel in) {
        name = in.readString();
        sourceLang = in.readString();
        destLang = in.readString();
        tags = in.createStringArrayList();
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getSourceLang() {
        return sourceLang;
    }

    public String getDestLang() {
        return destLang;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(sourceLang);
        dest.writeString(destLang);
    }
}
