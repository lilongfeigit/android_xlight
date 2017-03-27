package com.umarbhutta.xlightcompanion.settings;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.Tools.StringUtil;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.imgloader.ImageLoaderOptions;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.model.LoginResult;
import com.umarbhutta.xlightcompanion.views.CircleImageView;
import com.umarbhutta.xlightcompanion.views.pickerview.lib.TimePickerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/5.
 * user信息修改页面
 */
public class UserMsgModifyActivity extends ShowPicSelectBaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private TextView nick_name;
    private TextView user_name;
    private TextView sex;
    private CircleImageView user_icon;
    /**
     * 性别
     */
    private ArrayList<String> sexList = new ArrayList<String>();
    private String usernameResult;
    private String nickNameResult;
    private String sexResResult;
    private final int WRITE_PERMISSION_REQ_CODE = 100;

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("image/x-markdown; charset=utf-8");

    @Override
    public void selectPicResult(String picPath) {
//        ToastUtil.showToast(this, "url = " + picPath);
//        user_icon.setImageBitmap(BitmapFactory.decodeFile(picPath));
        uploadPic2(picPath);
    }

    public static final String TYPE = "application/octet-stream";
    private OkHttpClient client;

    private void uploadPic2(String picPath) {

        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();


        File file = new File(picPath);
        if (!file.exists()) {
            Toast.makeText(UserMsgModifyActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
        } else {
            RequestBody fileBody = RequestBody.create(MediaType.parse(TYPE), file);
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("lightscrop.jpg", file.getName(), fileBody).build();

            Request requestPostFile = new Request.Builder()
                    .url(NetConfig.URL_UPLOAD_IMG + UserUtils.getUserInfo(this).getAccess_token())
                    .post(requestBody)
                    .build();
            client.newCall(requestPostFile).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(UserMsgModifyActivity.this, "头像设置失败，请稍后重试");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String jsonResult = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Logger.i("图片地址=" + jsonResult);
                            try {
                                JSONObject object = new JSONObject(jsonResult);
                                if (object.has("filePath")) {
                                    String filePath = object.getString("filePath");
                                    ImageLoader.getInstance().displayImage(NetConfig.SERVER_ADDRESS + filePath, user_icon, ImageLoaderOptions.getImageLoaderOptions());
                                    LoginResult infos = UserUtils.getUserInfo(UserMsgModifyActivity.this);
                                    infos.image = NetConfig.SERVER_ADDRESS + filePath;
                                    UserUtils.saveUserInfo(UserMsgModifyActivity.this, infos);
                                } else {
                                    ToastUtil.showToast(UserMsgModifyActivity.this, "头像设置失败，请稍后重试");
                                }
                            } catch (JSONException e) {
                                ToastUtil.showToast(UserMsgModifyActivity.this, "头像设置失败，请稍后重试");
                                e.printStackTrace();
                            }


                        }
                    });
                }
            });
        }
    }

    private boolean hasPermision = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg_modify);
        getSupportActionBar().hide();
        initViews();
        hasPermision = checkPublishPermission();
    }


    private boolean checkPublishPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }

            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this,
                        (String[]) permissions.toArray(new String[0]),
                        WRITE_PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    private UserMsgModifyAdapter mUserMsgModifyAdapter;
    private RecyclerView mRecyclerView;
    private List<String> userMsgs = new ArrayList<String>();

    private void initViews() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        btnSure.setVisibility(View.GONE);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("用户信息");
        sexList.add("女");
        sexList.add("男");
        sexList.add("不确定");


        findViewById(R.id.nick_name_layout).setOnClickListener(this);
        findViewById(R.id.avatar_layout).setOnClickListener(this);
        findViewById(R.id.sex_layout).setOnClickListener(this);

        user_name = (TextView) findViewById(R.id.user_name);
        nick_name = (TextView) findViewById(R.id.nick_name);
        sex = (TextView) findViewById(R.id.sex);
        user_icon = (CircleImageView) findViewById(R.id.user_icon);
        updateUserinfo();
    }

    private void updateUserinfo() {
        LoginResult info = UserUtils.getUserInfo(this);
        if(StringUtil.isNotEmpty(info.username,false)){
            user_name.setText(info.username);
        }else{
            user_name.setText("");
        }
        if(StringUtil.isNotEmpty(info.nickname,false)){
            nick_name.setText(info.nickname);
        }else{
            nick_name.setText("");
        }
        if(StringUtil.isNotEmpty(info.sex,true)){
            if(info.sex.equals("0")){
                sex.setText("女");
            }else if(info.sex.equals("1")){
                sex.setText("男");
            }else if(info.sex.equals("2")){
                sex.setText("不确定");
            }
        }else{
            sex.setText("不确定");
        }
        ImageLoader.getInstance().displayImage(info.getImage(), user_icon, ImageLoaderOptions.getImageLoaderOptions());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_layout://账号
                showDialog(v);
                break;
            case R.id.nick_name_layout://呢称
                showDialog(v);
                break;
            case R.id.avatar_layout:
                showPictureSelector();
                break;
            case R.id.sex_layout:
                showSexPicker();
                break;
        }
    }

    public int type = 0;

    private void showDialog(View view) {

        String title;
        if (R.id.account_layout == view.getId()) {
            title = "修改账号";
            type = 0;
        } else {
            title = "修改呢称";
            type = 1;
        }

        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle(title)
                .setView(et)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();

                        if (TextUtils.isEmpty(input)) {
                            ToastUtil.showToast(UserMsgModifyActivity.this, getString(R.string.content_is_null));
                            return;
                        }

                        modifyUserInfo(type, input);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private int sexPosition;

    private void showSexPicker() {
        TimePickerUtils.alertBottomWheelOption(this, sexList, 0, new TimePickerUtils.OnWheelViewClick() {
            @Override
            public void onClick(View view, int postion) {
                sexPosition = postion;
                sex.setText(sexList.get(postion));
                modifyUserInfo(2, null);
            }
        });
    }

    /**
     * @param type   0修改账号，1修改呢称，2修改性别
     * @param result
     */
    private void modifyUserInfo(int type, String result) {
        LoginResult userInfo = UserUtils.getUserInfo(this);
        usernameResult = userInfo.getUsername();
        nickNameResult = userInfo.getNickname();
        sexResResult = TextUtils.isEmpty(userInfo.getSex()) ? "0" : "1";//sex=0代表女，1代表男，没选的话就不传这个参数

        this.type = type;

        switch (type) {
            case 0:
                usernameResult = result;
                break;
            case 1:
                nickNameResult = result;
                break;
            case 2:
                sexResResult = String.valueOf(sexPosition);
                break;
        }


        JSONObject object = new JSONObject();
        try {
            object.put("username", usernameResult);
            object.put("nickname", nickNameResult);
            if("2".equals(sexResResult)){
                object.put("sex", "");
            }else{
                object.put("sex", sexResResult);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.getInstance().putRequestInfo(NetConfig.URL_MODIFY_USER_INFO + userInfo.getId() + "?access_token=" + userInfo.getAccess_token(), object.toString(), null, new HttpUtils.OnHttpRequestCallBack() {
            @Override
            public void onHttpRequestSuccess(Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        saveUserInfo();
                        ToastUtil.showToast(UserMsgModifyActivity.this, "修改成功");
                    }
                });
            }

            @Override
            public void onHttpRequestFail(int code, final String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(UserMsgModifyActivity.this, "" + errMsg);
                    }
                });
            }
        });
    }

    private void saveUserInfo() {
        LoginResult mLoginResult = UserUtils.getUserInfo(this);
        switch (type) {
            case 0:
                mLoginResult.username = usernameResult;
                user_name.setText(usernameResult);
                break;
            case 1:
                mLoginResult.nickname = nickNameResult;
                nick_name.setText(nickNameResult);
                break;
            case 2:
                if("0".equals(sexResResult)){
                    mLoginResult.sex = "女";
                }else if("1".equals(sexResResult)){
                    mLoginResult.sex = "男";
                }else{
                    mLoginResult.sex = "不确定";
                }
                break;
        }

        UserUtils.saveUserInfo(this, mLoginResult);

    }

}
