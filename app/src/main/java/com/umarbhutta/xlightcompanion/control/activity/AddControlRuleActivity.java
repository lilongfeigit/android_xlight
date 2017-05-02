package com.umarbhutta.xlightcompanion.control.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.adapter.NewRuleAdapter;
import com.umarbhutta.xlightcompanion.control.activity.condition.EntryConditionActivity;
import com.umarbhutta.xlightcompanion.control.activity.result.ControlRuseltActivity;
import com.umarbhutta.xlightcompanion.control.bean.ControlRuleDevice;
import com.umarbhutta.xlightcompanion.control.bean.NewRuleInfo;
import com.umarbhutta.xlightcompanion.control.bean.NewRuleItemInfo;
import com.umarbhutta.xlightcompanion.okHttp.model.Actioncmd;
import com.umarbhutta.xlightcompanion.okHttp.model.Actionnotify;
import com.umarbhutta.xlightcompanion.okHttp.model.Condition;
import com.umarbhutta.xlightcompanion.okHttp.model.CreateRuleResult;
import com.umarbhutta.xlightcompanion.okHttp.model.Rulecondition;
import com.umarbhutta.xlightcompanion.okHttp.model.Ruleresult;
import com.umarbhutta.xlightcompanion.okHttp.model.Rules;
import com.umarbhutta.xlightcompanion.okHttp.model.Schedule;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestAddRules;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umar Bhutta.
 * 新的规则页面
 */
public class AddControlRuleActivity extends AppCompatActivity {

    private String TAG = AddControlRuleActivity.class.getSimpleName();
    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private Rules rules;

    public static List<String> mScheduleListStr = new ArrayList<String>();

    /**
     * 条件
     */
    public static List<NewRuleItemInfo> mNewRuleConditionInfoList = new ArrayList<NewRuleItemInfo>();
    /**
     * 结果
     */
    public static List<NewRuleItemInfo> mNewRuleResultInfoList = new ArrayList<NewRuleItemInfo>();


    private ListView listview;
    private NewRuleAdapter mNewRuleAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNewRuleConditionInfoList.clear();
        mNewRuleResultInfoList.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_control_rule);
        //hide nav bar
        getSupportActionBar().hide();

        rules = new Rules();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNewRuleAdapter.notifyDataSetChanged();
    }

    private void initViews() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listview = (ListView) findViewById(R.id.listview);

        NewRuleInfo mNewRuleInfo = new NewRuleInfo();
        mNewRuleInfo.mNewRuleConditionInfoList = mNewRuleConditionInfoList;
        mNewRuleInfo.mNewRuleResultInfoList = mNewRuleResultInfoList;
        if (null == mNewRuleAdapter) {
            mNewRuleAdapter = new NewRuleAdapter(this, mNewRuleInfo);
            mNewRuleAdapter.setOnClickAddIconListener(new NewRuleAdapter.OnClickAddIconListener() {
                @Override
                public void clickAddCondition() {
                    onFabPressed(EntryConditionActivity.class);
                }

                @Override
                public void clickAddResult() {
                    onFabPressed(ControlRuseltActivity.class);
                }

                @Override
                public void clickDeleteIcon(boolean isCondition, int position) {
                    if (isCondition) {
                        mNewRuleConditionInfoList.remove(position);
                    } else {
                        mNewRuleResultInfoList.remove(position);
                    }
                    mNewRuleAdapter.notifyDataSetChanged();
                }
            });

            listview.setAdapter(mNewRuleAdapter);
        } else {
            mNewRuleAdapter.notifyDataSetChanged();
        }

        btnSure = (TextView) findViewById(R.id.tvEditSure);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Ruleresult mRuleresult = new Ruleresult();
                Rulecondition mRulecondition = new Rulecondition();
                //确定提交按钮
                rules.rulename = "rule1";
                rules.relationtype = 1;
                rules.type = 1;
                rules.status = 1;
                rules.userId = UserUtils.getUserInfo(getApplicationContext()).getId();

                List<List> lists = getResultList();


                List<Rulecondition> rulecondition = new ArrayList<Rulecondition>();
                List<Ruleresult> ruleresult = new ArrayList<Ruleresult>();
                mRulecondition.schedule = lists.get(0);
                mRulecondition.condition = lists.get(1);
                mRuleresult.actioncmd = lists.get(2);
                mRuleresult.actionnotify = lists.get(3);
                rulecondition.add(mRulecondition);
                ruleresult.add(mRuleresult);
                rules.rulecondition = rulecondition;
                rules.ruleresult = ruleresult;
                RequestAddRules.getInstance().createRule(AddControlRuleActivity.this, rules, new RequestAddRules.OnCreateRuleCallback() {

                    @Override
                    public void mOnCreateRuleCallbackFail(int code, String errMsg) {
                        Logger.e(TAG, "errMsg=" + errMsg);
                        ToastUtil.showToast(AddControlRuleActivity.this, "errMsg=" + errMsg);
                    }

                    @Override
                    public void mOnCreateRuleCallbackSuccess(CreateRuleResult mCreateRuleResult) {
                        Logger.e(TAG, "mCreateRuleResult=" + mCreateRuleResult.code);
                        ToastUtil.showToast(AddControlRuleActivity.this, getString(R.string.rule_create_success));
                        finish();
                    }
                });
            }
        });
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.create_rule);
    }

    /**
     * 过滤结果
     *
     * @return
     */
    private List<List> getResultList() {

        List<List> lists = new ArrayList<List>();

        List<Schedule> mScheduleList = new ArrayList<Schedule>();
        List<Condition> mConditionList = new ArrayList<Condition>();
        List<Actioncmd> mActioncmdList = new ArrayList<Actioncmd>();
        List<Actionnotify> mActionnotifyList = new ArrayList<Actionnotify>();
        List<ControlRuleDevice> mControlRuleDeviceList = new ArrayList<ControlRuleDevice>();

        //条件
        for (NewRuleItemInfo info : mNewRuleConditionInfoList) {
            switch (info.getType()) {
                case 0:
                    mScheduleList.add(info.getmSchedule());
                    break;
                case 1:
                    mConditionList.add(info.getmCondition());
                    break;
                case 2:
                    mActioncmdList.add(info.getmActioncmd());
                    break;
                case 3:
                    mActionnotifyList.add(info.getmActionnotify());
                    break;
            }
        }

        //结果
        for (NewRuleItemInfo info : mNewRuleResultInfoList) {
            switch (info.getType()) {
                case 0:
                    mScheduleList.add(info.getmSchedule());
                    break;
                case 1:
                    mConditionList.add(info.getmCondition());
                    break;
                case 2:
                    mActioncmdList.add(info.getmActioncmd());
                    break;
                case 3:
                    mActionnotifyList.add(info.getmActionnotify());
                    break;
                case 4:
                    mControlRuleDeviceList.add(info.getmControlRuleDevice());
                    break;
            }
        }

        lists.add(mScheduleList);
        lists.add(mConditionList);
        lists.add(mActioncmdList);
        lists.add(mActionnotifyList);

        return lists;

    }


    private void onFabPressed(Class activity) {
        Intent intent = new Intent(this, activity);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DEVICE_CONTROL", rules);
        intent.putExtra("BUNDLE", bundle);
        startActivityForResult(intent, 2018);
    }
}
