package com.umarbhutta.xlightcompanion.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;

/**
 * Created by Administrator on 2017/3/5.
 */

public class ModifyPasswordActivity extends AppCompatActivity {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        //hide nav bar
        getSupportActionBar().hide();
        initViews();
    }

    private void initViews() {
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
        tvTitle.setText("修改密码");
    }
}
