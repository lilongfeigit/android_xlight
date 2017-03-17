package com.umarbhutta.xlightcompanion.okHttp.model;

/**
 * Created by guangbinw on 2017/3/17.
 */

public class Condition {
    public String ruleconditionname;
    public int devicenodeId;
    public String attribute;
    public String operator;
    public String rightValue;
    public int status;

    @Override
    public String toString() {
        return "Condition{" +
                "ruleconditionname='" + ruleconditionname + '\'' +
                ", devicenodeId=" + devicenodeId +
                ", attribute='" + attribute + '\'' +
                ", operator='" + operator + '\'' +
                ", rightValue='" + rightValue + '\'' +
                ", status=" + status +
                '}';
    }
}
