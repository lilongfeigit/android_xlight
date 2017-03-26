package com.umarbhutta.xlightcompanion.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.control.ControlRuleFragment;
import com.umarbhutta.xlightcompanion.glance.GlanceMainFragment;
import com.umarbhutta.xlightcompanion.help.HelpFragment;
import com.umarbhutta.xlightcompanion.report.ReportFragment;
import com.umarbhutta.xlightcompanion.scenario.ScenarioMainFragment;
import com.umarbhutta.xlightcompanion.settings.SettingFragment;
import com.umarbhutta.xlightcompanion.settings.UserMsgModifyActivity;
import com.umarbhutta.xlightcompanion.userManager.LoginActivity;
import com.umarbhutta.xlightcompanion.views.CircleImageView;

public class MainMenuFragment extends Fragment implements View.OnClickListener {

    private TextView tv_userName, textView;
    private Button btnLogin;
    private LinearLayout llPerName;
    private TextView itemGlance, itemControl, itemScenario, itemSchedule, itemSettings, itemHelp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        llPerName = (LinearLayout) getActivity().findViewById(R.id.llPerName);
        tv_userName = (TextView) getActivity().findViewById(R.id.tv_userName);
        textView = (TextView) getActivity().findViewById(R.id.textView);

        btnLogin = (Button) getActivity().findViewById(R.id.btn_login);
        CircleImageView userIcon = (CircleImageView) getActivity().findViewById(R.id.userIcon);
        itemGlance = (TextView) getActivity().findViewById(R.id.nav_glance);
        itemControl = (TextView) getActivity().findViewById(R.id.nav_control);
        itemScenario = (TextView) getActivity().findViewById(R.id.nav_scenario);
        itemSchedule = (TextView) getActivity().findViewById(R.id.nav_schedule);
        itemSettings = (TextView) getActivity().findViewById(R.id.nav_settings);
        itemHelp = (TextView) getActivity().findViewById(R.id.nav_help);
        itemGlance.setOnClickListener(this);
        itemControl.setOnClickListener(this);
        itemScenario.setOnClickListener(this);
        itemSchedule.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        itemHelp.setOnClickListener(this);
        userIcon.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof SlidingMenuMainActivity) {
            SlidingMenuMainActivity ra = (SlidingMenuMainActivity) getActivity();
            ra.switchContent(fragment);
        }
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.btn_login:
                onFabPressed(LoginActivity.class);
                break;
            case R.id.userIcon:
                onFabPressed(UserMsgModifyActivity.class);
                break;
            case R.id.nav_glance:
                fragment = new GlanceMainFragment();//首页
                break;
            case R.id.nav_control:
                if (!UserUtils.isLogin(getActivity())) {
                    onFabPressed(LoginActivity.class);
                    return;
                }
                fragment = new ControlRuleFragment();//规则
                break;
            case R.id.nav_scenario:
                if (!UserUtils.isLogin(getActivity())) {
                    onFabPressed(LoginActivity.class);
                    return;
                }
                fragment = new ScenarioMainFragment();//场景
                break;
            case R.id.nav_schedule:
                if (!UserUtils.isLogin(getActivity())) {
                    onFabPressed(LoginActivity.class);
                    return;
                }
                fragment = new ReportFragment();//报表
                break;
            case R.id.nav_settings:
                if (!UserUtils.isLogin(getActivity())) {
                    onFabPressed(LoginActivity.class);
                    return;
                }
                fragment = new SettingFragment();//设置
                break;
            case R.id.nav_help:
                fragment = new HelpFragment();//帮助
                break;
        }
        if (fragment != null) {
            switchFragment(fragment);
        }
    }

    private void onFabPressed(Class activity) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof SlidingMenuMainActivity) {
            SlidingMenuMainActivity ra = (SlidingMenuMainActivity) getActivity();
            ra.onActivityPressed(activity);
        }
    }
}