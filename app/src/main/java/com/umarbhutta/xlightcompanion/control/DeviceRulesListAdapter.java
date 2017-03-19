package com.umarbhutta.xlightcompanion.control;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.SDK.xltDevice;
import com.umarbhutta.xlightcompanion.Tools.StatusReceiver;
import com.umarbhutta.xlightcompanion.main.EditDeviceActivity;
import com.umarbhutta.xlightcompanion.main.MainActivity;
import com.umarbhutta.xlightcompanion.okHttp.model.DeviceInfoResult;
import com.umarbhutta.xlightcompanion.schedule.AddScheduleActivity;

/**
 * Created by Umar Bhutta.
 * 规则适配器
 */
public class DeviceRulesListAdapter extends RecyclerView.Adapter {

    private Context mActivity;
    private DeviceInfoResult mDeviceInfoResult;

    public DeviceRulesListAdapter(Context activity,DeviceInfoResult deviceInfoResult){
        this.mActivity = activity;
        this.mDeviceInfoResult = deviceInfoResult;
    }

    private Handler m_handlerDeviceList;

    private class MyStatusRuleReceiver extends StatusReceiver {
        public Switch m_mainSwitch = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            if( m_mainSwitch != null ) {
                m_mainSwitch.setChecked(MainActivity.m_mainDevice.getState() > 0);
            }
        }
    }
    private final MyStatusRuleReceiver m_StatusReceiver = new MyStatusRuleReceiver();

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if( MainActivity.m_mainDevice.getEnableEventBroadcast() ) {
            IntentFilter intentFilter = new IntentFilter(xltDevice.bciDeviceStatus);
            intentFilter.setPriority(3);
            recyclerView.getContext().registerReceiver(m_StatusReceiver, intentFilter);
        }
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rules_list_item, parent, false);
        return new DevicesRuleViewHolder(view);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if( m_handlerDeviceList != null ) {
            MainActivity.m_mainDevice.removeDeviceEventHandler(m_handlerDeviceList);
        }
        if( MainActivity.m_mainDevice.getEnableEventBroadcast() ) {
            recyclerView.getContext().unregisterReceiver(m_StatusReceiver);
        }
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DevicesRuleViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private class DevicesRuleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView rule_time_one;
        private TextView rule_time_two;
        private TextView ruleMsg;
        private Switch mDeviceSwitch;

        public DevicesRuleViewHolder(View itemView) {
            super(itemView);
            rule_time_one = (TextView) itemView.findViewById(R.id.rule_time_one);
            rule_time_two = (TextView) itemView.findViewById(R.id.rule_time_two);
            ruleMsg = (TextView) itemView.findViewById(R.id.ruleMsg);
            mDeviceSwitch = (Switch) itemView.findViewById(R.id.deviceSwitch);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mDeviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //ParticleAdapter.FastCallPowerSwitch(ParticleAdapter.DEFAULT_DEVICE_ID, isChecked);
                    MainActivity.m_mainDevice.PowerSwitch(isChecked);
                    //TODO 右侧开关控制
                }
            });
        }

        public void bindView (int position) {
            String devicename  = mDeviceInfoResult.rows.get(position).devicename;
//            rule_time_one.setText(MainActivity.deviceNames[position]);
            rule_time_one.setText("09:00");
            rule_time_two.setText("18:00");
            ruleMsg.setText("卧室灯  开灯 亮度60 色温50");

            if (position == 0) {
                // Main device
                mDeviceSwitch.setChecked(MainActivity.m_mainDevice.getState() > 0);
                m_StatusReceiver.m_mainSwitch = mDeviceSwitch;

                if( MainActivity.m_mainDevice.getEnableEventSendMessage() ) {
                    m_handlerDeviceList = new Handler() {
                        public void handleMessage(Message msg) {
                            int intValue = msg.getData().getInt("State", -255);
                            if (intValue != -255) {
                                mDeviceSwitch.setChecked(MainActivity.m_mainDevice.getState() > 0);
                            }
                        }
                    };
                    MainActivity.m_mainDevice.addDeviceEventHandler(m_handlerDeviceList);
                }
            }
        }

        @Override
        public void onClick(View v) {
            //TODO 点击事件 跳转到编辑设备页面
            onFabPressed(EditDeviceActivity.class);
        }

        @Override
        public boolean onLongClick(View view) {
            //TODO 长按事件  长按删除设备
            onFabPressed(AddScheduleActivity.class);
            return true;
        }
    }
    private void onFabPressed(Class activity) {
        Intent intent = new Intent(mActivity, activity);
        mActivity.startActivity(intent);
    }
}
