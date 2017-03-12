package com.umarbhutta.xlightcompanion.userManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;

/**
 * Created by Administrator on 2017/3/4.
 */

public class FindPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_findpassword);
        //hide nav bar
        getSupportActionBar().hide();

        initViews();
    }

    private void initViews() {
        findViewById(R.id.btn_finash_registered).setOnClickListener(this);
        findViewById(R.id.tv_protocol).setOnClickListener(this);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 确定提交按钮
            }
        });
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("找回密码");
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
