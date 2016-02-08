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

import java.util.ArrayList;

import edu.rosehulman.manc.crowdtranslate.R;
import edu.rosehulman.manc.crowdtranslate.model.Line;
import edu.rosehulman.manc.crowdtranslate.model.Translation;

/**
 * Created by manc on 1/18/2016.
 */
public class TranslateAdapter extends RecyclerView.Adapter<TranslateAdapter.TranslateViewHolder> {

    private Line originalLine;
    private ArrayList<Translation> mTranslations;

    public TranslateAdapter(Line originalLine, ArrayList<Translation> translations){
        this.originalLine = originalLine;
        if (translations == null){
            mTranslations = new ArrayList();
        } else {
            mTranslations = translations;
        }
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
                    Translation translation = mTranslations.get(getAdapterPosition());
                    builder.setTitle("Translate")
                            .setMessage(originalLine.getText())
                            .setView(dialogView)
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Translation t = new Translation();
                                    t.setText(inputTranslation.getText().toString());
                                    t.setNumVotes(0);
                                    mTranslations.add(t);
                                    notifyItemInserted(getAdapterPosition());
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
}
