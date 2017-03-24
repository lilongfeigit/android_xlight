package com.umarbhutta.xlightcompanion.control.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/24.
 */

public class Ruleconditions implements Serializable{

    public List<Ruleconditions> data;

    public List<Activities> activities;
    public List<Voice> voice;
    public List<LeaveHome> leavehome;
    public List<GoHome> gohome;
    public List<Gas> gas;
    public List<Temperature> temperature;

    @Override
    public String toString() {
        return "Ruleconditions{" +
                "activities=" + activities +
                ", voice=" + voice +
                ", leavehome=" + leavehome +
                ", gohome=" + gohome +
                ", gas=" + gas +
                ", temperature=" + temperature +
                '}';
    }
}
