package com.umarbhutta.xlightcompanion.okHttp.model;

/**
 * Created by guangbinw on 2017/3/13.
 */

public class FirstPageBaseInfoResult_rows_devicenodes_ruleconditions {
    public int id;
    public String ruleconditionname;
    public int ruleId;
    public int devicenodeId;
    public String sensorId;
    public String attribute;
    public String operator;
    public String rightValue;
    public String starttime;
    public String endtime;
    public String weekdays;
    public String hour;
    public String minute;
    public String status;
    public String createdAt;
    public String updatedAt;

    @Override
    public String toString() {
        return "FirstPageBaseInfoResult_rows_devicenodes_ruleconditions{" +
                "id=" + id +
                ", ruleconditionname='" + ruleconditionname + '\'' +
                ", ruleId=" + ruleId +
                ", devicenodeId=" + devicenodeId +
                ", sensorId='" + sensorId + '\'' +
                ", attribute='" + attribute + '\'' +
                ", operator='" + operator + '\'' +
                ", rightValue='" + rightValue + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", weekdays='" + weekdays + '\'' +
                ", hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}