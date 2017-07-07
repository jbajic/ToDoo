package com.jbajic.todoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jbajic.todoo.helpers.DatabaseHelper;
import com.jbajic.todoo.interfaces.RequestListener;
import com.jbajic.todoo.models.Project;
import com.jbajic.todoo.models.Task;
import com.jbajic.todoo.models.User;
import com.jbajic.todoo.services.APIService;
import com.jbajic.todoo.utilis.AppConstants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddTaskActivity extends AppCompatActivity {

    @InjectView(R.id.my_toolbar)
    Toolbar myToolbar;
    @InjectView(R.id.et_name)
    EditText etName;
    @InjectView(R.id.sp_category)
    AppCompatSpinner spCategory;
    @InjectView(R.id.et_body)
    EditText etBody;
    @InjectView(R.id.bt_createTask)
    Button btCreateTask;
    @InjectView(R.id.tv_currentStatus)
    TextView tvCurrentStatus;
    @InjectView(R.id.ll_createTaskStatus)
    LinearLayout llCreateTaskStatus;
    @InjectView(R.id.sp_member)
    AppCompatSpinner spMember;
    @InjectView(R.id.et_estTime)
    EditText etEstTime;

    private DatabaseHelper databaseHelper;
    private User selectedUser;
    private Task selectedCategory;
    Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.inject(this);

        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the Up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        databaseHelper = DatabaseHelper.getInstance(this);
        Intent intent = getIntent();
        project = databaseHelper.getProject(intent.getLongExtra(AppConstants.EXTRA_KEY_PROJECT_ID, 0));

        final List<User> users = databaseHelper.getAllUsersFromProject(project);
        ArrayAdapter<User> usersAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_spinner_item, users);
        spMember.setAdapter(usersAdapter);

        spMember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUser = users.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final List<Task> categories = databaseHelper.getProjectCategoryTask(project.getServerId());
        ArrayAdapter<Task> categoryAdapter = new ArrayAdapter<Task>(this, android.R.layout.simple_spinner_item, categories);
        spCategory.setAdapter(categoryAdapter);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.bt_createTask)
    public void onViewClicked() {
        String name = String.valueOf(etName.getText());
        Integer estTime = Integer.valueOf(String.valueOf(etEstTime.getText()));
        String body = String.valueOf(etBody.getText());
        if (name.isEmpty()) {
            etName.setError(getResources().getString(R.string.error_missing, "Name"));
        } else if (estTime == null) {
            etEstTime.setError(getResources().getString(R.string.error_missing, "Estimated Time"));
        } else if (body.isEmpty()) {
            etBody.setError(getResources().getString(R.string.error_missing, "Description"));
        } else if (selectedUser == null) {
            Toast
                    .makeText(this, getResources().getString(R.string.error_missing, "manager"), Toast.LENGTH_SHORT)
                    .show();
        } else if (selectedCategory == null) {
            Toast
                    .makeText(this, getResources().getString(R.string.error_missing, "category"), Toast.LENGTH_SHORT)
                    .show();
        } else {
            APIService apiService = APIService.getInstance(this);
            btCreateTask.setVisibility(View.GONE);
            llCreateTaskStatus.setVisibility(View.VISIBLE);
            final Task task = new Task(
                    name,
                    body,
                    false,
                    estTime,
                    selectedCategory.getServerId(),
                    project.getServerId(),
                    selectedUser.getId()
                    );
            apiService.createTask(task, new RequestListener() {
                @Override
                public void failed(String message) {
                    Toast.makeText(AddTaskActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void finished(String message) {
                    Long taskId = Long.valueOf(message);
                    task.setServerId(taskId);
                    databaseHelper.addTask(task);
                    AddTaskActivity.this.finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            });
        }
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

}
