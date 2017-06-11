package com.umarbhutta.xlightcompanion.bindDevice;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.AndroidBug54971Workaround;
import com.umarbhutta.xlightcompanion.Tools.NetworkUtils;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.adapter.WifiListAdapter;
import com.umarbhutta.xlightcompanion.okHttp.model.AddDeviceResult;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestAddDevice;
import com.umarbhutta.xlightcompanion.settings.BaseActivity;
import com.umarbhutta.xlightcompanion.settings.utils.DisplayUtils;
import com.umarbhutta.xlightcompanion.views.DialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/4.
 * 绑定设备
 */

public class BindDeviceFirstActivity extends FragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private LinearLayout llBack;
    private TextView btnSure;
    private EditText ssidEdit;
    private EditText wifiPwdEdit;
    private ListView listView;
    private ImageView deleteIcon;
    private WifiListAdapter adapter;
    private List<ScanResult> listb = new ArrayList<ScanResult>();
    private ScanResult curWifi = null;
    private final int WIFI_PERMISSION_REQ_CODE = 100;
    private WifiReceiver mWifiReceiver;
    private ImageView clearBtn;
    private DialogUtils mDialogUtils;
    private AlertDialog mGpsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_bind_device_first);



        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        ViewGroup.LayoutParams params = rootLayout.getLayoutParams();
        params.height = DisplayUtils.getScreenHeight(this) - 100;
        rootLayout.setLayoutParams(params);




        AndroidBug54971Workaround.assistActivity(findViewById(android.R.id.content));
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        btnSure.setVisibility(View.INVISIBLE);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.link_wifi);

        ssidEdit = (EditText) findViewById(R.id.ssid);
        wifiPwdEdit = (EditText) findViewById(R.id.wifi_pwd);
        findViewById(R.id.rightIcon).setOnClickListener(this);
        deleteIcon = (ImageView) findViewById(R.id.pwdrightIcon);
        deleteIcon.setOnClickListener(this);
        findViewById(R.id.btn_connect_wifi).setOnClickListener(this);

        clearBtn = (ImageView) findViewById(R.id.pwdrightIcon);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiPwdEdit.setText("");
            }
        });
        clearBtn.setVisibility(View.INVISIBLE);

        wifiPwdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 0) {
                    clearBtn.setVisibility(View.INVISIBLE);
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                }
            }
        });


        listView = (ListView) findViewById(R.id.wifi_list);
        listView.setOnItemClickListener(this);
        adapter = new WifiListAdapter(this.getApplicationContext(), listb);
        listView.setAdapter(adapter);


        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        mWifiReceiver = new WifiReceiver();
        registerReceiver(mWifiReceiver, filter);
//        checkPublishPermission();
        getCurWifiInfo();
        handler.sendEmptyMessageDelayed(1, 5000);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.postDelayed(runnable, 100);
            handler.sendEmptyMessageDelayed(1, 5000);
        }
    };

    /**
     * 定时更新wifi列表
     */
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            getCurWifiInfo();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mWifiReceiver);
        handler.removeCallbacks(runnable);
    }

    public class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();


            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                //获取当前的wifi状态int类型数据
                int mWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (mWifiState) {
                    case WifiManager.WIFI_STATE_ENABLED:
                        //已打开
//                        checkPublishPermission();
                        getWifiList();
                        break;
                    case WifiManager.WIFI_STATE_ENABLING:
                        //打开中
                        break;
                    case WifiManager.WIFI_STATE_DISABLED:
                        //已关闭
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        //关闭中
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        //未知
                        break;
                }
            }
        }
    }

    private void checkPublishPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 获取wifi连接需要定位权限,没有获取权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
            }, WIFI_PERMISSION_REQ_CODE);
            return;
        } else {
            getCurWifiInfo();
            handler.sendEmptyMessageDelayed(1, 5000);
        }


//        if (Build.VERSION.SDK_INT >= 23) {
//            List<String> permissions = new ArrayList<>();
//            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)) {
//                permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
//            }
//
//            if (permissions.size() != 0) {
//                ActivityCompat.requestPermissions(this,
//                        (String[]) permissions.toArray(new String[0]),
//                        WIFI_PERMISSION_REQ_CODE);
//                return false;
//            }
//        }
//        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WIFI_PERMISSION_REQ_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {// 允许
                    getCurWifiInfo();
                    handler.sendEmptyMessageDelayed(1, 5000);
                } else { // 不允许
//                    ToastUtil.showToast(this, getString(R.string.you_refuse_wifi_list));
                }
                break;
        }
    }

    private void getCurWifiInfo() {
        if (!isWifiContect()) {
            ToastUtil.showToast(this, getString(R.string.please_open_wifi));
            return;
        }
        getWifiList();
    }

    /**
     * wifi是否打开
     *
     * @return
     */
    private boolean isWifiContect() {
        WifiManager wifimanager;
        wifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifimanager.isWifiEnabled()) {
            return true;
        }

        return false;
    }


    /**
     * wifi是否可用
     *
     * @return
     */
    private boolean isWifiAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * wifi列表
     */
    private void getWifiList() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        listb.clear();
        listb.addAll(wifiManager.getScanResults());
        adapter.notifyDataSetChanged();
        if (null != listb && listb.size() > 0) {
            if (null == curWifi) {
                curWifi = listb.get(0);
                ssidEdit.setText(curWifi.SSID);
            }
        } else {
            initGPS();
        }
    }

    boolean isshowOpenGps = false;

    /**
     * 提示用户开启gps
     */
    private void initGPS() {
        if(isshowOpenGps){
            return;
        }

        isshowOpenGps = true;

        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            // 转到手机设置界面，用户设置GPS
            // 设置完成后返回到原来的界面
            if (null == mGpsDialog) {
                mDialogUtils = new DialogUtils();
                mGpsDialog = mDialogUtils.getConfirmCancelDialog(BindDeviceFirstActivity.this, getString(R.string.open_gps), new DialogUtils.OnClickOkBtnListener() {
                    @Override
                    public void onClickOk(String editTextStr) {
                        // 转到手机设置界面，用户设置GPS
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                    }
                });
            } else {
                if (!mGpsDialog.isShowing())
                    mGpsDialog.show();
            }
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

        if (TextUtils.isEmpty(wifiPwdEdit.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.please_input_wifi_pwd));
            return;
        }

        RequestAddDevice.getInstance().addDevice(this, "deviceTest", 0, 1, 0, new RequestAddDevice.OnAddDeviceCallBack() {
            @Override
            public void mOnAddDeviceCallBackFail(int code, final String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(BindDeviceFirstActivity.this, getString(R.string.device_link_fail) + errMsg);
                    }
                });
            }

            @Override
            public void mOnAddDeviceCallBackSuccess(AddDeviceResult mAddDeviceResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setResult(100);
                        ToastUtil.showToast(BindDeviceFirstActivity.this, getString(R.string.device_link_success));
                        BindDeviceFirstActivity.this.finish();
                    }
                });

            }
        });
    }


}
