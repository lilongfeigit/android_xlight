package com.umarbhutta.xlightcompanion.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.imgloader.ImageLoaderOptions;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.model.LoginResult;
import com.umarbhutta.xlightcompanion.views.CircleImageView;
import com.umarbhutta.xlightcompanion.views.pickerview.lib.TimePickerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/5.
 * user信息修改页面
 */
public class UserMsgModifyActivity extends ShowPicSelectBaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private TextView nick_name;
    private TextView user_name;
    private TextView sex;
    private CircleImageView user_icon;
    /**
     * 性别
     */
    private ArrayList<String> sexList = new ArrayList<String>();
    private String usernameResult;
    private String nickNameResult;
    private String sexResResult;

    @Override
    public void selectPicResult(String picPath) {
        ToastUtil.showToast(this, "url = " + picPath);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg_modify);
        getSupportActionBar().hide();
        initViews();
    }

    private UserMsgModifyAdapter mUserMsgModifyAdapter;
    private RecyclerView mRecyclerView;
    private List<String> userMsgs = new ArrayList<String>();

    private void initViews() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        btnSure.setVisibility(View.GONE);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("用户信息");
        sexList.add("女");
        sexList.add("男");


        findViewById(R.id.account_layout).setOnClickListener(this);
        findViewById(R.id.nick_name_layout).setOnClickListener(this);
        findViewById(R.id.avatar_layout).setOnClickListener(this);
        findViewById(R.id.sex_layout).setOnClickListener(this);

        user_name = (TextView) findViewById(R.id.user_name);
        nick_name = (TextView) findViewById(R.id.nick_name);
        sex = (TextView) findViewById(R.id.sex);
        user_icon = (CircleImageView) findViewById(R.id.user_icon);
        updateUserinfo();
    }

    private void updateUserinfo() {
        LoginResult info = UserUtils.getUserInfo(this);
        user_name.setText("" + info.username);
        nick_name.setText("" + ((TextUtils.isEmpty(info.nickname) ? "" : info.nickname)));
        sex.setText("" + ((TextUtils.isEmpty(info.sex)) ? "女" : "男"));
        ImageLoader.getInstance().displayImage(info.getImage(), user_icon, ImageLoaderOptions.getImageLoaderOptions());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_layout://账号
                showDialog(v);
                break;
            case R.id.nick_name_layout://呢称
                showDialog(v);
                break;
            case R.id.avatar_layout:
                showPictureSelector();
                break;
            case R.id.sex_layout:
                showSexPicker();
                break;
        }
    }

    public int type = 0;

    private void showDialog(View view) {

        String title;
        if (R.id.account_layout == view.getId()) {
            title = "修改账号";
            type = 0;
        } else {
            title = "修改呢称";
            type = 1;
        }

        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle(title)
                .setView(et)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();

                        if (TextUtils.isEmpty(input)) {
                            ToastUtil.showToast(UserMsgModifyActivity.this, getString(R.string.content_is_null));
                            return;
                        }

                        modifyUserInfo(type, input);

                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private int sexPosition;

    private void showSexPicker() {
        TimePickerUtils.alertBottomWheelOption(this, sexList, 0, new TimePickerUtils.OnWheelViewClick() {
            @Override
            public void onClick(View view, int postion) {
                sexPosition = postion;
                sex.setText(sexList.get(postion));
                modifyUserInfo(2, null);
            }
        });
    }

    /**
     * @param type   0修改账号，1修改呢称，2修改性别
     * @param result
     */
    private void modifyUserInfo(int type, String result) {
        LoginResult userInfo = UserUtils.getUserInfo(this);
        usernameResult = userInfo.getUsername();
        nickNameResult = userInfo.getNickname();
        sexResResult = TextUtils.isEmpty(userInfo.getSex()) ? "0" : "1";

        this.type = type;

        switch (type) {
            case 0:
                usernameResult = result;
                break;
            case 1:
                nickNameResult = result;
                break;
            case 2:
                sexResResult = String.valueOf(sexPosition);
                break;
        }


        JSONObject object = new JSONObject();
        try {
            object.put("username", usernameResult);
            object.put("nickname", nickNameResult);
            object.put("sex", sexResResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.getInstance().putRequestInfo(NetConfig.URL_MODIFY_USER_INFO + userInfo.getId() + "?access_token=" + userInfo.getAccess_token(), object.toString(), null, new HttpUtils.OnHttpRequestCallBack() {
            @Override
            public void onHttpRequestSuccess(Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(UserMsgModifyActivity.this, "修改成功");
                        saveUserInfo();
                    }
                });
            }

            @Override
            public void onHttpRequestFail(int code, final String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(UserMsgModifyActivity.this, "" + errMsg);
                    }
                });
            }
        });
    }

    private void saveUserInfo() {
        LoginResult mLoginResult = UserUtils.getUserInfo(this);
        switch (type) {
            case 0:
                mLoginResult.username = usernameResult;
                user_name.setText(usernameResult);
                break;
            case 1:
                mLoginResult.nickname = nickNameResult;
                nick_name.setText(nickNameResult);
                break;
            case 2:
                mLoginResult.sex = "0".equals(sexResResult) ? "女" : "男";
                break;
        }

        UserUtils.saveUserInfo(this, mLoginResult);

    }

}
