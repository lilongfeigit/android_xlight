package com.umarbhutta.xlightcompanion.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.SDK.xltDevice;
import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.Tools.NetworkUtils;
import com.umarbhutta.xlightcompanion.Tools.StatusReceiver;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.glance.GlanceMainFragment;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.model.Devicenodes;
import com.umarbhutta.xlightcompanion.okHttp.model.Rows;
import com.umarbhutta.xlightcompanion.okHttp.model.SceneListResult;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestSceneListInfo;
import com.umarbhutta.xlightcompanion.scenario.ColorSelectActivity;
import com.umarbhutta.xlightcompanion.scenario.ScenarioFragment;
import com.umarbhutta.xlightcompanion.views.CircleDotView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/5.
 * 设置灯
 */

public class EditDeviceActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTitle;
    public SceneListResult mDeviceInfoResult;
    private Devicenodes deviceInfo;
    private int mPositon;
    private int red = 130;
    private int green = 255;
    private int blue = 0;
    private TextView mscenarioName;

    private static final String TAG = EditDeviceActivity.class.getSimpleName();

    private static final String DEFAULT_LAMP_TEXT = "LIVING ROOM";
    private static final String RINGALL_TEXT = "ALL RINGS";
    private static final String RING1_TEXT = "RING 1";
    private static final String RING2_TEXT = "RING 2";
    private static final String RING3_TEXT = "RING 3";

    private Switch powerSwitch;
    private SeekBar brightnessSeekBar;
    private SeekBar cctSeekBar;
    private TextView colorTextView;
    private Spinner scenarioSpinner;
    private LinearLayout scenarioNoneLL;
    private ToggleButton ring1Button, ring2Button, ring3Button;
    private TextView deviceRingLabel, powerLabel, brightnessLabel, cctLabel, colorLabel;
    private ImageView lightImageView;

    private LinearLayout llBack;
    private TextView btnSure;
    private LinearLayout linear;

    private LayoutInflater mInflater;

    private ArrayList<String> scenarioDropdown;

    private String colorHex;
    private boolean state = false;
    boolean ring1 = false, ring2 = false, ring3 = false;

    private Handler m_handlerControl;
    private CircleDotView circleIcon;
    private TextView cctLabelColor;
    private RelativeLayout rl_scenario;
    private LinearLayout colorLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_control);
        mInflater = LayoutInflater.from(this);
        //hide nav bar
        getSupportActionBar().hide();

        deviceInfo = (Devicenodes) getIntent().getSerializableExtra("info");
        mPositon = getIntent().getIntExtra("position", 0);

        scenarioDropdown = new ArrayList<>(ScenarioFragment.name);
        scenarioDropdown.add(0, "None");

        powerSwitch = (Switch) findViewById(R.id.powerSwitch);
        brightnessSeekBar = (SeekBar) findViewById(R.id.brightnessSeekBar);
        cctSeekBar = (SeekBar) findViewById(R.id.cctSeekBar);
        cctSeekBar.setMax(6500 - 2700);
        colorTextView = (TextView) findViewById(R.id.colorTextView);
        scenarioNoneLL = (LinearLayout) findViewById(R.id.scenarioNoneLL);
        scenarioNoneLL.setAlpha(1);
        ring1Button = (ToggleButton) findViewById(R.id.ring1Button);
        ring2Button = (ToggleButton) findViewById(R.id.ring2Button);
        ring3Button = (ToggleButton) findViewById(R.id.ring3Button);
        deviceRingLabel = (TextView) findViewById(R.id.deviceRingLabel);
        brightnessLabel = (TextView) findViewById(R.id.brightnessLabel);
        cctLabel = (TextView) findViewById(R.id.cctLabel);
        powerLabel = (TextView) findViewById(R.id.powerLabel);
        colorLabel = (TextView) findViewById(R.id.colorLabel);
        lightImageView = (ImageView) findViewById(R.id.lightImageView);
        linear = (LinearLayout) findViewById(R.id.ll_horizontal_scrollview);
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
        tvTitle.setText(R.string.edit_device);
        mscenarioName = (TextView) findViewById(R.id.scenarioName);
        mscenarioName.setOnClickListener(this);
        cctLabelColor = (TextView) findViewById(R.id.cctLabelColor);

        scenarioSpinner = (Spinner) findViewById(R.id.scenarioSpinner);
        ArrayAdapter<String> scenarioAdapter = new ArrayAdapter<>(this, R.layout.control_scenario_spinner_item, scenarioDropdown);
        scenarioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scenarioSpinner.setAdapter(scenarioAdapter);

        circleIcon = new CircleDotView(this);

        RelativeLayout dotLayout = (RelativeLayout) findViewById(R.id.dotLayout);
        dotLayout.addView(circleIcon);
        rl_scenario = (RelativeLayout) findViewById(R.id.rl_scenario);
        colorLL = (LinearLayout) findViewById(R.id.colorLL);

        if(deviceInfo.devicenodetype==1){
            rl_scenario.setVisibility(View.GONE);
            colorLL.setVisibility(View.GONE);
        }else {
            rl_scenario.setVisibility(View.VISIBLE);
            colorLL.setVisibility(View.VISIBLE);
        }

        Log.e("EditDeviceActivity", "nodeno=" + deviceInfo.nodeno + ";;;coreid=" + deviceInfo.coreid
        +";devicenodetype="+deviceInfo.devicenodetype);

        SlidingMenuMainActivity.m_mainDevice.setDeviceID(deviceInfo.nodeno);

