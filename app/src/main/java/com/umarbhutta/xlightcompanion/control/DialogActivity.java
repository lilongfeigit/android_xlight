package com.umarbhutta.xlightcompanion.control;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.umarbhutta.xlightcompanion.R;

/**
 * Created by Administrator on 2017/3/15.
 * 弹出框Activity
 */

public class DialogActivity extends AppCompatActivity {

    private ListView lvDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        //hide nav bar
        getSupportActionBar().hide();

        initViews();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        lvDialog = (ListView) findViewById(R.id.lv_dialog);
    }
}
