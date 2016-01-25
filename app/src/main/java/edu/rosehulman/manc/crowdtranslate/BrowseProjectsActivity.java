package edu.rosehulman.manc.crowdtranslate;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.StringTokenizer;

import edu.rosehulman.manc.crowdtranslate.model.Project;

public class BrowseProjectsActivity extends AppCompatActivity {

    ProjectListAdapter mProjectListAdapter;
    public static final int REQUEST_DETAIL_INFO = 1;
    public static final String EXTRA_PROJECT = Project.class.getCanonicalName();
    public static final String EXTRA_PROJECT_INDEX = "EXTRA_PROJECT_INDEX";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_projects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: match with project and go to translate page from here
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProject();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.browse_projects_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mProjectListAdapter = new ProjectListAdapter(this);
        recyclerView.setAdapter(mProjectListAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addProject(){
        DialogFragment df = new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_project, null, false);

                builder.setView(view);
                Locale[] locales = Locale.getAvailableLocales();
                ArrayList<String> langName = new ArrayList<String>();
                for(Locale l:locales){
                    langName.add(l.getDisplayName());
                }
                ArrayAdapter<String> lang = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line,langName);
                lang.sort(new Comparator<String>() {

                    @Override
                    public int compare(String lhs, String rhs) {
                        return lhs.compareToIgnoreCase(rhs);
                    }
                });
                final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.source_language);
                autoCompleteTextView.setAdapter(lang);
                final AutoCompleteTextView targetLang = (AutoCompleteTextView) view.findViewById(R.id.target_language);
                targetLang.setAdapter(lang);
                final TextView projectName = (TextView) view.findViewById(R.id.project_name);
                final TextView translateText = (TextView) view.findViewById(R.id.input_text);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProjectListAdapter.addProject(new Project(projectName.getText().toString(), autoCompleteTextView.getText().toString(), targetLang.getText().toString(), translateText.getText().toString()));
                    }
                });
                builder.setNegativeButton(android.R.string.cancel,null);


                return builder.create();
            }
        };
        df.show(getSupportFragmentManager(), "add");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_DETAIL_INFO && requestCode == Activity.RESULT_OK){
            int mPosition = data.getIntExtra(BrowseProjectsActivity.EXTRA_PROJECT_INDEX, 0);
            Project mProject =  data.getExtras().getParcelable(BrowseProjectsActivity.EXTRA_PROJECT);
            mProjectListAdapter.replaceProject(mProject,mPosition);
        }
    }

    public void displayInfo(Project project, int position) {
        Intent projectInfoIntent = new Intent(this, ProjectInfoActivity.class);
        projectInfoIntent.putExtra(EXTRA_PROJECT, project);
        projectInfoIntent.putExtra(EXTRA_PROJECT_INDEX, position);

        startActivityForResult(projectInfoIntent, REQUEST_DETAIL_INFO);
    }
}