//        boolean isControlConnect = SlidingMenuMainActivity.m_mainDevice.Connect(deviceInfo.coreid);
//
//        Log.e(TAG, "isControlConnect=" + isControlConnect);

        Log.e(TAG, "CCT=" + SlidingMenuMainActivity.m_mainDevice.getCCT(deviceInfo.nodeno)+ ";State=" + SlidingMenuMainActivity.m_mainDevice.getState(deviceInfo.nodeno) + ";blue=" +
                SlidingMenuMainActivity.m_mainDevice.getBlue(deviceInfo.nodeno) + ";red=" + SlidingMenuMainActivity.m_mainDevice.getRed(deviceInfo.nodeno) + ";green=" + SlidingMenuMainActivity.m_mainDevice.getGreen(deviceInfo.nodeno) + ";Brightness" +
                SlidingMenuMainActivity.m_mainDevice.getBrightness(deviceInfo.nodeno));


        if (SlidingMenuMainActivity.m_mainDevice.getEnableEventBroadcast()) {
            IntentFilter intentFilter = new IntentFilter(xltDevice.bciDeviceStatus);
            intentFilter.setPriority(3);
            registerReceiver(m_StatusReceiver, intentFilter);
        }

        if (SlidingMenuMainActivity.m_mainDevice.getEnableEventSendMessage()) {
            m_handlerControl = new Handler() {
                public void handleMessage(Message msg) {
                    int intValue = msg.getData().getInt("State", -255);
                    if (intValue != -255) {
                        powerSwitch.setChecked(intValue > 0);
                    }

                    intValue = msg.getData().getInt("BR", -255);
                    if (intValue != -255) {
                        brightnessSeekBar.setProgress(intValue);
                    }

                    intValue = msg.getData().getInt("CCT", -255);
                    if (intValue != -255) {
                        cctSeekBar.setProgress(intValue - 2700);
                    }
                }
            };
            SlidingMenuMainActivity.m_mainDevice.addDeviceEventHandler(m_handlerControl);
        }
        mscenarioName.setText(deviceInfo.devicenodename);
        powerSwitch.setChecked((SlidingMenuMainActivity.m_mainDevice.getState(deviceInfo.nodeno) == 0)?false : true);
        brightnessSeekBar.setProgress(SlidingMenuMainActivity.m_mainDevice.getBrightness(deviceInfo.nodeno));
        cctSeekBar.setProgress(SlidingMenuMainActivity.m_mainDevice.getCCT(deviceInfo.nodeno) - 2700);

        int R = SlidingMenuMainActivity.m_mainDevice.getRed(deviceInfo.nodeno);
        int G = SlidingMenuMainActivity.m_mainDevice.getGreen(deviceInfo.nodeno);
        int B = SlidingMenuMainActivity.m_mainDevice.getBlue(deviceInfo.nodeno);

        int color = Color.rgb(R, G, B);
        circleIcon.setColor(color);
        colorTextView.setText("RGB(" + R + "," + G + "," + B + ")");
        findViewById(com.umarbhutta.xlightcompanion.R.id.colorLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabPressed();
            }
        });

        powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //check if on or off
                Log.e(TAG, "The power value is " + (isChecked ? xltDevice.STATE_ON : xltDevice.STATE_OFF));
                state = isChecked;
                int stateInt = SlidingMenuMainActivity.m_mainDevice.PowerSwitch(isChecked ? xltDevice.STATE_ON : xltDevice.STATE_OFF);
                Log.e(TAG, "stateInt value is= " + stateInt);
                deviceInfo.ison = isChecked ? xltDevice.STATE_ON : xltDevice.STATE_OFF;
                GlanceMainFragment.devicenodes.remove(mPositon);
                GlanceMainFragment.devicenodes.add(mPositon, deviceInfo);
            }
        });
        /**
         * 亮度
         */
        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(TAG, "The brightness value is " + seekBar.getProgress());
                int brightnessInt = SlidingMenuMainActivity.m_mainDevice.ChangeBrightness(seekBar.getProgress());
                Log.e(TAG, "brightnessInt value is= " + brightnessInt);
                deviceInfo.brightness = seekBar.getProgress();
            }
        });

        /**
         *色温
         */
        cctSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int seekBarProgress = seekBar.getProgress() + 2700;
                if (seekBarProgress > 2700 && seekBarProgress < 3500) {
                    cctLabelColor.setText(com.umarbhutta.xlightcompanion.R.string.nuan_bai);
                }
                if (seekBarProgress > 3500 && seekBarProgress < 5500) {
                    cctLabelColor.setText(com.umarbhutta.xlightcompanion.R.string.zhengbai);
                }
                if (seekBarProgress > 5500 && seekBarProgress < 6500) {
                    cctLabelColor.setText(com.umarbhutta.xlightcompanion.R.string.lengbai);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "The CCT value is " + seekBar.getProgress() + 2700);
                int seekBarProgress = seekBar.getProgress() + 2700;
               int cctInt =  SlidingMenuMainActivity.m_mainDevice.ChangeCCT(seekBarProgress);
                Log.e(TAG, "cctInt value is= " + cctInt);
            }
        });

        scenarioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString() == "None") {
                    //scenarioNoneLL.animate().alpha(1).setDuration(600).start();

                    //enable all views below spinner
                    disableEnableControls(true);
                } else {
                    //if anything but "None" is selected, fade scenarioNoneLL out
                    //scenarioNoneLL.animate().alpha(0).setDuration(500).start();

                    //disable all views below spinner
                    disableEnableControls(false);

                    //ParticleAdapter.JSONCommandScenario(ParticleAdapter.DEFAULT_DEVICE_ID, position);
                    //position passed into above function corresponds to the scenarioId i.e. s1, s2, s3 to trigger
                    SlidingMenuMainActivity.m_mainDevice.ChangeScenario(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ring1Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ring1 = isChecked;
                updateDeviceRingLabel();
            }
        });
        ring2Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ring2 = isChecked;
                updateDeviceRingLabel();
            }
        });
        ring3Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ring3 = isChecked;
                updateDeviceRingLabel();
            }
        });
        initScenario();//初始化场景

        //TODO
