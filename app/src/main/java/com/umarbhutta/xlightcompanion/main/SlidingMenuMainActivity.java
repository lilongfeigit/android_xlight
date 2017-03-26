package com.umarbhutta.xlightcompanion.main;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.SDK.BLE.BLEAdapter;
import com.umarbhutta.xlightcompanion.SDK.xltDevice;
import com.umarbhutta.xlightcompanion.glance.GlanceMainFragment;
import com.umarbhutta.xlightcompanion.settings.BaseFragmentActivity;

/**
 * Created by Administrator
 */

public class SlidingMenuMainActivity extends BaseFragmentActivity {
    private Fragment mContent;

    public static xltDevice m_mainDevice;

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
        BLEAdapter.init(this);

        if (BLEAdapter.IsSupported() && !BLEAdapter.IsEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, BLEAdapter.REQUEST_ENABLE_BT);
        }

        // Initialize SmartDevice SDK
        m_mainDevice = new xltDevice();
        m_mainDevice.Init(this);
//        m_mainDevice.Connect(CloudAccount.DEVICE_ID);

        // Set SmartDevice Event Notification Flag
        //m_mainDevice.setEnableEventSendMessage(false);
        //m_mainDevice.setEnableEventBroadcast(true);

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
        Intent intent = new Intent(this,activity);
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
        if (requestCode == BLEAdapter.REQUEST_ENABLE_BT) {
            BLEAdapter.init(this);
        }
    }
}
