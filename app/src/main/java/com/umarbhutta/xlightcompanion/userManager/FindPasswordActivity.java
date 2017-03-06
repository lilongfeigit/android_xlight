package com.umarbhutta.xlightcompanion.userManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.umarbhutta.xlightcompanion.R;

/**
 * Created by Administrator on 2017/3/4.
 */

public class FindPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registerd);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.btn_finash_registered).setOnClickListener(this);
        findViewById(R.id.tv_protocol).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_finash_registered:
                //TODO
                finish();
                break;
            case R.id.tv_protocol:
                //TODO
                onFabPressed();
                break;
        }
    }
    private void onFabPressed() {
        Intent intent = new Intent(FindPasswordActivity.this, UserResProtocalActivity.class);
        startActivityForResult(intent, 1);
    }
}
