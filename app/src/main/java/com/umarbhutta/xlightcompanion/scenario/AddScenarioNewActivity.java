package com.umarbhutta.xlightcompanion.scenario;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.SDK.xltDevice;
import com.umarbhutta.xlightcompanion.Tools.NetworkUtils;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.main.MainActivity;
import com.umarbhutta.xlightcompanion.okHttp.model.AddSceneParams;
import com.umarbhutta.xlightcompanion.okHttp.model.Rows;
import com.umarbhutta.xlightcompanion.okHttp.model.Scenarionodes;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestAddScene;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestEditScene;
import com.umarbhutta.xlightcompanion.okHttp.requests.imp.CommentRequstCallback;
import com.umarbhutta.xlightcompanion.views.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/5.新建场景
 */
public class AddScenarioNewActivity extends AppCompatActivity {

    private static final String TAG = AddScenarioNewActivity.class.getSimpleName();
    private SeekBar brightnessSeekBar;
    private TextView colorTextView;
    private Button addButton;
    private EditText nameEditText;
    private ImageView backImageView;
    private Spinner filterSpinner;

    private LinearLayout llBack;
    private TextView btnSure;

    private int scenarioBrightness = 0;
    private int c = 0, cw = 0, ww = 0, r = 0, g = 0, b = 0;
    private String colorHex, scenarioName, scenarioInfo, scenarioFilter;
    private TextView tvTitle;
    private SeekBar colorTemperatureSeekBar;
    private int red = 130;
    private int green = 255;
    private int blue = 0;
    private Rows mSceneInfo;
    String from;
    private CircleImageView circleIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scenario_new);

        getSupportActionBar().hide();

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        brightnessSeekBar = (SeekBar) findViewById(R.id.brightnessSeekBar);
        colorTextView = (TextView) findViewById(R.id.colorTextView);
        addButton = (Button) findViewById(R.id.addButton);

        colorTemperatureSeekBar = (SeekBar) findViewById(R.id.colorTemperatureSeekBar);
        colorTemperatureSeekBar.setMax(3800);
        colorTemperatureSeekBar.setProgress(1800);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        backImageView = (ImageView) findViewById(R.id.backImageView);

        filterSpinner = (Spinner) findViewById(R.id.filterSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this, R.layout.control_scenario_spinner_item, MainActivity.filterNames);
        // Specify the layout to use when the list of choices appears
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the scenarioAdapter to the spinner
        filterSpinner.setAdapter(filterAdapter);

        circleIcon = (CircleImageView) findViewById(R.id.circle_icon);

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
                //send info back to ScenarioFragment


                scenarioName = nameEditText.getText().toString();


                if (TextUtils.isEmpty(scenarioName)) {
                    ToastUtil.showToast(AddScenarioNewActivity.this, "请输入场景名称");
                    return;
                }

                scenarioInfo = "A " + colorHex + " color with " + scenarioBrightness + "% brightness";

                //SEND TO PARTICLE CLOUD FOR ALL RINGS
                MainActivity.m_mainDevice.sceAddScenario(ScenarioFragment.name.size(), scenarioBrightness, cw, ww, r, g, b, xltDevice.DEFAULT_FILTER_ID);

                //send data to update the list
                Intent returnIntent = getIntent();
                returnIntent.putExtra(ScenarioFragment.SCENARIO_NAME, scenarioName);
                returnIntent.putExtra(ScenarioFragment.SCENARIO_INFO, scenarioInfo);
                setResult(Activity.RESULT_OK, returnIntent);
