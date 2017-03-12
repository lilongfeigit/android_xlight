package com.umarbhutta.xlightcompanion.Tools;

import android.content.Context;

import com.umarbhutta.xlightcompanion.okHttp.model.LoginResult;

/**
 * Created by guangbinw on 2017/3/12.
 */

public class UserUtils {

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
