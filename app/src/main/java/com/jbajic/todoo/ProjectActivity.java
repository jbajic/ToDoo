package com.jbajic.todoo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jbajic.todoo.adapters.TaskExpandableAdapter;
import com.jbajic.todoo.helpers.DatabaseHelper;
import com.jbajic.todoo.interfaces.RequestListener;
import com.jbajic.todoo.models.Project;
import com.jbajic.todoo.models.Task;
import com.jbajic.todoo.services.APIService;
import com.jbajic.todoo.services.DeadlineReceiver;
import com.jbajic.todoo.utilis.AppConstants;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ProjectActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

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

        //set notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        String[] dateString = project.getDeadline().split(".");
        GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
        gregorianCalendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString[0]));
        gregorianCalendar.add(Calendar.MONTH, Integer.parseInt(dateString[1]));
        gregorianCalendar.add(Calendar.YEAR, Integer.parseInt(dateString[2]));

        Log.e("REGISTER", "RECEIVER");
        Intent deadLineIntent = new Intent(this, DeadlineReceiver.class);
//        deadLineIntent.putExtra(AppConstants.EXTRA_KEY_PROJECT_ID, project.getId());
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, deadLineIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,  gregorianCalendar.getTimeInMillis(), broadcast);
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
        elvTasks.setOnItemLongClickListener(this);
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
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_deleteProject:
                //delete project
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete project")
                        .setMessage("Are you sure you want to delete this project?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                APIService apiService = APIService.getInstance(ProjectActivity.this);

                                apiService.deleteProject(project, new RequestListener() {
                                    @Override
                                    public void failed(String message) {
                                        Toast
                                                .makeText(ProjectActivity.this, message, Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                    @Override
                                    public void finished(String message) {
                                        databaseHelper.deleteProject(project);
                                        dialog.cancel();
                                        ProjectActivity.this.finish();
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                    }
                                });
//                                databaseHelper.deleteProject(project);
//                                finish();
//                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
//                                dialog.cancel();
                            }
                        })
                        .show();
                break;
            case R.id.btn_viewMembers:
                intent = new Intent(this, MembersActivity.class);
                intent.putExtra(AppConstants.EXTRA_KEY_PROJECT_ID, project.getId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
            case R.id.btn_addCategory:
                intent = new Intent(this, AddTaskActivity.class);
                intent.putExtra(AppConstants.EXTRA_KEY_PROJECT_ID, project.getId());
                intent.putExtra(AppConstants.EXTRA_KEY_TASK_TYPE, Task.TaskType.category);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
            case R.id.btn_addTask:
                //add task
                intent = new Intent(this, AddTaskActivity.class);
                intent.putExtra(AppConstants.EXTRA_KEY_PROJECT_ID, project.getId());
                intent.putExtra(AppConstants.EXTRA_KEY_TASK_TYPE, Task.TaskType.task);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            int groupPosition = ExpandableListView.getPackedPositionGroup(id);
            int childPosition = ExpandableListView.getPackedPositionChild(id);

            // You now have everything that you would as if this was an OnChildClickListener()
            // Add your logic here.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Task description")
                    .setMessage(categoryList.get(groupPosition).getTaskList().get(childPosition).getBody())
                    .setCancelable(true)
                    .show();

            // Return true as we are handling the event.
            return true;
        }

        return false;
    }

}
