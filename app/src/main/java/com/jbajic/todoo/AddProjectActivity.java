package com.jbajic.todoo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

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
                } else if(body.isEmpty()) {
                    etBody.setError(getResources().getString(R.string.error_missing, "Description"));
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
            case R.id.settings:
                break;
        }
        return true;
    }
}
