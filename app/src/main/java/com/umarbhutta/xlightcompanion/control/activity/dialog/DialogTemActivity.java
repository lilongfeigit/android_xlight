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
import android.widget.RadioButton;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.control.adapter.DialogTemAddListAdapter;
import com.umarbhutta.xlightcompanion.control.adapter.DialogTemMiuListAdapter;
import com.umarbhutta.xlightcompanion.control.bean.Ruleconditions;

/**
 * Created by Administrator on 2017/3/15.
 * 弹出框Activity
 */

public class DialogTemActivity extends Activity implements View.OnClickListener {

    private String TAG = DialogTemActivity.class.getSimpleName();

    DialogTemAddListAdapter dialogConditionListAdapter;
    DialogTemMiuListAdapter dialogTemMiuListAdapter;
    ListView dialogAddlist;
    ListView dialogMiuList;

    private RadioButton rb_add,rb_miu;

    private Ruleconditions ruleconditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tem_dialog);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initViews();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        dialogAddlist = (ListView) findViewById(R.id.dialogAddlist);
        dialogMiuList = (ListView) findViewById(R.id.dialogMiulist);

        rb_add = (RadioButton) findViewById(R.id.rb_add);
        rb_add.setOnClickListener(this);
        rb_miu = (RadioButton) findViewById(R.id.rb_miu);
        rb_miu.setOnClickListener(this);

//        settingStr = EntryConditionActivity.listStr;
        ruleconditions = (Ruleconditions) getIntent().getBundleExtra("BUNDLE").getSerializable("RULECONDITIONS");
        Logger.e(TAG,ruleconditions.data.get(0).getTemperature().toString());
        String addValueTem = ruleconditions.data.get(0).temperature.get(0).value;
        String[] addTem = addValueTem.substring(1,addValueTem.length()-1).split(",");
        String miuAddValueTem = ruleconditions.data.get(0).temperature.get(1).value;
        String[] miuTem = miuAddValueTem.substring(1,miuAddValueTem.length()-1).split(",");
        // 是温度
        dialogConditionListAdapter = new DialogTemAddListAdapter(DialogTemActivity.this.getApplicationContext(),addTem);
        dialogTemMiuListAdapter = new DialogTemMiuListAdapter(DialogTemActivity.this.getApplicationContext(),miuTem);
        dialogAddlist.setAdapter(dialogConditionListAdapter);
        dialogMiuList.setAdapter(dialogTemMiuListAdapter);

        dialogAddlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rb_add:
                dialogAddlist.setVisibility(View.VISIBLE);
                dialogMiuList.setVisibility(View.GONE);
                if(dialogConditionListAdapter!=null){
                    dialogConditionListAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.rb_miu:
                dialogAddlist.setVisibility(View.GONE);
                dialogMiuList.setVisibility(View.VISIBLE);
                if(dialogTemMiuListAdapter!=null){
                    dialogTemMiuListAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
