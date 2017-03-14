package com.umarbhutta.xlightcompanion.okHttp.model;

import java.util.List;

/**
 * Created by guangbinw on 2017/3/13.
 */

public class Rows {
    public int id;
    public String devicename;
    public int userId;
    public String iphoneidentify;
    public String androididentify;
    public int maindevice;
    public int status;
    public String ison;
    public String createdAt;
    public String updatedAt;
    public int devicegroupId;

    public Devicegroup devicegroup;
    public DeviceUser user;
    public List<Devicenodes> devicenodes;


    @Override
    public String toString() {
        return "Rows{" +
                "id=" + id +
                ", devicename='" + devicename + '\'' +
                ", userId=" + userId +
                ", iphoneidentify='" + iphoneidentify + '\'' +
                ", androididentify='" + androididentify + '\'' +
                ", maindevice=" + maindevice +
                ", status=" + status +
                ", ison='" + ison + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", devicegroupId=" + devicegroupId +
                ", devicegroup=" + devicegroup +
                ", user=" + user +
                ", devicenodes=" + devicenodes +
                '}';
    }
}
