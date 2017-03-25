package com.umarbhutta.xlightcompanion.okHttp.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/25.
 */

public class Ruleactionnotify implements Serializable {
    public int id;
    public int ruleId;
    public String msisdn;
    public String content;
    public String emailaddress;
    public String subject;
    public String createdAt;
    public String updatedAt;

    @Override
    public String toString() {
        return "Ruleactionnotify{" +
                "id=" + id +
                ", ruleId=" + ruleId +
                ", msisdn='" + msisdn + '\'' +
                ", content='" + content + '\'' +
                ", emailaddress='" + emailaddress + '\'' +
                ", subject='" + subject + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
