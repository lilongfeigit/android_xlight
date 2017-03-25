package com.umarbhutta.xlightcompanion.control.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.main.MainActivity;
import com.umarbhutta.xlightcompanion.okHttp.model.DeviceInfoResult;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestRuleSwitchDevice;
import com.umarbhutta.xlightcompanion.okHttp.requests.imp.CommentRequstCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umar Bhutta.
 * 规则适配器
 */
public class DeviceRulesListAdapter extends BaseAdapter {

    private LayoutInflater inflater;//这个一定要懂它的用法及作用
    private Context mActivity;
    private DeviceInfoResult mDeviceInfoResult;
//    private List<String> strLists;

        public DeviceRulesListAdapter(Context activity, DeviceInfoResult deviceInfoResult) {
        this.mActivity = activity;
        this.mDeviceInfoResult = deviceInfoResult;
        this.inflater = LayoutInflater.from(mActivity);
    }
//    public DeviceRulesListAdapter(Context activity, List<String> strLists) {
//        this.mActivity = activity;
//        this.strLists = strLists;
//        this.inflater = LayoutInflater.from(mActivity);
//    }

    @Override
    public int getCount() {
        return mDeviceInfoResult.rows.size();
    }

    @Override
    public Object getItem(int position) {
        return mDeviceInfoResult.rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.rules_list_item, null);
            //通过上面layout得到的view来获取里面的具体控件
            holder.mDeviceSwitch = (Switch) convertView.findViewById(R.id.deviceSwitch);
            holder.listViewTerm = (ListView) convertView.findViewById(R.id.lv_term);
            holder.listViewResult = (ListView) convertView.findViewById(R.id.lv_result);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mDeviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //ParticleAdapter.FastCallPowerSwitch(ParticleAdapter.DEFAULT_DEVICE_ID, isChecked);
                MainActivity.m_mainDevice.PowerSwitch(isChecked);
                //右侧开关控制 status:1代表启用，0代表禁用
                int isCheckedInt=0;
                if(isChecked){
                    isCheckedInt=1;
                }else{
                    isCheckedInt=0;
                }
                RequestRuleSwitchDevice.getInstance().switchRule(mActivity,mDeviceInfoResult.rows.get(position).id,isCheckedInt,new CommentRequstCallback(){
                    @Override
                    public void onCommentRequstCallbackSuccess() {
                        ToastUtil.showToast(mActivity,"修改成功position="+position);
                    }

                    @Override
                    public void onCommentRequstCallbackFail(int code, String errMsg) {
                        ToastUtil.showToast(mActivity,"errMsg="+errMsg);
                    }
                });
            }
        });

        List<String> strListTerm = new ArrayList<String>();
        strListTerm.add("09:00");
        strListTerm.add("09:00");
        strListTerm.add("09:00");
        strListTerm.add("09:00");
        List<String> strListResult = new ArrayList<String>();
        strListResult.add("卧室灯开");
        strListResult.add("卧室灯开");
        strListResult.add("卧室灯开");
        strListResult.add("卧室灯开");

        holder.listViewTerm.setAdapter(new TermAdapter(mActivity, strListTerm));
        //根据innerlistview的高度机损parentlistview item的高度
        setListViewHeightBasedOnChildren(holder.listViewTerm);
        holder.listViewResult.setAdapter(new ResultAdapter(mActivity, strListResult));
        //根据innerlistview的高度机损parentlistview item的高度
        setListViewHeightBasedOnChildren(holder.listViewResult);

        return convertView;
    }

    class ViewHolder {
        private Switch mDeviceSwitch;
        private ListView listViewTerm, listViewResult;
    }

