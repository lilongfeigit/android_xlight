package com.umarbhutta.xlightcompanion.userManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.scenario.AddScenarioActivity;
import com.umarbhutta.xlightcompanion.scenario.ColorSelectActivity;

/**
 * Created by Administrator on 2017/3/4.
 * login
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        //hide nav bar
        getSupportActionBar().hide();

        initViews();
    }

    private void initViews() {
       findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_forget_password).setOnClickListener(this);
        findViewById(R.id.tv_new_user_res).setOnClickListener(this);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("登录");
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 确定提交按钮
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                //TODO
                finish();
                break;
            case R.id.tv_forget_password:
                //TODO
                onFabPressed(FindPasswordActivity.class);
                break;
            case R.id.tv_new_user_res:
                //TODO
                onFabPressed(RegisteredActivity.class);
                break;
        }
    }
    private void onFabPressed(Class activity) {
        Intent intent = new Intent(LoginActivity.this,activity);
        startActivityForResult(intent, 1);
    }
}
