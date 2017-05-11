package com.umarbhutta.xlightcompanion.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.main.SlidingMenuMainActivity;
import com.umarbhutta.xlightcompanion.userManager.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/5.
 */

public class SettingFragment extends Fragment implements View.OnClickListener {


    private List<String> settingStr = new ArrayList<String>();

    //    public static String[] settingStr = {"个人信息","修改密码","摇一摇","用户邀请","快速绑定","退出登录"};
    SettingListAdapter settingListAdapter;
    ListView settingListView;

    private ImageView iv_menu;
    private TextView textTitle;
    private Button btn_add;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        iv_menu = (ImageView) view.findViewById(R.id.iv_menu);
        iv_menu.setOnClickListener(this);
        textTitle = (TextView) view.findViewById(R.id.tvTitle);
        textTitle.setText("设置");
        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_add.setVisibility(View.INVISIBLE);

        settingListView = (ListView) view.findViewById(R.id.settingListView);
        settingListAdapter = new SettingListAdapter(getActivity(), settingStr);
        settingListView.setAdapter(settingListAdapter);

        settingStr.add(getString(R.string.persion_inco));
        settingStr.add(getString(R.string.modify_pwd));
        settingStr.add(getString(R.string.shake));
//        settingStr.add("用户邀请");
//        settingStr.add("快速绑定");
        settingStr.add(getString(R.string.logout));
        settingListAdapter.notifyDataSetChanged();
        settingListAdapter.setmOnItemClickListener(new SettingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0://个人信息
                        if (!UserUtils.isLogin(getActivity())) {
                            onFabPressed(LoginActivity.class);
                            getActivity().overridePendingTransition(R.anim.activity_open,0);
                            return;
                        }
                        onFabPressed(UserMsgModifyActivity.class);
                        break;
                    case 1://修改密码
                        if (!UserUtils.isLogin(getActivity())) {
                            onFabPressed(LoginActivity.class);
                            getActivity().overridePendingTransition(R.anim.activity_open,0);
                            return;
                        }
                        onFabPressed(ModifyPasswordActivity.class);
                        break;
                    case 2://摇一摇
                        if (!UserUtils.isLogin(getActivity())) {
                            onFabPressed(LoginActivity.class);
                            getActivity().overridePendingTransition(R.anim.activity_open,0);
                            return;
                        }
                        onFabPressed(ShakeActivity.class);
                        break;
//                    case 3://用户邀请
//                        onFabPressed(UserInvitationActivity.class);
//                        break;
//                    case 4://快速绑定
//                        onFabPressed(FastBindingActivity.class);
//                        break;
                    case 3://退出登录
                        if (!UserUtils.isLogin(getActivity())) {
                            onFabPressed(LoginActivity.class);
                            return;
                        }
                        logout();
                        break;
                }
            }
        });
        return view;
    }

    private void onFabPressed(Class activity) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
    }

    /**
     * 退出登录
     */
    private void logout() {
        UserUtils.saveUserInfo(getActivity(), null);
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                switchFragment();
                break;
        }
    }

    // the meat of switching the above fragment
    private void switchFragment() {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof SlidingMenuMainActivity) {
            SlidingMenuMainActivity ra = (SlidingMenuMainActivity) getActivity();
            ra.toggle();
        }
    }
}
