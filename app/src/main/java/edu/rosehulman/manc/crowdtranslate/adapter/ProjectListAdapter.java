package edu.rosehulman.manc.crowdtranslate.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.rosehulman.manc.crowdtranslate.BrowseProjectsActivity;
import edu.rosehulman.manc.crowdtranslate.ManageProjectsActivity;
import edu.rosehulman.manc.crowdtranslate.R;
import edu.rosehulman.manc.crowdtranslate.model.Project;

/**
 * Created by manc on 1/18/2016.
 */
public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder> {

    private final ArrayList<Project> mProjectList;

    private Object mContext;

    public ProjectListAdapter(ArrayList<Project> projectList, Object context){
        mContext = context;
        mProjectList = projectList;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_project_view, parent, false);
        return new ProjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProjectViewHolder holder, final int position) {
        final Project project = mProjectList.get(position);
        holder.titleView.setText(project.getTitle());
        holder.sourceLanguageView.setText(project.getSourceLang());
        holder.targetLanguageView.setText(project.getDestLang());
        holder.tagsView.setText(project.getTagString());
        holder.textView.setText(project.getLinesString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof BrowseProjectsActivity) {
                    ((BrowseProjectsActivity) mContext).displayInfo(project, position);

                } else if (mContext instanceof ManageProjectsActivity){
                    ((ManageProjectsActivity) mContext).displayInfo(project, position);

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mProjectList.size();
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

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        public TextView titleView;
        public TextView sourceLanguageView;
        public TextView targetLanguageView;
        public TextView tagsView;
        public TextView textView;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.project_card_title);
            sourceLanguageView = (TextView) itemView.findViewById(R.id.project_card_source_language);
            targetLanguageView = (TextView) itemView.findViewById(R.id.project_card_target_language);
            tagsView = (TextView) itemView.findViewById(R.id.project_card_tags);
            textView = (TextView) itemView.findViewById(R.id.project_card_sample_text);
        }
    }
}
