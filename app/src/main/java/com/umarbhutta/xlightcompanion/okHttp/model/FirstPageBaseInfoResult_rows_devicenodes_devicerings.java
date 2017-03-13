package com.umarbhutta.xlightcompanion.okHttp.model;

/**
 * Created by guangbinw on 2017/3/13.
 */

public class FirstPageBaseInfoResult_rows_devicenodes_devicerings {
    public int id;
    public int devicenodeId;
    public int deviceId;
    public int nodeno;
    public String ringname;
    public int ringno;
    public String ison;
    public int brightness;
    public int R;
    public int G;
    public int B;
    public int W;
    public String createdAt;
    public String updatedAt;

    @Override
    public String toString() {
        return "FirstPageBaseInfoResult_rows_devicenodes_devicerings{" +
                "id=" + id +
                ", devicenodeId=" + devicenodeId +
                ", deviceId=" + deviceId +
                ", nodeno=" + nodeno +
                ", ringname='" + ringname + '\'' +
                ", ringno=" + ringno +
                ", ison='" + ison + '\'' +
                ", brightness=" + brightness +
                ", R=" + R +
                ", G=" + G +
                ", B=" + B +
                ", W=" + W +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
