package com.umarbhutta.xlightcompanion.okHttp.model;

/**
 * Created by guangbinw on 2017/3/13.
 */

public class FirstPageBaseInfoResult_rows_devicegroup {
    public int id;
    public String devicegroupname;
    public String createdAt;
    public String updatedAt;

    @Override
    public String toString() {
        return "FirstPageBaseInfoResult_rows_devicegroup{" +
                "id=" + id +
                ", devicegroupname='" + devicegroupname + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
