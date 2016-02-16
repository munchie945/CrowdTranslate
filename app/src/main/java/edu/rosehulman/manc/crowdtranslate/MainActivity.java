package edu.rosehulman.manc.crowdtranslate;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.rosehulman.manc.crowdtranslate.fragment.LandingScreenFragment;
import edu.rosehulman.manc.crowdtranslate.fragment.LoginFragment;

public class MainActivity extends AppCompatActivity implements OnLoginListener{

    private String mUserUid;
    private FragmentManager mFragmentManager;

    private Fragment mLoginFragment;
    private Fragment mLandingScreenFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Implement credential saving
        // initialize Fragments
        mFragmentManager = getFragmentManager();
        mLoginFragment = new LoginFragment();
        mLandingScreenFragment = new LandingScreenFragment();

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
    public void onLogin(String userUid) {
        if (userUid != null) {
            mUserUid = userUid;
            switchToFragment(mLandingScreenFragment);
        }
    }

    private void switchToFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_container, fragment);
        fragmentTransaction.commit();
    }
}
