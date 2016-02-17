package edu.rosehulman.manc.crowdtranslate.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import edu.rosehulman.manc.crowdtranslate.R;
import edu.rosehulman.manc.crowdtranslate.fragment.CreateAccountFragment;
import edu.rosehulman.manc.crowdtranslate.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentListener,
        CreateAccountFragment.OnCreateAccountFragmentListener {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar
        Toolbar supportToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(supportToolbar);

        // TODO: Implement credential saving; will need change in Login
        mFragmentManager = getFragmentManager();
        Fragment loginFragment = LoginFragment.newInstance();
        switchToFragment(loginFragment);
    }

    @Override
    /**
     * On normal login, go to Landing screen with the authed user's UID
     */
    public void onNormalLogin(String userUid) {
        mFragmentManager.popBackStack();
        startLandingActivity(userUid);
    }

    @Override
    /**
     * On new account login, start up account settings, then go to Landing screen
     */
    public void onNewAccountLogin(String userUid) {
        Fragment createAccountFragment = CreateAccountFragment.newInstance(userUid);
        switchToFragment(createAccountFragment);
    }

    /**
     * After new account preferences are initialized, proceed to Landing screen
     * @param userUid id of user
     */
    @Override
    public void onSettingsChanged(String userUid) {
        startLandingActivity(userUid);
    }

    private void switchToFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_container, fragment);
        fragmentTransaction.commit();
    }

    private void startLandingActivity(String userUid){
        Intent intent = new Intent(this, LandingActivity.class);
        intent.putExtra(LandingActivity.LANDING_INTENT_USER_UID_KEY, userUid);
        startActivity(intent);
    }
}
