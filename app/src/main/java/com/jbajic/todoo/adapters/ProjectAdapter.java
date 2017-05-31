package com.jbajic.todoo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jbajic.todoo.R;
import com.jbajic.todoo.models.Project;
import com.jbajic.todoo.models.Task;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jbajic on 30.05.17..
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private List<Project> projectsList;
    private Context context;

    public ProjectAdapter(Context context, List<Project> projectsList) {
        this.context = context;
        this.projectsList = projectsList;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View projectView = LayoutInflater.from(context).inflate(R.layout.item_project, parent, false);

        return new ProjectViewHolder(projectView);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        Project project = projectsList.get(position);
        holder.tvProjectName.setText(project.getName());
        holder.tvProjectClient.setText(project.getClient());
        holder.tvProjectDeadline.setText(project.getDeadline());

        Integer completedTasks = 0;
        Integer activeTasks = 0;
        Log.e("nTasks", String.valueOf(project.getTaskArrayList().size()));
        for (Task task:project.getTaskArrayList()) {
            if(task.getCompleted()) {
                ++completedTasks;
                Log.e("COMPLETED", "LADIDA");
            } else {
                ++activeTasks;
                Log.e("NOTCOMPLETED", "LADIDA");
            }
        }
        double progress = ((double) completedTasks / (completedTasks + activeTasks)) * 100;
        Log.e("PROGRESS ", String.valueOf(progress));
        holder.pbProjectProgress.setProgress((int) progress);
    }

    @Override
    public int getItemCount() {
        return projectsList.size();
    }


    public static class ProjectViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.iv_managerInitials)
        ImageView ivManagerInitials;
        @InjectView(R.id.tv_projectName)
        TextView tvProjectName;
        @InjectView(R.id.tv_projectDeadline)
        TextView tvProjectDeadline;
        @InjectView(R.id.tv_projectClient)
        TextView tvProjectClient;
        @InjectView(R.id.pb_projectProgress)
        ProgressBar pbProjectProgress;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}