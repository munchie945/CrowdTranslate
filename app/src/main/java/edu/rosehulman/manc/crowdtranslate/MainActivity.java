package edu.rosehulman.manc.crowdtranslate;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import edu.rosehulman.manc.crowdtranslate.fragment.CreateAccountFragment;
import edu.rosehulman.manc.crowdtranslate.fragment.LandingScreenFragment;
import edu.rosehulman.manc.crowdtranslate.fragment.LoginFragment;

public class MainActivity extends AppCompatActivity implements OnLoginListener{

    private String mUserUid;

    private ActionBar mToolbar;
    private FragmentManager mFragmentManager;

    private Fragment mLoginFragment;
    private Fragment mLandingScreenFragment;
    private Fragment mCreateAccountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar
        Toolbar supportToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(supportToolbar);

        mToolbar = getSupportActionBar();

        // TODO: Implement credential saving
        // TODO: Change fragment to use factory methods
        // initialize Fragments
        mFragmentManager = getFragmentManager();
        mLoginFragment = new LoginFragment();

        // Launch Login if not logged in, else launch Landing Page
        Fragment fragmentToLaunch;
        if (mUserUid == null){
            fragmentToLaunch = mLoginFragment;
        } else {
            fragmentToLaunch = mLandingScreenFragment;
        }
        switchToFragment(fragmentToLaunch);
    }

    @Override
    public void onSuccessfulLogin(String userUid) {
        mUserUid = userUid;
        mFragmentManager.popBackStack();
        createFragmentsForUsers(userUid);
        switchToFragment(mLandingScreenFragment);
    }

    @Override
    public void onAccountCreated(String userUid) {
        mUserUid = userUid;
        mFragmentManager.popBackStack();
        createFragmentsForUsers(userUid);
        switchToFragment(mCreateAccountFragment);
    }

    private void createFragmentsForUsers(String uid){
        mLandingScreenFragment = LandingScreenFragment.newInstance(mUserUid);
        mCreateAccountFragment = CreateAccountFragment.newInstance(mUserUid);
    }

    private void switchToFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_container, fragment);
        fragmentTransaction.commit();
    }

}
