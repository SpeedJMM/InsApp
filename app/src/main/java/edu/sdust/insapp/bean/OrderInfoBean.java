package edu.sdust.insapp.bean;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 * 派工单内容
 */

public class OrderInfoBean {
    //派工单编号
    private int orderId;
    //专业
    private String maintenanceMajorClsCode;
    //巡检路线名
    private String insRouteName;
    //调度派工时间
    private Date dispatchingTime;
    //班组派工时间
    private Date secondaryDispatchingTime;
    //派工单描述
    private String description;
    //巡检人员
    private List<String> insPeople;
    //巡检设备
    private List<DeviceBean> insRoute;

//    public OrderInfoBean(int orderId, String insRouteName, Date dispatchingTime, Date secondaryDispatchingTime, String description, List<String> insPeople, List<DeviceBean> insRoute) {
//        this.orderId = orderId;
//        this.insRouteName = insRouteName;
//        this.dispatchingTime = dispatchingTime;
//        this.secondaryDispatchingTime = secondaryDispatchingTime;
//        this.description = description;
//        this.insPeople = insPeople;
//        this.insRoute = insRoute;
//    }


    public OrderInfoBean(int orderId, String maintenanceMajorClsCode, String insRouteName, Date dispatchingTime, Date secondaryDispatchingTime, String description, List<String> insPeople, List<DeviceBean> insRoute) {
        this.orderId = orderId;
        this.maintenanceMajorClsCode = maintenanceMajorClsCode;
        this.insRouteName = insRouteName;
        this.dispatchingTime = dispatchingTime;
        this.secondaryDispatchingTime = secondaryDispatchingTime;
        this.description = description;
        this.insPeople = insPeople;
        this.insRoute = insRoute;
    }

    public String getMaintenanceMajorClsCode() {
        return maintenanceMajorClsCode;
    }

    public void setMaintenanceMajorClsCode(String maintenanceMajorClsCode) {
        this.maintenanceMajorClsCode = maintenanceMajorClsCode;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getInsRouteName() {
        return insRouteName;
    }

    public void setInsRouteName(String insRouteName) {
        this.insRouteName = insRouteName;
    }

    public Date getDispatchingTime() {
        return dispatchingTime;
    }

    public void setDispatchingTime(Date dispatchingTime) {
        this.dispatchingTime = dispatchingTime;
    }

    public Date getSecondaryDispatchingTime() {
        return secondaryDispatchingTime;
    }

    public void setSecondaryDispatchingTime(Date secondaryDispatchingTime) {
        this.secondaryDispatchingTime = secondaryDispatchingTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getInsPeople() {
        return insPeople;
    }

    public void setInsPeople(List<String> insPeople) {
        this.insPeople = insPeople;
    }

    public List<DeviceBean> getInsRoute() {
        return insRoute;
    }

    public void setInsRoute(List<DeviceBean> insRoute) {
        this.insRoute = insRoute;
    }
}
