package edu.rosehulman.manc.crowdtranslate;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;

import java.util.ArrayList;

import edu.rosehulman.manc.crowdtranslate.fragment.LandingScreenFragment;
import edu.rosehulman.manc.crowdtranslate.fragment.LoginFragment;
import edu.rosehulman.manc.crowdtranslate.model.Project;
import edu.rosehulman.manc.crowdtranslate.model.User;
import edu.rosehulman.manc.crowdtranslate.projectMatcher.IProjectMatcher;
import edu.rosehulman.manc.crowdtranslate.model.Line;
import edu.rosehulman.manc.crowdtranslate.projectMatcher.RelevanceProjectMatcher;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // open login fragment on start up
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        LandingScreenFragment landingFragment = new LandingScreenFragment();
        fragmentTransaction.replace(R.id.main_activity_container, landingFragment);
        fragmentTransaction.commit();

    }
}
