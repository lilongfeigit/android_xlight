package com.umarbhutta.xlightcompanion.settings;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.imgloader.ImageLoaderOptions;
import com.umarbhutta.xlightcompanion.okHttp.model.LoginResult;
import com.umarbhutta.xlightcompanion.views.CircleImageView;

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
                break;
            case R.id.nick_name_layout:

                break;
            case R.id.avatar_layout:
                showPictureSelector();
                break;
            case R.id.sex_layout:
                break;
        }
    }



}
