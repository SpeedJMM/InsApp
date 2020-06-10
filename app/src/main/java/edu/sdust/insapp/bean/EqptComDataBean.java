package edu.sdust.insapp.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/4.
 */

public class EqptComDataBean {
    public  String result;
    public ArrayList<EqptComOrderBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<EqptComOrderBean> getData() {
        return data;
    }

    public void setData(ArrayList<EqptComOrderBean> data) {
        this.data = data;
    }
}
