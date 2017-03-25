package com.umarbhutta.xlightcompanion.main;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.SDK.xltDevice;
import com.umarbhutta.xlightcompanion.Tools.StatusReceiver;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.control.ControlFragment;
import com.umarbhutta.xlightcompanion.okHttp.model.Rows;
import com.umarbhutta.xlightcompanion.okHttp.model.SceneListResult;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestSceneListInfo;
import com.umarbhutta.xlightcompanion.scenario.ColorSelectActivity;
import com.umarbhutta.xlightcompanion.scenario.ScenarioFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/5.
 * 设置灯
 */

public class EditDeviceActivity extends AppCompatActivity {
    private TextView tvTitle;
    public SceneListResult mDeviceInfoResult;
    private Rows deviceInfo;
    private xltDevice mDevice;
    private TextView mscenarioName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_control);
        mInflater = LayoutInflater.from(this);
        //hide nav bar
        getSupportActionBar().hide();

        deviceInfo = (Rows) getIntent().getSerializableExtra("info");

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
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 确定提交按钮
            }
        });
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("编辑设备");
        mscenarioName = (TextView) findViewById(R.id.scenarioName);

        scenarioSpinner = (Spinner) findViewById(R.id.scenarioSpinner);
        ArrayAdapter<String> scenarioAdapter = new ArrayAdapter<>(this, R.layout.control_scenario_spinner_item, scenarioDropdown);
        scenarioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scenarioSpinner.setAdapter(scenarioAdapter);


        mDevice = new xltDevice();
        mDevice.Init(this);
        mDevice.setDeviceID(deviceInfo.id);
        mDevice.setDeviceName("" + deviceInfo.type);


//        MainActivity.m_mainDevice.setDeviceName(DEFAULT_LAMP_TEXT);

        mscenarioName.setText(deviceInfo.devicename);
        powerSwitch.setChecked(mDevice.getState() > 0);
        brightnessSeekBar.setProgress(mDevice.getBrightness());
        cctSeekBar.setProgress(mDevice.getCCT() - 2700);

        if (mDevice.getEnableEventBroadcast()) {
            IntentFilter intentFilter = new IntentFilter(xltDevice.bciDeviceStatus);
            intentFilter.setPriority(3);
            registerReceiver(m_StatusReceiver, intentFilter);
        }

        if (mDevice.getEnableEventSendMessage()) {
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
            mDevice.addDeviceEventHandler(m_handlerControl);
        }

        powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //check if on or off
                state = isChecked;
                //ParticleAdapter.JSONCommandPower(ParticleAdapter.DEFAULT_DEVICE_ID, state);
                //ParticleAdapter.FastCallPowerSwitch(ParticleAdapter.DEFAULT_DEVICE_ID, state);
                mDevice.PowerSwitch(state);
            }
        });

        colorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabPressed();
