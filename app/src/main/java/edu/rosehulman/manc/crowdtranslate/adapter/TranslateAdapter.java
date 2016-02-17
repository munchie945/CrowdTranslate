package edu.rosehulman.manc.crowdtranslate.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

import edu.rosehulman.manc.crowdtranslate.Constants;
import edu.rosehulman.manc.crowdtranslate.R;
import edu.rosehulman.manc.crowdtranslate.model.Line;
import edu.rosehulman.manc.crowdtranslate.model.Translation;
import edu.rosehulman.manc.crowdtranslate.model.User;

/**
 * Created by manc on 1/18/2016.
 */
public class TranslateAdapter extends RecyclerView.Adapter<TranslateAdapter.TranslateViewHolder> {

    // the key for "project key" field
    private static final String LINE_KEY_STRING = "lineKey";

    private User mUser;
    private Line mOriginalLine;
    private String mCurrVotedKey = null; // key of translation I'm voting for

    private Firebase mVoteRef;
    private Firebase mTranslationRef;

    private ArrayList<Translation> mTranslations = new ArrayList<>();

    public TranslateAdapter(Line originalLine){
        this.mOriginalLine = originalLine;
        mTranslationRef = new Firebase(Constants.TRANSLATION_PATH);

        // TODO: temp User code for testing; replace with real stuff
        mUser = new User();
        mUser.setKey("testUser");

        // attach child listener on Translations for this line
        mTranslationRef.orderByChild(LINE_KEY_STRING)
                .equalTo(originalLine.getKey())
                .addChildEventListener(new TranslationChildEventListener());

        // attach child listener for votes on this line
        String voteKey = mUser.getKey() + "_" + mOriginalLine.getKey();
        mVoteRef = new Firebase(Constants.VOTE_PATH).child(voteKey);
        new Firebase(Constants.VOTE_PATH).orderByKey()
                .equalTo(voteKey)
                .addChildEventListener(new VoteChildEventListener());
    }

    @Override
    public TranslateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_translation_view, parent, false);
        return new TranslateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TranslateViewHolder holder, int position) {
        Translation translation = mTranslations.get(position);
        holder.translationLineTextView.setText(translation.getText());
        holder.numVotesTextView.setText(String.valueOf(translation.getNumVotes()));
        int color = (translation.keyEquals(mCurrVotedKey)) ? Color.GREEN : Color.GRAY;
        holder.voteImageView.setColorFilter(color);
    }

    private void createAndPushTranslation(String translationText){
        Translation t = new Translation();
        t.setLineKey(mOriginalLine.getKey());
        t.setText(translationText);
        t.setNumVotes(0);
        mTranslationRef.push().setValue(t);
    }

    private Translation getTranslationByKey(String key){
        for (Translation translation: mTranslations){
            if (translation.getKey().equals(key)){
                return translation;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mTranslations.size();
    }

    public class TranslateViewHolder extends RecyclerView.ViewHolder {

        public TextView translationLineTextView;
        public TextView numVotesTextView;
        public ImageView voteImageView;
        private Context context;

        public TranslateViewHolder(View itemView) {
            super(itemView);
            translationLineTextView = (TextView) itemView.findViewById(R.id.translation_line_view);
            numVotesTextView = (TextView) itemView.findViewById(R.id.translation_num_votes_view);
            voteImageView = (ImageView) itemView.findViewById(R.id.translation_vote_image_view);
            context = itemView.getContext();

            // set OnClickListener for overall item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    View dialogView = layoutInflater.inflate(R.layout.dialog_submit_translation, null);

                    // TODO: remove from layout
                    TextView lineView = (TextView) dialogView.findViewById(R.id.translate_dialog_original_line_text_view);
                    lineView.setText("Testing");

                    final EditText inputTranslation = (EditText) dialogView.findViewById(R.id.translate_dialog_edit_text);
                    // Setting the message of the dialog box
                    builder.setTitle("Translate")
                            .setMessage(mOriginalLine.getText())
                            .setView(dialogView)
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    createAndPushTranslation(inputTranslation.getText().toString());
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            });

            // set OnClickListener for voting
            voteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // realTranslation for reference, clone is for changing value with mutating
                    Translation realTranslation = mTranslations.get(getAdapterPosition());
                    Translation translationClone = (Translation) realTranslation.clone();

                    // update numVotes on database side
                    if (translationClone.keyEquals(mCurrVotedKey)){
                        // cancel vote
                        translationClone.decrementNumVotes();
                        mCurrVotedKey = null;
                        mVoteRef.setValue(null);
                    } else {
                        if (mCurrVotedKey != null){
                            // decrement the currently-voted translation, if it exists
                            Translation realCurrVoted = getTranslationByKey(mCurrVotedKey);
                            Translation currVotedClone = (Translation) realCurrVoted.clone();
                            currVotedClone.decrementNumVotes();
                            mTranslationRef.child(mCurrVotedKey).setValue(currVotedClone);
                        }

                        // register new vote
                        translationClone.incrementNumVotes();
                        mCurrVotedKey = translationClone.getKey(); // need it by reference
                        mVoteRef.setValue(translationClone.getKey());
                    }

                    // update database side: numVotes, as well as the vote table
                    mTranslationRef.child(translationClone.getKey()).setValue(translationClone);
                }
            });
        }
    }

    private class TranslationChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Translation addedTranslation = dataSnapshot.getValue(Translation.class);
            addedTranslation.setKey(dataSnapshot.getKey());
            mTranslations.add(addedTranslation);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Translation changedTranslation = extractTranslation(dataSnapshot);
            boolean translationFound = false;
            for (int i = 0; i < mTranslations.size(); i++){
                if (mTranslations.get(i).getKey().equals(changedTranslation.getKey())){
                    mTranslations.set(i, changedTranslation);
                    translationFound = true;
                    break;
                }
            }
            if (translationFound) {
                notifyDataSetChanged();
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            // not used
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            // not used
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.e("TranslateAdapter", firebaseError.getMessage());
            Log.d("TranslateAdapter", firebaseError.getDetails());
        }

        private Translation extractTranslation(DataSnapshot dataSnapshot){
            Translation addedTranslation = dataSnapshot.getValue(Translation.class);
            addedTranslation.setKey(dataSnapshot.getKey());
            return addedTranslation;
        }
    }

    private class VoteChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            updateVote(dataSnapshot);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            updateVote(dataSnapshot);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }

        private void updateVote(DataSnapshot dataSnapshot){
            String translationKey = (String) dataSnapshot.getValue();
            mCurrVotedKey = translationKey;
            notifyDataSetChanged();
        }
    }
}
