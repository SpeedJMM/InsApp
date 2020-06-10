package edu.sdust.insapp.bean;

public class DispaOrderBean {
    private Integer dispaorderid;//派工单id
    private Integer ovepargroupid;//班组id
    private String dispaorderstatecode;//派工单状态代码
    private String dispaordercontroldispatime;//调度派工时间
    private String dispaordersecdispatime;//二次派工时间
    private String dispaorderremark;//派工单备注

    public DispaOrderBean() {
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

    public String getDispaorderremark() {
        return dispaorderremark;
    }

    public void setDispaorderremark(String dispaorderremark) {
        this.dispaorderremark = dispaorderremark;
    }
}
