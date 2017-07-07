package com.jbajic.todoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.jbajic.todoo.adapters.TaskExpandableAdapter;
import com.jbajic.todoo.helpers.DatabaseHelper;
import com.jbajic.todoo.models.Project;
import com.jbajic.todoo.models.Task;
import com.jbajic.todoo.utilis.AppConstants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ProjectActivity extends AppCompatActivity {

    @InjectView(R.id.my_toolbar)
    Toolbar myToolbar;
    @InjectView(R.id.tv_projectName)
    TextView tvProjectName;
    @InjectView(R.id.tv_projectDeadline)
    TextView tvProjectDeadline;
    @InjectView(R.id.tv_numberOfActiveTasks)
    TextView tvNumberOfActiveTasks;
    @InjectView(R.id.tv_numberOfCompletedTasks)
    TextView tvNumberOfCompletedTasks;
    @InjectView(R.id.tv_projectBody)
    TextView tvProjectBody;
    @InjectView(R.id.btn_deleteProject)
    Button btnDeleteProject;
    @InjectView(R.id.btn_viewMembers)
    Button btnViewMembers;
    @InjectView(R.id.elv_tasks)
    ExpandableListView elvTasks;
    @InjectView(R.id.tv_projectClient)
    TextView tvProjectClient;
    @InjectView(R.id.btn_addCategory)
    Button btnAddCategory;
    @InjectView(R.id.btn_addTask)
    Button btnAddTask;

    private Project project;
    private DatabaseHelper databaseHelper;
    private List<Task> categoryList;
    private TaskExpandableAdapter taskExpandableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        ButterKnife.inject(this);

        databaseHelper = DatabaseHelper.getInstance(this);

        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the Up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        project = DatabaseHelper.getInstance(this).getProject(intent.getLongExtra(AppConstants.EXTRA_KEY_PROJECT_ID, 0));
        categoryList = databaseHelper.getProjectCategoryTask(project.getServerId());

    }


    @Override
    protected void onResume() {
        super.onResume();
        categoryList = databaseHelper.getProjectCategoryTask(project.getServerId());
        setUpProjectInfo();
        taskExpandableAdapter.updateExpandableListView(categoryList);
    }

    private void setUpProjectInfo() {
        if (project.getCompleted()) {
            tvProjectName.setBackgroundColor(getResources().getColor(R.color.finishedColor));
        }
        Integer numberOfCompletedTasks = 0, numberOfActiveTasks = 0;

        Log.e("NUMBER OF CAT", String.valueOf(categoryList.size()));
        for (Task category : categoryList) {
            Log.e("NUMBER OF TASKS", String.valueOf(category.getTaskList().size()));
            for (Task task : category.getTaskList()) {
                if (task.getCompleted()) {
                    ++numberOfCompletedTasks;
                } else {
                    ++numberOfActiveTasks;
                }
            }
        }

        tvProjectName.setText(project.getName());
        tvProjectDeadline.setText(project.getDeadline());
        tvProjectBody.setText(project.getBody());
        tvProjectClient.setText(project.getClient());
        tvNumberOfActiveTasks.setText(String.valueOf(numberOfActiveTasks));
        tvNumberOfCompletedTasks.setText(String.valueOf(numberOfCompletedTasks));

        taskExpandableAdapter = new TaskExpandableAdapter(this, categoryList);
        elvTasks.setAdapter(taskExpandableAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
        return true;
    }

    @OnClick({R.id.btn_deleteProject, R.id.btn_viewMembers, R.id.btn_addCategory, R.id.btn_addTask})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_deleteProject:
                //delete project
                databaseHelper.deleteProject(project);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.btn_viewMembers:
                //go to projectMembers
                break;
            case R.id.btn_addCategory:
                //
                break;
            case R.id.btn_addTask:
                //add task
                Intent intent = new Intent(this, AddTaskActivity.class);
                intent.putExtra(AppConstants.EXTRA_KEY_PROJECT_ID, project.getId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
        }
    }

}
