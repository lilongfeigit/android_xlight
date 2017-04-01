package com.umarbhutta.xlightcompanion.settings;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.model.CommentResult;
import com.umarbhutta.xlightcompanion.views.ProgressDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/5.
 * 重置密码
 */

public class ResetPasswordActivity extends AppCompatActivity implements HttpUtils.OnHttpRequestCallBack {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private EditText et_old_passwordTv;
    private EditText et_new_passwordTv;
    private EditText et_new_password_againTv;
    private ProgressDialog dialog;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        //hide nav bar
        getSupportActionBar().hide();
        email = getIntent().getStringExtra("email");
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
        tvTitle.setText(R.string.reset_pwd);


        et_old_passwordTv = (EditText) findViewById(R.id.et_old_password);
        et_new_passwordTv = (EditText) findViewById(R.id.et_new_password);
        et_new_password_againTv = (EditText) findViewById(R.id.et_new_password_again);


        findViewById(R.id.ib_clear1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_old_passwordTv.setText("");
            }
        });

        findViewById(R.id.ib_clear2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_new_passwordTv.setText("");
            }
        });


        findViewById(R.id.first_layout).setVisibility(View.GONE);

    }

    private void commit() {
        String et_new_passwordTvStr = et_new_passwordTv.getText().toString();
        String et_new_password_againTvStr = et_new_password_againTv.getText().toString();

        if (TextUtils.isEmpty(et_new_passwordTvStr)) {
            ToastUtil.showToast(this, R.string.input_new_pwd);
            return;
        }

        if (TextUtils.isEmpty(et_new_password_againTvStr)) {
            ToastUtil.showToast(this, getString(R.string.please_input_verifycode));
            return;
        }


        dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.commit_img));

        JSONObject object = new JSONObject();
        try {
            object.put("email", email);
            object.put("password", et_new_passwordTvStr);
            object.put("verificationcode", et_new_password_againTvStr);
            String paramStr = object.toString();
            HttpUtils.getInstance().putRequestInfo(NetConfig.URL_RESET_PWD, paramStr, CommentResult.class, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onHttpRequestSuccess(final Object result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
                ToastUtil.showToast(ResetPasswordActivity.this, getString(R.string.pwd_reset_success));
                ResetPasswordActivity.this.finish();
            }
        });

    }

    @Override
    public void onHttpRequestFail(int code, final String errMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
                ToastUtil.showToast(ResetPasswordActivity.this, "" + errMsg);
            }
        });

    }
}
