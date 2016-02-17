package edu.rosehulman.manc.crowdtranslate.projectMatcher;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import edu.rosehulman.manc.crowdtranslate.Constants;
import edu.rosehulman.manc.crowdtranslate.model.Line;
import edu.rosehulman.manc.crowdtranslate.model.Project;
import edu.rosehulman.manc.crowdtranslate.model.User;

/**
 * Implements relevance ranking, but not the language matching part
 * New bit: Translations are no longer loaded; that's done in TranslateAdapter
 * Because Line doesn't need to know about its own translations
 *
 * Created by manc on 2/14/2016.
 */
public class RelevanceProjectMatcher implements IProjectMatcher {

    private User user;
    private Firebase baseRef;
    private ArrayList<Project> projectList = new ArrayList<>();

    // Last lines and projects returned
    private int lastLineInd;
    private int lastProjectInd;

    public RelevanceProjectMatcher(User user){
        lastLineInd = 0;
        lastProjectInd = 0;

        this.user = user;
        this.baseRef = new Firebase(Constants.BASE_FIREBASE_URL);
        Firebase projectRef = baseRef.child(Constants.PROJECT_KEY);
        projectRef.addListenerForSingleValueEvent(new ProjectValueListener());
    }

    @Override
    // TODO: Throw error if no line?
    public Line getNewLine() {
        Project project = projectList.get(lastProjectInd);
        ArrayList<Line> currLines = project.getLines();
        lastLineInd++;
        if (lastLineInd < currLines.size()){
            return currLines.get(lastLineInd);
        } else {
            lastLineInd = 0;
            lastProjectInd++;

            // TODO: THIS WILL THROW ERROR IF LAST PROJECT IS REACHED
            Project newProject = projectList.get(lastProjectInd);
            return newProject.getLines().get(lastLineInd);
        }
    }

    @Override
    public ArrayList<Project> getProjects(int nProjects) {
        // TODO: make it so we can track which projects have been returned, and automatically update with more projects
        return projectList;
    }

    private class ProjectValueListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("RelevanceProjectMatcher", "Found " + dataSnapshot.getChildrenCount() + " projects");
            Iterator<DataSnapshot> currProjects = dataSnapshot.getChildren().iterator();
            Firebase lineRef = baseRef.child(Constants.LINE_KEY);

            for (int i = 0; i < dataSnapshot.getChildrenCount(); i++){
                DataSnapshot projectSnapshot = currProjects.next();
                String currProjectKey = projectSnapshot.getKey();
                Project currProject = projectSnapshot.getValue(Project.class);
                currProject.setKey(currProjectKey);
                currProject.computeAndSaveRelevance(user.getTagSet());
                projectList.add(currProject);

                // TODO: Refactor; we probably don't need this here
                // add lines to project
                lineRef.orderByChild("projectKey")
                        .equalTo(currProjectKey)
                        .addValueEventListener(new LineValueListener(currProject));
            }

            Collections.sort(projectList, new Project.RelevanceComparator());
            Collections.reverse(projectList); // want descending order
            Log.d("RelevanceProjectMatcher", "Finished sorting " + projectList.size() + " projects.");
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            // empty; not used
        }
    }

    private class LineValueListener implements ValueEventListener{

        private Project parentProject;

        public LineValueListener(Project parentProject) {
            this.parentProject = parentProject;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterable<DataSnapshot> rawLines = dataSnapshot.getChildren();
            for (DataSnapshot rawLine: rawLines){
                // TODO: might be out of order right now
                Line currLine = rawLine.getValue(Line.class);
                currLine.setKey(rawLine.getKey());
                parentProject.addLine(currLine);
            }

            String msg = String.format("Added %d strings to project with title \"%s\"", dataSnapshot.getChildrenCount(), parentProject.getTitle());
            Log.d("RelevanceProjectMatcher", msg);

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            // empty; not used
        }
    }


}
