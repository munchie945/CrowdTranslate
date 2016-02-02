package edu.rosehulman.manc.crowdtranslate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import edu.rosehulman.manc.crowdtranslate.model.DefaultIProjectMatcher;
import edu.rosehulman.manc.crowdtranslate.model.IProjectMatcher;
import edu.rosehulman.manc.crowdtranslate.model.Line;
import edu.rosehulman.manc.crowdtranslate.model.Translation;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_LINE_KEY = "line";
    public static final String EXTRA_TRANSLATIONS_KEY = "translations";

    public IProjectMatcher projectMatcher = new DefaultIProjectMatcher();

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
                MainActivity.this.startActivity(intent);
            }
        });

        Button translateButton = (Button) findViewById(R.id.translate_button);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TranslateActivity.class);
                Line line = projectMatcher.getNewLine();

                intent.putExtra(EXTRA_LINE_KEY, line.getText());
                intent.putParcelableArrayListExtra(EXTRA_TRANSLATIONS_KEY, line.getTranslations());
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
