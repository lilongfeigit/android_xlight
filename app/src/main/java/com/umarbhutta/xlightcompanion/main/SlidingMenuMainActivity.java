package com.umarbhutta.xlightcompanion.main;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.SDK.BLE.BLEPairedDeviceList;
import com.umarbhutta.xlightcompanion.SDK.xltDevice;
import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.glance.GlanceMainFragment;
import com.umarbhutta.xlightcompanion.location.BaiduMapUtils;
import com.umarbhutta.xlightcompanion.settings.BaseFragmentActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator
 */

public class SlidingMenuMainActivity extends BaseFragmentActivity {
    private Fragment mContent;

//    public static xltDevice m_mainDevice;

    public static Map<String, xltDevice> xltDeviceMaps;

    public static xltDevice m_mainDevice;

    //TODO 测试数据
    public static final String[] deviceNames = {"Living Room", "Bedroom", "Bar"};
    //    public static final int[] deviceNodeIDs = {1, 8, 11};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
//        setTitle("ResponsiveUI");
        // 设置主视图界面
        setContentView(R.layout.responsive_content_frame);
        getWindow().setStatusBarColor(getResources().getColor(R.color.bar_color));
        initSlidingMenu(savedInstanceState);

        // Check Bluetooth
        BLEPairedDeviceList.init(this);
        if (BLEPairedDeviceList.IsSupported() && !BLEPairedDeviceList.IsEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, BLEPairedDeviceList.REQUEST_ENABLE_BT);
        }

        xltDeviceMaps = new HashMap<String, xltDevice>();
        // Initialize SmartDevice SDK
//        m_mainDevice = new xltDevice();
//        m_mainDevice.Init(this);

        //TODO 测试数据
        // Setup Device/Node List
//        for( int lv_idx = 0; lv_idx < 3; lv_idx++ ) {
//            m_mainDevice.addNodeToDeviceList(deviceNodeIDs[lv_idx], xltDevice.DEFAULT_DEVICE_TYPE, deviceNames[lv_idx]);
//        }
//        m_mainDevice.setDeviceID(deviceNodeIDs[0]);
//
//        // Connect to Controller
//        m_mainDevice.Connect(CloudAccount.DEVICE_ID);

    }

    private void initSlidingMenu(Bundle savedInstanceState) {
        // check if the content frame contains the menu frame
        if (findViewById(R.id.menu_frame) == null) {
            setBehindContentView(R.layout.menu_frame);
            getSlidingMenu().setSlidingEnabled(true);
            getSlidingMenu()
                    .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            // add a dummy view
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

        // 设置主界面Fragment视图内容
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        if (mContent == null)
            mContent = new GlanceMainFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mContent).commit();

        // set the Behind View Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new MainMenuFragment()).commit();

        // 设置滑动菜单的属性值
        SlidingMenu sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);
    }

    public void switchContent(final Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).commit();

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                getSlidingMenu().showContent();
            }
        }, 50);
    }

    public void onActivityPressed(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLEPairedDeviceList.REQUEST_ENABLE_BT) {
            BLEPairedDeviceList.init(this);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//点击的是返回键
            showContent();
        }
        return super.dispatchKeyEvent(event);
    }


}
