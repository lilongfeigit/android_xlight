package com.umarbhutta.xlightcompanion.settings;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.Tools.StringUtil;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.control.activity.dialog.DialogRowNameActivity;
import com.umarbhutta.xlightcompanion.glance.GlanceMainFragment;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.model.Devicenodes;
import com.umarbhutta.xlightcompanion.okHttp.model.LoginResult;
import com.umarbhutta.xlightcompanion.okHttp.model.Rows;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/5.
 */

public class ShakeActivity extends BaseActivity {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;

    private SensorManager sensorManager;
    private Vibrator vibrator;
    private static final int SENSOR_SHAKE = 10;
    private Devicenodes curMainNodes;
    private TextView deviceName;
    private CheckBox powerSwitch;
    private CheckBox scene_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        initViews();
    }


    private void initViews() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 确定提交按钮
            }
        });
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.shake);
        deviceName = (TextView) findViewById(R.id.scenarioName);
        powerSwitch = (CheckBox) findViewById(R.id.powerSwitch);
        scene_switch = (CheckBox) findViewById(R.id.scene_switch);

        powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (null == curMainNodes) {
                    powerSwitch.setChecked(!isChecked);
                    ToastUtil.showToast(ShakeActivity.this, R.string.select_device);
                    return;
                }

                if (isChecked) {
                    if (scene_switch.isChecked()) {
                        scene_switch.setChecked(false);
                    }
                }

                configDeviceInfo();

            }
        });

        scene_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (null == curMainNodes) {
                    scene_switch.setChecked(!isChecked);
                    ToastUtil.showToast(ShakeActivity.this, R.string.select_device);
                    return;
                }
                if (isChecked) {
                    if (powerSwitch.isChecked()) {
                        powerSwitch.setChecked(false);
                    }
                }

                configDeviceInfo();
            }
        });

        findViewById(R.id.scenarioNameLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == GlanceMainFragment.devicenodes || GlanceMainFragment.devicenodes.size() <= 0) {
                    ToastUtil.showToast(ShakeActivity.this, getString(R.string.no_device));
                    return;
                }

                Intent intent = new Intent(ShakeActivity.this, DialogRowNameActivity.class);
                startActivityForResult(intent, 29);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 35:

                Devicenodes mDevicenodes = (Devicenodes) data.getSerializableExtra("deviceInfo");

                String coreId = getCoreId(mDevicenodes);
                if (TextUtils.isEmpty(coreId)) {
                    ToastUtil.showToast(ShakeActivity.this, getString(R.string.do_not_supoort_shake));
                    return;
                }

                curMainNodes = mDevicenodes;
                deviceName.setText("" + curMainNodes.devicenodename);
                configDeviceInfo();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
    }


    /**
     * * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
//            Log.i("xlight", "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y + "；z轴方向的重力加速度" + z);
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 25;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
//            Logger.i("shake", "x = " + Math.abs(x) + ",y = " + y + ",z = " + z);
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                    //ToastUtil.showToast(ShakeActivity.this, "检测到摇晃，执行操作！");
                    shakeAction();
                    break;
            }
        }

    };


    /**
     * 配置设备信息
     */
    private void configDeviceInfo() {
        LoginResult userInfo = UserUtils.getUserInfo(this);

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfo.id);
            object.put("deviceId", curMainNodes.deviceId);
            object.put("devicenodeId", curMainNodes.id);
            object.put("devicenodename", curMainNodes.devicenodename);
            object.put("coreid", getCoreId(curMainNodes));
            object.put("shakeaction", powerSwitch.isChecked() ? 1 : 2);  //摇一摇要触发的动作。1：切换开关；2：切换场景

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.getInstance().postRequestInfo(NetConfig.URL_CONFIG_SHAKE_INFO + userInfo.access_token, object.toString(), null, new HttpUtils.OnHttpRequestCallBack() {
            @Override
            public void onHttpRequestSuccess(Object result) {

            }

            @Override
            public void onHttpRequestFail(int code, String errMsg) {

            }
        });
    }

    /**
     * 获取灯所在设备的coreid
     *
     * @return
     */
    private String getCoreId(Devicenodes curMainNodes) {
        if (null != GlanceMainFragment.deviceList && GlanceMainFragment.deviceList.size() > 0) {
            for (Rows rows : GlanceMainFragment.deviceList) {
                if (curMainNodes.deviceId == rows.id) {
                    return rows.coreid;
                }
            }
        }
        return null;
    }

    /**
     * 触发摇一摇动作
     */
    private void shakeAction() {
        if (null == curMainNodes) {
            ToastUtil.showToast(ShakeActivity.this, R.string.select_device);
            return;
        }

        showProgressDialog(getString(R.string.commit_img));

        JSONObject object = new JSONObject();
        try {
            object.put("userId", UserUtils.getUserInfo(ShakeActivity.this).getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * 此接口服务器返回异常，应该是code=1为成功，此接口为code=0成功
         */
        HttpUtils.getInstance().postRequestInfo(NetConfig.URL_ACTION_SHAKE + UserUtils.getUserInfo(this).access_token, object.toString(), null, new HttpUtils.OnHttpRequestCallBack() {
            @Override
            public void onHttpRequestSuccess(Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShakeActivity.this.cancelProgressDialog();
                        Logger.i("shake", "配置成功了");
                    }
                });

            }

            @Override
            public void onHttpRequestFail(int code, String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShakeActivity.this.cancelProgressDialog();
                        Logger.i("shake", "配置失败了");
                    }
                });
            }
        });


    }

}
