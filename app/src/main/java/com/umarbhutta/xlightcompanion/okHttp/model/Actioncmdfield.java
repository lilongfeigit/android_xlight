package com.umarbhutta.xlightcompanion.okHttp.model;

import java.io.Serializable;

/**
 * Created by guangbinw on 2017/3/17.
 */

public class Actioncmdfield implements Serializable{
    public String cmd;
    public String paralist;

    @Override
    public String toString() {
        return "Actioncmdfield{" +
                "cmd='" + cmd + '\'' +
                ", paralist='" + paralist + '\'' +
                '}';
    }
}
