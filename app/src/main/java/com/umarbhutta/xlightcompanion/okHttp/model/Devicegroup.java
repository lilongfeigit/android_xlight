package com.umarbhutta.xlightcompanion.okHttp.model;

/**
 * Created by guangbinw on 2017/3/13.
 */

public class Devicegroup {
    public int id;
    public String devicegroupname;
    public String createdAt;
    public String updatedAt;

    @Override
    public String toString() {
        return "Devicegroup{" +
                "id=" + id +
                ", devicegroupname='" + devicegroupname + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
