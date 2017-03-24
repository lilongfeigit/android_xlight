package com.umarbhutta.xlightcompanion.bindDevice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;

/**
 * Created by Administrator on 2017/3/4.
 * 绑定设备
 */

public class BindDeviceFirstActivity extends AppCompatActivity {
    private LinearLayout llBack;
    private TextView btnSure;

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
        getCurWifiInfo();
    }

    private void getCurWifiInfo() {
        if (!isWifiAvailable()) {
            ToastUtil.showToast(this, "请打开WiFi");
            return;
        }


        //获取当前连接的wifi信息
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (null == wifiInfo || TextUtils.isEmpty(wifiInfo.getSSID())) {
            ToastUtil.showToast(this, "请连接wifi");
        }

        Logger.i("wifi信息：" + wifiInfo.toString());

        Logger.i("wifi名称：" + wifiInfo.getSSID());
    }


    /**
     * wifi是否可用
     *
     * @return
     */
    public boolean isWifiAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo
                .getType() == ConnectivityManager.TYPE_WIFI);
    }


}
