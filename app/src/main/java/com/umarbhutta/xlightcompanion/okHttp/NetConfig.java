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

    /**
     * 网络异常
     */
    public static final int ERROR_NET_ERROT = -101;


    public static final String ERROR_PARSE_MSG = "数据解析异常";


    /**
     * 登录
     */
    public static final String URL_LOGIN = "http://123.207.166.211:8080/users/login";
    /**
     * 注册
     */
    public static final String URL_REGISTER = "http://123.207.166.211:8080/users";


    /**
     * 首页基本信息
     */
    public static final String URL_FIRST_PAGE_INFO = "http://123.207.166.211:8080/devices/?access_token=";
    /**
     * 修改密码
     */
    public static final String URL_MODIFY_PWD = "http://123.207.166.211:8080/users/2/resetpassword?access_token=";
}
