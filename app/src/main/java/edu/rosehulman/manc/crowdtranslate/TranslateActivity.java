package edu.rosehulman.manc.crowdtranslate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import edu.rosehulman.manc.crowdtranslate.model.Translation;

public class TranslateActivity extends AppCompatActivity {

    private TranslateAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // get extras
        Intent intent = getIntent();
        String textString = intent.getStringExtra(MainActivity.EXTRA_LINE_KEY);
        ArrayList<Translation> translations = intent.getParcelableArrayListExtra(MainActivity.EXTRA_TRANSLATIONS_KEY);



        // Set the original text string
        TextView originalLineView = (TextView) findViewById(R.id.translate_line_text_view);
        originalLineView.setText(textString);

        // recycler view and adapter
        this.mAdapter = new TranslateAdapter(translations);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.translate_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.mAdapter);
    }

}
