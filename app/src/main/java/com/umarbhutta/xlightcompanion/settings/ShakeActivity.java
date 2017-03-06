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

public class ShakeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        initViews();
    }

    private ShakeAdapter mShakeAdapter;
    private RecyclerView mRecyclerView;
    private List<String> shakes = new ArrayList<String>();

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.shakeRecyclerView);
        mShakeAdapter = new ShakeAdapter(ShakeActivity.this, shakes);
        mRecyclerView.setAdapter(mShakeAdapter);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        shakes.add("peter");
        mShakeAdapter.notifyDataSetChanged();
    }
}
