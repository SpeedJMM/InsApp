package edu.sdust.insapp.bean;

import java.io.Serializable;
import java.util.List;

public class NightKeepWorkAndStaffBean implements Serializable {
    private Integer nightkeepworkworkid;//夜间保运工作id
    private Integer keepworkworkrecordid;//保运工作记录id
    private String maintenancemajorclscode;//维修专业代码
    private String maintenancemajorclsname;//维修专业名称
    private String nightkeepworktype;//夜间保运类型
    private String nightkeepworkworkcontent;//夜间保运工作内容
    private String nightkeepworkworkstarttime;//夜间保运工作开始时间
    private String nightkeepworkworkendtime;//夜间保运工作结束时间
    private Boolean nightkeepworkworkfinishsituation;//夜间保运工作完成情况
    private String nightkeepworkworkremark;
    private List<Integer> oveparstaffidList;//班组人员id列表
    private String NightKeepWorkWorkStaff;//夜间保运工作人员
    private List<Integer> eqptAccntIds;//设备台账id列表
    private String NightKeepWorkAttribValue;//夜间保运设备位号

    public NightKeepWorkAndStaffBean() {
    }



    public Integer getNightkeepworkworkid() {
        return nightkeepworkworkid;
    }

    public void setNightkeepworkworkid(Integer nightkeepworkworkid) {
        this.nightkeepworkworkid = nightkeepworkworkid;
    }

    public Integer getKeepworkworkrecordid() {
        return keepworkworkrecordid;
    }

    public void setKeepworkworkrecordid(Integer keepworkworkrecordid) {
        this.keepworkworkrecordid = keepworkworkrecordid;
    }

    public String getMaintenancemajorclscode() {
        return maintenancemajorclscode;
    }

    public void setMaintenancemajorclscode(String maintenancemajorclscode) {
        this.maintenancemajorclscode = maintenancemajorclscode;
    }

    public String getMaintenancemajorclsname() {
        return maintenancemajorclsname;
    }

    public void setMaintenancemajorclsname(String maintenancemajorclsname) {
        this.maintenancemajorclsname = maintenancemajorclsname;
    }

    public String getNightkeepworktype() {
        return nightkeepworktype;
    }

    public void setNightkeepworktype(String nightkeepworktype) {
        this.nightkeepworktype = nightkeepworktype;
    }

    public String getNightkeepworkworkcontent() {
        return nightkeepworkworkcontent;
    }

    public void setNightkeepworkworkcontent(String nightkeepworkworkcontent) {
        this.nightkeepworkworkcontent = nightkeepworkworkcontent;
    }

    public String getNightkeepworkworkstarttime() {
        return nightkeepworkworkstarttime;
    }

    public void setNightkeepworkworkstarttime(String nightkeepworkworkstarttime) {
        this.nightkeepworkworkstarttime = nightkeepworkworkstarttime;
    }

    public String getNightkeepworkworkendtime() {
        return nightkeepworkworkendtime;
    }

    public void setNightkeepworkworkendtime(String nightkeepworkworkendtime) {
        this.nightkeepworkworkendtime = nightkeepworkworkendtime;
    }

    public Boolean getNightkeepworkworkfinishsituation() {
        return nightkeepworkworkfinishsituation;
    }

    public void setNightkeepworkworkfinishsituation(Boolean nightkeepworkworkfinishsituation) {
        this.nightkeepworkworkfinishsituation = nightkeepworkworkfinishsituation;
    }

    public String getNightkeepworkworkremark() {
        return nightkeepworkworkremark;
    }

    public void setNightkeepworkworkremark(String nightkeepworkworkremark) {
        this.nightkeepworkworkremark = nightkeepworkworkremark;
    }

    public List<Integer> getOveparstaffidList() {
        return oveparstaffidList;
    }

    public void setOveparstaffidList(List<Integer> oveparstaffidList) {
        this.oveparstaffidList = oveparstaffidList;
    }

    public String getNightKeepWorkWorkStaff() {
        return NightKeepWorkWorkStaff;
    }

    public void setNightKeepWorkWorkStaff(String nightKeepWorkWorkStaff) {
        NightKeepWorkWorkStaff = nightKeepWorkWorkStaff;
    }

    public List<Integer> getEqptAccntIds() {
        return eqptAccntIds;
    }

    public void setEqptAccntIds(List<Integer> eqptAccntIds) {
        this.eqptAccntIds = eqptAccntIds;
    }

    public String getNightKeepWorkAttribValue() {
        return NightKeepWorkAttribValue;
    }

    public void setNightKeepWorkAttribValue(String nightKeepWorkAttribValue) {
        NightKeepWorkAttribValue = nightKeepWorkAttribValue;
    }

}
