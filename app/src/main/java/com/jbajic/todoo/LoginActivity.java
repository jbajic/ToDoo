package com.jbajic.todoo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jbajic.todoo.interfaces.RequestListener;
import com.jbajic.todoo.services.APIService;
import com.jbajic.todoo.utilis.AppConstants;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {


    @InjectView(R.id.et_email)
    EditText etEmail;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.bt_login)
    Button btLogin;
    @InjectView(R.id.bt_register)
    Button btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.bt_register, R.id.bt_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                String email = String.valueOf(etEmail.getText());
                String password = String.valueOf(etPassword.getText());
                if (email.isEmpty()) {
                    etEmail.setError(getResources().getString(R.string.error_missing, "Email"));
                } else if(password.isEmpty()) {
                    etPassword.setError(getResources().getString(R.string.error_missing, "Email"));
                } else {
                    APIService loginService = APIService.getInstance(this);
                    loginService.login(email, password, new RequestListener() {
                        @Override
                        public void failed(String message) {
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void finished(String message) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                break;
            case R.id.bt_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
