package com.umarbhutta.xlightcompanion.okHttp;

import com.google.gson.Gson;

/**
 * Created by guangbinw on 2017/3/12.
 */
public class HttpUtils extends BaseHttp {


    private final Gson gson;
    private OnHttpRequestCallBack mOnHttpRequestCallBack;
    private Class mClass;

    public static HttpUtils getInstance() {
        return new HttpUtils();
    }

    private HttpUtils() {
        gson = new Gson();
    }

    /**
     * @param isGetMethod            是否为get请求，是true，否false
     * @param url                    请求地址
     * @param jsonParam              请求参数json形式
     * @param mClass                 请求结果直接解析成指定的对象，填null为不做任何处理返回字符串
     * @param mOnHttpRequestCallBack 用于接受服务器返回数据
     */
    public void requestInfo(boolean isGetMethod, String url, String jsonParam, Class mClass, OnHttpRequestCallBack mOnHttpRequestCallBack) {

        this.mClass = mClass;
        this.mOnHttpRequestCallBack = mOnHttpRequestCallBack;

        if (isGetMethod) {
            getData(url);
        } else {
            postData(url, jsonParam);
        }

    }

    @Override
    public void okOnError(String errResult) {
        if (null != mOnHttpRequestCallBack) {
            mOnHttpRequestCallBack.onHttpRequestFail(NetConfig.ERROR_PARSE, "数据解析异常");
        }
    }

    @Override
    public void okOnResponse(String result) {
        if (null != mOnHttpRequestCallBack) {
            if (null != mClass) {
                Object responseResult = gson.fromJson(result, mClass);
                mOnHttpRequestCallBack.onHttpRequestSuccess(responseResult);
            }
        }
    }

    public interface OnHttpRequestCallBack {
        void onHttpRequestSuccess(Object result);

        void onHttpRequestFail(int code, String errMsg);
    }

}
