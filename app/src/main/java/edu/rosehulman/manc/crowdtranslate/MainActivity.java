package edu.rosehulman.manc.crowdtranslate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.manc.crowdtranslate.model.Project;
import edu.rosehulman.manc.crowdtranslate.model.User;
import edu.rosehulman.manc.crowdtranslate.projectMatcher.DefaultIProjectMatcher;
import edu.rosehulman.manc.crowdtranslate.projectMatcher.IProjectMatcher;
import edu.rosehulman.manc.crowdtranslate.model.Line;
import edu.rosehulman.manc.crowdtranslate.projectMatcher.RelevanceProjectMatcher;
import edu.rosehulman.manc.crowdtranslate.projectMatcher.SimpleProjectMatcher;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_LINE_KEY = "line";
    public static final String EXTRA_PROJECTS_KEY = "projects";

    public IProjectMatcher projectMatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: add logic for other buttons when their activities are done
        Button browseProjectsButton = (Button) findViewById(R.id.browse_projects_button);
        browseProjectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BrowseProjectsActivity.class);
                ArrayList<Project> projects = projectMatcher.getProjects(10);
                intent.putParcelableArrayListExtra(EXTRA_PROJECTS_KEY, projects);
                MainActivity.this.startActivity(intent);
            }
        });

        // TODO: replace with code to get real user
        User user = new User();
        ArrayList<String> userTags = new ArrayList<>();
        userTags.add("music");
        userTags.add("art");
        user.setUsername("inlocoparentis");
        user.setTagArray(userTags);

        Firebase.setAndroidContext(this);
        projectMatcher = new RelevanceProjectMatcher(user);

        Button translateButton = (Button) findViewById(R.id.translate_button);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TranslateActivity.class);
                Line lineToTranslate = projectMatcher.getNewLine();
                intent.putExtra(EXTRA_LINE_KEY, lineToTranslate);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
