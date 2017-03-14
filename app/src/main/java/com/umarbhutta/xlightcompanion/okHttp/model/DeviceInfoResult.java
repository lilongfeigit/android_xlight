package com.umarbhutta.xlightcompanion.okHttp.model;

import java.util.List;

/**
 * Created by guangbinw on 2017/3/13.
 * 设备信息
 */

public class DeviceInfoResult {

    public int code;
    public String msg;

    public DeviceInfoResult data;

    public int count;

    public List<Rows> rows;

    @Override
    public String toString() {
        return "DeviceInfoResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", count=" + count +
                ", rows=" + rows +
                '}';
    }
}
