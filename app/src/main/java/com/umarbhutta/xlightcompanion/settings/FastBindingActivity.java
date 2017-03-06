package com.umarbhutta.xlightcompanion.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.main.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/5.
 */

public class FastBindingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_bingding);
        initViews();
    }

    private FastBindingAdapter mFastBindingAdapter;
    private RecyclerView mRecyclerView;
    private List<String> faseBings = new ArrayList<String>();

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.settingFastBindingView);
        mFastBindingAdapter = new FastBindingAdapter(FastBindingActivity.this, faseBings);
        mRecyclerView.setAdapter(mFastBindingAdapter);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        faseBings.add("扫描二维码");
        faseBings.add("输入口令码");
        mFastBindingAdapter.notifyDataSetChanged();
    }
}
