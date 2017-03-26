package com.umarbhutta.xlightcompanion.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.umarbhutta.xlightcompanion.R;

public class WelcomActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_welcom);
        getWindow().setStatusBarColor(getResources().getColor(R.color.bar_color));
        handler.sendEmptyMessageDelayed(100, 2000);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(WelcomActivity.this, SlidingMenuMainActivity.class);
            startActivity(intent);
            WelcomActivity.this.finish();
        }
    };

}
