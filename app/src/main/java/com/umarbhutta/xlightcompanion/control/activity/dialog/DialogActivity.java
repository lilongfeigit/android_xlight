package com.umarbhutta.xlightcompanion.control.activity.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.control.adapter.DialogListAdapter;
import com.umarbhutta.xlightcompanion.control.bean.Ruleconditions;
import com.umarbhutta.xlightcompanion.okHttp.model.Condition;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/15.
 * 弹出框Activity
 */

public class DialogActivity extends Activity {

    private int type;
    DialogListAdapter dialogConditionListAdapter;
    ListView dialoglist;
    private Condition mCondition;
    private Ruleconditions ruleconditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initViews();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        dialoglist = (ListView) findViewById(R.id.dialoglist);
//        settingStr = EntryConditionActivity.listStr;
        type = getIntent().getBundleExtra("BUNDLE").getInt("TYPE");
        mCondition = (Condition) getIntent().getBundleExtra("BUNDLE").getSerializable("CONDITION");
        ruleconditions = (Ruleconditions) getIntent().getBundleExtra("BUNDLE").getSerializable("RULECONDITIONS");
        // 0是定时 1是亮度 2是活动，3是声音，4是温度 5是离家，6是回家，7是气体 8是大于，小于，等于 ，9是温度
        dialogConditionListAdapter = new DialogListAdapter(DialogActivity.this.getApplicationContext(),ruleconditions,type);
        dialoglist.setAdapter(dialogConditionListAdapter);

        dialoglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type){// 0是定时 1是亮度 2是活动，3是声音，4是温度 5是离家，6是回家，7是气体 8是大于，小于，等于 ，9是温度
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                }
                mCondition.ruleconditionname = "condition1";
                mCondition.devicenodeId = 0;
                mCondition.attribute = "温度";
                mCondition.operator = ">";
                mCondition.rightValue = "100";
                mCondition.status = 0;
                Intent intent = new Intent();
                intent.putExtra("MCONDITION",mCondition);
                setResult(31,intent);
                finish();
            }
        });
        dialogConditionListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
