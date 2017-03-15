package com.umarbhutta.xlightcompanion.okHttp.model;

/**
 * Created by guangbinw on 2017/3/15.
 * 添加设备接口返回
 */

public class AddDeviceResult {
    public int code;
    public String msg;
    public AddDeviceResult data;

    public int id;
    public String devicename;
    public int userId;
    public int maindevice;
    public int status;
    public int ison;
    public String updatedAt;
    public String createdAt;

    @Override
    public String toString() {
        return "AddDeviceResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", id=" + id +
                ", devicename='" + devicename + '\'' +
                ", userId=" + userId +
                ", maindevice=" + maindevice +
                ", status=" + status +
                ", ison=" + ison +
                ", updatedAt='" + updatedAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
