package com.umarbhutta.xlightcompanion.bindDevice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.NetworkUtils;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.adapter.WifiListAdapter;
import com.umarbhutta.xlightcompanion.okHttp.model.AddDeviceResult;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestAddDevice;
import com.umarbhutta.xlightcompanion.settings.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/4.
 * 绑定设备
 */

public class BindDeviceFirstActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private LinearLayout llBack;
    private TextView btnSure;
    private EditText ssidEdit;
    private EditText wifiPwdEdit;
    private ListView listView;
    private ImageView deleteIcon;
    private WifiListAdapter adapter;
    private List<ScanResult> listb = new ArrayList<ScanResult>();
    private ScanResult curWifi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_device_first);
        getSupportActionBar().hide();
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        btnSure.setVisibility(View.GONE);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("连接Wifi");

        ssidEdit = (EditText) findViewById(R.id.ssid);
        wifiPwdEdit = (EditText) findViewById(R.id.wifi_pwd);
        findViewById(R.id.rightIcon).setOnClickListener(this);
        deleteIcon = (ImageView) findViewById(R.id.pwdrightIcon);
        deleteIcon.setOnClickListener(this);
        findViewById(R.id.btn_connect_wifi).setOnClickListener(this);

        listView = (ListView) findViewById(R.id.wifi_list);
        listView.setOnItemClickListener(this);
        adapter = new WifiListAdapter(this, listb);
        listView.setAdapter(adapter);

        getCurWifiInfo();
    }

    private void getCurWifiInfo() {
        if (!isWifiAvailable()) {
            ToastUtil.showToast(this, "请打开WiFi");
            return;
        }

        getWifiList();
    }


    /**
     * wifi是否可用
     *
     * @return
     */
    private boolean isWifiAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo
                .getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * wifi列表
     */
    private void getWifiList() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        listb.addAll(wifiManager.getScanResults());
        adapter.notifyDataSetChanged();
        if (null != listb && listb.size() > 0) {
            curWifi = listb.get(0);
            ssidEdit.setText(curWifi.SSID);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightIcon: //下箭头
                listView.setVisibility((View.VISIBLE == listView.getVisibility()) ? View.GONE : View.VISIBLE);
                break;
            case R.id.pwdrightIcon: //清除密码按钮
                wifiPwdEdit.setText("");
                break;
            case R.id.btn_connect_wifi: //连接wifi
                bindDevice();
                break;
            default:
                listView.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listView.setVisibility(View.GONE);
        curWifi = listb.get(position);
        ssidEdit.setText(curWifi.SSID);
    }


    /**
     * 绑定设备
     * TODO 需要先把调用SDK的接口，再调用服务器的接口，SDK接口目前先跳过
     */
    private void bindDevice() {
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            ToastUtil.showToast(this, R.string.net_error);
            return;
        }


        RequestAddDevice.getInstance().addDevice(this, "测试设备", 0, 1, 0, new RequestAddDevice.OnAddDeviceCallBack() {
            @Override
            public void mOnAddDeviceCallBackFail(int code, final String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(BindDeviceFirstActivity.this, "设备连接失败" + errMsg);
                    }
                });
            }

            @Override
            public void mOnAddDeviceCallBackSuccess(AddDeviceResult mAddDeviceResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setResult(100);
                        ToastUtil.showToast(BindDeviceFirstActivity.this, "设备连接成功");
                        BindDeviceFirstActivity.this.finish();
                    }
                });

            }
        });
    }


}
