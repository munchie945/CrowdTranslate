package edu.rosehulman.manc.crowdtranslate.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import edu.rosehulman.manc.crowdtranslate.BrowseProjectsActivity;
import edu.rosehulman.manc.crowdtranslate.Constants;
import edu.rosehulman.manc.crowdtranslate.R;
import edu.rosehulman.manc.crowdtranslate.TranslateActivity;
import edu.rosehulman.manc.crowdtranslate.fragment.CreateAccountFragment;
import edu.rosehulman.manc.crowdtranslate.model.Line;
import edu.rosehulman.manc.crowdtranslate.model.Project;
import edu.rosehulman.manc.crowdtranslate.model.User;
import edu.rosehulman.manc.crowdtranslate.projectMatcher.IProjectMatcher;
import edu.rosehulman.manc.crowdtranslate.projectMatcher.RelevanceProjectMatcher;

public class LandingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String LANDING_INTENT_USER_UID_KEY = "userUid";
    public static final String EXTRA_LINE_KEY = "line";
    public static final String EXTRA_PROJECTS_KEY = "projects";

    private static final String LOG_TAG = "LandingActivity";

    private String mUserUid;
    private IProjectMatcher mProjectMatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get userUid
        mUserUid = getIntent().getStringExtra(LANDING_INTENT_USER_UID_KEY);
        new Firebase(Constants.USER_PATH).child(mUserUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        user.setKey(dataSnapshot.getKey());
                        mProjectMatcher = new RelevanceProjectMatcher(user);
                        Log.i(LOG_TAG, "Retrieved data on User with uid: " + user.getKey()
                                + " and username: " + user.getUsername());
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.e(LOG_TAG, "Firebase error on trying to retrieve uesr data: "
                                + firebaseError.getMessage());
                    }
                });

        setContentView(R.layout.activity_landing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button browseProjectsButton = (Button) findViewById(R.id.browse_projects_button);
        browseProjectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, BrowseProjectsActivity.class);
                ArrayList<Project> projects = mProjectMatcher.getProjects(10);
                intent.putParcelableArrayListExtra(EXTRA_PROJECTS_KEY, projects);
                startActivity(intent);
            }
        });

        Button translateButton = (Button) findViewById(R.id.translate_button);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, TranslateActivity.class);
                Line lineToTranslate = mProjectMatcher.getNewLine();
                intent.putExtra(EXTRA_LINE_KEY, lineToTranslate);
                startActivity(intent);
            }
        });

        Button profileButton = (Button) findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Finish this to get to preferences page
                FragmentManager fragmentManager = getFragmentManager();
                Fragment managePreferencesFragment = CreateAccountFragment.newInstance(mUserUid);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
