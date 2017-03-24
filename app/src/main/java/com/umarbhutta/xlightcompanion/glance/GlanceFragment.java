package com.umarbhutta.xlightcompanion.glance;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.SDK.CloudAccount;
import com.umarbhutta.xlightcompanion.SDK.xltDevice;
import com.umarbhutta.xlightcompanion.Tools.DataReceiver;
import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.Tools.NetworkUtils;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.bindDevice.BindDeviceFirstActivity;
import com.umarbhutta.xlightcompanion.control.DevicesListAdapter;
import com.umarbhutta.xlightcompanion.main.MainActivity;
import com.umarbhutta.xlightcompanion.main.SimpleDividerItemDecoration;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.model.DeviceInfoResult;
import com.umarbhutta.xlightcompanion.okHttp.model.Rows;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestFirstPageInfo;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestUnBindDevice;
import com.umarbhutta.xlightcompanion.okHttp.requests.imp.CommentRequstCallback;
import com.umarbhutta.xlightcompanion.userManager.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umar Bhutta.
 */
public class GlanceFragment extends Fragment {
    private com.github.clans.fab.FloatingActionButton fab;
    TextView outsideTemp, degreeSymbol, roomTemp, roomHumidity, outsideHumidity, apparentTemp;
    ImageView imgWeather;

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView devicesRecyclerView;
    WeatherDetails mWeatherDetails;

    private Handler m_handlerGlance;

    private Bitmap icoDefault, icoClearDay, icoClearNight, icoRain, icoSnow, icoSleet, icoWind, icoFog;
    private Bitmap icoCloudy, icoPartlyCloudyDay, icoPartlyCloudyNight;
    private static int ICON_WIDTH = 70;
    private static int ICON_HEIGHT = 75;

    /**
     * 设备列表
     */
    public static List<Rows> deviceList = new ArrayList<Rows>();
    private DevicesListAdapter devicesListAdapter;
    private TextView default_text;

    private class MyDataReceiver extends DataReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            roomTemp.setText(MainActivity.m_mainDevice.m_Data.m_RoomTemp + "\u00B0");
            roomHumidity.setText(MainActivity.m_mainDevice.m_Data.m_RoomHumidity + "\u0025");
        }
    }

    private final MyDataReceiver m_DataReceiver = new MyDataReceiver();

    @Override
    public void onDestroyView() {
        devicesRecyclerView.setAdapter(null);
        MainActivity.m_mainDevice.removeDataEventHandler(m_handlerGlance);
        if (MainActivity.m_mainDevice.getEnableEventBroadcast()) {
            getContext().unregisterReceiver(m_DataReceiver);
        }
        super.onDestroyView();
    }

    private Dialog mSelectDialog = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_glance, container, false);
        //        hide nav bar
        fab = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 跳转到绑定设备页面
                Intent intent = new Intent(getContext(), BindDeviceFirstActivity.class);
                startActivityForResult(intent, 1);
