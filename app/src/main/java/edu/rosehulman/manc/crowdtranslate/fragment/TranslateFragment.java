package edu.rosehulman.manc.crowdtranslate.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import edu.rosehulman.manc.crowdtranslate.Constants;
import edu.rosehulman.manc.crowdtranslate.R;
import edu.rosehulman.manc.crowdtranslate.Utils;
import edu.rosehulman.manc.crowdtranslate.adapter.TranslateAdapter;
import edu.rosehulman.manc.crowdtranslate.model.Line;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTranslateFragmentListener} interface
 * to handle interaction events.
 * Use the {@link TranslateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TranslateFragment extends Fragment {

    private static final String LINE_KEY_KEY = "mLineKey";
    private static final String LOG_TAG = "Translate";

    // TODO: Rename and change types of parameters
    private String mLineKey;
    private TranslateAdapter mAdapter;

    private Activity mParentActivity;
    private OnTranslateFragmentListener mListener;
    private TextView mOriginalLineView;
    private RecyclerView mRecyclerView;

    public TranslateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lineKey key of line to be translated
     * @return A new instance of fragment TranslateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TranslateFragment newInstance(String lineKey) {
        TranslateFragment fragment = new TranslateFragment();
        Bundle args = new Bundle();
        args.putString(LINE_KEY_KEY, lineKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mLineKey = getArguments().getString(LINE_KEY_KEY);
        }
        if (mParentActivity != null){
            ActionBar toolbar = getActivity().getActionBar();
            if (toolbar != null){
                toolbar.setTitle("Translate");
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_translate, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set the original text string
        mOriginalLineView = (TextView) view.findViewById(R.id.translate_line_text_view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.translate_recycler_view);
        populateData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actionsOnAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParentActivity = activity;
        actionsOnAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Since there is only one item, "Next Translation"
        Log.i(LOG_TAG, "Translate fragment requesting new line from parentActivity");
        mLineKey = mListener.requestNewLine();
        populateData();
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem nextTranslation = menu.add("Next");
        nextTranslation.setIcon(R.drawable.ic_arrow_forward_black_24dp);
        nextTranslation.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    private void actionsOnAttach(Context context){
        if (context instanceof OnTranslateFragmentListener) {
            mListener = (OnTranslateFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTranslateFragmentListener");
        }
    }

    /**
     * Populate line and translation data based on current lineKey
     */
    private void populateData() {
        new Firebase(Constants.LINE_PATH).child(mLineKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Line currLine = dataSnapshot.getValue(Line.class);
                currLine.setKey(dataSnapshot.getKey());
                mOriginalLineView.setText(currLine.getText());

                mAdapter = new TranslateAdapter(currLine);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Utils.makeToast(TranslateFragment.this, "A problem occurred when signing you win. Please try again!");
            }
        });
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
    public interface OnTranslateFragmentListener {

        /**
         * @return key of new line to be translated
         */
        String requestNewLine();
    }
}
