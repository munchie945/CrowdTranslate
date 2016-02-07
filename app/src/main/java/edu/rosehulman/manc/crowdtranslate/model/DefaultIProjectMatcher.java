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
        Translation t1 = new Translation("Ich habe einen Apfel");
        Translation t2 = new Translation("Ich hat eine Apfel");
        t1.setOriginalLine("I have an apple");
        t2.setOriginalLine("I have an apple");
        line.addTranslation(t1);
        line.addTranslation(t2);
        return line;
    }
}
