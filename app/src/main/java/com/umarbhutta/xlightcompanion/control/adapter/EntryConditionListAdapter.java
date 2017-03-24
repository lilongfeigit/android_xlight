package com.umarbhutta.xlightcompanion.control.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/5.
 */

public class EntryConditionListAdapter extends RecyclerView.Adapter {

    private Context mActivity;
    private List<Integer> mIntList;
    private List<String> mSettingStr;
    public EntryConditionListAdapter(Context activity, List<String> settingStr,List<Integer> intList){
        this.mActivity = activity;
        this.mIntList = intList;
        this.mSettingStr = settingStr;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_condition_list_item, parent, false);
        return new EntryConditionListAdapter.SettingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((EntryConditionListAdapter.SettingListViewHolder) holder).bindView(holder.itemView,position);
    }

    @Override
    public int getItemCount() {
        return mSettingStr.size();
    }

    private class SettingListViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_setting_name;
        private ImageView iv_lift_img;

        public SettingListViewHolder(View itemView) {
            super(itemView);
            tv_setting_name = (TextView) itemView.findViewById(R.id.tv_setting_name);
            iv_lift_img = (ImageView) itemView.findViewById(R.id.iv_lift_img);
        }

        public void bindView(final View itemView,final int position) {
            tv_setting_name.setText(mSettingStr.get(position));
            iv_lift_img.setImageResource(mIntList.get(position));

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
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
