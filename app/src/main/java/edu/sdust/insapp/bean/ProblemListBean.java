package edu.sdust.insapp.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/3.
 */

public class ProblemListBean {
    public String result;
    public ArrayList<DeviceProblemBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<DeviceProblemBean> getData() {
        return data;
    }

    public void setData(ArrayList<DeviceProblemBean> data) {
        this.data = data;
    }
}