//                mSelectDialog = new Dialog(getActivity(), R.style.select_bing_dialog);
//                LinearLayout root = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
//                        R.layout.layout_select_bing_control, null);
//                root.findViewById(R.id.btn_choose_add_wifi_device).setOnClickListener(btnlistener);
//                root.findViewById(R.id.btn_choose_scan_add_device).setOnClickListener(btnlistener);
//                root.findViewById(R.id.btn_choose_add_line_device).setOnClickListener(btnlistener);
//                root.findViewById(R.id.btn_cancel).setOnClickListener(btnlistener);
//                mSelectDialog.setContentView(root);
//                Window dialogWindow = mSelectDialog.getWindow();
//                dialogWindow.setGravity(Gravity.BOTTOM);
//                dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
//                WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                lp.x = 0; // 新位置X坐标
//                lp.y = -20; // 新位置Y坐标
//                lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
//                //      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
//                //      lp.alpha = 9f; // 透明度
//                root.measure(0, 0);
//                lp.height = root.getMeasuredHeight();
//                lp.alpha = 9f; // 透明度
//                dialogWindow.setAttributes(lp);
//                mSelectDialog.show();
            }
        });

        default_text = (TextView) view.findViewById(R.id.default_text);

        outsideTemp = (TextView) view.findViewById(R.id.outsideTemp);
        degreeSymbol = (TextView) view.findViewById(R.id.degreeSymbol);
        outsideHumidity = (TextView) view.findViewById(R.id.valLocalHumidity);
        apparentTemp = (TextView) view.findViewById(R.id.valApparentTemp);
        roomTemp = (TextView) view.findViewById(R.id.valRoomTemp);
        roomTemp.setText(MainActivity.m_mainDevice.m_Data.m_RoomTemp + "\u00B0");
        roomHumidity = (TextView) view.findViewById(R.id.valRoomHumidity);
        roomHumidity.setText(MainActivity.m_mainDevice.m_Data.m_RoomHumidity + "\u0025");
        imgWeather = (ImageView) view.findViewById(R.id.weatherIcon);

        Resources res = getResources();
        Bitmap weatherIcons = decodeResource(res, R.drawable.weather_icons_1, 420, 600);
        icoDefault = Bitmap.createBitmap(weatherIcons, 0, 0, ICON_WIDTH, ICON_HEIGHT);
        icoClearDay = Bitmap.createBitmap(weatherIcons, ICON_WIDTH, 0, ICON_WIDTH, ICON_HEIGHT);
        icoClearNight = Bitmap.createBitmap(weatherIcons, ICON_WIDTH * 2, 0, ICON_WIDTH, ICON_HEIGHT);
        icoRain = Bitmap.createBitmap(weatherIcons, ICON_WIDTH * 5, ICON_HEIGHT * 2, ICON_WIDTH, ICON_HEIGHT);
        icoSnow = Bitmap.createBitmap(weatherIcons, ICON_WIDTH * 4, ICON_HEIGHT * 3, ICON_WIDTH, ICON_HEIGHT);
        icoSleet = Bitmap.createBitmap(weatherIcons, ICON_WIDTH * 5, ICON_HEIGHT * 3, ICON_WIDTH, ICON_HEIGHT);
        icoWind = Bitmap.createBitmap(weatherIcons, 0, ICON_HEIGHT * 3, ICON_WIDTH, ICON_HEIGHT);
        icoFog = Bitmap.createBitmap(weatherIcons, 0, ICON_HEIGHT * 2, ICON_WIDTH, ICON_HEIGHT);
        icoCloudy = Bitmap.createBitmap(weatherIcons, ICON_WIDTH, ICON_HEIGHT * 5, ICON_WIDTH, ICON_HEIGHT);
        icoPartlyCloudyDay = Bitmap.createBitmap(weatherIcons, ICON_WIDTH, ICON_HEIGHT, ICON_WIDTH, ICON_HEIGHT);
        icoPartlyCloudyNight = Bitmap.createBitmap(weatherIcons, ICON_WIDTH * 2, ICON_HEIGHT, ICON_WIDTH, ICON_HEIGHT);

        if (MainActivity.m_mainDevice.getEnableEventBroadcast()) {
            IntentFilter intentFilter = new IntentFilter(xltDevice.bciSensorData);
            intentFilter.setPriority(3);
            getContext().registerReceiver(m_DataReceiver, intentFilter);
        }

        if (MainActivity.m_mainDevice.getEnableEventSendMessage()) {
            m_handlerGlance = new Handler() {
                public void handleMessage(Message msg) {
                    int intValue = msg.getData().getInt("DHTt", -255);
                    if (intValue != -255) {
                        roomTemp.setText(intValue + "\u00B0");
                    }
                    intValue = msg.getData().getInt("DHTh", -255);
                    if (intValue != -255) {
                        roomHumidity.setText(intValue + "\u0025");
                    }
                }
            };
            MainActivity.m_mainDevice.addDataEventHandler(m_handlerGlance);
        }

        //setup recycler view
        devicesRecyclerView = (RecyclerView) view.findViewById(R.id.devicesRecyclerView);
        devicesListAdapter = new DevicesListAdapter(getContext(), deviceList);
        devicesRecyclerView.setAdapter(devicesListAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        devicesRecyclerView.setLayoutManager(layoutManager);
        devicesRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        getBaseInfos();

        double latitude = 43.4643;
        double longitude = -80.5204;
        String forecastUrl = "https://api.forecast.io/forecast/" + CloudAccount.DarkSky_apiKey + "/" + latitude + "," + longitude;

        if (NetworkUtils.isNetworkAvaliable(getActivity())) {
            OkHttpClient client = new OkHttpClient();
            //build request
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();
            //put request in call object to use for returning data
            Call call = client.newCall(request);
            //make async call
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {
                            mWeatherDetails = getWeatherDetails(jsonData);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "There was an error retrieving weather data.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException | NullPointerException e) {
                        Logger.i("Exception caught: " + e);
                    }
                }
            });
        } else {
            //if network isn't available
            Toast.makeText(getActivity(), "Please connect to the network before continuing.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_choose_add_wifi_device: // 扫描设备添加
                    Toast.makeText(getActivity(), "扫描设备添加", Toast.LENGTH_SHORT).show();
                    break;
                //扫描二维码添加
                case R.id.btn_choose_scan_add_device:
                    Toast.makeText(getActivity(), "扫描二维码添加", Toast.LENGTH_SHORT).show();
                    break;
                // 输入口令添加
                case R.id.btn_choose_add_line_device:
                    Toast.makeText(getActivity(), "输入口令添加", Toast.LENGTH_SHORT).show();
                    break;
                // 取消
                case R.id.btn_cancel:
                    if (mSelectDialog != null) {
                        mSelectDialog.dismiss();
                    }
                    break;
            }
        }
    };

    private void updateDisplay() {
        imgWeather.setImageBitmap(getWeatherIcon(mWeatherDetails.getIcon()));
        outsideTemp.setText(" " + mWeatherDetails.getTemp("celsius"));
        degreeSymbol.setText("\u00B0");
        outsideHumidity.setText(mWeatherDetails.getmHumidity() + "\u0025");
        apparentTemp.setText(mWeatherDetails.getApparentTemp("celsius") + "\u00B0");

        roomTemp.setText(MainActivity.m_mainDevice.m_Data.m_RoomTemp + "\u00B0");
        roomHumidity.setText(MainActivity.m_mainDevice.m_Data.m_RoomHumidity + "\u0025");
    }

    private WeatherDetails getWeatherDetails(String jsonData) throws JSONException {
        WeatherDetails weatherDetails = new WeatherDetails();

        //make JSONObject for all JSON
        JSONObject forecast = new JSONObject(jsonData);

        //JSONObject for nested JSONObject inside 'forecast' for current weather details
        JSONObject currently = forecast.getJSONObject("currently");

        weatherDetails.setTemp(currently.getDouble("temperature"));
        weatherDetails.setIcon(currently.getString("icon"));
        weatherDetails.setApparentTemp(currently.getDouble("apparentTemperature"));
        weatherDetails.setHumidity((int) (currently.getDouble("humidity") * 100 + 0.5));

        return weatherDetails;
    }

    private Bitmap decodeResource(Resources resources, final int id, final int newWidth, final int newHeight) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        Bitmap loadBmp = BitmapFactory.decodeResource(resources, id, opts);

        int width = loadBmp.getWidth();
        int height = loadBmp.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap newBmp = Bitmap.createBitmap(loadBmp, 0, 0, width, height, matrix, true);
        return newBmp;
    }

    public Bitmap getWeatherIcon(final String iconName) {
        if (iconName.equalsIgnoreCase("clear-day")) {
            return icoClearDay;
        } else if (iconName.equalsIgnoreCase("clear-night")) {
            return icoClearNight;
        } else if (iconName.equalsIgnoreCase("rain")) {
            return icoRain;
        } else if (iconName.equalsIgnoreCase("snow")) {
            return icoSnow;
        } else if (iconName.equalsIgnoreCase("sleet")) {
            return icoSleet;
        } else if (iconName.equalsIgnoreCase("wind")) {
            return icoWind;
        } else if (iconName.equalsIgnoreCase("fog")) {
            return icoFog;
        } else if (iconName.equalsIgnoreCase("cloudy")) {
            return icoCloudy;
        } else if (iconName.equalsIgnoreCase("partly-cloudy-day")) {
            return icoPartlyCloudyDay;
        } else if (iconName.equalsIgnoreCase("partly-cloudy-night")) {
            return icoPartlyCloudyNight;
        } else {
            return icoDefault;
        }
    }

    public void getBaseInfos() {
        if (!NetworkUtils.isNetworkAvaliable(getActivity())) {
            ToastUtil.showToast(getActivity(), R.string.net_error);
            return;
        }

        if (!UserUtils.isLogin(getActivity())) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }

        devicesListAdapter.setOnSwitchStateChangeListener(new DevicesListAdapter.OnSwitchStateChangeListener() {
            @Override
            public void onLongClick(int position) { //长按删除
                showDeleteSceneDialog(position);
            }

            @Override
            public void onSwitchChange(int position, boolean checked) {
                deviceList.get(position).ison = checked ? 1 : 0;
                switchLight(checked, deviceList.get(position));
            }
        });

        RequestFirstPageInfo.getInstance(getActivity()).getBaseInfo(new RequestFirstPageInfo.OnRequestFirstPageInfoCallback() {
            @Override
            public void onRequestFirstPageInfoSuccess(final DeviceInfoResult mDeviceInfoResult) {
                Logger.i("mDeviceInfoResult = " + mDeviceInfoResult.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        List<Rows> devices = mDeviceInfoResult.rows;
                        deviceList.addAll(devices);
                        devicesListAdapter.notifyDataSetChanged();


                        if (null != deviceList && deviceList.size() > 0) {
                            default_text.setVisibility(View.GONE);
                        }
                    }
                });
            }

            @Override
            public void onRequestFirstPageInfoFail(int code, String errMsg) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        devicesListAdapter.notifyDataSetChanged();
        if (null != deviceList && deviceList.size() > 0) {
            default_text.setVisibility(View.GONE);
        } else {
            default_text.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设备开关,调用SDK
     *
     * @param deviceInfo
     */
    private void switchLight(boolean checked, Rows deviceInfo) {
        MainActivity.m_mainDevice.PowerSwitch(checked);

        String param = "{\"ison\":" + (checked ? 1 : 0) + "}";

        HttpUtils.getInstance().putRequestInfo(NetConfig.URL_LAMP_SWITCH + deviceInfo.id + "/onoff?access_token=" + UserUtils.getUserInfo(getActivity()).getAccess_token(), param, null, new HttpUtils.OnHttpRequestCallBack() {
            @Override
            public void onHttpRequestSuccess(Object result) {
                Logger.i("开关结果 = " + result.toString());
            }

            @Override
            public void onHttpRequestFail(int code, String errMsg) {
                Logger.i("开关结果失败 = " + errMsg);
            }
        });
    }


    /**
     * 弹出解绑设备确认框
     */
    private void showDeleteSceneDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("解绑设备提示");
        builder.setMessage("确定解绑此设备吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unBindDevice(position);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    /**
     * 解绑设备
     *
     * @param position
     */
    private void unBindDevice(final int position) {
        if (!NetworkUtils.isNetworkAvaliable(getActivity())) {
            ToastUtil.showToast(getActivity(), R.string.net_error);
            return;
        }

        ((MainActivity) getActivity()).showProgressDialog(getString(R.string.setting));

        RequestUnBindDevice.getInstance().unBindDevice(getActivity(), "" + GlanceFragment.deviceList.get(position).id, new CommentRequstCallback() {
            @Override
            public void onCommentRequstCallbackSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) getActivity()).cancelProgressDialog();
                        ToastUtil.showToast(getActivity(), getString(R.string.unbind_sucess));
                        updateUnbindList(position);
                    }
                });
            }

            @Override
            public void onCommentRequstCallbackFail(int code, final String errMsg) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) getActivity()).cancelProgressDialog();
                        ToastUtil.showToast(getActivity(), getString(R.string.unbind_fail) + errMsg);
                    }
                });
            }
        });
    }

    /**
     * 解绑后更新页面
     *
     * @param position
     */
    private void updateUnbindList(int position) {
        deviceList.remove(position);
        devicesListAdapter.notifyDataSetChanged();
    }
}
