package com.umarbhutta.xlightcompanion.userManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.Tools.StringUtil;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.model.RegisteResult;
import com.umarbhutta.xlightcompanion.okHttp.model.RigsteParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/4.
 */

public class RegisteredActivity extends AppCompatActivity implements View.OnClickListener, HttpUtils.OnHttpRequestCallBack {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private EditText et_user_account;
    private EditText et_user_password;
    private EditText et_verification_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registerd);
        //hide nav bar
        getSupportActionBar().hide();
        initViews();
        getHelpUrl();
    }

    private void initViews() {
        et_user_account = (EditText) findViewById(R.id.et_user_account);
        et_user_password = (EditText) findViewById(R.id.et_user_password);
        et_user_password.setHint("请输入密码");
        et_user_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et_verification_code = (EditText) findViewById(R.id.et_verification_code);
        et_verification_code.setVisibility(View.GONE);
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
        btnSure.setText("关闭");
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("注册");
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finash_registered:
                //TODO
                registe();
                break;
            case R.id.tv_protocol:
                //TODO
                onFabPressed();
                break;
        }
    }

    private void onFabPressed() {
        Intent intent = new Intent(RegisteredActivity.this, UserResProtocalActivity.class);
        intent.putExtra("url", url);
        startActivityForResult(intent, 1);
    }

    private void registe() {
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
            ToastUtil.showToast(this, "邮箱格式不正确");
            return;
        }

        RigsteParam param = new RigsteParam(et_user_accountStr, et_user_passwordStr);

        Gson gson = new Gson();
        String paramStr = gson.toJson(param);

        HttpUtils.getInstance().postRequestInfo(NetConfig.URL_REGISTER, paramStr, RegisteResult.class, this);
    }

    @Override
    public void onHttpRequestSuccess(final Object result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RegisteResult info = (RegisteResult) result;
                if (info.code == 0) {
                    ToastUtil.showToast(RegisteredActivity.this, info.msg);
                } else if (1 == info.code) {
                    ToastUtil.showToast(RegisteredActivity.this, getString(R.string.registe_success));
                    finish();
                } else {
                    ToastUtil.showToast(RegisteredActivity.this, getString(R.string.net_error));
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
                ToastUtil.showToast(RegisteredActivity.this, getString(R.string.register_fail) + errMsg);
            }
        });
    }

    public String url;

    /**
     * 获取注册协议的url
     */
    public void getHelpUrl() {
        HttpUtils.getInstance().getRequestInfo(NetConfig.URL_GET_REGISTER_URL, null, new HttpUtils.OnHttpRequestCallBack() {
            @Override
            public void onHttpRequestSuccess(final Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject((String) result);
                            JSONObject dataObj = jsonObject.getJSONObject("data");
                            url = dataObj.getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onHttpRequestFail(int code, final String errMsg) {

            }
        });
    }


}
