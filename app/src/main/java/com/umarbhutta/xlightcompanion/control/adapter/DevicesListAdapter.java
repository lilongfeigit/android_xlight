package com.umarbhutta.xlightcompanion.control.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.main.EditDeviceActivity;
import com.umarbhutta.xlightcompanion.okHttp.model.Rows;

import java.util.List;

/**
 * Created by Umar Bhutta.
 */
public class DevicesListAdapter extends RecyclerView.Adapter {

    private Context mActivity;
    private List<Rows> deviceList;

    public DevicesListAdapter(Context activity, List<Rows> deviceList) {
        this.deviceList = deviceList;
        this.mActivity = activity;
    }

    private Handler m_handlerDeviceList;

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
        return deviceList.size();
    }

    private class DevicesListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mDeviceName;
        private Switch mDeviceSwitch;
        private int mPositon;

        public DevicesListViewHolder(View itemView) {
            super(itemView);
            mDeviceName = (TextView) itemView.findViewById(R.id.deviceName);
            mDeviceSwitch = (Switch) itemView.findViewById(R.id.deviceSwitch);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mDeviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // 右侧开关控制
                    if (null != mOnSwitchStateChangeListener) {
                        mOnSwitchStateChangeListener.onSwitchChange(mPositon, isChecked);
                    }
                }
            });
        }

        public void bindView(int position) {
            mPositon = position;
            Rows deviceInfo = deviceList.get(position);
            mDeviceName.setText(TextUtils.isEmpty(deviceInfo.devicename) ? "灯" : deviceInfo.devicename);
            mDeviceSwitch.setChecked(deviceInfo.ison > 0);
        }

        @Override
        public void onClick(View v) {
            // 点击事件 跳转到编辑设备页面
            Intent intent = new Intent(mActivity, EditDeviceActivity.class);
            intent.putExtra("info", deviceList.get(mPositon));
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
