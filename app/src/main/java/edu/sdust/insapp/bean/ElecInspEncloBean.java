package edu.sdust.insapp.bean;

import java.util.List;

public class ElecInspEncloBean {

    //派工单id
    private int dispaOrderId;
    //设备id
    private int inspEqptId;
    //电气巡检备注
    private String elecInspRemark;
    //引用的附件id
    private List<Integer> elecInspEncloRoute;

    public ElecInspEncloBean(){
    }

    public List<Integer> getElecInspEncloRoute() {
        return elecInspEncloRoute;
    }

    public void setElecInspEncloRoute(List<Integer> elecInspEncloRoute) {
        this.elecInspEncloRoute = elecInspEncloRoute;
    }

    public int getDispaOrderId() {
        return dispaOrderId;
    }

    public void setDispaOrderId(int dispaOrderId) {
        this.dispaOrderId = dispaOrderId;
    }

    public int getInspEqptId() {
        return inspEqptId;
    }

    public void setInspEqptId(int inspEqptId) {
        this.inspEqptId = inspEqptId;
    }

    public String getElecInspRemark() {
        return elecInspRemark;
    }

    public void setElecInspRemark(String elecInspRemark) {
        this.elecInspRemark = elecInspRemark;
    }
}
