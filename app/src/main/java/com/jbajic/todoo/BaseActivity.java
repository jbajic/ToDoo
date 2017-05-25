package com.jbajic.todoo;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by jbajic on 25.05.17..
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        super.onBackPressed();
    }
}
