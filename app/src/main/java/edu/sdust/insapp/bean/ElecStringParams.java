package edu.sdust.insapp.bean;

import java.util.List;

public class ElecStringParams {
    //用户名
    private String username;
    //密码
    private String password;
    //派工单id
    private int orderID;
    //完工时间
    private String completeTime;
    //巡检结果
    private String insResult;
    //备注
    private String remark;
    //设备列表
    private List<DeviceMeaDataBean> deviceMeaDatas;
    //电气巡检附件列表，无照片
    private List<ElecInspEncloBean> elecInspEnclo;
    //问题列表
    private List<DeviceFaultBean> deviceFaults;
    //非设备问题列表
    private List<NonDeviceFaultBean> nonDeviceFaults;

    public ElecStringParams() {
    }

    public ElecStringParams(String username, String password, int orderID, String completeTime, String insResult, String remark, List<DeviceMeaDataBean> deviceMeaDatas, List<ElecInspEncloBean> elecInspEnclo, List<DeviceFaultBean> deviceFaults, List<NonDeviceFaultBean> nonDeviceFaults) {
        this.username = username;
        this.password = password;
        this.orderID = orderID;
        this.completeTime = completeTime;
        this.insResult = insResult;
        this.remark = remark;
        this.deviceMeaDatas = deviceMeaDatas;
        this.elecInspEnclo = elecInspEnclo;
        this.deviceFaults = deviceFaults;
        this.nonDeviceFaults = nonDeviceFaults;
    }

    public List<ElecInspEncloBean> getElecInspEnclo() {
        return elecInspEnclo;
    }

    public void setElecInspEnclo(List<ElecInspEncloBean> elecInspEnclo) {
        this.elecInspEnclo = elecInspEnclo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getInsResult() {
        return insResult;
    }

    public void setInsResult(String insResult) {
        this.insResult = insResult;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<DeviceMeaDataBean> getDeviceMeaDatas() {
        return deviceMeaDatas;
    }

    public void setDeviceMeaDatas(List<DeviceMeaDataBean> deviceMeaDatas) {
        this.deviceMeaDatas = deviceMeaDatas;
    }

    public List<DeviceFaultBean> getDeviceFaults() {
        return deviceFaults;
    }

    public void setDeviceFaults(List<DeviceFaultBean> deviceFaults) {
        this.deviceFaults = deviceFaults;
    }

    public List<NonDeviceFaultBean> getNonDeviceFaults() {
        return nonDeviceFaults;
    }

    public void setNonDeviceFaults(List<NonDeviceFaultBean> nonDeviceFaults) {
        this.nonDeviceFaults = nonDeviceFaults;
    }
}
