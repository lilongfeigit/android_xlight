package com.umarbhutta.xlightcompanion.settings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/5.
 */

public class ShakeAdapter extends RecyclerView.Adapter {


    private List<String> mShakes;
    private Context mActivity;
    public ShakeAdapter(Context activity, List<String> shake){
        this.mShakes = shake;
        this.mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shake_list_item, parent, false);
        return new ShakeAdapter.ShakeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ShakeAdapter.ShakeListViewHolder) holder).bindView(holder.itemView,position);
    }

    @Override
    public int getItemCount() {
        return mShakes.size();
    }

    private class ShakeListViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tvMsg;
        private Switch shakeSwitch;

        public ShakeListViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            shakeSwitch = (Switch) itemView.findViewById(R.id.shake_switch);
        }

        public void bindView(final View itemView,final int position) {
            switch (position){
                case 0:
                    tv_name.setText("设备（选择摇一摇默认设备）");
                    tvMsg.setVisibility(View.VISIBLE);
                    shakeSwitch.setVisibility(View.GONE);
                    break;
                case 1:
                    tv_name.setText("开关切换");
                    tvMsg.setVisibility(View.GONE);
                    shakeSwitch.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tv_name.setText("场景切换");
                    tvMsg.setVisibility(View.GONE);
                    shakeSwitch.setVisibility(View.VISIBLE);
                    break;

            }

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

    private ShakeAdapter.OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(ShakeAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
