package edu.sdust.insapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/10.
 */

public class NonEqptComOrderBean {
    private String ViDispaOrderSecurityHarmAnalysis;//安全危害分析
    private String ViDispaOrderSecurityMeas;//安全措施
    private String ViDispaStaff;//派工人员
    private String NonEqptRepairTask;//非设备维修任务
    private List<Integer> DispaStaffList;//派工人员list
    private List<String> DispaOrderSecurityHarmAnalysisList;//安全危害分析代码list
    private List<String> DispaOrderSecurityMeasList;//安全措施代码list
    private Integer dispaorderid;//派工单id
    private String dispaorderstatecode;//派工单状态代码
    private String dispaorderstatename;//派工单状态名称
    private String dispaordercontroldispatime;//调度派工时间
    private String dispaordersecdispatime;//二次派工时间
    private Integer noneqptrepairdispaorderid;//非设备维修派工单id
    private Integer ovepargroupid;//班组id
    private String ovepargroupname;//班组名称
    private Integer noneqptrepaircompletorderid;//非设备维修完工单id
    private Integer completorderid;//完工单id
    private String completordersubmittime;//完工单提交时间
    private String dispaorderremark;//派工单备注
    private String completorderremark;//完工单备注
    private String FaultTreatmentmeasContent;//故障处理措施
    //EqptTepairContent

    public String getDispaorderremark() {
        return dispaorderremark;
    }

    public void setDispaorderremark(String dispaorderremark) {
        this.dispaorderremark = dispaorderremark;
    }

    public String getFaultTreatmentmeasContent() {
        return FaultTreatmentmeasContent;
    }

    public void setFaultTreatmentmeasContent(String faultTreatmentmeasContent) {
        FaultTreatmentmeasContent = faultTreatmentmeasContent;
    }

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

    public String getNonEqptRepairTask() {
        return NonEqptRepairTask;
    }

    public void setNonEqptRepairTask(String nonEqptRepairTask) {
        NonEqptRepairTask = nonEqptRepairTask;
    }

    public List<Integer> getDispaStaffList() {
        return DispaStaffList;
    }

    public void setDispaStaffList(List<Integer> dispaStaffList) {
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

    public String getDispaorderstatecode() {
        return dispaorderstatecode;
    }

    public void setDispaorderstatecode(String dispaorderstatecode) {
        this.dispaorderstatecode = dispaorderstatecode;
    }

    public String getDispaorderstatename() {
        return dispaorderstatename;
    }

    public void setDispaorderstatename(String dispaorderstatename) {
        this.dispaorderstatename = dispaorderstatename;
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

    public Integer getNoneqptrepairdispaorderid() {
        return noneqptrepairdispaorderid;
    }

    public void setNoneqptrepairdispaorderid(Integer noneqptrepairdispaorderid) {
        this.noneqptrepairdispaorderid = noneqptrepairdispaorderid;
    }

    public Integer getOvepargroupid() {
        return ovepargroupid;
    }

    public void setOvepargroupid(Integer ovepargroupid) {
        this.ovepargroupid = ovepargroupid;
    }

    public String getOvepargroupname() {
        return ovepargroupname;
    }

    public void setOvepargroupname(String ovepargroupname) {
        this.ovepargroupname = ovepargroupname;
    }

    public Integer getNoneqptrepaircompletorderid() {
        return noneqptrepaircompletorderid;
    }

    public void setNoneqptrepaircompletorderid(Integer noneqptrepaircompletorderid) {
        this.noneqptrepaircompletorderid = noneqptrepaircompletorderid;
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

    public String getCompletorderremark() {
        return completorderremark;
    }

    public void setCompletorderremark(String completorderremark) {
        this.completorderremark = completorderremark;
    }
}
