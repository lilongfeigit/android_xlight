package com.umarbhutta.xlightcompanion.settings;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.model.CommentResult;
import com.umarbhutta.xlightcompanion.okHttp.model.ModifyPwdParam;
import com.umarbhutta.xlightcompanion.views.ProgressDialogUtils;

/**
 * Created by Administrator on 2017/3/5.
 */

public class ModifyPasswordActivity extends AppCompatActivity implements HttpUtils.OnHttpRequestCallBack {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private EditText et_old_passwordTv;
    private EditText et_new_passwordTv;
    private EditText et_new_password_againTv;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
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
        findViewById(R.id.tvEditSure).setVisibility(View.GONE);
        btnSure = (TextView) findViewById(R.id.btn_login);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("修改密码");


        et_old_passwordTv = (EditText) findViewById(R.id.et_old_password);
        et_new_passwordTv = (EditText) findViewById(R.id.et_new_password);
        et_new_password_againTv = (EditText) findViewById(R.id.et_new_password_again);

    }

    private void commit() {
        String et_old_passwordTvStr = et_old_passwordTv.getText().toString();
        String et_new_passwordTvStr = et_new_passwordTv.getText().toString();
        String et_new_password_againTvStr = et_new_password_againTv.getText().toString();

        if (TextUtils.isEmpty(et_old_passwordTvStr)) {
            ToastUtil.showToast(this, R.string.input_old_pwd);
            return;
        }

        if (TextUtils.isEmpty(et_new_passwordTvStr)) {
            ToastUtil.showToast(this, R.string.input_new_pwd);
            return;
        }


        if (TextUtils.isEmpty(et_new_password_againTvStr)) {
            ToastUtil.showToast(this, getString(R.string.input_new_pwd_again));
            return;
        }


        if (!et_new_passwordTvStr.equals(et_new_password_againTvStr)) {
            ToastUtil.showToast(this, getString(R.string.two_pwd_not_same));
            return;
        }


        dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.commit_img));

        ModifyPwdParam param = new ModifyPwdParam(et_old_passwordTvStr, et_new_passwordTvStr);

        Gson gson = new Gson();
        String paramStr = gson.toJson(param);

        HttpUtils.getInstance().putRequestInfo(NetConfig.URL_MODIFY_PWD + UserUtils.getUserInfo(this).getId() + "/resetpassword?access_token=" + UserUtils.getUserInfo(this).getAccess_token(), paramStr, CommentResult.class, this);

    }

    @Override
    public void onHttpRequestSuccess(final Object result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
                CommentResult info = (CommentResult) result;
                if (info.code == 1) {
                    ToastUtil.showToast(ModifyPasswordActivity.this, getString(R.string.modify_pwd_success));
                    finish();
                } else {
                    ToastUtil.showToast(ModifyPasswordActivity.this, info.msg);
                }
            }
        });

    }

    @Override
    public void onHttpRequestFail(int code, String errMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
                ToastUtil.showToast(ModifyPasswordActivity.this, R.string.net_error);
            }
        });

    }
}
