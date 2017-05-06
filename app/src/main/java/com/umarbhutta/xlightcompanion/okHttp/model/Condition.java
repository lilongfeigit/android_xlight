package com.umarbhutta.xlightcompanion.okHttp.model;

import java.io.Serializable;

/**
 * Created by guangbinw on 2017/3/17.
 */

public class Condition implements Serializable{
    public String ruleconditionname;
    public int devicenodeId;
    public String attribute;
    public String operator;
    public String rightValue;
    public int status;
    public int conditionType;
    public String temAbove;

    @Override
    public String toString() {
        return "Condition{" +
                "ruleconditionname='" + ruleconditionname + '\'' +
                ", devicenodeId=" + devicenodeId +
                ", attribute='" + attribute + '\'' +
                ", operator='" + operator + '\'' +
                ", rightValue='" + rightValue + '\'' +
                ", status=" + status +
                ", conditionType=" + conditionType +
                '}';
    }
}
