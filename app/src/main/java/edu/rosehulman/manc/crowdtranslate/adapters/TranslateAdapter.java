package edu.rosehulman.manc.crowdtranslate.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.rosehulman.manc.crowdtranslate.R;
import edu.rosehulman.manc.crowdtranslate.model.Translation;

/**
 * Created by manc on 1/18/2016.
 */
public class TranslateAdapter extends RecyclerView.Adapter<TranslateAdapter.TranslateViewHolder> {

    ArrayList<Translation> mTranslations;

    public TranslateAdapter(ArrayList<Translation> translations){
        if (translations == null){
            mTranslations = new ArrayList();
        } else {
            mTranslations = translations;
        }
    }

    @Override
    public TranslateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_translation_view, parent, false);
        return new TranslateViewHolder(itemView);    }

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

        public TranslateViewHolder(View itemView) {
            super(itemView);
            translationLineTextView = (TextView) itemView.findViewById(R.id.translation_line_view);
            numVotesTextView = (TextView) itemView.findViewById(R.id.translation_num_votes_view);
            voteImageView = (ImageView) itemView.findViewById(R.id.translation_vote_image_view);
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
