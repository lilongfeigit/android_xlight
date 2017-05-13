package com.umarbhutta.xlightcompanion.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.SharedPreferencesUtils;
import com.umarbhutta.xlightcompanion.settings.BaseActivity;

public class WelcomActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_welcom);
        int currentapiVersion=android.os.Build.VERSION.SDK_INT;
        if(currentapiVersion>=19){
            getWindow().setStatusBarColor(getResources().getColor(R.color.bar_color));
        }
        handler.sendEmptyMessageDelayed(100, 2000);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            boolean isFirst = (boolean) SharedPreferencesUtils.getObject(WelcomActivity.this, SharedPreferencesUtils.KEY_IS_FIRST_LUNCH, true);
            SharedPreferencesUtils.putObject(WelcomActivity.this, SharedPreferencesUtils.KEY_IS_FIRST_LUNCH, false);
            if (!isFirst) {
                startActivity(new Intent(WelcomActivity.this, SlidingMenuMainActivity.class));
            } else {
                Intent intent = new Intent(WelcomActivity.this, SplashActivity.class);
                startActivity(intent);
                WelcomActivity.this.finish();
            }
            finish();


        }
    };

}
