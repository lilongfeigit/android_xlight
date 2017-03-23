package com.umarbhutta.xlightcompanion.deviceList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.NetworkUtils;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.adapter.DeviceListAdapter;
import com.umarbhutta.xlightcompanion.glance.GlanceFragment;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestSettingMainDevice;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestUnBindDevice;
import com.umarbhutta.xlightcompanion.okHttp.requests.imp.CommentRequstCallback;
import com.umarbhutta.xlightcompanion.settings.BaseActivity;

/**
 * Created by Administrator on 2017/3/4.
 * 设备列表
 */

public class DeviceListActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private ListView listView;
    public static int selectPosition = 0;
    private DeviceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        getSupportActionBar().hide();
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        btnSure.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("选择主设备");
        listView = (ListView) findViewById(R.id.lv_devices);


        for (int i = 0; i < GlanceFragment.deviceList.size(); i++) {
            if (1 == GlanceFragment.deviceList.get(i).maindevice) {
                selectPosition = i;
                break;
            }
        }


        adapter = new DeviceListAdapter(this, GlanceFragment.deviceList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectPosition = position;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        //设置主设备
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            ToastUtil.showToast(this, R.string.net_error);
            return;
        }

        showProgressDialog(getString(R.string.setting));

        int deviceId = GlanceFragment.deviceList.get(selectPosition).id;
        RequestSettingMainDevice.getInstance().settingDevice(this, 1, deviceId, UserUtils.getUserInfo(this).getId(), new CommentRequstCallback() {
            @Override
            public void onCommentRequstCallbackSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateDeviceListInfo();
                        cancelProgressDialog();
                        ToastUtil.showToast(DeviceListActivity.this, "设置成功");
                    }
                });
            }

            @Override
            public void onCommentRequstCallbackFail(int code, final String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cancelProgressDialog();
                        ToastUtil.showToast(DeviceListActivity.this, "设置失败" + errMsg);
                    }
                });
            }
        });
    }

    private void updateDeviceListInfo() {
        for (int i = 0; i < GlanceFragment.deviceList.size(); i++) {
            GlanceFragment.deviceList.get(i).maindevice = 0;
        }
        GlanceFragment.deviceList.get(selectPosition).maindevice = 1;
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        showDeleteSceneDialog(position);

        return true;
    }

    /**
     * 解绑设备
     */
    private void showDeleteSceneDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("解绑设备提示");
        builder.setMessage("确定解绑此设备吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unBindDevice(position);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    /**
     * 解绑设备
     *
     * @param position
     */
    private void unBindDevice(final int position) {
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            ToastUtil.showToast(this, R.string.net_error);
            return;
        }

        showProgressDialog(getString(R.string.setting));

        RequestUnBindDevice.getInstance().unBindDevice(this, "" + GlanceFragment.deviceList.get(position).id, new CommentRequstCallback() {
            @Override
            public void onCommentRequstCallbackSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cancelProgressDialog();
                        ToastUtil.showToast(DeviceListActivity.this, "解绑成功");
                        updateUnbindList(position);
                    }
                });
            }

            @Override
            public void onCommentRequstCallbackFail(int code, final String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cancelProgressDialog();
                        ToastUtil.showToast(DeviceListActivity.this, "解绑失败" + errMsg);
                    }
                });
            }
        });
    }

    /**
     * 解绑后更新页面
     *
     * @param position
     */
    private void updateUnbindList(int position) {

        if (position == selectPosition) {
            selectPosition = 0;
        } else if (selectPosition > position) {
            selectPosition -= 1;
        }

        GlanceFragment.deviceList.remove(position);
        adapter.notifyDataSetChanged();
    }


}
