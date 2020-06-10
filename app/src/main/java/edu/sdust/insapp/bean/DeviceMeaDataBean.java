package edu.sdust.insapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */

public class DeviceMeaDataBean {

    //设备id
    private int deviceID;
    //是否巡检
    private boolean isSkip;
    //未巡检原因
    private String skipReason;
    //测点数据
    private List<PointInfoBean> pointInfos;
    //整机数据
    private List<CompMacInfoBean> compMacInfos;
    //测点采集时间
    private String pointInsTime;
    //整机数据采集时间
    private String compMacInsTime;

    public DeviceMeaDataBean() {
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public boolean isSkip() {
        return isSkip;
    }

    public void setSkip(boolean skip) {
        isSkip = skip;
    }

    public String getSkipReason() {
        return skipReason;
    }

    public void setSkipReason(String skipReason) {
        this.skipReason = skipReason;
    }

    public List<PointInfoBean> getPointInfos() {
        return pointInfos;
    }

    public void setPointInfos(List<PointInfoBean> pointInfos) {
        this.pointInfos = pointInfos;
    }

    public List<CompMacInfoBean> getCompMacInfos() {
        return compMacInfos;
    }

    public void setCompMacInfos(List<CompMacInfoBean> compMacInfos) {
        this.compMacInfos = compMacInfos;
    }

    public String getPointInsTime() {
        return pointInsTime;
    }

    public void setPointInsTime(String pointInsTime) {
        this.pointInsTime = pointInsTime;
    }

    public String getCompMacInsTime() {
        return compMacInsTime;
    }

    public void setCompMacInsTime(String compMacInsTime) {
        this.compMacInsTime = compMacInsTime;
    }
}
