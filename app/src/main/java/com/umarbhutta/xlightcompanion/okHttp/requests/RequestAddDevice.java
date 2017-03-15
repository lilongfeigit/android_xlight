package com.umarbhutta.xlightcompanion.okHttp.requests;

import android.content.Context;

import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.model.AddDeviceResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by guangbinw on 2017/3/14.
 * 添加设备接口
 */
public class RequestAddDevice implements HttpUtils.OnHttpRequestCallBack {

    private Context context;
    private OnAddDeviceCallBack mOnAddDeviceCallBack;

    public static RequestAddDevice getInstance() {
        return new RequestAddDevice();
    }

    /**
     * 添加设备
     *
     * @param context
     * @param devicename
     * @param maindevice
     * @param status
     * @param ison
     * @param mOnAddDeviceCallBack
     */
    public void addDevice(Context context, String devicename, int maindevice, int status, int ison, OnAddDeviceCallBack mOnAddDeviceCallBack) {
        this.context = context;
        this.mOnAddDeviceCallBack = mOnAddDeviceCallBack;
        if (UserUtils.isLogin(context)) {

            try {
                JSONObject object = new JSONObject();
                object.put("devicename", devicename);
                object.put("userId", UserUtils.getUserInfo(context).getId());
                object.put("maindevice", maindevice);
                object.put("status", status);
                object.put("ison", ison);
                HttpUtils.getInstance().postRequestInfo(NetConfig.URL_ADD_DEVICE + UserUtils.getUserInfo(context).getAccess_token(),
                        object.toString(), AddDeviceResult.class, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onHttpRequestSuccess(Object result) {
        AddDeviceResult infos = (AddDeviceResult) result;
        if (null != mOnAddDeviceCallBack) {
            mOnAddDeviceCallBack.mOnAddDeviceCallBackSuccess(infos.data);
        }
    }

    @Override
    public void onHttpRequestFail(int code, String errMsg) {
        if (null != mOnAddDeviceCallBack) {
            mOnAddDeviceCallBack.mOnAddDeviceCallBackFail(code, errMsg);
        }
    }

    public interface OnAddDeviceCallBack {
        void mOnAddDeviceCallBackFail(int code, String errMsg);

        void mOnAddDeviceCallBackSuccess(AddDeviceResult mAddDeviceResult);
    }


}
