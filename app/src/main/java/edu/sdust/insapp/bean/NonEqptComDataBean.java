package edu.sdust.insapp.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/10.
 */

public class NonEqptComDataBean {
    public  String result;
    public ArrayList<NonEqptComOrderBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<NonEqptComOrderBean> getData() {
        return data;
    }

    public void setData(ArrayList<NonEqptComOrderBean> data) {
        this.data = data;
    }
}
