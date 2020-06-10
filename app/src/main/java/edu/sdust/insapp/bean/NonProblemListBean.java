package edu.sdust.insapp.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/3.
 */

public class NonProblemListBean {
    public String result;
    public ArrayList<NonDeviceFaultBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<NonDeviceFaultBean> getData() {
        return data;
    }

    public void setData(ArrayList<NonDeviceFaultBean> data) {
        this.data = data;
    }
}
