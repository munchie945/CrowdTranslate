package edu.rosehulman.manc.crowdtranslate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.StringTokenizer;

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
    private Context context;

    private ArrayList<String> mLines;
    public ProjectInfoAdapter(Project p, Context context){
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
    public void onBindViewHolder(ProjectInfoViewHolder holder, int position) {
        String line = mLines.get(position);
        holder.lineTextView.setText(line);

    }

    @Override
    public int getItemCount() {
        return mLines.size();
    }

    public class ProjectInfoViewHolder extends RecyclerView.ViewHolder {
        public TextView lineTextView;
        public ProjectInfoViewHolder(View itemView) {
            super(itemView);
            lineTextView = (TextView) itemView.findViewById(R.id.project_info_line_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:Navigate to Translation page
//                    Intent intent = new Intent(context, TranslateActivity.class);
//                    Line lineToTranslate =
//                    intent.putExtra(EXTRA_LINE_KEY, lineToTranslate);
//                    parentActivity.startActivity(intent);
                    Log.e("LINE CLICKED:","LINE: "+lineTextView.getText()+" HAS BEEN CLICKED");
                }
            });
        }
    }
}