//                finish();

                if ("list".equals(from)) {
                    editScene();
                } else {
                    addScence();
                }


            }
        });

        colorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出选择颜色的页面
                onFabPressed();
            }
        });

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
                scenarioBrightness = seekBar.getProgress();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send info back to ScenarioFragment
                scenarioName = nameEditText.getText().toString();

                scenarioInfo = "A " + colorHex + " color with " + scenarioBrightness + "% brightness";

                //SEND TO PARTICLE CLOUD FOR ALL RINGS
                MainActivity.m_mainDevice.sceAddScenario(ScenarioFragment.name.size(), scenarioBrightness, cw, ww, r, g, b, xltDevice.DEFAULT_FILTER_ID);

                //send data to update the list
                Intent returnIntent = getIntent();
                returnIntent.putExtra(ScenarioFragment.SCENARIO_NAME, scenarioName);
                returnIntent.putExtra(ScenarioFragment.SCENARIO_INFO, scenarioInfo);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back to ScenarioFragment, do nothing
                finish();
            }
        });

        Intent intent = getIntent();

        if (null != intent) {
            from = intent.getStringExtra("from");
            if ("list".equals(from)) { //是从场景列表点击进来的
                mSceneInfo = (Rows) intent.getSerializableExtra("infos");
                initViewState();
            }
        }

        if (!TextUtils.isEmpty(from) && "list".equals(from)) {
            tvTitle.setText("编辑场景");
        } else {
            tvTitle.setText("添加场景");
        }
    }

    private void initViewState() {
        nameEditText.setText(mSceneInfo.scenarioname);
        colorTemperatureSeekBar.setProgress(mSceneInfo.cct - 2700);
        brightnessSeekBar.setProgress(mSceneInfo.brightness);
    }

    private void onFabPressed() {
        Intent intent = new Intent(AddScenarioNewActivity.this, ColorSelectActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        int color = data.getIntExtra("color", -1);
        if (-1 != color) {
            red = (color & 0xff0000) >> 16;
            green = (color & 0x00ff00) >> 8;
            blue = (color & 0x0000ff);
        }

        colorTextView.setTextColor(Color.parseColor(toHexEncoding(color)));
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


    /**
     * 编辑场景
     */
    private void editScene() {
        if (!UserUtils.isLogin(this)) {
            ToastUtil.showToast(this, getString(R.string.login_first));
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            ToastUtil.showToast(this, R.string.net_error);
            return;
        }

        String sceneName = nameEditText.getText().toString();
        if (TextUtils.isEmpty(sceneName)) {
            ToastUtil.showToast(this, "请填写场景名称");
            return;
        }

        RequestEditScene.getInstance().editScene(this, mSceneInfo.id, getParams(1), new CommentRequstCallback() {
            @Override
            public void onCommentRequstCallbackSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(AddScenarioNewActivity.this, "编辑场景成功");
                        AddScenarioNewActivity.this.finish();
                    }
                });
            }

            @Override
            public void onCommentRequstCallbackFail(int code, final String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(AddScenarioNewActivity.this, "" + errMsg);
                    }
                });
            }
        });


    }

    /**
     * 添加场景
     */
    private void addScence() {

        if (!UserUtils.isLogin(this)) {
            ToastUtil.showToast(this, getString(R.string.login_first));
            return;
        }

        if (!NetworkUtils.isNetworkAvaliable(this)) {
            ToastUtil.showToast(this, R.string.net_error);
            return;
        }

        String sceneName = nameEditText.getText().toString();
        if (TextUtils.isEmpty(sceneName)) {
            ToastUtil.showToast(this, "请填写场景名称");
            return;
        }


        RequestAddScene.getInstance().addScene(this, getParams(2), new CommentRequstCallback() {
            @Override
            public void onCommentRequstCallbackSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(AddScenarioNewActivity.this, "场景添加成功");
                        AddScenarioNewActivity.this.finish();
                    }
                });
            }

            @Override
            public void onCommentRequstCallbackFail(int code, final String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(AddScenarioNewActivity.this, "" + errMsg);
                    }
                });
            }
        });


    }

    /**
     * @param mType 添加场景2，编辑场景1
     * @return
     */
    private AddSceneParams getParams(int mType) {
        int brightNess = brightnessSeekBar.getProgress();  // 亮度
        int colorTemper = colorTemperatureSeekBar.getProgress() + 2700;  // 色温
        int type = mType;
        String sceneName = nameEditText.getText().toString();

        AddSceneParams params = new AddSceneParams();
        params.type = type;
        params.userId = UserUtils.getUserInfo(this).id;
        params.scenarioname = sceneName;
        params.cct = colorTemper;
        params.brightness = brightNess;
        params.color = "rgb(" + red + "," + green + "," + blue + ")";

        List<Scenarionodes> list = new ArrayList<Scenarionodes>();
        list.add(new Scenarionodes(45, 235, 7, 103, 3644, "rgb(235,7,103)"));
        list.add(new Scenarionodes(45, 235, 7, 103, 3644, "rgb(235,7,103)"));
        list.add(new Scenarionodes(45, 235, 7, 103, 3644, "rgb(235,7,103)"));

        params.scenarionodes = list;
        return params;
    }

}
