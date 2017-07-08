package com.jbajic.todoo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jbajic.todoo.interfaces.RequestListener;
import com.jbajic.todoo.services.APIService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @InjectView(R.id.et_email)
    EditText etEmail;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.et_password_confirm)
    EditText etPasswordConfirm;
    @InjectView(R.id.bt_register)
    Button btRegister;
    @InjectView(R.id.my_toolbar)
    Toolbar myToolbar;
    @InjectView(R.id.pb_progress_bar)
    ProgressBar pbProgressBar;
    @InjectView(R.id.et_firstName)
    EditText etFirstName;
    @InjectView(R.id.et_lastName)
    EditText etLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);

        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the Up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @OnClick(R.id.bt_register)
    public void onViewClicked() {
        String email = String.valueOf(etEmail.getText());
        String firstName = String.valueOf(etFirstName.getText());
        String lastName = String.valueOf(etLastName.getText());
        String password = String.valueOf(etPassword.getText());
        String passwordConfirm = String.valueOf(etPasswordConfirm.getText());
        if (email.isEmpty()) {
            etEmail.setError(getResources().getString(R.string.error_missing, "Email"));
        } else if (password.isEmpty()) {
            etPassword.setError(getResources().getString(R.string.error_missing, "First name"));
        } else if (firstName.isEmpty()) {
            etFirstName.setError(getResources().getString(R.string.error_missing, "Password"));
        } else if (lastName.isEmpty()) {
            etLastName.setError(getResources().getString(R.string.error_missing, "Last name"));
        } else if (passwordConfirm.isEmpty()) {
            etPasswordConfirm.setError(getResources().getString(R.string.error_missing, "Password confirmation"));
        } else if (!password.equals(passwordConfirm)) {
            etPassword.setError(getResources().getString(R.string.error_password));
            etPasswordConfirm.setError(getResources().getString(R.string.error_password));
        } else {
            pbProgressBar.setVisibility(View.VISIBLE);
            btRegister.setVisibility(View.GONE);
            APIService registerService = APIService.getInstance(this);
            registerService.register(email, password, firstName, lastName, new RequestListener() {
                @Override
                public void failed(String message) {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    pbProgressBar.setVisibility(View.GONE);
                    btRegister.setVisibility(View.VISIBLE);
                    Log.e("FAILED", message);
                }

                @Override
                public void finished(String message) {
                    Log.e("FINISHED", message);
                    RegisterActivity.this.finish();
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
