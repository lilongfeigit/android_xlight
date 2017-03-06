package com.umarbhutta.xlightcompanion.settings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.umarbhutta.xlightcompanion.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/5.
 */

public class SettingListAdapter extends RecyclerView.Adapter {

    private Context mActivity;
    private List<String> mSettingStr;
    public SettingListAdapter(Context activity,List<String> settingStr){
        this.mActivity = activity;
        this.mSettingStr = settingStr;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_list_item, parent, false);
        return new SettingListAdapter.SettingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SettingListAdapter.SettingListViewHolder) holder).bindView(holder.itemView,position);
    }

    @Override
    public int getItemCount() {
        return mSettingStr.size();
    }

    private class SettingListViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_setting_name;

        public SettingListViewHolder(View itemView) {
            super(itemView);
            tv_setting_name = (TextView) itemView.findViewById(R.id.tv_setting_name);
        }

        public void bindView(final View itemView,final int position) {
            tv_setting_name.setText(mSettingStr.get(position));

            //如果设置了回调，就设置点击事件
            if (mOnItemClickListener != null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(itemView,position);
                    }
                });
            }
        }

    }
    /** * ItemClick的回调接口 */
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
