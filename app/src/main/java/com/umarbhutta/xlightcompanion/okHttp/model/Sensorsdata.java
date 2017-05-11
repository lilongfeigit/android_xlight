package com.umarbhutta.xlightcompanion.okHttp.model;

import java.io.Serializable;

/**
 * Created by luomengxin on 2017/5/11.
 * 主页上展示的温度、设备列表等信息
 */

public class Sensorsdata implements Serializable {
    /**
     * 温度
     */
    public int DHTt;
    /**
     * 湿度
     */
    public int DHTh;
    /**
     * 亮度
     */
    public int ALS;

    @Override
    public String toString() {
        return "Sensorsdata{" +
                "DHTt=" + DHTt +
                ", DHTh=" + DHTh +
                ", ALS=" + ALS +
                '}';
    }
}