//        if (null != deviceInfo && null != deviceInfo.devicerings && deviceInfo.devicerings.size() > 0) {
//            Scenarionodes scenarionodes = deviceInfo.devicerings.get(0);
//            int R = scenarionodes.R;
//            int G = scenarionodes.G;
//            int B = scenarionodes.B;
//
//
//            int color = Color.rgb(R, G, B);
//            circleIcon.setColor(color);
//            colorTextView.setText("RGB(" + R + "," + G + "," + B + ")");
//        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scenarioName:
                String title = getString(R.string.edit_device_name);
                final EditText et = new EditText(this);
                new AlertDialog.Builder(this).setTitle(title)
                        .setView(et)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et.getText().toString();

                                if (TextUtils.isEmpty(input)) {
                                    ToastUtil.showToast(EditDeviceActivity.this, getString(R.string.content_is_null));
                                    return;
                                }
                                editDeViceInfo();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
                break;
        }
    }

    private class MyStatusReceiver extends StatusReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            powerSwitch.setChecked(SlidingMenuMainActivity.m_mainDevice.getState() > 0);
            brightnessSeekBar.setProgress(SlidingMenuMainActivity.m_mainDevice.getBrightness());
            cctSeekBar.setProgress(SlidingMenuMainActivity.m_mainDevice.getCCT() - 2700);
        }
    }

    private final EditDeviceActivity.MyStatusReceiver m_StatusReceiver = new EditDeviceActivity.MyStatusReceiver();

    @Override
    public void onDestroy() {
        SlidingMenuMainActivity.m_mainDevice.removeDeviceEventHandler(m_handlerControl);
        if (SlidingMenuMainActivity.m_mainDevice.getEnableEventBroadcast()) {
            unregisterReceiver(m_StatusReceiver);
        }
        super.onDestroy();
    }


    private void disableEnableControls(boolean isEnabled) {
        powerSwitch.setEnabled(isEnabled);
        colorTextView.setEnabled(isEnabled);
        brightnessSeekBar.setEnabled(isEnabled);
        cctSeekBar.setEnabled(isEnabled);

        int selectColor = R.color.colorAccent, allLabels = R.color.textColorPrimary;
        if (isEnabled) {
            selectColor = R.color.colorAccent;
            allLabels = R.color.textColorPrimary;
        } else {
            selectColor = R.color.colorDisabled;
            allLabels = R.color.colorDisabled;
        }
        colorTextView.setTextColor(ContextCompat.getColor(this, selectColor));
        powerLabel.setTextColor(ContextCompat.getColor(this, allLabels));
        brightnessLabel.setTextColor(ContextCompat.getColor(this, allLabels));
        cctLabel.setTextColor(ContextCompat.getColor(this, allLabels));
        colorLabel.setTextColor(ContextCompat.getColor(this, allLabels));
    }

    private void updateDeviceRingLabel() {
        String label = SlidingMenuMainActivity.m_mainDevice.getDeviceName();

        if (ring1 && ring2 && ring3) {
            label += ": " + RINGALL_TEXT;
            lightImageView.setImageResource(R.drawable.aquabg_ring123);
        } else if (!ring1 && !ring2 && !ring3) {
            label += ": " + RINGALL_TEXT;
            lightImageView.setImageResource(R.drawable.aquabg_noring);
        } else if (ring1 && ring2) {
            label += ": " + RING1_TEXT + " & " + RING2_TEXT;
            lightImageView.setImageResource(R.drawable.aquabg_ring12);
        } else if (ring2 && ring3) {
            label += ": " + RING2_TEXT + " & " + RING3_TEXT;
            lightImageView.setImageResource(R.drawable.aquabg_ring23);
        } else if (ring1 && ring3) {
            label += ": " + RING1_TEXT + " & " + RING3_TEXT;
            lightImageView.setImageResource(R.drawable.aquabg_ring13);
        } else if (ring1) {
            label += ": " + RING1_TEXT;
            lightImageView.setImageResource(R.drawable.aquabg_ring1);
        } else if (ring2) {
            label += ": " + RING2_TEXT;
            lightImageView.setImageResource(R.drawable.aquabg_ring2);
        } else if (ring3) {
            label += ": " + RING3_TEXT;
            lightImageView.setImageResource(R.drawable.aquabg_ring3);
        } else {
            label += "";
            lightImageView.setImageResource(R.drawable.aquabg_noring);
        }

        deviceRingLabel.setText(label);
    }

    private void onFabPressed() {
        Intent intent = new Intent(EditDeviceActivity.this, ColorSelectActivity.class);
        startActivityForResult(intent, 1);
    }

    private void initScenario() {
        RequestSceneListInfo.getInstance().getSceneListInfo(this, new RequestSceneListInfo.OnRequestFirstPageInfoCallback() {
            @Override
            public void onRequestFirstPageInfoSuccess(final SceneListResult mDeviceInfoResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EditDeviceActivity.this.mDeviceInfoResult = mDeviceInfoResult;
                        initList();
                    }
                });
            }

            @Override
            public void onRequestFirstPageInfoFail(int code, final String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(EditDeviceActivity.this, "" + errMsg);
                    }
                });
            }
        });
    }

    private List<View> viewList = new ArrayList<View>();
    private List<TextView> textViews = new ArrayList<TextView>();

    private void initList() {
        for (int i = 0; i < mDeviceInfoResult.rows.size() + 1; i++) {
            View view;
            TextView textView;
            if (i == 0) {
                view = mInflater.inflate(R.layout.add_scenario_zdy_item,
                        linear, false);
                textView = (TextView) view.findViewById(R.id.textView);
                view.setBackgroundResource(R.drawable.add_scenario_blue_bg);
            } else {
                Rows info = mDeviceInfoResult.rows.get(i - 1);
                view = mInflater.inflate(R.layout.add_scenario_item,
                        linear, false);
                view.setBackgroundResource(R.drawable.add_scenario_bg);
                textView = (TextView) view.findViewById(R.id.sceneName);
                textView.setText(info.scenarioname);
            }

            viewList.add(view);
            textViews.add(textView);
            view.setTag(i);
            view.setOnClickListener(mSceneClick);
            linear.addView(view);
        }
    }

    private Rows curSene = null;

    View.OnClickListener mSceneClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();

            if (0 == index) {
                curSene = null;
            } else {
                Rows sceneInfo = mDeviceInfoResult.rows.get(index - 1);
                curSene = sceneInfo;
                updateSceneInfo(sceneInfo);
            }

            for (int i = 0; i < viewList.size(); i++) {
                View view = viewList.get(i);
                TextView textView = textViews.get(i);
                view.setBackgroundResource(R.drawable.add_scenario_bg);
                textView.setTextColor(getResources().getColor(R.color.black));
            }

            View mView = viewList.get(index);
            mView.setBackgroundResource(R.drawable.add_scenario_blue_bg);
            TextView mText = textViews.get(index);
            mText.setTextColor(getResources().getColor(R.color.white));
        }
    };

    /**
     * 选择了某一个场景
     *
     * @param sceneInfo
     */
    private void updateSceneInfo(Rows sceneInfo) {
        if (null == sceneInfo) {
            return;
        }
        powerSwitch.setChecked((1 == sceneInfo.ison) ? true : false);
        brightnessSeekBar.setProgress(sceneInfo.brightness);
        cctSeekBar.setProgress(sceneInfo.cct - 2700);
    }

    /**
     * 提交编辑设备
     */
    private void editDeViceInfo() {
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            ToastUtil.showToast(this, R.string.net_error);
            return;
        }

        String deviceName = mscenarioName.getText().toString();
        if (TextUtils.isEmpty(deviceName)) {
            ToastUtil.showToast(this, getString(R.string.please_set_lamp_name));
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ison", powerSwitch.isChecked() ? 1 : 0);
            jsonObject.put("userId", UserUtils.getUserInfo(this).getId());
            jsonObject.put("devicename", deviceName);

            JSONArray devicenodes = new JSONArray();
            jsonObject.put("devicenodes", devicenodes);

            JSONObject devicenodesJSONObject = new JSONObject();
            devicenodes.put(devicenodesJSONObject);

            devicenodesJSONObject.put("deviceId", deviceInfo.id);
            devicenodesJSONObject.put("devicenodename", deviceName);
            devicenodesJSONObject.put("ison", powerSwitch.isChecked() ? 1 : 0);
            if (null == curSene) {
                devicenodesJSONObject.put("scenarioId", 0);  //TODO 场景id
            } else {
                devicenodesJSONObject.put("scenarioId", curSene.id);  //TODO 场景id
            }
            JSONArray deviceringsArr = new JSONArray();
            devicenodesJSONObject.put("devicerings", deviceringsArr);


            for (int i = 0; i < 3; i++) {

                JSONObject deviceringsObj = new JSONObject();
                deviceringsArr.put(deviceringsObj);

                deviceringsObj.put("ison", powerSwitch.isChecked() ? 1 : 0);
                deviceringsObj.put("R", red);
                deviceringsObj.put("G", green);
                deviceringsObj.put("B", blue);
                deviceringsObj.put("color", "rgb(" + red + "," + green + "," + blue + ")");
                deviceringsObj.put("cct", cctSeekBar.getProgress() + 2700);
                deviceringsObj.put("brightness", brightnessSeekBar.getProgress());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        HttpUtils.getInstance().putRequestInfo(NetConfig.URL_EDIT_DEVICE_INFO + deviceInfo.id + "?access_token=" + UserUtils.getUserInfo(this).getAccess_token(),
                jsonObject.toString(), null, new HttpUtils.OnHttpRequestCallBack() {
                    @Override
                    public void onHttpRequestSuccess(Object result) {
                        Logger.i("编辑成功 = " + result.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(EditDeviceActivity.this, getString(R.string.modify_success));
//                                setResult(0);
//                                EditDeviceActivity.this.finish();
                            }
                        });
                    }

                    @Override
                    public void onHttpRequestFail(int code, final String errMsg) {
                        Logger.i("编辑失败 = " + errMsg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(EditDeviceActivity.this, getString(R.string.modify_fail) + errMsg);

                            }
                        });
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            int color = data.getIntExtra("color", -1);
            if (-1 != color) {
                red = (color & 0xff0000) >> 16;
                green = (color & 0x00ff00) >> 8;
                blue = (color & 0x0000ff);
            }
            circleIcon.setColor(color);
            colorTextView.setText("RGB(" + red + "," + green + "," + blue + ")");

            int br = brightnessSeekBar.getProgress();
            int ww = 0;
            SlidingMenuMainActivity.m_mainDevice.ChangeColor(xltDevice.RING_ID_ALL, state, br, ww, red, green, blue);
            SlidingMenuMainActivity.m_mainDevice.setRed(xltDevice.RING_ID_ALL, red);
            SlidingMenuMainActivity.m_mainDevice.setGreen(xltDevice.RING_ID_ALL, green);
            SlidingMenuMainActivity.m_mainDevice.setBlue(xltDevice.RING_ID_ALL, blue);
        }
    }

    public String toHexEncoding(int color) {
        String R, G, B;
        StringBuffer sb = new StringBuffer();
        R = Integer.toHexString(Color.red(color));
        G = Integer.toHexString(Color.green(color));
        B = Integer.toHexString(Color.blue(color));
        //判断获取到的R,G,B值的长度 如果长度等于1 给R,G,B值的前边添0
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;
        sb.append("#");
        sb.append(R);
        sb.append(G);
        sb.append(B);
        return sb.toString();
    }

}
