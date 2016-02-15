package edu.rosehulman.manc.crowdtranslate.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;

import com.firebase.client.Firebase;

import java.util.ArrayList;

import edu.rosehulman.manc.crowdtranslate.BrowseProjectsActivity;
import edu.rosehulman.manc.crowdtranslate.R;
import edu.rosehulman.manc.crowdtranslate.TranslateActivity;
import edu.rosehulman.manc.crowdtranslate.model.Line;
import edu.rosehulman.manc.crowdtranslate.model.Project;
import edu.rosehulman.manc.crowdtranslate.model.User;
import edu.rosehulman.manc.crowdtranslate.projectMatcher.IProjectMatcher;
import edu.rosehulman.manc.crowdtranslate.projectMatcher.RelevanceProjectMatcher;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LandingScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LandingScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandingScreenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String EXTRA_LINE_KEY = "line";
    public static final String EXTRA_PROJECTS_KEY = "projects";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private IProjectMatcher projectMatcher;

    public LandingScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LandingScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LandingScreenFragment newInstance(String param1, String param2) {
        LandingScreenFragment fragment = new LandingScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getActivity());

        // TODO: replace with code to get real user
        User user = new User();
        ArrayList<String> userTags = new ArrayList<>();
        userTags.add("music");
        userTags.add("art");
        user.setUsername("inlocoparentis");
        user.setTagArray(userTags);
        projectMatcher = new RelevanceProjectMatcher(user);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final Activity parentActivity = getActivity();

        // TODO: add logic for other buttons when their activities are done
        Button browseProjectsButton = (Button) view.findViewById(R.id.browse_projects_button);
        browseProjectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parentActivity, BrowseProjectsActivity.class);
                ArrayList<Project> projects = projectMatcher.getProjects(10);
                intent.putParcelableArrayListExtra(EXTRA_PROJECTS_KEY, projects);
                parentActivity.startActivity(intent);
            }
        });


        Button translateButton = (Button) view.findViewById(R.id.translate_button);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parentActivity, TranslateActivity.class);
                Line lineToTranslate = projectMatcher.getNewLine();
                intent.putExtra(EXTRA_LINE_KEY, lineToTranslate);
                parentActivity.startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing_screen, container, false);
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
