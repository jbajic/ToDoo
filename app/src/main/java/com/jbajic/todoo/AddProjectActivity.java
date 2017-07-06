package com.jbajic.todoo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jbajic.todoo.helpers.DatabaseHelper;
import com.jbajic.todoo.interfaces.RequestListener;
import com.jbajic.todoo.models.Project;
import com.jbajic.todoo.models.User;
import com.jbajic.todoo.services.APIService;
import com.jbajic.todoo.utilis.AppConstants;

import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddProjectActivity extends BaseActivity {

    @InjectView(R.id.my_toolbar)
    Toolbar myToolbar;
    @InjectView(R.id.et_name)
    EditText etName;
    @InjectView(R.id.et_client)
    EditText etClient;
    @InjectView(R.id.bt_setDeadline)
    Button btSetDeadline;
    @InjectView(R.id.et_body)
    EditText etBody;
    @InjectView(R.id.bt_createProject)
    Button btCreateProject;
    @InjectView(R.id.tv_deadline)
    TextView tvDeadline;
    @InjectView(R.id.sp_manager)
    AppCompatSpinner spManager;
    @InjectView(R.id.ll_createProjectStatus)
    LinearLayout llCreateProjectStatus;
    @InjectView(R.id.tv_currentStatus)
    TextView tvCurrentStatus;

    private User selectedUser;
    private DatabaseHelper databaseHelper;
    Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        ButterKnife.inject(this);

        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the Up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        databaseHelper = DatabaseHelper.getInstance(this);
        final List<User> users = databaseHelper.getAllUsers();
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(this, android.R.layout.simple_spinner_item, users);
        spManager.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent.hasExtra(AppConstants.EXTRA_KEY_PROJECT)) {
            btCreateProject.setText(R.string.update);

            project = (Project) intent.getSerializableExtra(AppConstants.EXTRA_KEY_PROJECT);
            fillFields(project);
            for (User user : users) {
                if (user.getId().equals(project.getManagerId())) {
                    spManager.setSelection(adapter.getPosition(user));
                }
            }
        }
        spManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUser = users.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fillFields(Project project) {
        etName.setText(project.getName());
        etClient.setText(project.getClient());
        etBody.setText(project.getBody());
        tvDeadline.setText(project.getDeadline());
    }

    @OnClick({R.id.bt_setDeadline, R.id.bt_createProject})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case android.R.id.home:
                break;
            case R.id.bt_setDeadline:
                Calendar calendar = Calendar.getInstance();
                Integer year = calendar.get(Calendar.YEAR);
                Integer month = calendar.get(Calendar.MONTH);
                Integer dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = dayOfMonth + "." + monthOfYear + "." + year;
                        tvDeadline.setText(date);
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
                break;
            case R.id.bt_createProject:
                String name = String.valueOf(etName.getText());
                String client = String.valueOf(etClient.getText());
                String body = String.valueOf(etBody.getText());
                String deadline = String.valueOf(tvDeadline.getText());
                if (name.isEmpty()) {
                    etName.setError(getResources().getString(R.string.error_missing, "Name"));
                } else if (client.isEmpty()) {
                    etClient.setError(getResources().getString(R.string.error_missing, "Client"));
                } else if (body.isEmpty()) {
                    etBody.setError(getResources().getString(R.string.error_missing, "Description"));
                } else if (selectedUser == null) {
                    Toast
                            .makeText(this, getResources().getString(R.string.error_missing, "manager"), Toast.LENGTH_SHORT)
                            .show();
                } else if (deadline.isEmpty()) {
                    Toast
                            .makeText(this, getResources().getString(R.string.error_missing, "deadline"), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (project == null) {
                        APIService apiService = APIService.getInstance(this);
                        btCreateProject.setVisibility(View.GONE);
                        llCreateProjectStatus.setVisibility(View.VISIBLE);
                        final Project project = new Project(
                                name,
                                body,
                                client,
                                deadline,
                                false,
                                selectedUser.getServerId()
                        );
                        apiService.createProject(project, new RequestListener() {
                            @Override
                            public void failed(String message) {
                                Toast.makeText(AddProjectActivity.this, message, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void finished(String message) {
                                Long projectId = Long.valueOf(message);
                                project.setServerId(projectId);
                                databaseHelper.addProject(project);
                                AddProjectActivity.this.finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                            }
                        });
                    } else {
                        APIService apiService = APIService.getInstance(this);
                        btCreateProject.setVisibility(View.GONE);
                        llCreateProjectStatus.setVisibility(View.VISIBLE);
                        tvCurrentStatus.setText(R.string.status_update_project);
                        project.setName(name);
                        project.setBody(body);
                        project.setClient(client);
                        project.setDeadline(deadline);
                        project.setManagerId(selectedUser.getId());
                        apiService.updateProject(project, new RequestListener() {
                            @Override
                            public void failed(String message) {
                                Toast.makeText(AddProjectActivity.this, message, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void finished(String message) {
                                databaseHelper.updateProject(project);
                                AddProjectActivity.this.finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                            }
                        });
                    }

                }
                break;
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
