package com.umarbhutta.xlightcompanion.control;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.main.SimpleDividerItemDecoration;
import com.umarbhutta.xlightcompanion.settings.FastBindingActivity;
import com.umarbhutta.xlightcompanion.settings.ModifyPasswordActivity;
import com.umarbhutta.xlightcompanion.settings.ShakeActivity;
import com.umarbhutta.xlightcompanion.settings.UserInvitationActivity;
import com.umarbhutta.xlightcompanion.settings.UserMsgModifyActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */

public class ControlRuseltActivity extends AppCompatActivity {



    private List<String> settingStr = new ArrayList<String>();
    private List<Integer> imgInter = new ArrayList<Integer>();

    //    public static String[] settingStr = {"个人信息","修改密码","摇一摇","用户邀请","快速绑定","退出登录"};
    ControlResultListAdapter controlResultListAdapter;
    RecyclerView settingRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        settingRecyclerView = (RecyclerView) findViewById(R.id.settingRecyclerView);
        controlResultListAdapter = new ControlResultListAdapter(this, settingStr,imgInter);
        settingRecyclerView.setAdapter(controlResultListAdapter);

        //set LayoutManager for recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        //attach LayoutManager to recycler view
        settingRecyclerView.setLayoutManager(layoutManager);
        //divider lines
        settingRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));

        settingStr.add("灯具控制");
        imgInter.add( R.drawable.add_scenario_bg);
        settingStr.add("场景切换（切换主设备场景）");
        imgInter.add( R.drawable.add_scenario_bg);
        settingStr.add("Email通知");
        imgInter.add( R.drawable.add_scenario_bg);
        settingStr.add("APP通知");
        imgInter.add( R.drawable.add_scenario_bg);

        controlResultListAdapter.notifyDataSetChanged();
        controlResultListAdapter.setmOnItemClickListener(new ControlResultListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0://个人信息
                        onFabPressed(UserMsgModifyActivity.class);
                        break;
                    case 1://修改密码
                        onFabPressed(ModifyPasswordActivity.class);
                        break;
                    case 2://摇一摇
                        onFabPressed(ShakeActivity.class);
                        break;
                    case 3://用户邀请
                        onFabPressed(UserInvitationActivity.class);
                        break;
                    case 4://快速绑定
                        onFabPressed(FastBindingActivity.class);
                        break;
                    case 5://退出登录
                        logout();
                        break;
                }
            }
        });
    }

    private void onFabPressed(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    /**
     * 退出登录
     */
    private void logout() {
        UserUtils.saveUserInfo(getApplicationContext(), null);
    }
}
