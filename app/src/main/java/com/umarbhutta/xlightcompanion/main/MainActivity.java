package com.umarbhutta.xlightcompanion.main;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.SDK.BLE.BLEAdapter;
import com.umarbhutta.xlightcompanion.SDK.xltDevice;
import com.umarbhutta.xlightcompanion.Tools.SharedPreferencesUtils;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.control.activity.AddControlRuleActivity;
import com.umarbhutta.xlightcompanion.control.ControlRuleFragment;
import com.umarbhutta.xlightcompanion.deviceList.DeviceListActivity;
import com.umarbhutta.xlightcompanion.glance.GlanceFragment;
import com.umarbhutta.xlightcompanion.help.HelpFragment;
import com.umarbhutta.xlightcompanion.imgloader.ImageLoaderOptions;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.model.LoginResult;
import com.umarbhutta.xlightcompanion.okHttp.model.Rows;
import com.umarbhutta.xlightcompanion.okHttp.model.SceneListResult;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestSceneListInfo;
import com.umarbhutta.xlightcompanion.report.ReportFragment;
import com.umarbhutta.xlightcompanion.scenario.AddScenarioNewActivity;
import com.umarbhutta.xlightcompanion.scenario.ScenarioFragment;
import com.umarbhutta.xlightcompanion.settings.BaseActivity;
import com.umarbhutta.xlightcompanion.settings.SettingFragment;
import com.umarbhutta.xlightcompanion.settings.UserMsgModifyActivity;
import com.umarbhutta.xlightcompanion.userManager.LoginActivity;
import com.umarbhutta.xlightcompanion.views.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

