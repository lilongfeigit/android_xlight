package com.umarbhutta.xlightcompanion.okHttp.model;

import java.io.Serializable;

/**
 * Created by guangbinw on 2017/3/17.
 */

public class Schedule implements Serializable{
    public String ruleconditionname;
    public int devicenodeId;
    public String weekdays;
    public int hour;
    public int minute;
    public int status;
    public int isrepeat;//0代表不重复，1代表重复
    public String scheduleName;

    @Override
    public String toString() {
        return "Schedule{" +
                "ruleconditionname='" + ruleconditionname + '\'' +
                ", devicenodeId=" + devicenodeId +
                ", weekdays='" + weekdays + '\'' +
                ", hour=" + hour +
                ", minute=" + minute +
                ", status=" + status +
                ", isrepeat=" + isrepeat +
                ", scheduleName='" + scheduleName + '\'' +
                '}';
    }
}
