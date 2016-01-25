package edu.rosehulman.manc.crowdtranslate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.rosehulman.manc.crowdtranslate.model.Project;

import static edu.rosehulman.manc.crowdtranslate.BrowseProjectsActivity.*;

/**
 * Created by manc on 1/18/2016.
 */
public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private final ArrayList<Project> mProjectList;

    private BrowseProjectsActivity mContext;

    public ProjectListAdapter(BrowseProjectsActivity context){
        mContext = context;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Project project = mProjectList.get(position);
        holder.titleView.setText(project.getName());
        holder.sourceLanguageView.setText(project.getSourceLang());
        holder.targetLanguageView.setText(project.getDestLang());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.displayInfo(project, position);

            }
        });
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
            titleView = (TextView) itemView.findViewById(R.id.project_card_title);
            sourceLanguageView = (TextView) itemView.findViewById(R.id.project_card_source_language);
            targetLanguageView = (TextView) itemView.findViewById(R.id.project_card_target_language);
        }
    }
    public ArrayList<Project> getProjectList(){
        return mProjectList;
    }
    public void addProject(Project project){
        mProjectList.add(project);
        notifyDataSetChanged();
    }
    public void replaceProject(Project project, int index){
        mProjectList.set(index, project);
        notifyDataSetChanged();
    }
}
