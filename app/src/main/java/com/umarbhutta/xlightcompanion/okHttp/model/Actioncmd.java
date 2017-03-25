package com.umarbhutta.xlightcompanion.okHttp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guangbinw on 2017/3/17.
 */

public class Actioncmd implements Serializable{
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
