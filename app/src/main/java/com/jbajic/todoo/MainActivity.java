package com.jbajic.todoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jbajic.todoo.adapters.ProjectAdapter;
import com.jbajic.todoo.helpers.DatabaseHelper;
import com.jbajic.todoo.models.Project;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.rv_projects)
    RecyclerView rvProjects;
    @InjectView(R.id.my_toolbar)
    Toolbar myToolbar;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(myToolbar);

        databaseHelper = DatabaseHelper.getInstance(this);
        List<Project> projectsList = databaseHelper.getAllProjects();
        Log.e("PROJECTS SIZE", String.valueOf(projectsList.size()));

        ProjectAdapter projectAdapter = new ProjectAdapter(this, projectsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvProjects.addItemDecoration(itemDecoration);
        rvProjects.setLayoutManager(layoutManager);
        rvProjects.setAdapter(projectAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                break;
            case R.id.add_project:
                Intent intent = new Intent(this, AddProjectActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
        }
        return true;
    }

}
