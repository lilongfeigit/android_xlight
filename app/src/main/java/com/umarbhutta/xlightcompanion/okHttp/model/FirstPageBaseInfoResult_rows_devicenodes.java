package com.umarbhutta.xlightcompanion.okHttp.model;

import java.util.List;

/**
 * Created by guangbinw on 2017/3/13.
 */

public class FirstPageBaseInfoResult_rows_devicenodes {
    public int id;
    public int deviceId;
    public String devicenodename;
    public int nodeno;
    public String ison;
    public int brightness;
    public int cct;
    public String scenarioId;
    public String createdAt;
    public String updatedAt;
    //    public List<String> sensorsdata;
    public List<FirstPageBaseInfoResult_rows_devicenodes_devicerings> devicerings;
    public List<FirstPageBaseInfoResult_rows_devicenodes_ruleconditions> ruleconditions;

    @Override
    public String toString() {
        return "FirstPageBaseInfoResult_rows_devicenodes{" +
                "id=" + id +
                ", deviceId=" + deviceId +
                ", devicenodename='" + devicenodename + '\'' +
                ", nodeno=" + nodeno +
                ", ison='" + ison + '\'' +
                ", brightness=" + brightness +
                ", cct=" + cct +
                ", scenarioId='" + scenarioId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
//                ", sensorsdata=" + sensorsdata +
                ", devicerings=" + devicerings +
                ", ruleconditions=" + ruleconditions +
                '}';
    }
}
