package edu.sdust.insapp.bean;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */

public class DeviceFaultBean {

    //设备台帐id
    private int deviceID;
    //问题现象id
    private String faultPhenoID;
    //问题部位ID列表
    private List<String> faultPositionIDs;
    //现场处理方式
    private String treatMethodID;
    //描述
    private String describe;
    //发现时间
    private Date discoveryTime;
    //引用的附件id
    private List<Integer> attachmentIDs;

    //专业
    private int major;

    public DeviceFaultBean() {
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public String getFaultPhenoID() {
        return faultPhenoID;
    }

    public void setFaultPhenoID(String faultPhenoID) {
        this.faultPhenoID = faultPhenoID;
    }

    public List<String> getFaultPositionIDs() {
        return faultPositionIDs;
    }

    public void setFaultPositionIDs(List<String> faultPositionIDs) {
        this.faultPositionIDs = faultPositionIDs;
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

    public Date getDiscoveryTime(Date date) {
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

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }
}
