package edu.sdust.insapp.bean;

import java.io.Serializable;

public class ViInspDispaOrderForDispaBean implements Serializable {
    private Integer inspdispaorderid;//巡检派工单id
    private Integer dispaorderid;//派工单id
    private Integer ovepargroupid;//班组id
    private Integer insprouteid;//巡检路线id
    private Integer systemuserid;//系统用户id
    private String dispaorderstatecode;//派工单状态代码
    private String dispaordercontroldispatime;//调度派工时间
    private String dispaordersecdispatime;//二次派工时间
    private String ovepargroupname;//班组名称
    private String dispaorderstatename;//派工单状态名称
    private String insproutename;//巡检路线名称
    private String username;//用户名
    private String dispaorderremark;//派工单备注

    public Integer getInspdispaorderid() {
        return inspdispaorderid;
    }

    public void setInspdispaorderid(Integer inspdispaorderid) {
        this.inspdispaorderid = inspdispaorderid;
    }

    public Integer getDispaorderid() {
        return dispaorderid;
    }

    public void setDispaorderid(Integer dispaorderid) {
        this.dispaorderid = dispaorderid;
    }

    public Integer getOvepargroupid() {
        return ovepargroupid;
    }

    public void setOvepargroupid(Integer ovepargroupid) {
        this.ovepargroupid = ovepargroupid;
    }

    public Integer getInsprouteid() {
        return insprouteid;
    }

    public void setInsprouteid(Integer insprouteid) {
        this.insprouteid = insprouteid;
    }

    public Integer getSystemuserid() {
        return systemuserid;
    }

    public void setSystemuserid(Integer systemuserid) {
        this.systemuserid = systemuserid;
    }

    public String getDispaorderstatecode() {
        return dispaorderstatecode;
    }

    public void setDispaorderstatecode(String dispaorderstatecode) {
        this.dispaorderstatecode = dispaorderstatecode;
    }

    public String getDispaordercontroldispatime() {
        return dispaordercontroldispatime;
    }

    public void setDispaordercontroldispatime(String dispaordercontroldispatime) {
        this.dispaordercontroldispatime = dispaordercontroldispatime;
    }

    public String getDispaordersecdispatime() {
        return dispaordersecdispatime;
    }

    public void setDispaordersecdispatime(String dispaordersecdispatime) {
        this.dispaordersecdispatime = dispaordersecdispatime;
    }

    public String getOvepargroupname() {
        return ovepargroupname;
    }

    public void setOvepargroupname(String ovepargroupname) {
        this.ovepargroupname = ovepargroupname;
    }

    public String getDispaorderstatename() {
        return dispaorderstatename;
    }

    public void setDispaorderstatename(String dispaorderstatename) {
        this.dispaorderstatename = dispaorderstatename;
    }

    public String getInsproutename() {
        return insproutename;
    }

    public void setInsproutename(String insproutename) {
        this.insproutename = insproutename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDispaorderremark() {
        return dispaorderremark;
    }

    public void setDispaorderremark(String dispaorderremark) {
        this.dispaorderremark = dispaorderremark;
    }
}
