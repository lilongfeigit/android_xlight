package com.umarbhutta.xlightcompanion.deviceList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.AndroidBug54971Workaround;
import com.umarbhutta.xlightcompanion.Tools.NetworkUtils;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.adapter.DeviceListAdapter;
import com.umarbhutta.xlightcompanion.glance.GlanceMainFragment;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestSettingMainDevice;
import com.umarbhutta.xlightcompanion.okHttp.requests.imp.CommentRequstCallback;
import com.umarbhutta.xlightcompanion.settings.BaseActivity;

/**
 * Created by Administrator on 2017/3/4.
 * 设备列表
 */

public class DeviceListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private ListView listView;
    public static int selectPosition = 0;
    private DeviceListAdapter adapter;

    private TextView tv_select_main_device;
    private TextView tv_no_device;
    private View view_top_line, view_bottom_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        AndroidBug54971Workaround.assistActivity(findViewById(android.R.id.content));
//        getSupportActionBar().hide();
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        btnSure.setVisibility(View.INVISIBLE);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.select_main_device);
        listView = (ListView) findViewById(R.id.lv_devices);
        tv_select_main_device = (TextView) findViewById(R.id.tv_select_main_device);
        tv_no_device = (TextView) findViewById(R.id.tv_no_device);
        view_top_line = findViewById(R.id.view_top_line);
        view_bottom_line = findViewById(R.id.view_bottom_line);
        if (UserUtils.isLogin(getApplicationContext()) && GlanceMainFragment.devicenodes != null && GlanceMainFragment.devicenodes.size() > 0) {
            tv_no_device.setVisibility(View.GONE);
            tv_select_main_device.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            adapter = new DeviceListAdapter(this, GlanceMainFragment.deviceList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            view_top_line.setVisibility(View.VISIBLE);
            view_bottom_line.setVisibility(View.VISIBLE);
        } else {
            tv_no_device.setVisibility(View.VISIBLE);
            tv_select_main_device.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            view_top_line.setVisibility(View.GONE);
            view_bottom_line.setVisibility(View.GONE);
        }


        if (null == GlanceMainFragment.deviceList || GlanceMainFragment.deviceList.size() <= 0) {
//            ToastUtil.showToast(this, R.string.you_have_no_device_and_add);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectPosition = position;
        setMainDevice(position);

    }

    //设置主设备
    private void setMainDevice(int position) {
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            ToastUtil.showToast(this, R.string.net_error);
            return;
        }
        showProgressDialog(getString(R.string.setting));

        int deviceId = GlanceMainFragment.deviceList.get(position).id;
        RequestSettingMainDevice.getInstance().settingDevice(this, 1, deviceId, UserUtils.getUserInfo(this).getId(), new CommentRequstCallback() {
            @Override
            public void onCommentRequstCallbackSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateDeviceListInfo();
                        cancelProgressDialog();
                        ToastUtil.showToast(DeviceListActivity.this, getString(R.string.setting_success));
                    }
                });
            }

            @Override
            public void onCommentRequstCallbackFail(int code, final String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cancelProgressDialog();
                        ToastUtil.showToast(DeviceListActivity.this, errMsg);
                    }
                });
            }
        });
    }

    private void updateDeviceListInfo() {
        for (int i = 0; i < GlanceMainFragment.deviceList.size(); i++) {
            GlanceMainFragment.deviceList.get(i).maindevice = 0;
        }
        GlanceMainFragment.deviceList.get(selectPosition).maindevice = 1;
        adapter.notifyDataSetChanged();
    }
}
