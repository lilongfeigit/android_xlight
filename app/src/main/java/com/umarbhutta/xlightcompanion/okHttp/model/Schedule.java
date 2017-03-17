package com.umarbhutta.xlightcompanion.okHttp.model;

/**
 * Created by guangbinw on 2017/3/17.
 */

public class Schedule {
    public String ruleconditionname;
    public int devicenodeId;
    public String weekdays;
    public int hour;
    public int minute;
    public int status;

    @Override
    public String toString() {
        return "Schedule{" +
                "ruleconditionname='" + ruleconditionname + '\'' +
                ", devicenodeId=" + devicenodeId +
                ", weekdays='" + weekdays + '\'' +
                ", hour=" + hour +
                ", minute=" + minute +
                ", status=" + status +
                '}';
    }
}
