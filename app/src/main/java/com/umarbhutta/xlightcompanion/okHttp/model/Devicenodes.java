package com.umarbhutta.xlightcompanion.okHttp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guangbinw on 2017/3/13.
 */

public class Devicenodes implements Serializable{
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
    public List<Devicerings> devicerings;
    public List<Ruleconditions> ruleconditions;

    @Override
    public String toString() {
        return "Devicenodes{" +
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
