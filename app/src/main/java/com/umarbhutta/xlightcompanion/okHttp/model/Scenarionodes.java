package com.umarbhutta.xlightcompanion.okHttp.model;

/**
 * Created by guangbinw on 2017/3/15.
 */

public class Scenarionodes {
    public int id;
    public int scenarioId;
    public int brightness;
    public int R;
    public int G;
    public int B;
    public String W;
    public int cct;
    public String createdAt;
    public String updatedAt;

    @Override
    public String toString() {
        return "Scenarionodes{" +
                "id=" + id +
                ", scenarioId=" + scenarioId +
                ", brightness=" + brightness +
                ", R=" + R +
                ", G=" + G +
                ", B=" + B +
                ", W='" + W + '\'' +
                ", cct=" + cct +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
