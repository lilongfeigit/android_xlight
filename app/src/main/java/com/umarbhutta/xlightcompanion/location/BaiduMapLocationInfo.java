package com.umarbhutta.xlightcompanion.location;

/**
 * create by：guangbinw on 2016/11/28 15:45
 * email：guangbingwang@126.com
 */
public class BaiduMapLocationInfo {
    public double latitude;
    public double longitude;
    public String city;
    public String address;

    @Override
    public String toString() {
        return "BaiduMapLocationInfo{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
