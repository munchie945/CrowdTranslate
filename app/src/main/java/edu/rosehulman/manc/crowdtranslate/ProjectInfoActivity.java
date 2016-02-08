package edu.rosehulman.manc.crowdtranslate;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import edu.rosehulman.manc.crowdtranslate.model.Project;

public class ProjectInfoActivity extends AppCompatActivity {

    private Project mProject;
    private int mPosition;
    private Button mTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        TextView projectName = (TextView)findViewById(R.id.info_project_name);
        TextView sourceName = (TextView)findViewById(R.id.info_source_lang);
        TextView targetLang = (TextView)findViewById(R.id.info_target_lang);

        Intent intent = getIntent();
        mPosition = intent.getIntExtra(BrowseProjectsActivity.EXTRA_PROJECT_INDEX, 0);
        mProject =  intent.getExtras().getParcelable(BrowseProjectsActivity.EXTRA_PROJECT);
        mTranslate = (Button) findViewById(R.id.info_translate_button);
        mTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateLine(v, mProject);
            }
        });


        projectName.setText(mProject.getTitle());
        sourceName.setText(mProject.getSourceLang());
        targetLang.setText(mProject.getDestLang());

    }

    private void translateLine(View v, Project project) {
        //TODO:
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra(BrowseProjectsActivity.EXTRA_PROJECT_INDEX, mPosition);
        intent.putExtra(BrowseProjectsActivity.EXTRA_PROJECT, mProject);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }
}
