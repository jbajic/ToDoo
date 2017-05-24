package com.jbajic.todoo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddProjectActivity extends AppCompatActivity {

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
            case R.id.bt_setDeadline:
                break;
            case R.id.bt_createProject:
                break;
        }
    }
}
