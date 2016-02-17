package edu.rosehulman.manc.crowdtranslate.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import edu.rosehulman.manc.crowdtranslate.Constants;
import edu.rosehulman.manc.crowdtranslate.R;
import edu.rosehulman.manc.crowdtranslate.Utils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLoginFragmentListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private Context mContext;
    private OnLoginFragmentListener mFragmentListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Firebase.setAndroidContext(getActivity());

        final Firebase baseRef = new Firebase(Constants.BASE_FIREBASE_URL);
        final EditText emailEditText = (EditText) view.findViewById(R.id.login_email_edit_text);
        final EditText passwordEditText = (EditText) view.findViewById(R.id.login_password_edit_text);

        // Login
        Button loginButton = (Button) view.findViewById(R.id.login_submit_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginUser(email, password, false);
            }
        });

        // Create account
        Button createAccountButton = (Button) view.findViewById(R.id.login_new_account_button);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                if (!(email.isEmpty() || password.isEmpty())) {
                    baseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            Log.i("LoginFragment", "Successfully created account with Uid " + result.get("uid"));
                            loginUser(email, password, true);
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Utils.makeToast(LoginFragment.this, "Problem with account creation, please try again.");
                            Log.e("LoginFragment", "Account creation error happened: " + firebaseError.getMessage());
                        }
                    });
                } else {
                    Utils.makeToast(LoginFragment.this, "Email and password cannot be empty.");
                }
            }
        });
    }

    public void loginUser(String email, String password, final boolean isNewAccount){
        final Firebase baseRef = new Firebase(Constants.BASE_FIREBASE_URL);

        baseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.i("LoginFragment", "Logged in user with Uid " + authData.getUid());
                String uid = authData.getUid();
                if (isNewAccount) {
                    mFragmentListener.onNewAccountLogin(uid);
                } else {
                    mFragmentListener.onNormalLogin(authData.getUid());
                }
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Utils.makeToast(LoginFragment.this, "A problem occurred when signing you win. Please try again!");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    /**
     * Only called on SDK level 23 or above
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        saveContextOnAttach(context);
    }

    /**
     * Note: Deprecated, but sdk 23 and below calls this instead of the one w/ context
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        saveContextOnAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    private void saveContextOnAttach(Context context){
        if (context instanceof OnLoginFragmentListener) {
            mContext = context;
            mFragmentListener = (OnLoginFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginFragmentListener");
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnLoginFragmentListener {

        public void onNormalLogin(String userUid);

        /**
         * New account was created (and logged into)
         * @param userUid UID of current user
         */
        public void onNewAccountLogin(String userUid);
    }
}
