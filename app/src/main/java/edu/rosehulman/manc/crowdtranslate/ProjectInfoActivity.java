package edu.rosehulman.manc.crowdtranslate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.rosehulman.manc.crowdtranslate.adapter.ProjectInfoAdapter;
import edu.rosehulman.manc.crowdtranslate.model.Line;
import edu.rosehulman.manc.crowdtranslate.model.Project;

public class ProjectInfoActivity extends AppCompatActivity {

    private Project mProject;
    private int mPosition;
    private Button mTranslate;
    private ProjectInfoAdapter mAdapter;
    public static final String EXTRA_LINE_KEY = "line";
    public static final int REQUEST_TRANSLATE = 1;

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
//        mTranslate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                translateLine(v, mProject);
//            }
//        });

        this.mAdapter = new ProjectInfoAdapter(mProject,this);
        projectName.setText(mProject.getTitle());
        sourceName.setText(mProject.getSourceLang());
        targetLang.setText(mProject.getDestLang());
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.project_info_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.mAdapter);

    }

    private void translateLine(View v, Project project) {
        //TODO:
    }

    public void displayTranslate(Line line, int position){
        Intent intent = new Intent(this, TranslateActivity.class);
        Line lineToTranslate = mProject.getLine(position);
        intent.putExtra(EXTRA_LINE_KEY, lineToTranslate);
        startActivityForResult(intent, REQUEST_TRANSLATE);
//        context.start.startActivity(intent);
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
