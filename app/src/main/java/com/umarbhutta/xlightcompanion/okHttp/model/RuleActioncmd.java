package com.umarbhutta.xlightcompanion.okHttp.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/25.
 */

public class RuleActioncmd implements Serializable {
    public int id;
    public int ruleId;
    public int devicenodeId;
    public int sensorId;
    public String createdAt;
    public String updatedAt;

    @Override
    public String toString() {
        return "RuleActioncmd{" +
                "id=" + id +
                ", ruleId=" + ruleId +
                ", devicenodeId=" + devicenodeId +
                ", sensorId=" + sensorId +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
