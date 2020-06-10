package edu.sdust.insapp.bean;

public class EqptRepairTaskCompletSituationBean {
    private Integer eqptrepairtaskcompletsituationid;
    private Integer eqptrepaircompletorderid;
    private Integer eqptrepairtaskid;
    private String faultcausecode;//故障原因代码
    private String faultmechanismcode;//故障机理代码
    private String faulttreatmentmeascode;//故障处理措施代码
    private String eqptrepairtaskstarttime;//任务开始时间
    private String eqptrepairtaskendtime;//任务结束时间
    private Boolean eqptrepairtaskwhetherfinish;//是否完成
    private String eqptrepairtaskcompletsituationremark;//任务完成备注

    public Integer getEqptrepairtaskcompletsituationid() {
        return eqptrepairtaskcompletsituationid;
    }

    public void setEqptrepairtaskcompletsituationid(Integer eqptrepairtaskcompletsituationid) {
        this.eqptrepairtaskcompletsituationid = eqptrepairtaskcompletsituationid;
    }

    public Integer getEqptrepaircompletorderid() {
        return eqptrepaircompletorderid;
    }

    public void setEqptrepaircompletorderid(Integer eqptrepaircompletorderid) {
        this.eqptrepaircompletorderid = eqptrepaircompletorderid;
    }

    public Integer getEqptrepairtaskid() {
        return eqptrepairtaskid;
    }

    public void setEqptrepairtaskid(Integer eqptrepairtaskid) {
        this.eqptrepairtaskid = eqptrepairtaskid;
    }

    public String getFaultcausecode() {
        return faultcausecode;
    }

    public void setFaultcausecode(String faultcausecode) {
        this.faultcausecode = faultcausecode;
    }

    public String getFaultmechanismcode() {
        return faultmechanismcode;
    }

    public void setFaultmechanismcode(String faultmechanismcode) {
        this.faultmechanismcode = faultmechanismcode;
    }

    public String getFaulttreatmentmeascode() {
        return faulttreatmentmeascode;
    }

    public void setFaulttreatmentmeascode(String faulttreatmentmeascode) {
        this.faulttreatmentmeascode = faulttreatmentmeascode;
    }

    public String getEqptrepairtaskstarttime() {
        return eqptrepairtaskstarttime;
    }

    public void setEqptrepairtaskstarttime(String eqptrepairtaskstarttime) {
        this.eqptrepairtaskstarttime = eqptrepairtaskstarttime;
    }

    public String getEqptrepairtaskendtime() {
        return eqptrepairtaskendtime;
    }

    public void setEqptrepairtaskendtime(String eqptrepairtaskendtime) {
        this.eqptrepairtaskendtime = eqptrepairtaskendtime;
    }

    public Boolean getEqptrepairtaskwhetherfinish() {
        return eqptrepairtaskwhetherfinish;
    }

    public void setEqptrepairtaskwhetherfinish(Boolean eqptrepairtaskwhetherfinish) {
        this.eqptrepairtaskwhetherfinish = eqptrepairtaskwhetherfinish;
    }

    public String getEqptrepairtaskcompletsituationremark() {
        return eqptrepairtaskcompletsituationremark;
    }

    public void setEqptrepairtaskcompletsituationremark(String eqptrepairtaskcompletsituationremark) {
        this.eqptrepairtaskcompletsituationremark = eqptrepairtaskcompletsituationremark;
    }
}
