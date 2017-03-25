package com.umarbhutta.xlightcompanion.control.activity.result;

import android.content.Intent;
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
import com.umarbhutta.xlightcompanion.control.activity.AddControlRuleActivity;
import com.umarbhutta.xlightcompanion.okHttp.model.Actionnotify;

/**
 * Created by Administrator on 2017/3/15.
 * email 通知页面
 */

public class EmailActivity extends AppCompatActivity {


    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;

    private EditText et_email, et_title, et_context;

    private Actionnotify mActionnotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
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
        btnSure.setText("完成");
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("通知Email");

        et_email = (EditText) findViewById(R.id.et_email);
        et_title = (EditText) findViewById(R.id.et_title);
        et_context = (EditText) findViewById(R.id.et_context);

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String emailTitle = et_title.getText().toString();
                String emailContext = et_context.getText().toString();
                if(TextUtils.isEmpty(email)){
                    ToastUtil.showToast(getApplicationContext(),"请输入邮箱");
                    return;
                }
                if(TextUtils.isEmpty(emailTitle)){
                    ToastUtil.showToast(getApplicationContext(),"请输入通知邮箱的主题");
                    return;
                }
                if(TextUtils.isEmpty(emailContext)){
                    ToastUtil.showToast(getApplicationContext(),"请输入通知内容");
                    return;
                }
                mActionnotify.emailaddress = email;
                mActionnotify.content = emailContext;
                mActionnotify.subject = emailTitle;
                AddControlRuleActivity.mActionnotify.add(mActionnotify);
                ((App)getApplicationContext()).finishActivity();
            }
        });
    }

}