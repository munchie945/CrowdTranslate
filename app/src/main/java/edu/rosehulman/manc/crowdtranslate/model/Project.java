package edu.rosehulman.manc.crowdtranslate.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Arrays;
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
    private ArrayList<Line> lines = new ArrayList<Line>();

    @JsonIgnore
    private double relevance; // how relevant project is to user

    // TODO: Implement actual tag system
//    private ArrayList<String> tags;

    public Project(){
        // empty constructor for Jackson
        this.lines = new ArrayList<>();
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
        tags = new ArrayList<>();
        lines = new ArrayList<>();
        setKey(in.readString());
        title = in.readString();
        sourceLang = in.readString();
        destLang = in.readString();
        relevance = in.readDouble();

        in.readStringList(tags);
        in.readTypedList(lines, Line.CREATOR);
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

    public String getLinesString(){
        String text = "";
        if(!lines.isEmpty()){
            for (Line l: lines){
                text += l.getText()+"\n";
            }
            text=text.substring(0, text.length()-1);
        }
        return text;
    }

    public void setKey(String key) {
        this.key = key;
        if(!lines.isEmpty()){
        for(Line l: lines){
            l.setProjectKey(key);
        }
        }
    }
    public String getLineKey(int position){
        return lines.get(position).getKey();
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

    private double getRelevance(){
        // only for use in the private comparator
        return this.relevance;
    }

    public String getTagString(){
        String arrayString = Arrays.toString(tags.toArray());
        return arrayString.substring(1, arrayString.length() - 1); // remove '[' and ']'
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
        dest.writeDouble(relevance);
        dest.writeStringList(tags);
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
