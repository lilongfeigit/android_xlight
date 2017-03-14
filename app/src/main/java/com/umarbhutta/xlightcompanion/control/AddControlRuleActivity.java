package com.umarbhutta.xlightcompanion.control;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;

/**
 * Created by Umar Bhutta.
 * 新的规则页面
 */
public class AddControlRuleActivity extends AppCompatActivity {
    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private Button btn_add_term, btn_add_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_control_rule);
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
        tvTitle.setText("新建规则");
        btn_add_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 添加启动条件
                onFabPressed(EntryConditionActivity.class);
            }
        });
        btn_add_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 添加执行结果
                onFabPressed(ControlRuseltActivity.class);
            }
        });
    }
    private void onFabPressed(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

}
