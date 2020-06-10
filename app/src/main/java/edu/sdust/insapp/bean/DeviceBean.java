package edu.sdust.insapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 */

public class DeviceBean {
    //测点个数
    private int pointSum;
    //设备id
    private  int deviceId;
    //设备位号
    private String deviceTag;

    //设备巡检状态
    private int status;
    //整机数据列表
    private List<CompMacBean> compMacs;


    public DeviceBean(int pointSum, int deviceId, String deviceTag, List<CompMacBean> compMacs) {
        this.pointSum = pointSum;
        this.deviceId = deviceId;
        this.deviceTag = deviceTag;
        status = 0;
        this.compMacs = compMacs;
    }

    public int getPointSum() {
        return pointSum;
    }

    public void setPointSum(int pointSum) {
        this.pointSum = pointSum;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceTag() {
        return deviceTag;
    }

    public void setDeviceTag(String deviceTag) {
        this.deviceTag = deviceTag;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<CompMacBean> getCompMacs() {
        return compMacs;
    }

    public void setCompMacs(List<CompMacBean> compMacs) {
        this.compMacs = compMacs;
    }
}
