package edu.sdust.insapp.bean;

/**
 * Created by Administrator on 2017/10/16.
 */

public class InsOrderBean {
    //返回结果
    private int result;
    //是否有派工单
    private boolean hasOrder;
    //派工单信息
    private OrderInfoBean orderInfo;

    private String username;

    public InsOrderBean() {
    }

    public InsOrderBean(int result, boolean hasOrder, OrderInfoBean orderInfo,String username) {
        this.result = result;
        this.hasOrder = hasOrder;
        this.orderInfo = orderInfo;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public boolean isHasOrder() {
        return hasOrder;
    }

    public void setHasOrder(boolean hasOrder) {
        this.hasOrder = hasOrder;
    }

    public OrderInfoBean getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfoBean orderInfo) {
        this.orderInfo = orderInfo;
    }

    @Override
    public String toString() {
        return "InsOrderBean{" +
                "result=" + result +
                ", hasOrder=" + hasOrder +
                '}';
    }
}
