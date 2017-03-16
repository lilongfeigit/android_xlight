package com.umarbhutta.xlightcompanion.control;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.main.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 * 弹出框Activity
 */

public class DialogActivity extends AppCompatActivity {

    private ArrayList<String> settingStr = new ArrayList<String>();

    DialogListAdapter dialogConditionListAdapter;
    RecyclerView settingRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        //hide nav bar
        getSupportActionBar().hide();

        initViews();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        settingRecyclerView = (RecyclerView) findViewById(R.id.settingRecyclerView);
        settingStr = EntryConditionActivity.listStr;
        dialogConditionListAdapter = new DialogListAdapter(this, settingStr);
        settingRecyclerView.setAdapter(dialogConditionListAdapter);

        //set LayoutManager for recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        //attach LayoutManager to recycler view
        settingRecyclerView.setLayoutManager(layoutManager);
        //divider lines
        settingRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));

        dialogConditionListAdapter.notifyDataSetChanged();

        dialogConditionListAdapter.notifyDataSetChanged();
        dialogConditionListAdapter.setmOnItemClickListener(new DialogListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
                switch (position) {
                }
            }
        });
    }
}
