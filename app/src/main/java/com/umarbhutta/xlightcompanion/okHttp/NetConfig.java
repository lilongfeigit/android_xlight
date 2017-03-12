package com.umarbhutta.xlightcompanion.okHttp;

/**
 * Created by guangbinw on 2017/3/12.
 */

public class NetConfig {

    /**
     * 正式版开关
     */
    public static final boolean isDebug = true;

    /**
     * 数据解析错误
     */
    public static final int ERROR_PARSE = -100;
    public static final String ERROR_PARSE_MSG = "数据解析异常";


    /**
     * 登录
     */
    public static final String URL_LOGIN = "http://123.207.166.211:8080/users/login";
    /**
     * 注册
     */
    public static final String URL_REGISTER = "http://123.207.166.211:8080/users";
}
