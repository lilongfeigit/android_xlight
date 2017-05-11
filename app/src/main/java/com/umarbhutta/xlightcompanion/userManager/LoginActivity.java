package com.umarbhutta.xlightcompanion.userManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umarbhutta.xlightcompanion.App;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.Tools.StringUtil;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.model.LoginParam;
import com.umarbhutta.xlightcompanion.okHttp.model.LoginResult;
import com.umarbhutta.xlightcompanion.settings.BaseActivity;

/**
 * Created by Administrator on 2017/3/4.
 * login
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, HttpUtils.OnHttpRequestCallBack {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private EditText et_user_account;
    private EditText et_user_password;
    private ImageButton clearBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        //hide nav bar
//        getSupportActionBar().hide();
        ((App) getApplicationContext()).setActivity(this);
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
        llBack.setVisibility(View.INVISIBLE);
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        btnSure.setText(R.string.close);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.login);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.activity_close);
            }
        });


        clearBtn = (ImageButton) findViewById(R.id.ib_clear2);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_user_password.setText("");
            }
        });
        clearBtn.setVisibility(View.INVISIBLE);


        et_user_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 0) {
                    clearBtn.setVisibility(View.INVISIBLE);
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        et_user_account.setText("");
        et_user_password.setText("");
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

        if (!StringUtil.isEmail(et_user_accountStr)) {
            ToastUtil.showToast(this, getString(R.string.email_error));
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
                    info.data.get(0).setImage(NetConfig.SERVER_ADDRESS + info.data.get(0).getImage());
                    UserUtils.saveUserInfo(LoginActivity.this, info.data.get(0));
                    ToastUtil.showToast(LoginActivity.this, getString(R.string.login_success));
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
                ToastUtil.showToast(LoginActivity.this, "" + errMsg);
            }
        });
    }
}
