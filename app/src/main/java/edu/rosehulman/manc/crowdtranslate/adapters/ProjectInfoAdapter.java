package edu.rosehulman.manc.crowdtranslate.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.StringTokenizer;

import edu.rosehulman.manc.crowdtranslate.ProjectInfoActivity;
import edu.rosehulman.manc.crowdtranslate.R;
import edu.rosehulman.manc.crowdtranslate.model.Project;
import edu.rosehulman.manc.crowdtranslate.model.User;

/**
 * Created by Alexis on 2/15/16.
 */
public class ProjectInfoAdapter extends RecyclerView.Adapter<ProjectInfoAdapter.ProjectInfoViewHolder> {

    private User mUser;
    private Project mProject;
    private Firebase mProjectRef;
    private Firebase mLineRef;
    private ProjectInfoActivity context;
    public static final String EXTRA_LINE_KEY = "line";
    public static final int REQUEST_DETAIL_INFO = 1;



    private ArrayList<String> mLines;
    public ProjectInfoAdapter(Project p, ProjectInfoActivity context){
        mProject = p;
        this.context = context;
        mLines = new ArrayList<>();
        String wholeText = p.getLinesString();
        StringTokenizer stringTokenizer = new StringTokenizer(wholeText,"\n");
        while(stringTokenizer.hasMoreElements()){
            mLines.add(stringTokenizer.nextToken());
        }
    }

    @Override
    public ProjectInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_project_info_view, parent, false);
        return new ProjectInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProjectInfoViewHolder holder, final int position) {
        String line = mLines.get(position);
        holder.lineTextView.setText(line);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Navigate to Translation page
                Log.e("LINE CLICKED:","LINE: "+mProject.getLineKey(position)+" HAS BEEN CLICKED");
                context.displayTranslate(mProject.getLine(position),position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLines.size();
    }

    public class ProjectInfoViewHolder extends RecyclerView.ViewHolder {
        public TextView lineTextView;
        public ProjectInfoViewHolder(final View itemView) {
            super(itemView);
            lineTextView = (TextView) itemView.findViewById(R.id.project_info_line_view);

        }
    }
}
