package edu.rosehulman.manc.crowdtranslate.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import edu.rosehulman.manc.crowdtranslate.Constants;
import edu.rosehulman.manc.crowdtranslate.R;
import edu.rosehulman.manc.crowdtranslate.fragment.LandingFragment;
import edu.rosehulman.manc.crowdtranslate.fragment.TranslateFragment;
import edu.rosehulman.manc.crowdtranslate.model.Line;
import edu.rosehulman.manc.crowdtranslate.model.User;
import edu.rosehulman.manc.crowdtranslate.projectMatcher.IProjectMatcher;
import edu.rosehulman.manc.crowdtranslate.projectMatcher.RelevanceProjectMatcher;

public class LandingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LandingFragment.OnLandingFragmentListener, TranslateFragment.OnTranslateFragmentListener{

    public static final String LANDING_INTENT_USER_UID_KEY = "userUid";
    public static final String EXTRA_LINE_KEY = "line";
    public static final String EXTRA_PROJECTS_KEY = "projects";

    private static final String LOG_TAG = "LandingActivity";

    private User mUser;
    private IProjectMatcher mProjectMatcher;
    private FragmentManager mFragmentManager;

    private Fragment mLandingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get userUid, then get user information, switch to Landing fragment
        String userUid = getIntent().getStringExtra(LANDING_INTENT_USER_UID_KEY);
        new Firebase(Constants.USER_PATH).child(userUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mUser = dataSnapshot.getValue(User.class);
                        mUser.setKey(dataSnapshot.getKey());
                        mProjectMatcher = new RelevanceProjectMatcher(mUser);

                        // set up Landing fragment
                        Fragment mLandingFragment = LandingFragment.newInstance(mUser.getUsername());
                        switchToFragment(mLandingFragment, "LANDING");

                        Log.i(LOG_TAG, "Retrieved data on user with uid: " + mUser.getKey()
                                + " and username: " + mUser.getUsername());
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

//        Button browseProjectsButton = (Button) findViewById(R.id.browse_projects_button);
//        browseProjectsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LandingActivity.this, BrowseProjectsActivity.class);
//                ArrayList<Project> projects = mProjectMatcher.getProjects(10);
//                intent.putParcelableArrayListExtra(EXTRA_PROJECTS_KEY, projects);
//                startActivity(intent);
//            }
//        });
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

    @Override
    public void onTranslateButtonPress() {
        Line newLine = mProjectMatcher.getNewLine();
        TranslateFragment translateFragment = TranslateFragment.newInstance(newLine.getKey());
        switchToFragment(translateFragment, "TRANSLATE");
    }

    @Override
    public void onStartProjectButtonPress() {
        // TODO: Start a new project
    }

    @Override
    public void onBackButtonPress() {
        // TODO: when back button on Translate is pressed
    }

    private void switchToFragment(Fragment fragment, String tag){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.landing_screen_container, fragment)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public String requestNewLine() {
        Line newLine = mProjectMatcher.getNewLine();
        return newLine.getKey();
    }
}
