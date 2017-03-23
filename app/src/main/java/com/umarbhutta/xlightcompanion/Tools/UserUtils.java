package com.umarbhutta.xlightcompanion.Tools;

import android.content.Context;
import android.text.TextUtils;

import com.umarbhutta.xlightcompanion.okHttp.model.LoginResult;

/**
 * Created by guangbinw on 2017/3/12.
 */

public class UserUtils {

    /**
     * 判断是否登录
     *
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        LoginResult result = (LoginResult) SharedPreferencesUtils.getObject(context, SharedPreferencesUtils.KEY__USERINFO, null);
        if (null == result || TextUtils.isEmpty(result.access_token) || TextUtils.isEmpty(result.username)) {
            return false;
        }
        return true;
    }

    /**
     * 设置用户信息
     *
     * @param context
     * @param userInfo
     */
    public static void saveUserInfo(Context context, LoginResult userInfo) {
        SharedPreferencesUtils.putObject(context, SharedPreferencesUtils.KEY__USERINFO, userInfo);
    }

    /**
     * 获取用户信息
     *
     * @param context
     * @return
     */
    public static LoginResult getUserInfo(Context context) {
        return (LoginResult) SharedPreferencesUtils.getObject(context, SharedPreferencesUtils.KEY__USERINFO, null);
    }

}
