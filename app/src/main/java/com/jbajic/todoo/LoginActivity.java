package com.jbajic.todoo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jbajic.todoo.helpers.DatabaseHelper;
import com.jbajic.todoo.interfaces.RequestListener;
import com.jbajic.todoo.models.User;
import com.jbajic.todoo.services.APIService;
import com.jbajic.todoo.utilis.AppConstants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {

    private APIService apiService;
    @InjectView(R.id.et_email)
    EditText etEmail;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.bt_login)
    Button btLogin;
    @InjectView(R.id.bt_register)
    Button btRegister;
    @InjectView(R.id.my_toolbar)
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        setSupportActionBar(myToolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        if (sharedPreferences.contains(AppConstants.KEY_JWT) && sharedPreferences.contains(AppConstants.KEY_EMAIL)
                && sharedPreferences.contains(AppConstants.KEY_PASSWORD)) {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
        }
        apiService = APIService.getInstance(this);
    }

    @OnClick({R.id.bt_register, R.id.bt_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                String email = String.valueOf(etEmail.getText());
                String password = String.valueOf(etPassword.getText());
                if (email.isEmpty()) {
                    etEmail.setError(getResources().getString(R.string.error_missing, "Email"));
                } else if (password.isEmpty()) {
                    etPassword.setError(getResources().getString(R.string.error_missing, "Email"));
                } else {
                    apiService.login(email, password, new RequestListener() {
                        @Override
                        public void failed(String message) {
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void finished(String message) {
                            startSynchronization();
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    });
                }
                break;
            case R.id.bt_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
        }
    }

    private void startSynchronization() {
     apiService.synchronizeUsers(new RequestListener() {
         @Override
         public void failed(String message) {

         }

         @Override
         public void finished(String message) {

         }
     });
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        List<User> users = databaseHelper.getAllUsers();
        for (User user:users) {
            Log.e("USER ID", String.valueOf(user.getId()));
            Log.e("USER NAME", user.getfName() + user.getlName());
        }
    }

}

