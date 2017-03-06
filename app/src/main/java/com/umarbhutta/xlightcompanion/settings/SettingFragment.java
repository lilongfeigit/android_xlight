package com.umarbhutta.xlightcompanion.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.main.MainActivity;
import com.umarbhutta.xlightcompanion.main.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/5.
 */

public class SettingFragment extends Fragment {


    private List<String> settingStr = new ArrayList<String>();

//    public static String[] settingStr = {"个人信息","修改密码","摇一摇","用户邀请","快速绑定","退出登录"};
    SettingListAdapter settingListAdapter;
    RecyclerView settingRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        settingRecyclerView = (RecyclerView) view.findViewById(R.id.settingRecyclerView);
        settingListAdapter = new SettingListAdapter(getActivity(),settingStr);
        settingRecyclerView.setAdapter(settingListAdapter);

        //set LayoutManager for recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //attach LayoutManager to recycler view
        settingRecyclerView.setLayoutManager(layoutManager);
        //divider lines
        settingRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        settingStr.add("个人信息");
        settingStr.add("修改密码");
        settingStr.add("摇一摇");
        settingStr.add("用户邀请");
        settingStr.add("快速绑定");
        settingStr.add("退出登录");
        settingListAdapter.notifyDataSetChanged();
        settingListAdapter.setmOnItemClickListener(new SettingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(),position+"",Toast.LENGTH_SHORT).show();
                switch (position){
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
                        break;
                }
            }
        });
        return view;
    }
    private void onFabPressed(Class activity) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
    }
}
