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
     * get请求
     *
     * @param url                    请求地址
     * @param mClass                 请求结果直接解析成指定的对象，填null为不做任何处理返回字符串
     * @param mOnHttpRequestCallBack 用于接受服务器返回数据
     */
    public void getRequestInfo(String url, Class mClass, OnHttpRequestCallBack mOnHttpRequestCallBack) {

        this.mClass = mClass;
        this.mOnHttpRequestCallBack = mOnHttpRequestCallBack;

        getData(url);

    }

    /**
     * post请求
     *
     * @param url                    请求地址
     * @param jsonParam              请求参数json形式
     * @param mClass                 请求结果直接解析成指定的对象，填null为不做任何处理返回字符串
     * @param mOnHttpRequestCallBack 用于接受服务器返回数据
     */
    public void postRequestInfo(String url, String jsonParam, Class mClass, OnHttpRequestCallBack mOnHttpRequestCallBack) {

        this.mClass = mClass;
        this.mOnHttpRequestCallBack = mOnHttpRequestCallBack;

        postData(url, jsonParam);

    }


    /**
     * put请求
     *
     * @param url                    请求地址
     * @param jsonParam              请求参数json形式
     * @param mClass                 请求结果直接解析成指定的对象，填null为不做任何处理返回字符串
     * @param mOnHttpRequestCallBack 用于接受服务器返回数据
     */
    public void putRequestInfo(String url, String jsonParam, Class mClass, OnHttpRequestCallBack mOnHttpRequestCallBack) {

        this.mClass = mClass;
        this.mOnHttpRequestCallBack = mOnHttpRequestCallBack;

        putData(url, jsonParam);

    }

    @Override
    public void okOnError(String errResult) {
        if (null != mOnHttpRequestCallBack) {
            mOnHttpRequestCallBack.onHttpRequestFail(NetConfig.ERROR_PARSE, "网络异常，请检查网络");
        }
    }

    @Override
    public void okOnResponse(String result) {

        try {
            if (null != mOnHttpRequestCallBack) {
                if (null != mClass) {
                    Object responseResult = null;
                    responseResult = gson.fromJson(result, mClass);
                    mOnHttpRequestCallBack.onHttpRequestSuccess(responseResult);
                } else {
                    mOnHttpRequestCallBack.onHttpRequestSuccess(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mOnHttpRequestCallBack.onHttpRequestFail(NetConfig.ERROR_PARSE, NetConfig.ERROR_PARSE_MSG);
        }
    }

    public interface OnHttpRequestCallBack {
        void onHttpRequestSuccess(Object result);

        void onHttpRequestFail(int code, String errMsg);
    }

}
