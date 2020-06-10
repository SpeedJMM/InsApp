package edu.sdust.insapp.bean;

import java.io.Serializable;

public class ViKeepWorkKeepWatchStaffBean implements Serializable {
    private Integer keepworkkeepwatchstaffid;
    private String keepwatchcatgcode;
    private String keepwatchcatgname;
    private Integer keepworkworkrecordid;
    private Integer oveparstaffid;
    private String oveparstaffname;
    private String maintenancemajorclscode;
    private String maintenancemajorclsname;
    private Double keepworkrate;
    private String oveparstaffnameandmajor;

    public Integer getKeepworkkeepwatchstaffid() {
        return keepworkkeepwatchstaffid;
    }

    public void setKeepworkkeepwatchstaffid(Integer keepworkkeepwatchstaffid) {
        this.keepworkkeepwatchstaffid = keepworkkeepwatchstaffid;
    }

    public String getKeepwatchcatgcode() {
        return keepwatchcatgcode;
    }

    public void setKeepwatchcatgcode(String keepwatchcatgcode) {
        this.keepwatchcatgcode = keepwatchcatgcode;
    }

    public String getKeepwatchcatgname() {
        return keepwatchcatgname;
    }

    public void setKeepwatchcatgname(String keepwatchcatgname) {
        this.keepwatchcatgname = keepwatchcatgname;
    }

    public Integer getKeepworkworkrecordid() {
        return keepworkworkrecordid;
    }

    public void setKeepworkworkrecordid(Integer keepworkworkrecordid) {
        this.keepworkworkrecordid = keepworkworkrecordid;
    }

    public Integer getOveparstaffid() {
        return oveparstaffid;
    }

    public void setOveparstaffid(Integer oveparstaffid) {
        this.oveparstaffid = oveparstaffid;
    }

    public String getOveparstaffname() {
        return oveparstaffname;
    }

    public void setOveparstaffname(String oveparstaffname) {
        this.oveparstaffname = oveparstaffname;
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

    public Double getKeepworkrate() {
        return keepworkrate;
    }

    public void setKeepworkrate(Double keepworkrate) {
        this.keepworkrate = keepworkrate;
    }

    public String getOveparstaffnameandmajor() {
        return oveparstaffnameandmajor;
    }

    public void setOveparstaffnameandmajor(String oveparstaffnameandmajor) {
        this.oveparstaffnameandmajor = oveparstaffnameandmajor;
    }
}
