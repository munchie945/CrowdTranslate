package edu.rosehulman.manc.crowdtranslate.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Alexis on 2/15/16.
 */
public class ProjectInfoAdapter extends RecyclerView.Adapter<ProjectInfoAdapter.ProjectInfoViewHolder> {
    @Override
    public ProjectInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ProjectInfoViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ProjectInfoViewHolder extends RecyclerView.ViewHolder {
        public ProjectInfoViewHolder(View itemView) {
            super(itemView);
        }
    }
}
