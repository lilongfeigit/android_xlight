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

public class UserMsgModifyAdapter extends RecyclerView.Adapter {


    private List<String> mUserMsgList;
    private Context mActivity;
    public  UserMsgModifyAdapter(Context activity,List<String> userMsgList){
        this.mUserMsgList = userMsgList;
        this.mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg_list_item, parent, false);
        return new UserMsgModifyAdapter.ModifyUserMsgListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((UserMsgModifyAdapter.ModifyUserMsgListViewHolder) holder).bindView(holder.itemView,position);
    }

    @Override
    public int getItemCount() {
        return mUserMsgList.size();
    }

    private class ModifyUserMsgListViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tvUserMsg;
        private ImageView imageViewHeadImg;

        public ModifyUserMsgListViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tvUserMsg = (TextView) itemView.findViewById(R.id.tv_user_msg);
            imageViewHeadImg = (ImageView) itemView.findViewById(R.id.iv_head_img);
        }

        public void bindView(final View itemView,final int position) {
            switch (position){
                case 0:
                    tv_name.setText("账号");
                    tvUserMsg.setText(mUserMsgList.get(position));
                    tvUserMsg.setVisibility(View.VISIBLE);
                    imageViewHeadImg.setVisibility(View.GONE);
                    break;
                case 1:
                    tv_name.setText("昵称");
                    tvUserMsg.setText(mUserMsgList.get(position));
                    tvUserMsg.setVisibility(View.VISIBLE);
                    imageViewHeadImg.setVisibility(View.GONE);
                    break;
                case 2:
                    tv_name.setText("头像");
                    tvUserMsg.setVisibility(View.GONE);
                    imageViewHeadImg.setVisibility(View.VISIBLE);
                    imageViewHeadImg.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.logo));
                    break;
                case 3:
                    tv_name.setText("性别");
                    tvUserMsg.setText(mUserMsgList.get(position));
                    tvUserMsg.setVisibility(View.VISIBLE);
                    imageViewHeadImg.setVisibility(View.GONE);
                    break;
                case 4:
                    tv_name.setText("出生年月");
                    tvUserMsg.setText(mUserMsgList.get(position));
                    tvUserMsg.setVisibility(View.VISIBLE);
                    imageViewHeadImg.setVisibility(View.GONE);
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
        void onItemClick(View view,int position);
    }

    private UserMsgModifyAdapter.OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(UserMsgModifyAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
