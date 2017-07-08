package com.jbajic.todoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jbajic.todoo.adapters.MemberAdapter;
import com.jbajic.todoo.helpers.DatabaseHelper;
import com.jbajic.todoo.models.Project;
import com.jbajic.todoo.models.User;
import com.jbajic.todoo.utilis.AppConstants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MembersActivity extends AppCompatActivity {

    @InjectView(R.id.my_toolbar)
    Toolbar myToolbar;
    @InjectView(R.id.rv_members)
    RecyclerView rvMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        ButterKnife.inject(this);

        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the Up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        Intent intent = getIntent();
        Project project = DatabaseHelper.getInstance(this).getProject(intent.getLongExtra(AppConstants.EXTRA_KEY_PROJECT_ID, 0));
        List<User> usersList = databaseHelper.getAllUsersNotFromProject(project);

        MemberAdapter memberAdapter = new MemberAdapter(usersList, this, project);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMembers.setLayoutManager(layoutManager);
        rvMembers.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        rvMembers.setAdapter(memberAdapter);
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
