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
 * user信息修改页面
 */

public class UserMsgModifyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg_modify);
        initViews();
    }

    private UserMsgModifyAdapter mUserMsgModifyAdapter;
    private RecyclerView mRecyclerView;
    private List<String> userMsgs = new ArrayList<String>();
    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.settingModifyUserMsgView);
        mUserMsgModifyAdapter = new UserMsgModifyAdapter(UserMsgModifyActivity.this,userMsgs);
        mRecyclerView.setAdapter(mUserMsgModifyAdapter);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        userMsgs.add("peter");
        mUserMsgModifyAdapter.notifyDataSetChanged();
    }
}
