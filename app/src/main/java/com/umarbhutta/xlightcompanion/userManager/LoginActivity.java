package com.umarbhutta.xlightcompanion.userManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.model.LoginParam;
import com.umarbhutta.xlightcompanion.okHttp.model.LoginResult;

/**
 * Created by Administrator on 2017/3/4.
 * login
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, HttpUtils.OnHttpRequestCallBack {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private EditText et_user_account;
    private EditText et_user_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        //hide nav bar
        getSupportActionBar().hide();

        initViews();
    }

    private void initViews() {
        et_user_account = (EditText) findViewById(R.id.et_user_account);
        et_user_password = (EditText) findViewById(R.id.et_user_password);
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
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_forget_password:
                //
                onFabPressed(FindPasswordActivity.class);
                break;
            case R.id.tv_new_user_res:
                //
                onFabPressed(RegisteredActivity.class);
                break;
        }
    }

    private void onFabPressed(Class activity) {
        Intent intent = new Intent(LoginActivity.this, activity);
        startActivityForResult(intent, 1);
    }

    private void login() {
        String et_user_accountStr = et_user_account.getText().toString();
        String et_user_passwordStr = et_user_password.getText().toString();

        if (TextUtils.isEmpty(et_user_accountStr)) {
            ToastUtil.showToast(this, getString(R.string.account_is_null));
            return;
        }

        if (TextUtils.isEmpty(et_user_passwordStr)) {
            ToastUtil.showToast(this, getString(R.string.password_is_null));
            return;
        }

        LoginParam param = new LoginParam(et_user_accountStr, et_user_passwordStr);

        Gson gson = new Gson();
        String paramStr = gson.toJson(param);

        HttpUtils.getInstance().postRequestInfo(NetConfig.URL_LOGIN, paramStr, LoginResult.class, this);
    }

    @Override
    public void onHttpRequestSuccess(final Object result) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginResult info = (LoginResult) result;
                if (info.code == 1) {   //登录成功
                    ToastUtil.showToast(LoginActivity.this, getString(R.string.login_success));
                    UserUtils.saveUserInfo(LoginActivity.this, info.data.get(0));
                    finish();
                } else if (info.code == 0) {  //登录失败，提示服务端返回的信息
                    ToastUtil.showToast(LoginActivity.this, info.msg);
                } else {
                    ToastUtil.showToast(LoginActivity.this, getString(R.string.net_error));
                }
            }
        });


    }

    @Override
    public void onHttpRequestFail(int code, final String errMsg) {
        Logger.i("login fail = ");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(LoginActivity.this,getString(R.string.login_fail)+errMsg);
            }
        });
    }
}
