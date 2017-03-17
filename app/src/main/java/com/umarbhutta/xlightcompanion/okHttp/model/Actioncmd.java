package com.umarbhutta.xlightcompanion.okHttp.model;

import java.util.List;

/**
 * Created by guangbinw on 2017/3/17.
 */

public class Actioncmd {
    public int devicenodeId;
    public List<Actioncmdfield> actioncmdfield;

    @Override
    public String toString() {
        return "Actioncmd{" +
                "devicenodeId=" + devicenodeId +
                ", actioncmdfield=" + actioncmdfield +
                '}';
    }
}
