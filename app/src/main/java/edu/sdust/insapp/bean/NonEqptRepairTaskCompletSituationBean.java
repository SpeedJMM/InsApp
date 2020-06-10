package edu.sdust.insapp.bean;

public class NonEqptRepairTaskCompletSituationBean {
    private Integer noneqptrepairtaskcompletsituationid;//非设备维修任务完成情况id
    private Integer noneqptrepaircompletorderid;//非设备维修完工单id
    private Integer noneqptrepairtaskid;//非设备维修任务id
    private String faulttreatmentmeascode;//故障处理措施代码
    private String noneqptrepairtaskstarttime;//非设备维修任务开始时间
    private String noneqptrepairtaskendtime;//非设备维修任务结束时间
    private Boolean problemwhetheralreadysol;//是否完成
    private String noneqptrepairtaskcompletsituationremark;//非设备维修任务完成情况备注

    public Integer getNoneqptrepairtaskcompletsituationid() {
        return noneqptrepairtaskcompletsituationid;
    }

    public void setNoneqptrepairtaskcompletsituationid(Integer noneqptrepairtaskcompletsituationid) {
        this.noneqptrepairtaskcompletsituationid = noneqptrepairtaskcompletsituationid;
    }

    public Integer getNoneqptrepaircompletorderid() {
        return noneqptrepaircompletorderid;
    }

    public void setNoneqptrepaircompletorderid(Integer noneqptrepaircompletorderid) {
        this.noneqptrepaircompletorderid = noneqptrepaircompletorderid;
    }

    public Integer getNoneqptrepairtaskid() {
        return noneqptrepairtaskid;
    }

    public void setNoneqptrepairtaskid(Integer noneqptrepairtaskid) {
        this.noneqptrepairtaskid = noneqptrepairtaskid;
    }

    public String getFaulttreatmentmeascode() {
        return faulttreatmentmeascode;
    }

    public void setFaulttreatmentmeascode(String faulttreatmentmeascode) {
        this.faulttreatmentmeascode = faulttreatmentmeascode;
    }

    public String getNoneqptrepairtaskstarttime() {
        return noneqptrepairtaskstarttime;
    }

    public void setNoneqptrepairtaskstarttime(String noneqptrepairtaskstarttime) {
        this.noneqptrepairtaskstarttime = noneqptrepairtaskstarttime;
    }

    public String getNoneqptrepairtaskendtime() {
        return noneqptrepairtaskendtime;
    }

    public void setNoneqptrepairtaskendtime(String noneqptrepairtaskendtime) {
        this.noneqptrepairtaskendtime = noneqptrepairtaskendtime;
    }

    public Boolean getProblemwhetheralreadysol() {
        return problemwhetheralreadysol;
    }

    public void setProblemwhetheralreadysol(Boolean problemwhetheralreadysol) {
        this.problemwhetheralreadysol = problemwhetheralreadysol;
    }

    public String getNoneqptrepairtaskcompletsituationremark() {
        return noneqptrepairtaskcompletsituationremark;
    }

    public void setNoneqptrepairtaskcompletsituationremark(String noneqptrepairtaskcompletsituationremark) {
        this.noneqptrepairtaskcompletsituationremark = noneqptrepairtaskcompletsituationremark;
    }
}
