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
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.main.SimpleDividerItemDecoration;
import com.umarbhutta.xlightcompanion.okHttp.model.DeviceInfoResult;
import com.umarbhutta.xlightcompanion.settings.FastBindingActivity;
import com.umarbhutta.xlightcompanion.settings.ModifyPasswordActivity;
import com.umarbhutta.xlightcompanion.settings.ShakeActivity;
import com.umarbhutta.xlightcompanion.settings.UserInvitationActivity;
import com.umarbhutta.xlightcompanion.settings.UserMsgModifyActivity;
import com.umarbhutta.xlightcompanion.views.pickerview.TimePickerView;
import com.umarbhutta.xlightcompanion.views.pickerview.lib.TimePickerUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 * 定时设置
 */

public class TimingActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private RelativeLayout llStartTime, llWeek;
    private TextView btnSure;
    private TextView tvTitle,tv_week;
    private int requestCode = 210;

    private DeviceInfoResult deviceInfoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing);
        //hide nav bar
        getSupportActionBar().hide();

        deviceInfoResult = (DeviceInfoResult) getIntent().getBundleExtra("BUNDLE").getSerializable("DEVICE_CONTROL_ENTRY");

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
        tv_week = (TextView) findViewById(R.id.tv_week);
    }

    private void onFabPressed(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivityForResult(intent, 222);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llStartTime:
                requestCode = 212;
//                onFabPressed(DialogTimeActivity.class);
                TimePickerView.Type type = TimePickerView.Type.ALL;
                String format = "HH:mm";
                TimePickerUtils.alertTimerPicker(this, type, format, new TimePickerUtils.TimerPickerCallBack() {
                    @Override
                    public void onTimeSelect(Date date, String dateStr) {
                        ToastUtil.showToast(TimingActivity.this,dateStr);
                    }
                });
                break;
            case R.id.tvEditSure:
                //TODO 编辑提交
                break;
            case R.id.llWeek:
                requestCode = 213;
                onFabPressed(DialogWeelActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 10:
                String strTimelist = data.getStringExtra("SELECTTIME");
                tv_week.setText(strTimelist);
                break;
            case 20:
                ArrayList<String> array = data.getStringArrayListExtra("SELECTWEEK");
                String strWeekList = "";
                for(int i=0;i<array.size();i++){
                    strWeekList =strWeekList+","+array.get(i);
                }
                tv_week.setText(strWeekList);
                break;
            default:
                break;
        }

    }
}
