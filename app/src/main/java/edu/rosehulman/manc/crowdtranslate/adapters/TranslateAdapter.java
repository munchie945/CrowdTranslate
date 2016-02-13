package edu.rosehulman.manc.crowdtranslate.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by manc on 1/18/2016.
 */
public class TranslateAdapter extends RecyclerView.Adapter<TranslateAdapter.TranslateViewHolder> {

    // the key for "project key" field
    private static final String LINE_KEY_STRING = "lineKey";

    private Line mOriginalLine;
    private Firebase mTranslationRef;
    private ArrayList<Translation> mTranslations = new ArrayList<>();;

    public TranslateAdapter(Line originalLine){
        this.mOriginalLine = originalLine;
        mTranslationRef = new Firebase(Constants.TRANSLATION_PATH);
        mTranslationRef.orderByChild(LINE_KEY_STRING)
                .equalTo(originalLine.getKey())
                .addChildEventListener(new TranslationChildEventListener());
    }

    @Override
    public TranslateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_translation_view, parent, false);
        return new TranslateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TranslateViewHolder holder, int position) {
        final Translation translation = mTranslations.get(position);
        holder.translationLineTextView.setText(translation.getText());
        holder.numVotesTextView.setText(translation.getNumVotes() + "");
    }

    private void createAndPushTranslation(String translationText){
        Translation t = new Translation();
        t.setLineKey(mOriginalLine.getKey());
        t.setText(translationText);
        t.setNumVotes(0);
        mTranslationRef.push().setValue(t);
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
                    // TODO: Only temp "Submit" behavior; replace with real stuff
                    final Translation translation = mTranslations.get(getAdapterPosition());
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
                    Translation translation = mTranslations.get(getAdapterPosition());
                    translation.incrementNumVotes();
                    notifyItemChanged(getAdapterPosition());
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
    }
}
