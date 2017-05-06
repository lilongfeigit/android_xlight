package com.umarbhutta.xlightcompanion.control.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.SDK.xltDevice;
import com.umarbhutta.xlightcompanion.main.EditDeviceActivity;
import com.umarbhutta.xlightcompanion.main.SlidingMenuMainActivity;
import com.umarbhutta.xlightcompanion.okHttp.model.Devicenodes;

import java.util.List;

/**
 * Created by Umar Bhutta.
 */
public class DevicesMainListAdapter extends RecyclerView.Adapter {

    private Context mActivity;
    //    private List<Rows> deviceList;
    private List<Devicenodes> mDevicenodes;

    public DevicesMainListAdapter(Context activity, List<Devicenodes> devicenodes) {
        this.mDevicenodes = devicenodes;
        this.mActivity = activity;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_list_item, parent, false);
        return new DevicesListViewHolder(view);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DevicesListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mDevicenodes.size();
    }

    private class DevicesListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mDeviceName, main_device, devicePlan;
        private Switch mDeviceSwitch;
        private int mPositon;

        public DevicesListViewHolder(View itemView) {
            super(itemView);
            mDeviceName = (TextView) itemView.findViewById(R.id.deviceName);
            main_device = (TextView) itemView.findViewById(R.id.main_device);
            devicePlan = (TextView) itemView.findViewById(R.id.devicePlan);
            mDeviceSwitch = (Switch) itemView.findViewById(R.id.deviceSwitch);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mDeviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // 右侧开关控制
//                    if (null != mOnSwitchStateChangeListener) {
//                        mOnSwitchStateChangeListener.onSwitchChange(mPositon, isChecked);
//                    }
                    if (mDevicenodes.size() <= 0 || null == mDevicenodes.get(mPositon)) {
                        return;
                    }
                    //mDevicenodes
                    SlidingMenuMainActivity.m_mainDevice.setDeviceID(mDevicenodes.get(mPositon).nodeno);
//                    SlidingMenuMainActivity.m_mainDevice.Connect(mDevicenodes.get(mPositon).coreid);
                    SlidingMenuMainActivity.m_mainDevice.PowerSwitch(isChecked ? xltDevice.STATE_ON : xltDevice.STATE_OFF);
                    mDevicenodes.get(mPositon).ison = isChecked ? xltDevice.STATE_ON : xltDevice.STATE_OFF;
                }
            });
        }

        public void bindView(int position) {
            mPositon = position;
            Devicenodes devicenodes = mDevicenodes.get(position);
            mDeviceName.setText(TextUtils.isEmpty(devicenodes.devicenodename) ? mActivity.getString(R.string.lamp) : devicenodes.devicenodename);
            mDeviceSwitch.setChecked(devicenodes.ison == 0 ? false : true);
            devicePlan.setText(devicenodes.ison == 0 ? R.string.no_start_plan : R.string.has_start_plan);
        }

        @Override
        public void onClick(View v) {
            // 点击事件 跳转到编辑设备页面
            Intent intent = new Intent(mActivity, EditDeviceActivity.class);
            intent.putExtra("info", mDevicenodes.get(mPositon));
            intent.putExtra("position", mPositon);
            mActivity.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            // 长按事件  长按删除设备
            if (null != mOnSwitchStateChangeListener) {
                mOnSwitchStateChangeListener.onLongClick(mPositon);
            }
            return true;
        }
    }

    private OnSwitchStateChangeListener mOnSwitchStateChangeListener;

    /**
     * 设置item开关通知
     *
     * @param mOnSwitchStateChangeListener
     */
    public void setOnSwitchStateChangeListener(OnSwitchStateChangeListener mOnSwitchStateChangeListener) {
        this.mOnSwitchStateChangeListener = mOnSwitchStateChangeListener;
    }

    public interface OnSwitchStateChangeListener {
        void onLongClick(int position);

        void onSwitchChange(int position, boolean checked);
    }


}