//    private Context mActivity;
////    private DeviceInfoResult mDeviceInfoResult;
//    private List<String> strLists;
//
////    public DeviceRulesListAdapter(Context activity, DeviceInfoResult deviceInfoResult) {
////        this.mActivity = activity;
////        this.mDeviceInfoResult = deviceInfoResult;
////    }
//    public DeviceRulesListAdapter(Context activity, List<String> strLists) {
//        this.mActivity = activity;
//        this.strLists = strLists;
//    }
//
//    private Handler m_handlerDeviceList;
//
//    private class MyStatusRuleReceiver extends StatusReceiver {
//        public Switch m_mainSwitch = null;
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (m_mainSwitch != null) {
//                m_mainSwitch.setChecked(MainActivity.m_mainDevice.getState() > 0);
//            }
//        }
//    }
//
//    private final MyStatusRuleReceiver m_StatusReceiver = new MyStatusRuleReceiver();
//
//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        if (MainActivity.m_mainDevice.getEnableEventBroadcast()) {
//            IntentFilter intentFilter = new IntentFilter(xltDevice.bciDeviceStatus);
//            intentFilter.setPriority(3);
//            recyclerView.getContext().registerReceiver(m_StatusReceiver, intentFilter);
//        }
//        super.onAttachedToRecyclerView(recyclerView);
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rules_list_item, parent, false);
//        return new DevicesRuleViewHolder(view);
//    }
//
//    @Override
//    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
//        if (m_handlerDeviceList != null) {
//            MainActivity.m_mainDevice.removeDeviceEventHandler(m_handlerDeviceList);
//        }
//        if (MainActivity.m_mainDevice.getEnableEventBroadcast()) {
//            recyclerView.getContext().unregisterReceiver(m_StatusReceiver);
//        }
//        super.onDetachedFromRecyclerView(recyclerView);
//    }
//
//    @Override
//    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
//        super.onViewDetachedFromWindow(holder);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        ((DevicesRuleViewHolder) holder).bindView(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return 3;
//    }
//
//    private class DevicesRuleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
////        private TextView rule_time_one;
////        private TextView rule_time_two;
////        private TextView ruleMsg;
//        private Switch mDeviceSwitch;
//        private RecyclerView listViewTerm, listViewResult;
//
//        public DevicesRuleViewHolder(View itemView) {
//            super(itemView);
////            rule_time_one = (TextView) itemView.findViewById(R.id.rule_time_one);
////            rule_time_two = (TextView) itemView.findViewById(R.id.rule_time_two);
////            ruleMsg = (TextView) itemView.findViewById(R.id.ruleMsg);
//            mDeviceSwitch = (Switch) itemView.findViewById(R.id.deviceSwitch);
//            listViewTerm = (RecyclerView) itemView.findViewById(R.id.lv_term);
//            listViewResult = (RecyclerView) itemView.findViewById(R.id.lv_result);
//            List<String> strListTerm = new ArrayList<String>();
//            strListTerm.add("09:00");
//            strListTerm.add("09:00");
//            strListTerm.add("09:00");
//            strListTerm.add("09:00");
//            List<String> strListResult = new ArrayList<String>();
//            strListResult.add("卧室灯开");
//            strListResult.add("卧室灯开");
//            strListResult.add("卧室灯开");
//            strListResult.add("卧室灯开");
//
//            listViewTerm.setAdapter(new TermAdapter(mActivity, strListTerm));
//            //根据innerlistview的高度机损parentlistview item的高度
////            setListViewHeightBasedOnChildren(listViewTerm);
//            listViewResult.setAdapter(new ResultAdapter(mActivity, strListResult));
//            //根据innerlistview的高度机损parentlistview item的高度
////            setListViewHeightBasedOnChildren(listViewResult);
//
//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
//
//            mDeviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    //ParticleAdapter.FastCallPowerSwitch(ParticleAdapter.DEFAULT_DEVICE_ID, isChecked);
//                    MainActivity.m_mainDevice.PowerSwitch(isChecked);
//                    //TODO 右侧开关控制
//                }
//            });
//        }
//
//        public void bindView(int position) {
////            String devicename = mDeviceInfoResult.rows.get(position).devicename;
////            rule_time_one.setText(MainActivity.deviceNames[position]);
////            rule_time_one.setText("09:00");
////            rule_time_two.setText("18:00");
////            ruleMsg.setText("卧室灯  开灯 亮度60 色温50");
//
//            if (position == 0) {
//                // Main device
//                mDeviceSwitch.setChecked(MainActivity.m_mainDevice.getState() > 0);
//                m_StatusReceiver.m_mainSwitch = mDeviceSwitch;
//
//                if (MainActivity.m_mainDevice.getEnableEventSendMessage()) {
//                    m_handlerDeviceList = new Handler() {
//                        public void handleMessage(Message msg) {
//                            int intValue = msg.getData().getInt("State", -255);
//                            if (intValue != -255) {
//                                mDeviceSwitch.setChecked(MainActivity.m_mainDevice.getState() > 0);
//                            }
//                        }
//                    };
//                    MainActivity.m_mainDevice.addDeviceEventHandler(m_handlerDeviceList);
//                }
//            }
//        }
//
//        @Override
//        public void onClick(View v) {
//            //点击事件 跳转到编辑设备页面
//            onFabPressed(AddControlRuleActivity.class);
//        }
//
//        @Override
//        public boolean onLongClick(View view) {
//            //TODO 长按事件  长按删除设备
////            showDeleteSceneDialog(position);
//            return true;
//        }
//    }
//
//    private void onFabPressed(Class activity) {
//        Intent intent = new Intent(mActivity, activity);
//        mActivity.startActivity(intent);
//    }
//
//    /**
//     * 删除规则
//     */
//    private void showDeleteSceneDialog(final int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//        builder.setTitle("删除规则提示");
//        builder.setMessage("确定删除此规则吗？");
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////                deleteScene(position);
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//
//        builder.show();
//    }
////    private void deleteScene(final int position) {
////        Rows mSceneInfo = mDeviceInfoResult.rows.get(position);
////        RequestDeleteScene.getInstance().deleteScene(getActivity(), mSceneInfo.id, new CommentRequstCallback() {
////            @Override
////            public void onCommentRequstCallbackSuccess() {
////                getActivity().runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        ToastUtil.showToast(getActivity(), "删除成功");
////                        mDeviceInfoResult.rows.remove(position);
////                        scenarioListAdapter.notifyDataSetChanged();
////                    }
////                });
////            }
////
////            @Override
////            public void onCommentRequstCallbackFail(int code, final String errMsg) {
////                mActivity.runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        ToastUtil.showToast(mActivity, "" + errMsg);
////                    }
////                });
////            }
////        });
////    }
//

    /**
     * @param listView 此方法是本次listview嵌套listview的核心方法：计算parentlistview item的高度。
     *                 如果不使用此方法，无论innerlistview有多少个item，则只会显示一个item。
     **/
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
