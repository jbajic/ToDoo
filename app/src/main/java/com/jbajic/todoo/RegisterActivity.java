package com.jbajic.todoo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jbajic.todoo.interfaces.RequestListener;
import com.jbajic.todoo.services.APIService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @InjectView(R.id.et_email)
    EditText etEmail;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.et_password_confirm)
    EditText etPasswordConfirm;
    @InjectView(R.id.bt_login)
    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.bt_login)
    public void onViewClicked() {
        String email = String.valueOf(etEmail.getText());
        String password = String.valueOf(etPassword.getText());
        String passwordConfirm = String.valueOf(etPasswordConfirm.getText());
        if (email.isEmpty()) {
            etEmail.setError(getResources().getString(R.string.error_missing, "Email"));
        } else if (password.isEmpty()) {
            etPassword.setError(getResources().getString(R.string.error_missing, "Password"));
        } else if (passwordConfirm.isEmpty()) {
            etPasswordConfirm.setError(getResources().getString(R.string.error_missing, "Password confirmation"));
        } else if (!password.equals(passwordConfirm)) {
            etPassword.setError(getResources().getString(R.string.error_password));
            etPasswordConfirm.setError(getResources().getString(R.string.error_password));
        } else {
            APIService registerService = APIService.getInstance(this);
            registerService.register(email, password, new RequestListener() {
                @Override
                public void failed(String message) {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void finished(String message) {
                    RegisterActivity.this.finish();
                }
            });
        }
    }
}
