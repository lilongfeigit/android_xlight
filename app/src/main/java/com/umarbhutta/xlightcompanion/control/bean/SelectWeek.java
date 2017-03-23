package com.umarbhutta.xlightcompanion.control.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/23.
 */

public class SelectWeek implements Serializable {

    public SelectWeek(String name, boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }

    public String name;
    public boolean isSelect;
}
