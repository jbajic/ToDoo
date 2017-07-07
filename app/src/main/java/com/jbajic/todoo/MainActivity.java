package com.jbajic.todoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jbajic.todoo.adapters.ProjectAdapter;
import com.jbajic.todoo.helpers.DatabaseHelper;
import com.jbajic.todoo.helpers.SimpleItemTouchHelperCallback;
import com.jbajic.todoo.interfaces.DismissedProjectListener;
import com.jbajic.todoo.models.Project;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.rv_projects)
    RecyclerView rvProjects;
    @InjectView(R.id.my_toolbar)
    Toolbar myToolbar;

    private ItemTouchHelper itemTouchHelper;
    private List<Project> projectsList;
    private ProjectAdapter projectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(myToolbar);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        projectsList = databaseHelper.getAllProjects();
        Log.e("PROJECTS SIZE", String.valueOf(projectsList.size()));

        projectAdapter = new ProjectAdapter(this, projectsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvProjects.addItemDecoration(itemDecoration);
        rvProjects.setLayoutManager(layoutManager);
        rvProjects.setAdapter(projectAdapter);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(projectAdapter, R.drawable.ic_info, R.drawable.ic_edit, this);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvProjects);
    }

    @Override
    protected void onResume() {
        projectAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
            case R.id.add_project:
                intent = new Intent(this, AddProjectActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
        }
        return true;
    }

}
