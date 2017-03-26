package com.umarbhutta.xlightcompanion.control.activity.result;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.App;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.control.activity.AddControlRuleActivity;
import com.umarbhutta.xlightcompanion.main.EditDeviceActivity;
import com.umarbhutta.xlightcompanion.okHttp.model.Actionnotify;

/**
 * Created by Administrator on 2017/3/15.
 * email 通知页面
 */

public class AppNotifyActivity extends AppCompatActivity {


    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private EditText et_time;

    private Actionnotify mActionnotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_notify);
        //hide nav bar
        getSupportActionBar().hide();
        ((App)getApplicationContext()).setActivity(this);
        mActionnotify = (Actionnotify) getIntent().getSerializableExtra("MACTIONNOTIFY");
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
        tvTitle.setText("APP通知");
        btnSure.setText("完成");
        et_time = (EditText) findViewById(R.id.tv_time);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定提交按钮
                String appContext = et_time.getText().toString();
                if(TextUtils.isEmpty(appContext)){
                    ToastUtil.showToast(getApplicationContext(),"请输入通知内容");
                    return;
                }
                mActionnotify.msisdn = UserUtils.getUserInfo(AppNotifyActivity.this.getApplicationContext()).getUsergroupId();
                mActionnotify.content = appContext;
                mActionnotify.subject = "App通知";

                AddControlRuleActivity.mActionnotify.add(mActionnotify);
                ((App)getApplicationContext()).finishActivity();
            }
        });
    }

}
