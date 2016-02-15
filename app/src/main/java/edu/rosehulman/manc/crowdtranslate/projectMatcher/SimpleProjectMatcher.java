package edu.rosehulman.manc.crowdtranslate.projectMatcher;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

import edu.rosehulman.manc.crowdtranslate.Constants;
import edu.rosehulman.manc.crowdtranslate.model.Line;
import edu.rosehulman.manc.crowdtranslate.model.Project;
import edu.rosehulman.manc.crowdtranslate.model.Translation;

/**
 * Only partially matches language criteria (hard coded), with no relevance ranking.
 * Done as first step to integration with database
 * Created by manc on 2/7/2016.
 */
public class SimpleProjectMatcher implements IProjectMatcher {

    private static final int NUM_PROJECTS = 10;

    private Firebase baseRef;
    private Firebase projectRef;
    private Firebase lineRef;
    private Firebase translationRef;
    private String sourceLang = "german";
    private String destLang = "english";

    private ArrayList<Project> mProjects = new ArrayList<>();

    public SimpleProjectMatcher(Firebase baseRef) {
        this.baseRef = baseRef;
        projectRef = baseRef.child(Constants.PROJECT_KEY);
        lineRef = baseRef.child(Constants.LINE_KEY);
        translationRef = baseRef.child(Constants.TRANSLATION_KEY);

        // TODO: How to have two constraints on the same query?
        projectRef.orderByChild("sourceLang").equalTo(sourceLang)
//                .orderByChild("dest_lang").equalTo(destLang)
                .limitToFirst(NUM_PROJECTS)
                .addListenerForSingleValueEvent(new ProjectValueListener());
    }

    @Override
    public Line getNewLine() {
        Project chosenProject = mProjects.get(0);
        return chosenProject.getLines().get(0);
    }

    @Override
    public ArrayList<Project> getProjects(int nProjects) {
        // TODO: make it so we can track which projects have been returned, and automatically update with more projects
        return mProjects;
    }

    private class ProjectValueListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("SimpleProjectMatcher", dataSnapshot.getRef().getPath() + " " + dataSnapshot.getChildrenCount());
            Iterator<DataSnapshot> currProjects = dataSnapshot.getChildren().iterator();
            for (int i = 0; i < dataSnapshot.getChildrenCount(); i++){
                DataSnapshot projectSnapshot = currProjects.next();
                Project currProject = projectSnapshot.getValue(Project.class);
                mProjects.add(currProject);

                // get lines
                String currProjectKey = projectSnapshot.getKey();
                lineRef.orderByChild("projectKey")
                        .equalTo(currProjectKey)
                        .addValueEventListener(new LineValueListener(currProject));
            }
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
            Log.d("SimpleProjectMatcher", dataSnapshot.getRef().getPath() + " " + dataSnapshot.getChildrenCount());
            Iterable<DataSnapshot> rawLines = dataSnapshot.getChildren();
            for (DataSnapshot rawLine: rawLines){
                // TODO: might be out of order right now
                Line currLine = rawLine.getValue(Line.class);
                currLine.setKey(rawLine.getKey());
                parentProject.addLine(currLine);
                translationRef.orderByChild("lineKey")
                        .equalTo(rawLine.getKey())
                        .addValueEventListener(new TranslationValueListener(currLine));
            }
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }

    private class TranslationValueListener implements ValueEventListener{

        private Line projectLine;

        public TranslationValueListener(Line projectLine) {
            this.projectLine = projectLine;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("SimpleProjectMatcher", dataSnapshot.getRef().getPath() + " " + dataSnapshot.getChildrenCount());
            Iterable<DataSnapshot> rawTranslations = dataSnapshot.getChildren();
            for (DataSnapshot rawTranslation: rawTranslations){
                Translation translation = rawTranslation.getValue(Translation.class);
                translation.setKey(rawTranslation.getKey());
                projectLine.addTranslation(translation);
            }
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            // empty
        }
    }
}