//import com.umarbhutta.xlightcompanion.SDK.CloudAccount;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //constants for testing lists
    public static final String[] deviceNames = {"Living Room", "Bedroom", "Basement Kitchen"};
    public static final String[] scheduleTimes = {"10:30 AM", "12:45 PM", "02:00 PM", "06:45 PM", "08:00 PM", "11:30 PM"};
    public static final String[] scheduleDays = {"Mo Tu We Th Fr", "Every day", "Mo We Th Sa Su", "Tomorrow", "We", "Mo Tu Fr Sa Su"};
    public static final String[] scenarioNames = {"Brunching", "Guests", "Naptime", "Dinner", "Sunset", "Bedtime"};
    public static final String[] scenarioDescriptions = {"A red color at 52% brightness", "A blue-green color at 100% brightness", "An amber color at 50% brightness", "Turn off", "A warm-white color at 100% brightness", "A green color at 52% brightness"};
    public static final String[] filterNames = {"Breathe", "Music Match", "Flash"};

    public static xltDevice m_mainDevice;
    private TextView tv_center_title;
    private Button btnRight;
    private CircleImageView userIcon;

    private TextView tv_userName, textView;
    private Button btnLogin;
    private LinearLayout llPerName;

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(helpUrl)) {
            getHelpUrl();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_center_title = (TextView) findViewById(R.id.tv_center_title);
        btnRight = (Button) findViewById(R.id.btnRight);
        setSupportActionBar(toolbar);
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

        //setup drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //NavigationView 的获取头部控件
        View headerView = navigationView.getHeaderView(0);

//        user_nameTv = (TextView) headerView.findViewById(R.id.user_name);
        llPerName = (LinearLayout) headerView.findViewById(R.id.llPerName);
        tv_userName = (TextView) headerView.findViewById(R.id.tv_userName);
        textView = (TextView) headerView.findViewById(R.id.textView);

        btnLogin = (Button) headerView.findViewById(R.id.btn_login);
        CircleImageView userIcon = (CircleImageView) headerView.findViewById(R.id.userIcon);
        userIcon.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if (UserUtils.isLogin(MainActivity.this)) {
            LoginResult userInfo = UserUtils.getUserInfo(this);
            btnLogin.setVisibility(View.GONE);
            llPerName.setVisibility(View.VISIBLE);
            tv_userName.setText(UserUtils.getUserInfo(MainActivity.this).getUsername());
            textView.setText(UserUtils.getUserInfo(MainActivity.this).getEmail());
            ImageLoader.getInstance().displayImage(userInfo.getImage(), userIcon, ImageLoaderOptions.getImageLoaderOptions());
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            llPerName.setVisibility(View.GONE);
        }

        //右边菜单按钮
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!UserUtils.isLogin(MainActivity.this)) {
                    gotoLoginActivity();
                    return;
                }

                // 跳转到选择的主设备列表页面
                if (type == 0) {
                    onFabPressed(DeviceListActivity.class);
                } else if (type == 1) {
                    // 添加规则页面
                    onFabPressed(AddControlRuleActivity.class);
                } else if (type == 2) {
                    onFabPressed(AddScenarioNewActivity.class);
                } else {
                    onFabPressed(DeviceListActivity.class);
                }
            }
        });

        displayView(R.id.nav_glance);
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    /**
     * 获取主设备信息
     *
     * @return
     */
    public static Rows getMainDeviceInfo() {
        if (null != GlanceFragment.deviceList && GlanceFragment.deviceList.size() > 0) {
            for (Rows info : GlanceFragment.deviceList) {
                if (info.maindevice == 1) {
                    return info;
                }
            }
        }

        return null;

    }

    private void gotoLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void onFabPressed(Class activity) {
        Intent intent = new Intent(MainActivity.this, activity);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLEAdapter.REQUEST_ENABLE_BT) {
            BLEAdapter.init(this);
        }
    }

    private int type = 0;

    public void displayView(int viewId) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (viewId) {
            case R.id.nav_glance:
                type = 0;
                btnRight.setVisibility(View.VISIBLE);
                btnRight.setBackground(getDrawable(R.drawable.home_setting));
                fragment = new GlanceFragment();//首页
                title = "首页";
                break;
            case R.id.nav_control:
                if (!UserUtils.isLogin(MainActivity.this)) {
                    gotoLoginActivity();
                    break;
                }
                type = 1;
                btnRight.setVisibility(View.VISIBLE);
                btnRight.setBackground(getDrawable(R.drawable.control_add));
                fragment = new ControlRuleFragment();//规则
                title = "规则";
                break;
            case R.id.nav_schedule:
                if (!UserUtils.isLogin(MainActivity.this)) {
                    gotoLoginActivity();
                    break;
                }
                type = 0;
                btnRight.setVisibility(View.GONE);
//                fragment = new ScheduleFragment();//时间表
                fragment = new ReportFragment();//报表
                title = "报表";
                break;
            case R.id.nav_scenario:
                if (!UserUtils.isLogin(MainActivity.this)) {
                    gotoLoginActivity();
                    break;
                }
                type = 2;
                btnRight.setVisibility(View.VISIBLE);
                btnRight.setBackground(getDrawable(R.drawable.control_add));
                fragment = new ScenarioFragment();//场景
                title = "场景";
                break;
            case R.id.nav_settings:
                if (!UserUtils.isLogin(MainActivity.this)) {
                    gotoLoginActivity();
                    break;
                }
                btnRight.setVisibility(View.GONE);
                fragment = new SettingFragment();//设置
                title = "设置";
                break;
            case R.id.nav_help:
                type = 0;
                btnRight.setVisibility(View.GONE);
                fragment = new HelpFragment();//帮助
                title = "帮助";
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.placeholder, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        }
        tv_center_title.setText(title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // 跳转到选择的主设备列表页面
            onFabPressed(DeviceListActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displayView(item.getItemId());
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
//                LinearLayout llPerName = (LinearLayout)findViewById(R.id.llPerName);
//                btn_login.setVisibility(View.GONE);
////                llPerName.setVisibility(View.VISIBLE);
                onFabPressed(LoginActivity.class);
                break;
            case R.id.userIcon:
                onFabPressed(UserMsgModifyActivity.class);
                break;
        }
    }

    public static String helpUrl = null;

    /**
     * 获取帮助的url
     */
    public void getHelpUrl() {
        HttpUtils.getInstance().getRequestInfo(NetConfig.URL_GET_HELP_URL, null, new HttpUtils.OnHttpRequestCallBack() {
            @Override
            public void onHttpRequestSuccess(final Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject((String) result);
                            JSONObject dataObj = jsonObject.getJSONObject("data");
                            helpUrl = dataObj.getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onHttpRequestFail(int code, String errMsg) {

            }
        });
    }

    /**
     * 获取场景进行本地化
     */
    private void initSceneListInfo() {
        if (!UserUtils.isLogin(this)) {
            return;
        }
        RequestSceneListInfo.getInstance().getSceneListInfo(this, new RequestSceneListInfo.OnRequestFirstPageInfoCallback() {
            @Override
            public void onRequestFirstPageInfoSuccess(final SceneListResult deviceInfoResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null != deviceInfoResult && null != deviceInfoResult.rows && deviceInfoResult.rows.size() > 0) {
                            SharedPreferencesUtils.putObject(MainActivity.this, SharedPreferencesUtils.KEY_SCENE_LIST, deviceInfoResult.rows);
                        }
                    }
                });

            }

            @Override
            public void onRequestFirstPageInfoFail(int code, String errMsg) {
            }
        });
    }


}
