package edu.rosehulman.manc.crowdtranslate.model;

import java.util.ArrayList;

/**
 * Created by manc on 1/17/2016.
 */
public class Line {

    // TODO: look up Unicode encoding for forein languages
    private String text;
    private ArrayList<Translation> translations;

    public Line(String lineOfText){
        this.text = lineOfText;
        this.translations = new ArrayList();
    }

    public String getText() {
        return text;
    }

    public ArrayList<Translation> getTranslations() {
        return translations;
    }

    public void addTranslation(Translation t){
        this.translations.add(t);
    }

}
