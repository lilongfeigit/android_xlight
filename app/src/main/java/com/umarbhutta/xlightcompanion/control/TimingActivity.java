package com.umarbhutta.xlightcompanion.control;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.main.SimpleDividerItemDecoration;
import com.umarbhutta.xlightcompanion.settings.FastBindingActivity;
import com.umarbhutta.xlightcompanion.settings.ModifyPasswordActivity;
import com.umarbhutta.xlightcompanion.settings.ShakeActivity;
import com.umarbhutta.xlightcompanion.settings.UserInvitationActivity;
import com.umarbhutta.xlightcompanion.settings.UserMsgModifyActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 * 定时设置
 */

public class TimingActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private RelativeLayout llStartTime,llWeek;
    private TextView btnSure;
    private TextView tvTitle;
    public ArrayList<String> listStr = new ArrayList<String>();
    private int requestCode = 210;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing);
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
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("定时设置");
        llStartTime = (RelativeLayout) findViewById(R.id.llStartTime);
        llWeek = (RelativeLayout) findViewById(R.id.llWeek);
        llWeek.setOnClickListener(this);
        llStartTime.setOnClickListener(this);
        btnSure.setOnClickListener(this);
    }

    private void onFabPressed(Class activity,ArrayList<String> listStr) {
        Intent intent = new Intent(this, activity);
        intent.putStringArrayListExtra("DILOGLIST",listStr);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llStartTime:
                listStr.clear();
                listStr.add("00:00");
                listStr.add("01:00");
                listStr.add("02:00");
                listStr.add("03:00");
                listStr.add("04:00");
                listStr.add("05:00");
                listStr.add("06:00");
                listStr.add("07:00");
                listStr.add("08:00");
                listStr.add("09:00");
                listStr.add("10:00");
                listStr.add("11:00");
                listStr.add("12:00");
                listStr.add("13:00");
                requestCode = 212;
                onFabPressed(DialogActivity.class,listStr);
                break;
            case R.id.tvEditSure:
                //TODO 编辑提交
                break;
            case R.id.llWeek:
                listStr.clear();
                listStr.add("执行一次");
                listStr.add("每天");
                listStr.add("周一至周五");
                listStr.add("自定义");
                requestCode = 213;
                onFabPressed(DialogActivity.class,listStr);
                break;
        }
    }
}
