package edu.rosehulman.manc.crowdtranslate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.rosehulman.manc.crowdtranslate.model.Project;

/**
 * Created by manc on 1/18/2016.
 */
public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private ArrayList<Project> mProjectList;

    public ProjectListAdapter(){
        mProjectList = new ArrayList();

        // TODO: only for testing; replace with methods to actually get projects
        for (int i = 1; i <= 10; i++){
            Project project = new Project("Default Title " + i, "English", "Spanish");
            mProjectList.add(project);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Project project = mProjectList.get(position);
        holder.titleView.setText(project.getName());
        holder.sourceLanguageView.setText(project.getSourceLang());
        holder.targetLanguageView.setText(project.getDestLang());
    }

    @Override
    public int getItemCount() {
        return mProjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleView;
        public TextView sourceLanguageView;
        public TextView targetLanguageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.project_card_source_language);
            sourceLanguageView = (TextView) itemView.findViewById(R.id.project_card_source_language);
            targetLanguageView = (TextView) itemView.findViewById(R.id.project_card_source_language);
        }
    }
}
