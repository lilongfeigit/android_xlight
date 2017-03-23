package com.umarbhutta.xlightcompanion.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.control.bean.SelectTime;
import com.umarbhutta.xlightcompanion.control.bean.SelectWeek;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 * 弹出框Activity
 */

public class DialogWeelActivity extends Activity implements View.OnClickListener {

    private ArrayList<SelectTime> settingStr = new ArrayList<SelectTime>();

    private ArrayList<SelectWeek> settingWeekStr = new ArrayList<SelectWeek>();

    BaseAdapter dialogConditionListAdapter;
    ListView dialoglist;
    private Button cancel,sure;

    private ArrayList<SelectTime> settingSelectStr = new ArrayList<SelectTime>();

    private ArrayList<SelectWeek> settingSelectWeekStr = new ArrayList<SelectWeek>();

    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initData();
        initViews();
    }

    private void initData() {
        type = 0;
       settingStr.clear();
        settingSelectStr.clear();
       settingStr.add(new SelectTime("执行一次",false));
       settingStr.add(new SelectTime("每天",false));
       settingStr.add(new SelectTime("周一至周五",false));
       settingStr.add(new SelectTime("自定义",false));
    }

    /**
     * 初始化控件
     */
    private void initViews() {

        dialoglist = (ListView) findViewById(R.id.dialoglist);
        dialogConditionListAdapter = new DialogWeekListAdapter(DialogWeelActivity.this.getApplicationContext(), settingStr);
        dialoglist.setAdapter(dialogConditionListAdapter);

        dialoglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if(type==0){
                    if(settingStr.get(position).name.equals("自定义")){
                        initWeeks();
                    }else {
                        if (!settingStr.get(position).isSelect) {
                            for (int i = 0; i < settingStr.size(); i++) {
                                if (settingStr.get(i).isSelect) {
                                    settingStr.get(i).isSelect = false;
                                }
                            }
                            settingSelectStr.clear();
                            settingStr.get(position).isSelect = true;
                            settingSelectStr.add(settingStr.get(position));
                        }
                        Intent in = new Intent();
                            in.putExtra("SELECTTIME",settingStr.get(position).name);
                            setResult(10, in );
                            finish();
                        finish();
                    }

                }else{
                    if(!settingWeekStr.get(position).isSelect){
                        settingWeekStr.get(position).isSelect=true;
                        settingSelectWeekStr.add(settingWeekStr.get(position));
                    }else{
                        settingWeekStr.get(position).isSelect=false;
                        settingSelectWeekStr.remove(settingWeekStr.get(position));
                    }
                }
                dialogConditionListAdapter.notifyDataSetChanged();
            }
        });
        dialogConditionListAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化周
     */
    private void initWeeks() {

        LayoutInflater.from(this).inflate( R.layout.foot_view, null);
        View footView = LayoutInflater.from(this).inflate( R.layout.foot_view, null);
        dialoglist.addFooterView(footView);
        cancel = (Button) footView.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        sure = (Button) footView.findViewById(R.id.sure);
        sure.setOnClickListener(this);

        type = 1;
        settingWeekStr.clear();
        settingSelectWeekStr.clear();
        settingWeekStr.add(new SelectWeek("星期一",false));
        settingWeekStr.add(new SelectWeek("星期二",false));
        settingWeekStr.add(new SelectWeek("星期三",false));
        settingWeekStr.add(new SelectWeek("星期四",false));
        settingWeekStr.add(new SelectWeek("星期五",false));
        settingWeekStr.add(new SelectWeek("星期六",false));
        settingWeekStr.add(new SelectWeek("星期日",false));

        dialogConditionListAdapter = new DialogWeeksListAdapter(DialogWeelActivity.this.getApplicationContext(), settingWeekStr);
        dialoglist.setAdapter(dialogConditionListAdapter);
        dialogConditionListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.sure:
                Intent in = new Intent();
                if(settingSelectWeekStr.size()>0){
                    ArrayList<String> str = new ArrayList<String>();
                    for(SelectWeek select:settingSelectWeekStr){
                        str.add(select.name);
                    }
                    in.putStringArrayListExtra("SELECTWEEK",str);
                    setResult(20, in );
                    finish();
                }else{
                    ToastUtil.showToast(this,"请选择");
                }

                break;
        }
    }
}
