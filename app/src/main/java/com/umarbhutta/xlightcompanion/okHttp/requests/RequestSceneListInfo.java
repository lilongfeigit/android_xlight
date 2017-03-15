package com.umarbhutta.xlightcompanion.okHttp.requests;

import android.content.Context;

import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.model.DeviceInfoResult;

/**
 * Created by guangbinw on 2017/3/14.
 * 场景列表信息
 */
public class RequestSceneListInfo implements HttpUtils.OnHttpRequestCallBack {
    private OnRequestFirstPageInfoCallback mOnRequestFirstPageInfoCallback;

    public static RequestSceneListInfo getInstance() {
        return new RequestSceneListInfo();
    }

    /**
     * 获取场景列表信息
     *
     * @param context
     */
    public void getSceneListInfo(Context context, OnRequestFirstPageInfoCallback mOnRequestFirstPageInfoCallback) {
        this.mOnRequestFirstPageInfoCallback = mOnRequestFirstPageInfoCallback;
        HttpUtils.getInstance().getRequestInfo(NetConfig.URL_SCENE_LIST + UserUtils.getUserInfo(context).getAccess_token(), DeviceInfoResult.class, this);
    }

    @Override
    public void onHttpRequestSuccess(Object result) {
        DeviceInfoResult info = (DeviceInfoResult) result;
        if (null != mOnRequestFirstPageInfoCallback) {
            mOnRequestFirstPageInfoCallback.onRequestFirstPageInfoSuccess(info.data);
        }
    }

    @Override
    public void onHttpRequestFail(int code, String errMsg) {
        if (null != mOnRequestFirstPageInfoCallback) {
            mOnRequestFirstPageInfoCallback.onRequestFirstPageInfoFail(code, errMsg);
        }
    }

    public interface OnRequestFirstPageInfoCallback {
        void onRequestFirstPageInfoSuccess(DeviceInfoResult mDeviceInfoResult);

        void onRequestFirstPageInfoFail(int code, String errMsg);
    }


}
