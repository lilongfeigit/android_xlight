package com.umarbhutta.xlightcompanion.control.activity.result;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;

/**
 * Created by Administrator on 2017/3/15.
 * email 通知页面
 */

public class EmailActivity extends AppCompatActivity {


    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        //hide nav bar
        getSupportActionBar().hide();

        initViews();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        btnSure.setText("完成");
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("通知Email");
        btnSure.setVisibility(View.GONE);
    }

}
