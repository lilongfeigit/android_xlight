package com.umarbhutta.xlightcompanion.settings;

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

public class FastBindingAdapter extends RecyclerView.Adapter {


    private List<String> mFaseBings;
    private Context mActivity;
    public FastBindingAdapter(Context activity, List<String> faseBings){
        this.mFaseBings = faseBings;
        this.mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fase_bing_list_item, parent, false);
        return new FastBindingAdapter.FastBindingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FastBindingAdapter.FastBindingListViewHolder) holder).bindView(holder.itemView,position);
    }

    @Override
    public int getItemCount() {
        return mFaseBings.size();
    }

    private class FastBindingListViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_fast_bind;

        public FastBindingListViewHolder(View itemView) {
            super(itemView);
            tv_fast_bind = (TextView) itemView.findViewById(R.id.tv_fast_bind);
        }

        public void bindView(final View itemView,final int position) {
            tv_fast_bind.setText(mFaseBings.get(position));
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

    private FastBindingAdapter.OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(FastBindingAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
