package edu.rosehulman.manc.crowdtranslate.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by manc on 1/17/2016.
 */
public class Project implements Parcelable{

    public static Creator<Project> CREATOR = new ProjectCreator();

    @JsonIgnore
    private String key;

    // TODO: better implementation of Lang to restrict options
    private String title;
    private String sourceLang; // language of text
    private String destLang; // language to be translated into
    private List<String> tags;

    @JsonIgnore
    private ArrayList<Line> lines;

    @JsonIgnore
    private double relevance; // how relevant project is to user

    // TODO: Implement actual tag system
//    private ArrayList<String> tags;

    public Project(){
        // empty constructor for Jackson
        lines = new ArrayList<>();
    }

    public Project(String name, String sourceLang, String destLang){
        this.title = name;
        this.sourceLang = sourceLang;
        this.destLang = destLang;
        this.lines = new ArrayList<>();
    }

    public Project(String name, String sourceLang, String destLang, String text){
        this.title = name;
        this.sourceLang = sourceLang;
        this.destLang = destLang;

        this.lines = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(text,"\n");
        while(stringTokenizer.hasMoreElements()){
            lines.add(new Line(stringTokenizer.nextToken()));
        }
    }
    protected Project(Parcel in) {
        key = in.readString();
        title = in.readString();
        sourceLang = in.readString();
        destLang = in.readString();

        // TODO: SUPER BAD FORM; FIND OUT ACTUAL ROOT CAUSE
        try {
            in.readTypedList(lines, Line.CREATOR);
        } catch (Exception e){
            // do nothing
        }

    }

    public String getTitle() {
        return title;
    }

    public String getSourceLang() {
        return sourceLang;
    }

    public String getDestLang() {
        return destLang;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    private double getRelevance(){
        return this.relevance;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }

    public void setDestLang(String destLang) {
        this.destLang = destLang;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addLine(Line line){
        this.lines.add(line);
    }

    /**
     * Compute and save relevance of project to the set of tags given
     * This rating only has meaning when the same tagSet is used across projects,
     * as is our usage.
     * @param tagSet
     */
    public void computeAndSaveRelevance(Set<String> tagSet){
        double nMatched = 0;
        for (String mTag: tags){
            if (tagSet.contains(mTag)) nMatched++;
        }
        this.relevance = nMatched / tags.size();

        // Logging
        String msg = String.format("Project with title %s has relevance %f", title, relevance);
        Log.d("Project", msg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(title);
        dest.writeString(sourceLang);
        dest.writeString(destLang);
        dest.writeTypedList(lines);
    }

    public static class RelevanceComparator implements Comparator<Project>{

        @Override
        public int compare(Project lhs, Project rhs) {
            // Times 10 is just to make sure sign is preserved
            return (int) (10 * (lhs.getRelevance() - rhs.getRelevance()));
        }
    }

    private static class ProjectCreator implements Creator<Project> {
        @Override
        public Project createFromParcel(Parcel source) {
            return new Project(source);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    }
}
