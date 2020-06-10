package edu.sdust.insapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/4.
 */

public class EqptComOrderBean {
    private  String ViDispaOrderSecurityHarmAnalysis;//安全分析
    private  String ViDispaOrderSecurityMeas;//安全措施
    private  String ViDispaStaff;//派工人员
    private  String EqptTepairTask;//设备任务
    private  String EqptTepairTaskDes;//设备任务描述
    private List<String> DispaStaffList	;//派工人员List
    private List<String> DispaOrderSecurityHarmAnalysisList;//安全分析List
    private List<String> DispaOrderSecurityMeasList;//安全措施List
    private  Integer dispaorderid;//派工单id
    private  Integer ovepargroupid;//班组id
    private  String dispaorderstatecode;//派工单状态代码
    private  String dispaordercontroldispatime;//调度派工时间
    private  String dispaordersecdispatime;//二次派工时间
    private  String ovepargroupname;//班组名称
    private  String dispaorderstatename;//派工单状态名称
    private  Integer eqptrepairdispaorderid;//设备维修派工单id
    private String whetherworkticket;
    private  Integer eqptrepaircompletorderid;//设备维修完工单id
    private  Integer completorderid;//完工单id
    private  String completordersubmittime;//完工单提交时间
    private  String dispaorderremark;//派工单备注
    private  String completorderremark;//完工单备注
    private  String Device;//装置
    private  String EqptName;//位号
    private  String EqptTepairImport;//问题描述
    private  String FaultCauseContent;//故障原因
    private  String FaultMechanismContent;//故障机理
    private  String FaultTreatmentmeasContent;//处理措施
    private  String EqptFaultpositionName;//故障部位

    public String getViDispaOrderSecurityHarmAnalysis() {
        return ViDispaOrderSecurityHarmAnalysis;
    }

    public void setViDispaOrderSecurityHarmAnalysis(String viDispaOrderSecurityHarmAnalysis) {
        ViDispaOrderSecurityHarmAnalysis = viDispaOrderSecurityHarmAnalysis;
    }

    public String getViDispaOrderSecurityMeas() {
        return ViDispaOrderSecurityMeas;
    }

    public void setViDispaOrderSecurityMeas(String viDispaOrderSecurityMeas) {
        ViDispaOrderSecurityMeas = viDispaOrderSecurityMeas;
    }

    public String getViDispaStaff() {
        return ViDispaStaff;
    }

    public void setViDispaStaff(String viDispaStaff) {
        ViDispaStaff = viDispaStaff;
    }

    public String getEqptTepairTask() {
        return EqptTepairTask;
    }

    public void setEqptTepairTask(String eqptTepairTask) {
        EqptTepairTask = eqptTepairTask;
    }

    public String getEqptTepairTaskDes() {
        return EqptTepairTaskDes;
    }

    public void setEqptTepairTaskDes(String eqptTepairTaskDes) {
        EqptTepairTaskDes = eqptTepairTaskDes;
    }

    public List<String> getDispaStaffList() {
        return DispaStaffList;
    }

    public void setDispaStaffList(List<String> dispaStaffList) {
        DispaStaffList = dispaStaffList;
    }

    public List<String> getDispaOrderSecurityHarmAnalysisList() {
        return DispaOrderSecurityHarmAnalysisList;
    }

    public void setDispaOrderSecurityHarmAnalysisList(List<String> dispaOrderSecurityHarmAnalysisList) {
        DispaOrderSecurityHarmAnalysisList = dispaOrderSecurityHarmAnalysisList;
    }

    public List<String> getDispaOrderSecurityMeasList() {
        return DispaOrderSecurityMeasList;
    }

    public void setDispaOrderSecurityMeasList(List<String> dispaOrderSecurityMeasList) {
        DispaOrderSecurityMeasList = dispaOrderSecurityMeasList;
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

    public Integer getEqptrepairdispaorderid() {
        return eqptrepairdispaorderid;
    }

    public void setEqptrepairdispaorderid(Integer eqptrepairdispaorderid) {
        this.eqptrepairdispaorderid = eqptrepairdispaorderid;
    }

    public String getWhetherworkticket() {
        return whetherworkticket;
    }

    public void setWhetherworkticket(String whetherworkticket) {
        this.whetherworkticket = whetherworkticket;
    }

    public Integer getEqptrepaircompletorderid() {
        return eqptrepaircompletorderid;
    }

    public void setEqptrepaircompletorderid(Integer eqptrepaircompletorderid) {
        this.eqptrepaircompletorderid = eqptrepaircompletorderid;
    }

    public Integer getCompletorderid() {
        return completorderid;
    }

    public void setCompletorderid(Integer completorderid) {
        this.completorderid = completorderid;
    }

    public String getCompletordersubmittime() {
        return completordersubmittime;
    }

    public void setCompletordersubmittime(String completordersubmittime) {
        this.completordersubmittime = completordersubmittime;
    }

    public String getDispaorderremark() {
        return dispaorderremark;
    }

    public void setDispaorderremark(String dispaorderremark) {
        this.dispaorderremark = dispaorderremark;
    }

    public String getCompletorderremark() {
        return completorderremark;
    }

    public void setCompletorderremark(String completorderremark) {
        this.completorderremark = completorderremark;
    }

    public String getDevice() {
        return Device;
    }

    public void setDevice(String device) {
        Device = device;
    }

    public String getEqptName() {
        return EqptName;
    }

    public void setEqptName(String eqptName) {
        EqptName = eqptName;
    }

    public String getEqptTepairImport() {
        return EqptTepairImport;
    }

    public void setEqptTepairImport(String eqptTepairImport) {
        EqptTepairImport = eqptTepairImport;
    }

    public String getFaultCauseContent() {
        return FaultCauseContent;
    }

    public void setFaultCauseContent(String faultCauseContent) {
        FaultCauseContent = faultCauseContent;
    }

    public String getFaultMechanismContent() {
        return FaultMechanismContent;
    }

    public void setFaultMechanismContent(String faultMechanismContent) {
        FaultMechanismContent = faultMechanismContent;
    }

    public String getFaultTreatmentmeasContent() {
        return FaultTreatmentmeasContent;
    }

    public void setFaultTreatmentmeasContent(String faultTreatmentmeasContent) {
        FaultTreatmentmeasContent = faultTreatmentmeasContent;
    }

    public String getEqptFaultpositionName() {
        return EqptFaultpositionName;
    }

    public void setEqptFaultpositionName(String eqptFaultpositionName) {
        EqptFaultpositionName = eqptFaultpositionName;
    }
}

