package com.umarbhutta.xlightcompanion.control.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/23.
 */

public class SelectTime implements Serializable{

    public SelectTime(String name, boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }

    public String name;
    public boolean isSelect;
}
