package edu.rosehulman.manc.crowdtranslate.model;

import java.util.ArrayList;

/*
 * Default implementation of ProjectMatcher; returns sample line.
 * Can be removed after actual implementation hooked to backend is complete.
 *
 * Created by manc on 2/1/2016.
 */
public class DefaultIProjectMatcher implements IProjectMatcher {

    @Override
    public Line getNewLine() {
        Line line = new Line("I have an apple");
        line.addTranslation(new Translation("Ich habe einen Apfel"));
        line.addTranslation(new Translation("Ich hat eine Apfel"));
        return line;
    }
}
