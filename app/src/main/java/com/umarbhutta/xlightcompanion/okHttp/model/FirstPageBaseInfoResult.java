package com.umarbhutta.xlightcompanion.okHttp.model;

import java.util.List;

/**
 * Created by guangbinw on 2017/3/13.
 * 首页基本信息
 */

public class FirstPageBaseInfoResult {

    public int code;
    public String msg;

    public FirstPageBaseInfoResult data;

    public int count;

    public List<FirstPageBaseInfoResult_rows> rows;

    @Override
    public String toString() {
        return "FirstPageBaseInfoResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", count=" + count +
                ", rows=" + rows +
                '}';
    }
}
