package edu.rosehulman.manc.crowdtranslate.projectMatcher;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.manc.crowdtranslate.model.Line;
import edu.rosehulman.manc.crowdtranslate.model.Project;

/**
 * Takes all lines in database; matches and returns most suitable line for user.
 *
 * Created by manc on 2/1/2016.
 */
public interface IProjectMatcher {

    Line getNewLine();

    ArrayList<Project> getProjects(int nProjects);

}
