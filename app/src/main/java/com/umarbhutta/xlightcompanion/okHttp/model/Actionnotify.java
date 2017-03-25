package com.umarbhutta.xlightcompanion.okHttp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guangbinw on 2017/3/17.
 */

public class Actionnotify implements Serializable{
    public String msisdn;
    public String emailaddress;
    public String content;
    public String subject;
    public List<Alarm> alarm;

    @Override
    public String toString() {
        return "Actionnotify{" +
                "msisdn='" + msisdn + '\'' +
                ", content='" + content + '\'' +
                ", alarm=" + alarm +
                '}';
    }
}
