package edu.rosehulman.manc.crowdtranslate.model;

import java.util.ArrayList;

/**
 * Created by manc on 1/17/2016.
 */
public class Project {

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

}
