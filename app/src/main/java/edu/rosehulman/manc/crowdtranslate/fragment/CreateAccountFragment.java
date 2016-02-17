package edu.rosehulman.manc.crowdtranslate.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import edu.rosehulman.manc.crowdtranslate.Constants;
import edu.rosehulman.manc.crowdtranslate.R;
import edu.rosehulman.manc.crowdtranslate.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCreateAccountFragmentListener} interface
 * to handle interaction events.
 * Use the {@link CreateAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateAccountFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_UID_KEY = "userUid";
    private static final String LOG_TAG = "CreateAccount";

    private Context mContext;
    private OnCreateAccountFragmentListener mFragmentListener;

    private String mUserUid;
    private Firebase mLanguagesRef;
    private Firebase mTagsRef;

    private ArrayAdapter<String> mLanguageAdapter;
    private ArrayAdapter<String> mTagAdapter;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userUid UID of curr authed user.
     * @return A new instance of fragment CreateAccountFragment.
     */
    public static CreateAccountFragment newInstance(String userUid) {
        CreateAccountFragment fragment = new CreateAccountFragment();
        Bundle args = new Bundle();
        args.putString(USER_UID_KEY, userUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserUid = getArguments().getString(USER_UID_KEY);
        }

        // Set up firebase
        // TODO: HARDCODED KEY STRINGS
        Firebase userRef = new Firebase(Constants.USER_PATH).child(mUserUid);
        mLanguagesRef = userRef.child("languages");
        mTagsRef = userRef.child("tags");

        // Toolbar
        setHasOptionsMenu(true);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        ActionBar toolbar = parentActivity.getSupportActionBar();
        if (toolbar != null){
            toolbar.setTitle("Create Account");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem submit = menu.add("Test");
        // replace with real thing
        submit.setIcon(R.drawable.ic_done_black_24dp);
        submit.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // since there is only one item
        // get all items from languageAdapter
        ArrayList<String> languages = adapterToArrayList(mLanguageAdapter);
        mLanguagesRef.setValue(languages);

        ArrayList<String> tags = adapterToArrayList(mTagAdapter);
        mTagsRef.setValue(tags);

        Log.i("AccountCreation", "Successfully updated user preferences");
        mFragmentListener.onSettingsChanged(mUserUid);
        return true;
    }

    public ArrayList adapterToArrayList(ArrayAdapter adapter){
        ArrayList result = new ArrayList();
        for (int i = 0; i < adapter.getCount(); i++){
            result.add(adapter.getItem(i));
        }
        return result;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ListView languageListView = (ListView) view.findViewById(R.id.preferences_languages_list_view);
        ListView tagListView = (ListView) view.findViewById(R.id.preferences_tags_list_view);
        AutoCompleteTextView addLanguageEditText = (AutoCompleteTextView) view.findViewById(R.id.preference_language_autocomplete_text);
        AutoCompleteTextView addTagEditText = (AutoCompleteTextView) view.findViewById(R.id.preference_tag_autocomplete_text);

        attachLanguageAdapter(languageListView);
        attachTagAdapter(tagListView);
        attachAddLanguageListener(addLanguageEditText);
        attachAddTagListener(addTagEditText);
    }

    private void attachLanguageAdapter(ListView languageListView){
        mLanguageAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        languageListView.setAdapter(mLanguageAdapter);

        mLanguagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("AccountCreation", "Fetching data from " + mLanguagesRef.getPath());
                ArrayList<String> languages = (ArrayList<String>) dataSnapshot.getValue();
                if (languages != null) {
                    // If it's a new account, this would be null
                    mLanguageAdapter.addAll(languages);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Utils.makeToast(CreateAccountFragment.this, "Sorry, we couldn't load your information");
                Log.e(LOG_TAG, "Firebase error when retrieving user languages: " + firebaseError.getMessage());
            }
        });
    }

    private void attachTagAdapter(ListView tagListView){
        mTagAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        tagListView.setAdapter(mTagAdapter);

        mTagsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("AccountCreation", "Fetching data from " + mTagsRef.getPath());
                ArrayList<String> tags = (ArrayList<String>) dataSnapshot.getValue();
                if (tags != null) {
                    // If it's a new account, this would be null
                    mLanguageAdapter.addAll(tags);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Utils.makeToast(CreateAccountFragment.this, "Sorry, we couldn't load your information");
                Log.e(LOG_TAG, "Firebase error when retrieving user tags: " + firebaseError.getMessage());            }
        });
    }

    private void attachAddLanguageListener(final EditText addLanguageView){
        addLanguageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(LOG_TAG, "Add Language Edit Text: Key press event with actionId " + actionId);
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // enter key is pressed
                    mLanguageAdapter.add(v.getText().toString());
                    addLanguageView.setText("");
                }
                return true;
            }
        });
    }

    private void attachAddTagListener(final EditText addTagView){
        addTagView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(LOG_TAG, "Add Tag Edit Text: Key press event with actionId " + actionId);
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // enter key is pressed
                    mTagAdapter.add(v.getText().toString());
                    addTagView.setText("");
                }
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        saveContextOnAttach(context);
    }

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
        if (context instanceof OnCreateAccountFragmentListener) {
            mContext = context;
            mFragmentListener = (OnCreateAccountFragmentListener) context;
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
    public interface OnCreateAccountFragmentListener {
        void onSettingsChanged(String userUid);
    }
}
