package com.umarbhutta.xlightcompanion.scenario;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.umarbhutta.xlightcompanion.R;

/**
 * Created by Administrator on 2017/3/4.
 */

public class ColorSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_select);
        //hide nav bar
        getSupportActionBar().hide();

        initViews();
    }

    /**
     * init views
     */
    private void initViews() {
        ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
        SVBar svBar = (SVBar) findViewById(R.id.svbar);
        OpacityBar opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        ValueBar valueBar = (ValueBar) findViewById(R.id.valuebar);
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 确定，选择的颜色
            }
        });

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);

            //To get the color
        picker.getColor();

         //To set the old selected color u can do it like this
        picker.setOldCenterColor(picker.getColor());
        // adds listener to the colorpicker which is implemented
        //in the activity
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                //TODO 颜色选择
//                Toast.makeText(getApplicationContext(),"setOnColorChangedListener"+color,Toast.LENGTH_SHORT).show();
            }
        });

        //to turn of showing the old color
        picker.setShowOldCenterColor(false);

        //adding onChangeListeners to bars
        opacityBar.setOnOpacityChangedListener(new OpacityBar.OnOpacityChangedListener() {
            @Override
            public void onOpacityChanged(int opacity) {
            //TODO
//                Toast.makeText(getApplicationContext(),"setOnOpacityChangedListener"+opacity,Toast.LENGTH_SHORT).show();
            }
        });

        valueBar.setOnValueChangedListener(new ValueBar.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
            //TODO 颜色值修改变化
//                Toast.makeText(getApplicationContext(),"setOnValueChangedListener"+value,Toast.LENGTH_SHORT).show();
            }
        });
        saturationBar.setOnSaturationChangedListener(new SaturationBar.OnSaturationChangedListener() {
            @Override
            public void onSaturationChanged(int saturation) {
            //TODO 颜色状态变化
//                Toast.makeText(getApplicationContext(),"setOnSaturationChangedListener"+saturation,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
