package com.jbajic.todoo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jbajic.todoo.helpers.DatabaseHelper;
import com.jbajic.todoo.utilis.AppConstants;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity {

    @InjectView(R.id.my_toolbar)
    Toolbar myToolbar;
    @InjectView(R.id.btn_logout)
    Button btnLogout;
//    @InjectView(R.id.btn_database)
//    Button btnDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);

        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the Up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @OnClick(R.id.btn_logout)
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_logout:
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
                databaseHelper.deleteUserProject();
                databaseHelper.deleteTasks();
                databaseHelper.deleteProjects();
                databaseHelper.deleteUsers();
                SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                finish();
                intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
//            case R.id.btn_database:
//                intent = new Intent(this, AndroidDatabaseManager.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
//                break;
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
