package edu.rosehulman.manc.crowdtranslate.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
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
import edu.rosehulman.manc.crowdtranslate.OnLoginListener;
import edu.rosehulman.manc.crowdtranslate.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity parentActivity = getActivity();
        Firebase.setAndroidContext(getActivity());
        final OnLoginListener loginListener = (OnLoginListener) parentActivity;

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
                if (!(email.isEmpty() || password.isEmpty())){
                    baseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            Log.i("LoginFragment", "Successfully created account with Uid " + result.get("uid"));
                            loginUser(email, password, true);
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            // TODO replace with actual error, at least with Toast
                            Log.e("LoginFragment", "Account creation error happened: " + firebaseError.getMessage());
                        }
                    });
                } else {
                    // TODO:
                    Log.e("LoginFragment", "Empty email or password; ask user to try again");
                }
            }
        });
    }

    public void loginUser(String email, String password, final boolean isNewAccount){
        final Firebase baseRef = new Firebase(Constants.BASE_FIREBASE_URL);
        final OnLoginListener loginListener = (OnLoginListener) getActivity();

        baseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.i("LoginFragment", "Logged in user with Uid " + authData.getUid());
                String uid = authData.getUid();
                if (isNewAccount) {
                    loginListener.onAccountCreated(uid);
                } else {
                    loginListener.onSuccessfulLogin(authData.getUid());
                }
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // TODO replace with actual error, at least with Toast
                Log.e("LoginFragment", "Authentication error happened: " + firebaseError.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
