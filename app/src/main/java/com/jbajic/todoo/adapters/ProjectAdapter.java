package com.jbajic.todoo.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jbajic.todoo.AddProjectActivity;
import com.jbajic.todoo.ProjectActivity;
import com.jbajic.todoo.R;
import com.jbajic.todoo.interfaces.ItemTouchHelperAdapter;
import com.jbajic.todoo.models.Project;
import com.jbajic.todoo.models.Task;
import com.jbajic.todoo.utilis.AppConstants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jbajic on 30.05.17..
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> implements ItemTouchHelperAdapter {

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
    public void onBindViewHolder(final ProjectViewHolder holder, int position) {
        final Project project = projectsList.get(position);
        holder.tvProjectName.setText(project.getName());
        holder.tvProjectClient.setText(project.getClient());
        holder.tvProjectDeadline.setText(project.getDeadline());

        Integer completedTasks = 0;
        Integer activeTasks = 0;
        for (Task task : project.getTaskArrayList()) {
            if (task.getCompleted() && task.getCategoryId() != null) {
                ++completedTasks;
            } else if(task.getCategoryId() != null) {
                ++activeTasks;
            }
        }
        double progress = ((double) completedTasks / (completedTasks + activeTasks)) * 100;
        holder.pbProjectProgress.setProgress((int) progress);
    }

    @Override
    public int getItemCount() {
        return projectsList.size();
    }

    @Override
    public void onItemDismiss(int position, int side) {
        if (side == ItemTouchHelper.END) {
            Log.e("POSITION SWIPED", "RIGHT");
            Intent intent = new Intent(context, ProjectActivity.class);
            intent.putExtra(AppConstants.EXTRA_KEY_PROJECT_ID, projectsList.get(position).getId());
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        } else if (side == ItemTouchHelper.START) {
            Log.e("POSITION SWIPED", "RIGHT");
            Intent intent = new Intent(context, AddProjectActivity.class);
            intent.putExtra(AppConstants.EXTRA_KEY_PROJECT, projectsList.get(position));
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }

    public void updateProjectAdapter(List<Project> projectsList) {
        this.projectsList = projectsList;
        notifyDataSetChanged();
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.ll_projectInfo)
        LinearLayout llProjectInfo;
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
        @InjectView(R.id.ll_projectView)
        LinearLayout llProjectView;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}