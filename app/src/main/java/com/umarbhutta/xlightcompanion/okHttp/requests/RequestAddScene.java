package com.umarbhutta.xlightcompanion.okHttp.requests;

import android.content.Context;

import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.requests.imp.CommentRequstCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by guangbinw on 2017/3/14.
 * 添加场景
 */
public class RequestAddScene implements HttpUtils.OnHttpRequestCallBack {

    private Context context;
    private CommentRequstCallback mCommentRequstCallback;

    public static RequestAddScene getInstance() {
        return new RequestAddScene();
    }

    /**
     * 添加场景
     *
     * @param context
     * @param scenarioname
     * @param brightness
     * @param cct
     * @param type
     * @param mCommentRequstCallback
     */
    public void addScene(Context context, String scenarioname, int brightness, int cct, int type, CommentRequstCallback mCommentRequstCallback) {
        this.context = context;
        this.mCommentRequstCallback = mCommentRequstCallback;
        if (UserUtils.isLogin(context)) {

            try {
                JSONObject object = new JSONObject();
                object.put("scenarioname", scenarioname);
                object.put("userId", UserUtils.getUserInfo(context).getId());
                object.put("brightness", brightness);
                object.put("cct", cct);
                object.put("type", type);
                HttpUtils.getInstance().postRequestInfo(NetConfig.URL_ADD_SCENE + UserUtils.getUserInfo(context).getAccess_token(),
                        object.toString(), null, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onHttpRequestSuccess(Object result) {
        if (null != mCommentRequstCallback) {
            mCommentRequstCallback.onCommentRequstCallbackSuccess();
        }
    }

    @Override
    public void onHttpRequestFail(int code, String errMsg) {
        if (null != mCommentRequstCallback) {
            mCommentRequstCallback.onCommentRequstCallbackFail(code, errMsg);
        }
    }


}
