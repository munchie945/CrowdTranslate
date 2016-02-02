package edu.rosehulman.manc.crowdtranslate.model;

/**
 * Created by manc on 1/17/2016.
 */
public class Translation {

    private String text;
    private User user;

    public Translation (String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

}
