package edu.rosehulman.manc.crowdtranslate.model;

/**
 * Takes all lines in database; matches and returns most suitable line for user.
 *
 * Created by manc on 2/1/2016.
 */
public interface IProjectMatcher {

    Line getNewLine();

}
