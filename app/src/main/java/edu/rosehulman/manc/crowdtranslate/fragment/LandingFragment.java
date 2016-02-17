package edu.rosehulman.manc.crowdtranslate.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.rosehulman.manc.crowdtranslate.BrowseProjectsActivity;
import edu.rosehulman.manc.crowdtranslate.R;
import edu.rosehulman.manc.crowdtranslate.model.Line;
import edu.rosehulman.manc.crowdtranslate.model.Project;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLandingFragmentListener} interface
 * to handle interaction events.
 * Use the {@link LandingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandingFragment extends Fragment {

    private static String USERNAME_KEY = "username";

    private String mUsername;
    private OnLandingFragmentListener mListener;

    public LandingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username Username of currently logged-in user
     * @return A new instance of fragment LandingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LandingFragment newInstance(String username) {
        LandingFragment fragment = new LandingFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME_KEY, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mUsername = getArguments().getString(USERNAME_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set welcome string
        TextView headerTextView = (TextView) view.findViewById(R.id.landing_fragment_welcome_header);
        String welcomeString = getResources().getString(R.string.welcome_format_string, mUsername);
        headerTextView.setText(welcomeString);

        Button browseProjectsButton = (Button) view.findViewById(R.id.browse_projects_button);
        browseProjectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: DELETE BUTTON OR DO SOMETHING
            }
        });

        Button translateButton = (Button) view.findViewById(R.id.translate_button);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTranslateButtonPress();
            }
        });

        Button profileButton = (Button) view.findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// TODO: DELETE BUTTON OR DO SOMETHING

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actionsOnAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        actionsOnAttach(activity);
    }

    private void actionsOnAttach(Context context){
        if (context instanceof OnLandingFragmentListener) {
            mListener = (OnLandingFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLandingFragmentListener");
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
     */
    public interface OnLandingFragmentListener {
        void onTranslateButtonPress();
        void onStartProjectButtonPress();
        void onBackButtonPress();
    }
}