//                new ChromaDialog.Builder()
//                        .initialColor(ContextCompat.getColor(EditDeviceActivity.this, R.color.colorAccent))
//                        .colorMode(ColorMode.RGB) // There's also ARGB and HSV
//                        .onColorSelected(new ColorSelectListener() {
//                            @Override
//                            public void onColorSelected(int color) {
//                                Log.e(TAG, "int: " + color);
//                                colorHex = String.format("%06X", (0xFFFFFF & color));
//                                Log.e(TAG, "HEX: #" + colorHex);
//
//                                int br = 65;
//                                int ww = 0;
//                                int c = (int) Long.parseLong(colorHex, 16);
//                                int r = (c >> 16) & 0xFF;
//                                int g = (c >> 8) & 0xFF;
//                                int b = (c >> 0) & 0xFF;
//                                Log.e(TAG, "RGB: " + r + "," + g + "," + b);
//
//                                colorHex = "#" + colorHex;
//                                colorTextView.setText(colorHex);
//                                colorTextView.setTextColor(Color.parseColor(colorHex));
//
//                                //send message to Particle based on which rings have been selected
//                                if ((ring1 && ring2 && ring3) || (!ring1 && !ring2 && !ring3)) {
//                                    //ParticleAdapter.JSONCommandColor(ParticleAdapter.DEFAULT_DEVICE_ID, ParticleAdapter.RING_ALL, state, br, ww, r, g, b);
//                                    MainActivity.m_mainDevice.ChangeColor(xltDevice.RING_ID_ALL, state, br, ww, r, g, b);
//                                } else if (ring1 && ring2) {
//                                    //ParticleAdapter.JSONCommandColor(ParticleAdapter.DEFAULT_DEVICE_ID, ParticleAdapter.RING_1, state, br, ww, r, g, b);
//                                    //ParticleAdapter.JSONCommandColor(ParticleAdapter.DEFAULT_DEVICE_ID, ParticleAdapter.RING_2, state, br, ww, r, g, b);
//                                    MainActivity.m_mainDevice.ChangeColor(xltDevice.RING_ID_1, state, br, ww, r, g, b);
//                                    MainActivity.m_mainDevice.ChangeColor(xltDevice.RING_ID_2, state, br, ww, r, g, b);
//                                } else if (ring2 && ring3) {
//                                    //ParticleAdapter.JSONCommandColor(ParticleAdapter.DEFAULT_DEVICE_ID, ParticleAdapter.RING_2, state, br, ww, r, g, b);
//                                    //ParticleAdapter.JSONCommandColor(ParticleAdapter.DEFAULT_DEVICE_ID, ParticleAdapter.RING_3, state, br, ww, r, g, b);
//                                    MainActivity.m_mainDevice.ChangeColor(xltDevice.RING_ID_2, state, br, ww, r, g, b);
//                                    MainActivity.m_mainDevice.ChangeColor(xltDevice.RING_ID_3, state, br, ww, r, g, b);
//                                } else if (ring1 && ring3) {
//                                    //ParticleAdapter.JSONCommandColor(ParticleAdapter.DEFAULT_DEVICE_ID, ParticleAdapter.RING_1, state, br, ww, r, g, b);
//                                    //ParticleAdapter.JSONCommandColor(ParticleAdapter.DEFAULT_DEVICE_ID, ParticleAdapter.RING_3, state, br, ww, r, g, b);
//                                    MainActivity.m_mainDevice.ChangeColor(xltDevice.RING_ID_1, state, br, ww, r, g, b);
//                                    MainActivity.m_mainDevice.ChangeColor(xltDevice.RING_ID_3, state, br, ww, r, g, b);
//                                } else if (ring1) {
//                                    //ParticleAdapter.JSONCommandColor(ParticleAdapter.DEFAULT_DEVICE_ID, ParticleAdapter.RING_1, state, br, ww, r, g, b);
//                                    MainActivity.m_mainDevice.ChangeColor(xltDevice.RING_ID_1, state, br, ww, r, g, b);
//                                } else if (ring2) {
//                                    //ParticleAdapter.JSONCommandColor(ParticleAdapter.DEFAULT_DEVICE_ID, ParticleAdapter.RING_2, state, br, ww, r, g, b);
//                                    MainActivity.m_mainDevice.ChangeColor(xltDevice.RING_ID_2, state, br, ww, r, g, b);
//                                } else if (ring3) {
//                                    //ParticleAdapter.JSONCommandColor(ParticleAdapter.DEFAULT_DEVICE_ID, ParticleAdapter.RING_3, state, br, ww, r, g, b);
//                                    MainActivity.m_mainDevice.ChangeColor(xltDevice.RING_ID_3, state, br, ww, r, g, b);
//                                } else {
//                                    //do nothing
//                                }
//                            }
//                        })
//                        .create()
//                        .show(getSupportFragmentManager(), "dialog");
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
                //ParticleAdapter.JSONCommandBrightness(ParticleAdapter.DEFAULT_DEVICE_ID, seekBar.getProgress());
                mDevice.ChangeBrightness(seekBar.getProgress());
            }
        });

        /**
         *
         */
        cctSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "The CCT value is " + seekBar.getProgress() + 2700);
                //ParticleAdapter.JSONCommandCCT(ParticleAdapter.DEFAULT_DEVICE_ID, seekBar.getProgress()+2700);
                mDevice.ChangeCCT(seekBar.getProgress() + 2700);
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
                    mDevice.ChangeScenario(position);
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
    }

    private static final String TAG = ControlFragment.class.getSimpleName();

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

    private class MyStatusReceiver extends StatusReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            powerSwitch.setChecked(mDevice.getState() > 0);
            brightnessSeekBar.setProgress(mDevice.getBrightness());
            cctSeekBar.setProgress(mDevice.getCCT() - 2700);
        }
    }

    private final EditDeviceActivity.MyStatusReceiver m_StatusReceiver = new EditDeviceActivity.MyStatusReceiver();

    @Override
    public void onDestroy() {
        mDevice.removeDeviceEventHandler(m_handlerControl);
        if (mDevice.getEnableEventBroadcast()) {
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
        String label = mDevice.getDeviceName();

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

    private void initList() {
        for (int i = 0; i < mDeviceInfoResult.rows.size(); i++) {
            Rows sceneInfo = mDeviceInfoResult.rows.get(i);
            View view;
            if (i == 0) {
                view = mInflater.inflate(R.layout.add_scenario_zdy_item,
                        linear, false);
            } else {
                view = mInflater.inflate(R.layout.add_scenario_item,
                        linear, false);
            }

            view.setTag(sceneInfo);
            view.setOnClickListener(mSceneClick);


            linear.addView(view);
        }
    }

    View.OnClickListener mSceneClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Rows sceneInfo = (Rows) v.getTag();
            updateSceneInfo(sceneInfo);
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

}
