package com.jbajic.todoo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jbajic.todoo.helpers.VolleyRequestQueue;
import com.jbajic.todoo.interfaces.RequestListener;
import com.jbajic.todoo.services.LoginService;
import com.jbajic.todoo.utilis.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Log.e("All", String.valueOf(sharedPreferences.getAll()));
    }

    @OnClick(R.id.bt_login)
    public void onViewClicked() {
        String email = String.valueOf(etEmail.getText());
        String password = String.valueOf(etPassword.getText());
        if (!email.isEmpty() && !password.isEmpty()) {
            LoginService loginService = LoginService.getInstance(this);
            loginService.login(email, password, new RequestListener() {
                @Override
                public void started(String message) {
                    Log.e("Success", message);
                }

                @Override
                public void failed(String message) {
                    Log.e("Failed", message);
                }

                @Override
                public void finished(String message) {
                    Log.e("Finished", message);
                }
            });

        }
    }
}
