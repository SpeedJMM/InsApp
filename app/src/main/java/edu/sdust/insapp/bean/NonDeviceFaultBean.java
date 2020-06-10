package edu.sdust.insapp.bean;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/17.
 */

public class NonDeviceFaultBean {

    //问题现象id
    private String faultPhenoID;
    //问题发现地点
    private String faultPosition;
    //现场处理方式
    private String treatMethodID;
    //描述
    private String describe;
    //发现时间
    private Date discoveryTime;
    //引用的附件Id
    private List<Integer> attachmentIDs;
    //装置ID
    public int deviceID;
    //专业
    public int major;

    public NonDeviceFaultBean() {
    }

    public String getFaultPhenoID() {
        return faultPhenoID;
    }

    public void setFaultPhenoID(String faultPhenoID) {
        this.faultPhenoID = faultPhenoID;
    }

    public String getFaultPosition() {
        return faultPosition;
    }

    public void setFaultPosition(String faultPosition) {
        this.faultPosition = faultPosition;
    }

    public String getTreatMethodID() {
        return treatMethodID;
    }

    public void setTreatMethodID(String treatMethodID) {
        this.treatMethodID = treatMethodID;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Date getDiscoveryTime() {
        return discoveryTime;
    }

    public void setDiscoveryTime(Date discoveryTime) {
        this.discoveryTime = discoveryTime;
    }

    public List<Integer> getAttachmentIDs() {
        return attachmentIDs;
    }

    public void setAttachmentIDs(List<Integer> attachmentIDs) {
        this.attachmentIDs = attachmentIDs;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }
}
