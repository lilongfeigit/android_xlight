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
import com.umarbhutta.xlightcompanion.okHttp.model.Condition;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/15.
 * 弹出框Activity
 */

public class DialogActivity extends Activity {

    private ArrayList<String> settingStr = new ArrayList<String>();
    private int type;

    DialogListAdapter dialogConditionListAdapter;
    ListView dialoglist;
    private Condition mCondition;

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
        settingStr = getIntent().getBundleExtra("BUNDLE").getStringArrayList("DILOGLIST");//
        type = getIntent().getBundleExtra("BUNDLE").getInt("TYPE");
        mCondition = (Condition) getIntent().getBundleExtra("BUNDLE").getSerializable("CONDITION");

        dialogConditionListAdapter = new DialogListAdapter(DialogActivity.this.getApplicationContext(), settingStr);
        dialoglist.setAdapter(dialogConditionListAdapter);

        dialoglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type){
                    case 0://从EntryConditionActivity传过来的
                        break;
                    case 1://从TimingActivity传过来的
                        break;
                    case 2://从AddControlRule传过来的
                        break;
                    case 3://从AddControlRule传过来的
                        break;
                    case 4://从AddControlRule传过来的
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
