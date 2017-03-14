package com.umarbhutta.xlightcompanion.control;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.SDK.xltDevice;
import com.umarbhutta.xlightcompanion.Tools.DataReceiver;
import com.umarbhutta.xlightcompanion.Tools.StatusReceiver;
import com.umarbhutta.xlightcompanion.glance.GlanceFragment;
import com.umarbhutta.xlightcompanion.main.MainActivity;
import com.umarbhutta.xlightcompanion.main.SimpleDividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by Umar Bhutta.
 * 新的规则页面
 */
public class ControlRuleFragment extends Fragment {
    private static final String TAG = ControlRuleFragment.class.getSimpleName();

    private Handler m_handlerGlance;

    private class MyStatusRuleReceiver extends DataReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            roomTemp.setText(MainActivity.m_mainDevice.m_Data.m_RoomTemp + "\u00B0");
//            roomHumidity.setText(MainActivity.m_mainDevice.m_Data.m_RoomHumidity + "\u0025");
        }
    }

    @Override
    public void onDestroyView() {
        rulesRecyclerView.setAdapter(null);
        MainActivity.m_mainDevice.removeDataEventHandler(m_handlerGlance);
        if (MainActivity.m_mainDevice.getEnableEventBroadcast()) {
            getContext().unregisterReceiver(m_DataReceiver);
        }
        super.onDestroyView();
    }

    private RecyclerView rulesRecyclerView;
    private final MyStatusRuleReceiver m_DataReceiver = new MyStatusRuleReceiver();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_control_rule, container, false);
        //setup recycler view
        rulesRecyclerView = (RecyclerView) view.findViewById(R.id.rulesRecyclerView);
        //create list adapter TODO 填充数据
        DeviceRulesListAdapter devicesListAdapter = new DeviceRulesListAdapter(getContext(),new ArrayList<String>());
        //attach adapter to recycler view
        rulesRecyclerView.setAdapter(devicesListAdapter);
        //set LayoutManager for recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //attach LayoutManager to recycler view
        rulesRecyclerView.setLayoutManager(layoutManager);
        //divider lines
        rulesRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        if (MainActivity.m_mainDevice.getEnableEventSendMessage()) {
            m_handlerGlance = new Handler() {
                public void handleMessage(Message msg) {
                    int intValue = msg.getData().getInt("DHTt", -255);
                    if (intValue != -255) {
//                        roomTemp.setText(intValue + "\u00B0");
                    }
                    intValue = msg.getData().getInt("DHTh", -255);
                    if (intValue != -255) {
//                        roomHumidity.setText(intValue + "\u0025");
                    }
                }
            };
            MainActivity.m_mainDevice.addDataEventHandler(m_handlerGlance);
        }
        if (MainActivity.m_mainDevice.getEnableEventBroadcast()) {
            IntentFilter intentFilter = new IntentFilter(xltDevice.bciSensorData);
            intentFilter.setPriority(3);
            getContext().registerReceiver(m_DataReceiver, intentFilter);
        }
        return view;
    }
}
