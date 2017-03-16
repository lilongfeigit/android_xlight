package com.umarbhutta.xlightcompanion.userManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestSendVerifyCode;
import com.umarbhutta.xlightcompanion.okHttp.requests.imp.CommentRequstCallback;
import com.umarbhutta.xlightcompanion.settings.ResetPasswordActivity;

/**
 * Created by Administrator on 2017/3/4.
 */

public class FindPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private EditText et_user_account;

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

        et_user_account = (EditText) findViewById(R.id.et_user_account);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finash_registered:
                findPwd();
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

    private void findPwd() {


        final String email = et_user_account.getText().toString();

        if (TextUtils.isEmpty(email)) {
            ToastUtil.showToast(this, "请输入您的邮箱");
            return;
        }

        RequestSendVerifyCode.getInstance().sendCode(this, email, new CommentRequstCallback() {
            @Override
            public void onCommentRequstCallbackSuccess() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(FindPasswordActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        FindPasswordActivity.this.finish();
                    }
                });


            }

            @Override
            public void onCommentRequstCallbackFail(int code, final String errMsg) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(FindPasswordActivity.this, "" + errMsg);
                    }
                });

            }
        });
    }

}
